package com.woonders.lacemsjsf.view.noticeboard;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.NoticeBoardViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.NoticeBoardService;
import com.woonders.lacemscommon.service.impl.NoticeBoardServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean(name = NoticeBoardDatatableView.NAME)
@ViewScoped
@Getter
@Setter
public class NoticeBoardDatatableView implements Serializable {


    public static final String NAME = "noticeBoardDatatableView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private static final String COLOR_ROW = "coloredNotice";

    @ManagedProperty(NoticeBoardServiceImpl.JSF_NAME)
    private NoticeBoardService noticeBoardService;
    private LazyDataModel<NoticeBoardViewModel> noticeBoardLazyDataModel;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    private NoticeBoardViewModel noticeSelected;

    @PostConstruct
    public void init() {
        noticeBoardLazyDataModel = new LazyDataModel<NoticeBoardViewModel>() {

            @Override
            public List<NoticeBoardViewModel> load(final int first, final int pageSize, final String sortField,
                                                   final SortOrder sortOrder, final Map<String, Object> filters) {

                final ViewModelPage<NoticeBoardViewModel> viewModelPage = noticeBoardService.getAllNotices(
                        aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAziendaId(),
                        httpSessionUtil.hasAuthority(Role.RoleName.NOTICE_BOARD_READ_SUPER),
                        first, pageSize, sortField,
                        com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()));

                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public NoticeBoardViewModel getRowData(final String rowKey) {
                return noticeBoardService.getNotice(Long.parseLong(rowKey));
            }
        };
    }

    public void onRowSelect() throws IOException {
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.NOTICE_SELECTED_ID, noticeSelected.getId());
        noticeBoardService.addOperatorIntoOperatorSet(noticeSelected.getId(), httpSessionUtil.getOperatorId());
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getNoticeBoardPath(false));
    }

    public boolean isColumnAziendaRendered() {
        return httpSessionUtil.hasAuthority(Role.RoleName.NOTICE_BOARD_READ_SUPER);
    }

    public String getRowStyle(final long noticeId) {
        return noticeBoardService.isNoticeToRead(httpSessionUtil.getOperatorId(), noticeId) ? COLOR_ROW : null;
    }
}