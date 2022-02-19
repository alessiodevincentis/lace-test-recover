package com.woonders.lacemsjsf.view.util;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.NominativoService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.NominativoServiceImpl;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@ManagedBean(name = UtilResolverView.NAME)
@RequestScoped
@Getter
@Setter
public class UtilResolverView implements Serializable {
    public static final String NAME = "utilResolverView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    @ManagedProperty(NominativoServiceImpl.JSF_NAME)
    private NominativoService nominativoService;
    @ManagedProperty(DateConversionUtil.JSF_NAME)
    private DateConversionUtil dateConversionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;

    //sempre i metodi rattoppi per i casi specifici, che palle!
    public List<String> getAllOperatorsForSearch() {
        return generateOperatorUsernameList(operatorService.findAllAppOperator());
    }

    public List<String> getOperatorsStringList(final OperatorViewModel.MenuSection menuSection, final OperatorViewModel.PermissionType permissionType) {
        return generateOperatorUsernameList(getOperatorsList(menuSection, permissionType));
    }

    private List<String> generateOperatorUsernameList(final List<OperatorViewModel> operatorViewModelList) {
        final List<String> operatorUsernameList = new LinkedList<>();
        if (operatorViewModelList != null) {
            for (final OperatorViewModel operatorViewModel : operatorViewModelList) {
                operatorUsernameList.add(operatorViewModel.getUsername());
            }
        }
        return operatorUsernameList;
    }

    public List<OperatorViewModel> getOperatorsList(final OperatorViewModel.MenuSection menuSection, final OperatorViewModel.PermissionType permissionType) {
        final List<OperatorViewModel> operatorViewModelList = new LinkedList<>();
        if (menuSection != null && permissionType != null) {

            final Role.RoleName actualPermission = httpSessionUtil.getAuthority(menuSection, permissionType);

            if (actualPermission != null) {
                final String actualPermissionString = actualPermission.toString();
                //se super e read lo stiamo usando per il filtro, quindi mostra solo gli operatori dell azienda scelta dal filtro
                //se super e write lo stiamo usando per modificare, quindi li mostra tutti quanti perche puoi assegnare pratiche nominativi a chiunque
                if (actualPermissionString.contains("SUPER") && actualPermissionString.contains("READ")) {
                    operatorViewModelList.addAll(operatorService.findAppOperatorListByAziendaId(aziendaSelectionView.getCurrentAziendaId()));
                } else if (actualPermissionString.contains("SUPER") && actualPermissionString.contains("WRITE")) {
                    operatorViewModelList.addAll(operatorService.findAllAppOperator());
                } else if (actualPermissionString.contains("ALL")) {
                    operatorViewModelList.addAll(operatorService.findAppOperatorListByAziendaId(httpSessionUtil.getAziendaId()));
                }
            }

        }

        if (operatorViewModelList.isEmpty()) {
            operatorViewModelList.add(operatorService.getOne(httpSessionUtil.getOperatorId()));
        }
        return operatorViewModelList;
    }

    public boolean isRenderedColumn(final OperatorViewModel.MenuSection menuSection) {
        if (menuSection != null) {
            if (OperatorViewModel.MenuSection.CLIENTI.equals(menuSection)) {
                return httpSessionUtil.hasAnyAuthority(Role.RoleName.CLIENTI_READ_ALL, Role.RoleName.CLIENTI_READ_SUPER);
            }
            if (OperatorViewModel.MenuSection.NOMINATIVI.equals(menuSection)) {
                return httpSessionUtil.hasAnyAuthority(Role.RoleName.NOMINATIVI_READ_ALL, Role.RoleName.NOMINATIVI_READ_SUPER);
            }
        }
        return false;
    }

    public boolean isCampagneMarketingMenuEntryRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getCampagneMarketingWriteRoles());
    }

    public Date getNewDate() {
        return new Date();
    }

    public String minDateCalendarFromLocalDateTime(final LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return dateConversionUtil.calcStringFromLocalDateTime(localDateTime);
        }
        return null;
    }

    public String minHourCalendarFromLocalDateTime(final LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return Integer.toString(localDateTime.getHour());
        }
        return null;
    }

    public String minMinuteCalendarFromLocalDateTime(final LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return Integer.toString(localDateTime.getMinute());
        }
        return null;
    }
  
    public List<String> getAllProvince() {
    	final List<String> provinceList = new LinkedList<>();
    	for (final String provicia : nominativoService.getAllProvince()) {
    		provinceList.add(provicia);
        }
    	
        return provinceList;
    }
}
