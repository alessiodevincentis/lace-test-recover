package com.woonders.lacemsjsf.view.simulator;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.service.SimulatorTableService;
import com.woonders.lacemscommon.service.impl.SimulatorTableServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by ema98 on 8/11/2017.
 */
@ManagedBean(name = "simulatorTableListView")
@ViewScoped
@Getter
@Setter
@Slf4j
public class SimulatorTableListView implements Serializable {

    private LazyDataModel<SimulatorTableViewModel> simulatorTableViewModelLazyDataModel;
    private SimulatorTableViewModel selectedSimulatorTableViewModel;

    @ManagedProperty(SimulatorTableServiceImpl.JSF_NAME)
    private SimulatorTableService simulatorTableService;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;

    @PostConstruct
    public void init() {
        simulatorTableViewModelLazyDataModel = new LazyDataModel<SimulatorTableViewModel>() {

            @Override
            public List<SimulatorTableViewModel> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                                      final Map<String, Object> filters) {

                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }

                final ViewModelPage<SimulatorTableViewModel> viewModelPage = simulatorTableService.findAll(aziendaSelectionView.getCurrentAziendaId(),
                        httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.SIMULATORI, OperatorViewModel.PermissionType.READ),
                        first, pageSize, sortField, com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);
                setRowCount((int) viewModelPage.getTotalElements());

                return viewModelPage.getContent();
            }

            @Override
            public SimulatorTableViewModel getRowData(final String rowKey) {
                return simulatorTableService.findOne(Long.parseLong(rowKey));
            }
        };
    }

    public void onRowSelect() throws IOException {
        Role.RoleName simulatorWriteRoleName = httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.SIMULATORI, OperatorViewModel.PermissionType.WRITE);
        boolean canReadTable = false;
        if(simulatorWriteRoleName != null) {
            switch (simulatorWriteRoleName) {
                case SIMULATORI_WRITE_OWN:
                    canReadTable = httpSessionUtil.getOperatorId() == selectedSimulatorTableViewModel.getCreatorOperatorViewModel().getId();
                    break;
                case SIMULATORI_WRITE_ALL:
                    canReadTable = httpSessionUtil.getAziendaId() == selectedSimulatorTableViewModel.getCreatorOperatorViewModel().getAzienda().getId();
                    break;
                case SIMULATORI_WRITE_SUPER:
                    canReadTable = true;
                    break;
            }
        }

        if(canReadTable) {
            passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_ID_PARAMETER, selectedSimulatorTableViewModel.getId());
            FacesUtil.redirect(Constants.getCreateNewSimulatorTablePath(true));
        } else {
            FacesUtil.showMessageInDialog(FacesMessage.SEVERITY_WARN, propertiesUtil.getMsgAttenzione(),
                    propertiesUtil.getMsgAccessoNonConsentitoSimulator());
        }
    }

    public String getAgenziaName(final SimulatorTableViewModel simulatorTableViewModel) {
        if (simulatorTableViewModel.getAziendaViewModel() != null) {
            return simulatorTableViewModel.getAziendaViewModel().getNomeAzienda();
        }
        return "Qualsiasi";
    }

    public boolean isDisabledSelection() {
        return !httpSessionUtil.hasAnyAuthority(Role.RoleName.getSimulatoriWriteRoles());
    }

    public String getJobTypeListString(SimulatorTableViewModel simulatorTableViewModel) {
        if(simulatorTableViewModel.getJobTypeList() == null || simulatorTableViewModel.getJobTypeList().isEmpty()) {
            return null;
        }
        //nota lo spazio per far andare a capo
        StringJoiner stringJoiner = new StringJoiner(", ");
        for(Impiego impiego : simulatorTableViewModel.getJobTypeList()) {
            stringJoiner.add(impiego.getValue());
        }
        return stringJoiner.toString();
    }
}
