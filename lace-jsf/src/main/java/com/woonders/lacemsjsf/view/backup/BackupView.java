package com.woonders.lacemsjsf.view.backup;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.woonders.lacemscommon.app.backupexcel.ExcelWriterComponent;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.UnableToGetFileException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import com.woonders.lacemscommon.exception.UnableToSendEmailException;
import com.woonders.lacemscommon.service.BackupService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.BackupServiceImpl;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ViewScoped
@ManagedBean(name = BackupView.NAME)
public class BackupView implements Serializable {

    public static final String NAME = "backupView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String FILENAME = "_backup.xlsx";
    private static final long DO_BACKUP_INTERVAL_DAYS = 30;
    private static final long BACKUP_EXPIRATION_INTERVAL_DAYS = 3;
    private static final String LACE = "Lace";

    @ManagedProperty(ExcelWriterComponent.JSF_NAME)
    private ExcelWriterComponent excelWriterComponent;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    private byte[] backupFile;
    @ManagedProperty(BackupServiceImpl.JSF_NAME)
    private BackupService backupService;
    @ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
    private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    private LocalDateTime dateLastBackup;
    private String password;
    private String emailOperator;


    @PostConstruct
    public void init() {
        dateLastBackup = backupService.getLastBackupDate();
        emailOperator = operatorService.getOne(httpSessionUtil.getOperatorId()).getEmail();
    }

    public String actionBackup() {
        if (!httpSessionUtil.hasAuthority(Role.RoleName.BACKUP_DOWNLOAD)) {
            FacesUtil.addMessage(propertiesUtil.getMsgAccessoNonConsentito(), FacesMessage.SEVERITY_WARN);
            return null;
        }
        if (dateLastBackup != null && dateLastBackup.plusDays(DO_BACKUP_INTERVAL_DAYS).isAfter(LocalDateTime.now())) {
            FacesUtil.addMessage(propertiesUtil.getMsgBackupNotPossible(), FacesMessage.SEVERITY_WARN);
            return null;
        }
        try {
            final String tenantId = requestTenantIdentifierResolver.getTenantIdentifier();
            backupService.updateDateBackup();
            excelWriterComponent.startBackupFile(tenantId, tenantId.concat(FILENAME), emailOperator,
                    LACE, propertiesUtil.getMsgBackupReplyto(), propertiesUtil.getMsgBackupSubject(),
                    propertiesUtil.getMsgBackupBody());
            FacesUtil.addMessage(propertiesUtil.getMsgBackupSuccess());
            return Constants.getDashboardPath(true);
        } catch (IllegalAccessException | UnableToSaveFileException | IOException | UnableToSendEmailException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric());
            return Constants.getDashboardPath(true);
        }
    }

    public StreamedContent downloadBackupFile() throws IllegalArgumentException {
        if (backupFile == null) {
            final String tenantId = requestTenantIdentifierResolver.getTenantIdentifier();
            if (!backupService.checkPassword(password)) {
                FacesUtil.addMessage(propertiesUtil.getMsgBackupPasswordWrong(), FacesMessage.SEVERITY_WARN);
                return null;
            }
            try {
                backupFile = backupService.getFileBackup(tenantId, tenantId.concat(FILENAME));
                password = "";
                return generateDefaultStreamedContentWithBackupFile();
            } catch (final UnableToGetFileException | AmazonS3Exception e) {
                FacesUtil.addMessage(propertiesUtil.getMsgBackupDownloadFailed(), FacesMessage.SEVERITY_WARN);
                return null;
            }
        }
        return generateDefaultStreamedContentWithBackupFile();
    }

    private DefaultStreamedContent generateDefaultStreamedContentWithBackupFile() {
        return new DefaultStreamedContent(new ByteArrayInputStream(backupFile),
                "application/vnd.ms-excel", "Backup-Lace.xlsx");
    }


    public boolean isDisabledActionBackupButton() {
        return StringUtils.isEmpty(emailOperator);
    }

    public boolean isDownloadBackupButtonDisabled() {
        return dateLastBackup == null || dateLastBackup.plusDays(BACKUP_EXPIRATION_INTERVAL_DAYS).isBefore(LocalDateTime.now());
    }
}