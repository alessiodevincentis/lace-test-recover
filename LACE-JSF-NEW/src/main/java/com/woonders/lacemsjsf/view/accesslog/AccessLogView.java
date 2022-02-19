package com.woonders.lacemsjsf.view.accesslog;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.AccessLogViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.service.AccessLogService;
import com.woonders.lacemscommon.service.impl.AccessLogServiceImpl;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.woonders.lacemsjsf.view.accesslog.AccessLogView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class AccessLogView {

    public static final String NAME = "accessLogView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private LazyDataModel<AccessLogViewModel> accessLogViewModelLazyDataModel;
    @ManagedProperty(AccessLogServiceImpl.JSF_NAME)
    private AccessLogService accessLogService;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    private LocalDateTime startLoginDateTime;
    private LocalDateTime endLoginDateTime;
    private List<String> selectedOperatorList;

    @PostConstruct
    public void init() {
        accessLogViewModelLazyDataModel = new LazyDataModel<AccessLogViewModel>() {

            @Override
            public List<AccessLogViewModel> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                                                 Map<String, Object> filters) {
                Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }

                ViewModelPage<AccessLogViewModel> viewModelPage = accessLogService.getAccessLogList(aziendaSelectionView.getCurrentAziendaId(),
                        startLoginDateTime, endLoginDateTime,
                        selectedOperatorList,
                        first,
                        pageSize, sortField,
                        com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);

                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public AccessLogViewModel getRowData(String rowKey) {
                return accessLogService.getOne(Long.parseLong(rowKey));
            }
        };

    }

}
