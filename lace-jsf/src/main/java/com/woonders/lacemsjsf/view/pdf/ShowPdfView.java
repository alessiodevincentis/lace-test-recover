package com.woonders.lacemsjsf.view.pdf;

import com.woonders.lacemscommon.exception.UnableToGetFileException;
import com.woonders.lacemscommon.service.FileService;
import com.woonders.lacemscommon.service.impl.AmazonS3FileServiceImpl;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by Emanuele on 16/11/2016.
 */
@ManagedBean
@RequestScoped
public class ShowPdfView implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ShowPdfView.class);

    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;

    @ManagedProperty(AmazonS3FileServiceImpl.JSF_NAME)
    private FileService fileService;

    private String fileName;
    private StreamedContent streamedContent;

    public String getFileName() {
        return fileName;
    }

    public void setPassaggioParametriUtils(final PassaggioParametriUtils passaggioParametriUtils) {
        this.passaggioParametriUtils = passaggioParametriUtils;
    }

    public void setFileService(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    public void init() {
        fileName = passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.FILE_NAME).toString();
        createStream();
    }

    public StreamedContent getStreamedContent() {
        return streamedContent;
    }

    public void createStream() {
        final StreamPdf streamPdf = new StreamPdf();
        final byte[] bytes;
        try {
            final HashMap<PassaggioParametriUtils.Parametro, Object> parameterMap = passaggioParametriUtils
                    .getParametri();
            bytes = fileService.getFile(parameterMap.get(PassaggioParametriUtils.Parametro.TENANT_ID).toString(),
                    Long.parseLong(parameterMap.get(PassaggioParametriUtils.Parametro.CLIENTE_PRATICA_ID).toString()),
                    ((FileService.FileCategory) parameterMap.get(PassaggioParametriUtils.Parametro.FILE_CATEGORY)),
                    fileName);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bytes.length);
            byteArrayOutputStream.write(bytes);
            byteArrayOutputStream.close();
            streamedContent = streamPdf.getStream(byteArrayOutputStream);
        } catch (final UnableToGetFileException | IOException e) {
            logger.error(
                    "Create PDF stream error: " + passaggioParametriUtils.getParametri().entrySet().stream()
                            .map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(", ")),
                    e);
        }

    }

    public String getIdFile() {
        return java.util.UUID.randomUUID().toString();
    }
}
