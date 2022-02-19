package com.woonders.lacemsjsf.view.noticeboard;

import com.woonders.lacemscommon.app.viewmodel.NoticeBoardViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.exception.UnableToSaveException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import com.woonders.lacemscommon.service.FileService;
import com.woonders.lacemscommon.service.NoticeBoardService;
import com.woonders.lacemscommon.service.impl.AmazonS3FileServiceImpl;
import com.woonders.lacemscommon.service.impl.NoticeBoardServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

@ManagedBean(name = NoticeBoardView.NAME)
@ViewScoped
@Getter
@Setter
public class NoticeBoardView implements Serializable {

    public static final String NAME = "noticeBoardView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    public static final long PDF_SIZE_LIMIT = 2097152; // bytes = 2mb
    private static final int MAX_TITLE_LENGTH = 50;
    private static final int NUMBER_FILE_LIMIT = 1;

    private static final String NOTICE_MSG1 = "Hai {0} avviso da leggere";
    private static final String NOTICE_MSG2_PLUS = "Hai {0} avvisi da leggere";
    private static final String NOTICE_MSG0 = "Vedi tutti gli avvisi";

    private NoticeBoardViewModel noticeBoardViewModel;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
    private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
    private byte[] contents;
    private String filename;

    @ManagedProperty(NoticeBoardServiceImpl.JSF_NAME)
    private NoticeBoardService noticeBoardService;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(AmazonS3FileServiceImpl.JSF_NAME)
    private FileService amazonS3FileService;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;

    @PostConstruct
    public void init() {
        setInitialNotice();
    }

    private void setInitialNotice() {
        if (passaggioParametriUtils.getParametri().containsKey(PassaggioParametriUtils.Parametro.NOTICE_SELECTED_ID)) {
            final long noticeId = (long) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.NOTICE_SELECTED_ID);
            noticeBoardViewModel = noticeBoardService.getNotice(noticeId);
            passaggioParametriUtils.getParametri().remove(PassaggioParametriUtils.Parametro.NOTICE_SELECTED_ID);
        } else {
            noticeBoardViewModel = new NoticeBoardViewModel();
        }
    }

    public boolean isSelectedAziendaRendered() {
        return httpSessionUtil.hasAuthority(Role.RoleName.NOTICE_BOARD_WRITE_SUPER) && noticeBoardViewModel.getId() == 0;
    }

    public boolean isAziendaValueRendered() {
        return httpSessionUtil.hasAuthority(Role.RoleName.NOTICE_BOARD_WRITE_SUPER) && noticeBoardViewModel.getId() != 0;
    }

    public int getMaxTitleLength() {
        return MAX_TITLE_LENGTH;
    }

    public long getPdfSizeLimit() {
        return PDF_SIZE_LIMIT;
    }

    public int getNumberFileLimit() {
        return NUMBER_FILE_LIMIT;
    }

    public void save() {
        try {
            noticeBoardViewModel = noticeBoardService.save(noticeBoardViewModel, httpSessionUtil.getAziendaId(),
                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOTICE_BOARD, OperatorViewModel.PermissionType.WRITE),
                    httpSessionUtil.getOperatorId(), requestTenantIdentifierResolver.getTenantIdentifier(), filename, contents);
            filename = null;
            contents = null;
            FacesUtil.addMessage(propertiesUtil.getMsgSuccess());
        } catch (final UnableToSaveException | UnableToSaveFileException e) {
            e.printStackTrace();
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric());
        }
    }

    public void upload(final FileUploadEvent event) {
        if (event != null && event.getFile() != null) {
            contents = event.getFile().getContents();
            filename = event.getFile().getFileName();
            FacesUtil.addMessage(propertiesUtil.getMsgConfirmUploadFile());
        }
    }

    public void deleteNotice() throws IOException {
        try {
            if (noticeBoardService.isFileAttached(noticeBoardViewModel.getId())) {
                amazonS3FileService.deleteFileForNoticeBoard(requestTenantIdentifierResolver.getTenantIdentifier(),
                        noticeBoardViewModel.getFileNameAttached(), noticeBoardViewModel.getId());
            }
            noticeBoardService.delete(noticeBoardViewModel.getId());
            FacesUtil.addMessage(propertiesUtil.getMsgDeleteNoticeSuccess());
            FacesUtil.redirect(Constants.getNoticeBoardDatatablePath(true));
        } catch (final UnableToDeleteException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgDeleteError());
        }
    }

    public void deleteFile() {
        try {
            if (noticeBoardService.isFileAttached(noticeBoardViewModel.getId())) {
                amazonS3FileService.deleteFileForNoticeBoard(requestTenantIdentifierResolver.getTenantIdentifier(),
                        noticeBoardViewModel.getFileNameAttached(), noticeBoardViewModel.getId());
                noticeBoardService.setFileNameNull(noticeBoardViewModel.getId());
            }
            FacesUtil.addMessage(propertiesUtil.getMsgFileViewDeleted());
        } catch (final UnableToDeleteException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgFileViewDeleteError());
        }
    }

    public boolean isButtonDeleteRendered() {
        return noticeBoardViewModel.getId() != 0 && httpSessionUtil.hasAnyAuthority(Role.RoleName.NOTICE_BOARD_WRITE,
                Role.RoleName.NOTICE_BOARD_WRITE_SUPER);
    }

    public boolean isButtonSaveRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.NOTICE_BOARD_WRITE, Role.RoleName.NOTICE_BOARD_WRITE_SUPER);
    }

    public boolean isButtonAttachDisabled() {
        return !httpSessionUtil.isStorageEnabled();
    }

    public boolean isButtonAttachRendered() {
        return !noticeBoardService.isFileAttached(noticeBoardViewModel.getId())
                && httpSessionUtil.hasAnyAuthority(Role.RoleName.NOTICE_BOARD_WRITE, Role.RoleName.NOTICE_BOARD_WRITE_SUPER);
    }

    public boolean isButtonShowFileRendered() {
        return noticeBoardService.isFileAttached(noticeBoardViewModel.getId());
    }

    public boolean isButtonDeleteFileRendered() {
        return noticeBoardViewModel.getId() != 0
                && noticeBoardService.isFileAttached(noticeBoardViewModel.getId())
                && httpSessionUtil.hasAnyAuthority(Role.RoleName.NOTICE_BOARD_WRITE,
                Role.RoleName.NOTICE_BOARD_WRITE_SUPER);
    }

    public void setNoticeIdToDisplayPdf() {
        passaggioParametriUtils.getParametri()
                .put(PassaggioParametriUtils.Parametro.NOTICE_ID_TO_DISPLAY_PDF, noticeBoardViewModel.getId());
    }

    public String getPdfPage() {
        return Constants.getPdfNoticeBoardPath(true);
    }

    private long numberOfNoticesToRead() {
        return noticeBoardService.countNoticesToRead(aziendaSelectionView.getCurrentAziendaId(),
                httpSessionUtil.getAziendaId(), httpSessionUtil.hasAuthority(Role.RoleName.NOTICE_BOARD_WRITE_SUPER),
                httpSessionUtil.getOperatorId());
    }

    public String numberOfNoticeToDisplay() {
        final long noticesNumber = numberOfNoticesToRead();
        switch ((int) noticesNumber) {
            case 1:
                return MessageFormat.format(NOTICE_MSG1, noticesNumber);
            case 0:
                return NOTICE_MSG0;
            default:
                return MessageFormat.format(NOTICE_MSG2_PLUS, noticesNumber);
        }
    }

    public String numberOfNoticeToDisplayOnIcon() {
        final long noticesNumber = numberOfNoticesToRead();
        return noticesNumber == 0 ? "" : String.valueOf(noticesNumber);
    }

    public String numberOfNoticeToDisplayOnStyle() {
        final long noticesNumber = numberOfNoticesToRead();
        return noticesNumber != 0 ? "topbar-badge" : "";
    }

    public boolean isDatatableRendered() {
        return noticeBoardViewModel.getId() != 0
                && noticeBoardViewModel.getCreatorOperator().getId() == httpSessionUtil.getOperatorId();
    }

    public List<OperatorViewModel> listOfOperatorsViewThisNotice() {
        return noticeBoardService.getAllOperatorsByNoticeIdExcludeCreatorId(noticeBoardViewModel.getId(),
                noticeBoardViewModel.getCreatorOperator().getId());
    }
}
