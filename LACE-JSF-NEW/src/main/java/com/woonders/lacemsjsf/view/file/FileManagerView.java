package com.woonders.lacemsjsf.view.file;

import com.woonders.lacemscommon.app.viewmodel.FileViewModel;
import com.woonders.lacemscommon.exception.NoFileFoundException;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import com.woonders.lacemscommon.service.ClienteManagerService;
import com.woonders.lacemscommon.service.FileService;
import com.woonders.lacemscommon.service.impl.AmazonS3FileServiceImpl;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.exception.FunctionalityNotEnabledException;
import com.woonders.lacemsjsf.exception.OutOfStorageException;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Emanuele on 14/11/2016.
 */
@ManagedBean
@ViewScoped
@Setter
@Getter
public class FileManagerView implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FileManagerView.class);
    @ManagedProperty(AmazonS3FileServiceImpl.JSF_NAME)
    private FileService fileService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    private List<FileViewModel> fileViewModelList;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
    private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
    @ManagedProperty(ClienteManagerServiceImpl.JSF_NAME)
    private ClienteManagerService clienteManagerService;
    private Map<FileService.FileCategory, List<RealUploadedFile>> categorizedRealUploadedFileMap;
    private int uploadingFilesCount;
    private FileService.FileCategory uploadingFileCategory;
    //used to check when it's the right time to execute processUploadFileList (the last time it is triggered by the onComplete, so the facesMessage won't disappear)
    private int processUploadedFileListCount = 0;

    @PostConstruct
    public void init() {
        categorizedRealUploadedFileMap = new HashMap<>();
    }

    public boolean isFileStorageDisabled() {
        try {
            checkFileStorageEnabled();
            return false;
        } catch (final FunctionalityNotEnabledException e) {
            return true;
        }
    }

    private void checkFileStorageEnabled() throws FunctionalityNotEnabledException {
        final boolean enabled = httpSessionUtil.isStorageEnabled();
        if (!enabled) {
            throw new FunctionalityNotEnabledException();
        }
    }

    // http://stackoverflow.com/questions/14159967/is-it-possible-to-call-setpropertyactionlistener-before-actionlistener
    public void setFileListToDisplay(final String fileCategoryString) {
        // TODO verificare permessi (tenant e pratica associata)
        try {
            final FileService.FileCategory fileCategory = FileService.FileCategory.valueOf(fileCategoryString);
            // TODO non si capisce perche non funziona! se abilitato fa crashare
            // // il bean. Era colpa della lamba expression sul delete, ora
            // funziona, quindi controllare e abilitare
            // checkFileStorageEnabled(fileCategory);
            fileViewModelList = fileService.getFileListInCategory(requestTenantIdentifierResolver.getTenantIdentifier(),
                    getClienteOrPraticaId(fileCategory), fileCategory);
            FacesUtil.openDialog(FacesUtil.DialogType.FILE_LIST);
        } catch (final NoFileFoundException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgFileViewNoFileFound(), FacesMessage.SEVERITY_INFO);
        }
    }

    private long getClienteOrPraticaId(final FileService.FileCategory fileCategory) {
        return FileService.FileCategory.ANAGRAFICA.equals(fileCategory)
                ? Long.parseLong(passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.CLIENTE_ID).toString())
                : Long.parseLong(passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.PRATICA_ID).toString());
    }

    public void handleFileUploadAntiriciclaggio(final FileUploadEvent event) {
        addUploadedFileToMap(event.getFile(), FileService.FileCategory.ANTIRICICLAGGIO);
    }

    public void handleFileUploadAnagrafica(final FileUploadEvent event) {
        addUploadedFileToMap(event.getFile(), FileService.FileCategory.ANAGRAFICA);
    }

    public void handleFileUploadReddito(final FileUploadEvent event) {
        addUploadedFileToMap(event.getFile(), FileService.FileCategory.REDDITO);
    }

    public void handleFileUploadPratica(final FileUploadEvent event) {
        addUploadedFileToMap(event.getFile(), FileService.FileCategory.PRATICA);
    }

    public void beginFileUpload() {
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            uploadingFilesCount = Integer.parseInt(params.get("uploadingFilesCount"));
            uploadingFileCategory = FileService.FileCategory.valueOf(params.get("uploadingFileCategory"));
        } catch (final Exception e) {
            FacesUtil.addMessage(propertiesUtil.getMsgFileUploadError(), FacesMessage.SEVERITY_WARN);
        }
    }

    private void addUploadedFileToMap(final UploadedFile uploadedFile, final FileService.FileCategory fileCategory) {
        // do not replace with lambda expression, fuck JSF!
        //serve per azzerare la mappa nel caso si passi da caricare una categoria di file dopo un'altra
        //nel caso si carichino piu file simultaneamente della stessa categoria questo metodo viene chiamato piu volte ma il clear non deve essere fatto
        if (!categorizedRealUploadedFileMap.isEmpty() && !categorizedRealUploadedFileMap.containsKey(fileCategory)) {
            categorizedRealUploadedFileMap.clear();
        }
        List<RealUploadedFile> realUploadedFileList = categorizedRealUploadedFileMap.get(fileCategory);
        if (realUploadedFileList == null) {
            realUploadedFileList = new LinkedList<>();
            categorizedRealUploadedFileMap.put(fileCategory, realUploadedFileList);
        }
        realUploadedFileList
                .add(RealUploadedFile.builder().fileName(uploadedFile.getFileName()).content(uploadedFile.getContents())
                        .contentType(uploadedFile.getContentType()).size(uploadedFile.getSize()).build());
    }

    /**
     * http://stackoverflow.com/questions/22994966/pass-value-of-another-input-component-when-uploading-file-via-pfileupload-mode?rq=1
     * http://stackoverflow.com/questions/7221495/pass-parameter-to-premotecommand-from-javascript/18510102#18510102
     * http://stackoverflow.com/questions/11365094/can-i-update-a-jsf-component-from-a-jsf-backing-bean-method
     */
    public void processUploadedFileList() {
        try { // TODO non si capisce perche non funziona! se abilitato fa
            // crashare // il bean. Era colpa della lamba expression sul
            // delete, ora funziona, quindi controllare e abilitare
            // checkFileStorageEnabled(fileCategory);
            final String tenantId = requestTenantIdentifierResolver.getTenantIdentifier();

            processUploadedFileListCount++;

            // >= just to avoid issues (but should never happen)
            if (processUploadedFileListCount >= uploadingFilesCount) {
                for (final Map.Entry<FileService.FileCategory, List<RealUploadedFile>> entry : categorizedRealUploadedFileMap
                        .entrySet()) {
                    checkSpaceStillAvailable(entry.getValue());
                    for (final RealUploadedFile realUploadedFile : entry.getValue()) {
                        fileService.save(tenantId, getClienteOrPraticaId(entry.getKey()), entry.getKey(),
                                realUploadedFile.getFileName(), realUploadedFile.getContent());
                    }
                    if (FileService.FileCategory.ANTIRICICLAGGIO.equals(entry.getKey())) {
                        clienteManagerService.setAntiriciclaggioFileCaricato((long) passaggioParametriUtils
                                .getParametri().get(PassaggioParametriUtils.Parametro.PRATICA_ID), true);
                    }
                }
                categorizedRealUploadedFileMap.clear();
                uploadingFilesCount = 0;
                processUploadedFileListCount = 0;
                FacesUtil.addMessage(propertiesUtil.getMsgFileUploadOk(), FacesMessage.SEVERITY_INFO);
                RequestContext.getCurrentInstance().update("form:messages");
            }
        } catch (final UnableToSaveFileException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgFileUploadError(), FacesMessage.SEVERITY_ERROR);
            RequestContext.getCurrentInstance().update("form:messages");
            processUploadedFileListCount = 0; //resets counter just in case an error happens
        } catch (final OutOfStorageException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgFileUploadNoSpace(), FacesMessage.SEVERITY_ERROR);
            RequestContext.getCurrentInstance().update("form:messages");
            processUploadedFileListCount = 0; //resets counter just in case an error happens
        }
    }

    private void checkSpaceStillAvailable(final List<RealUploadedFile> realUploadedFileList)
            throws OutOfStorageException {
        final long fileAvailableSpace = fileService.getTotalFileAvailableSpace();
        long uploadedFilesSpace = 0;
        for (final RealUploadedFile realUploadedFile : realUploadedFileList) {
            uploadedFilesSpace += realUploadedFile.getSize();
        }
        if (uploadedFilesSpace >= fileAvailableSpace) {
            throw new OutOfStorageException();
        }
    }

    public void setPdfToDisplayParameters(final String tenantId, final long clientePraticaId,
                                          final FileService.FileCategory fileCategory, final String fileName) {
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.TENANT_ID, tenantId);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.CLIENTE_PRATICA_ID,
                clientePraticaId);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.FILE_CATEGORY, fileCategory);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.FILE_NAME, fileName);
    }

    public void deleteFile(final String tenantId, final long clientePraticaId,
                           final FileService.FileCategory fileCategory, final String fileName) {
        // TODO permessi eliminazione
        try {
            fileService.delete(tenantId, clientePraticaId, fileCategory, fileName);

            // grazie JSF che non mi fai usare le lamba expression e facevi
            // esplodere tutto!!
            final List<FileViewModel> fileViewModelListToRemove = new LinkedList<>();
            for (final FileViewModel fileViewModel : fileViewModelList) {
                if (fileViewModel.getFileName().equals(fileName)) {
                    fileViewModelListToRemove.add(fileViewModel);
                    break;
                }
            }
            fileViewModelList.removeAll(fileViewModelListToRemove);

            if (FileService.FileCategory.ANTIRICICLAGGIO.equals(fileCategory)) {
                clienteManagerService.setAntiriciclaggioFileCaricato(
                        (long) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.PRATICA_ID),
                        false);
            }

            if (fileViewModelList.isEmpty()) {
                FacesUtil.closeDialog(FacesUtil.DialogType.FILE_LIST);
            }
            FacesUtil.addMessage(propertiesUtil.getMsgFileViewDeleted(), FacesMessage.SEVERITY_INFO);

            if (FileService.FileCategory.ANTIRICICLAGGIO.equals(fileCategory)) {
                FacesUtil.execute("PF('antiriciclaggioUploadWidget').uploadedFileCount=0");
            }
        } catch (final UnableToDeleteException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgFileViewDeleteError(), FacesMessage.SEVERITY_ERROR);
        }
    }

}
