package com.woonders.lacemsjsf.view.noticeboard;

import com.woonders.lacemscommon.app.viewmodel.NoticeBoardViewModel;
import com.woonders.lacemscommon.exception.UnableToGetFileException;
import com.woonders.lacemscommon.service.FileService;
import com.woonders.lacemscommon.service.NoticeBoardService;
import com.woonders.lacemscommon.service.impl.AmazonS3FileServiceImpl;
import com.woonders.lacemscommon.service.impl.NoticeBoardServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.view.pdf.StreamPdf;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

@ManagedBean(name = NoticeBoardPdf.NAME)
@RequestScoped
@Getter
@Setter
public class NoticeBoardPdf implements Serializable {

    public static final String NAME = "noticeBoardPdf";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private final StreamPdf stream;
    @ManagedProperty(NoticeBoardServiceImpl.JSF_NAME)
    private NoticeBoardService noticeBoardService;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(AmazonS3FileServiceImpl.JSF_NAME)
    private FileService amazonS3FileService;
    @ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
    private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;

    public NoticeBoardPdf() {
        stream = new StreamPdf();
    }


    public StreamedContent getStream() throws IOException {
        final long noticeId = (long) passaggioParametriUtils.getParametri()
                .get(PassaggioParametriUtils.Parametro.NOTICE_ID_TO_DISPLAY_PDF);

        final NoticeBoardViewModel noticeBoardViewModel = noticeBoardService.getNotice(noticeId);
        if (noticeBoardService.isFileAttached(noticeBoardViewModel.getId())) {
            final StreamPdf streamPdf = new StreamPdf();
            try {
                final byte[] content = amazonS3FileService.getFileForNoticeBoard(
                        requestTenantIdentifierResolver.getTenantIdentifier(),
                        noticeBoardViewModel.getFileNameAttached(), noticeBoardViewModel.getId());
                final ByteArrayOutputStream file = new ByteArrayOutputStream(content.length);
                file.write(content);
                file.close();
                return streamPdf.getStream(file);
            } catch (final UnableToGetFileException e) {
                FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric());
            }
        }
        return null;
    }

    public String getIdFile() {
        return java.util.UUID.randomUUID().toString();
    }
}
