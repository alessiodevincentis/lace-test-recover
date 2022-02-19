package com.woonders.lacemscommon.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.querydsl.core.types.Predicate;
import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.AmministrazioneViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.config.AWSConfiguration;
import com.woonders.lacemscommon.db.entity.Backup;
import com.woonders.lacemscommon.db.entity.QCliente;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.repository.AmministrazioneRepository;
import com.woonders.lacemscommon.db.repository.BackupRepository;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.exception.UnableToGetFileException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import com.woonders.lacemscommon.service.BackupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class BackupServiceImpl implements BackupService {

    public static final String NAME = "backupServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";


    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private AmministrazioneViewModelCreator amministrazioneViewModelCreator;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AmministrazioneRepository amministrazioneRepository;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private BackupRepository backupRepository;

    @Override
    public List<ClienteViewModel> findAllPratiche(final int firstElementIndex, final int pageSize) {
        final QCliente qCliente = QCliente.cliente;
        final Predicate soloClientiPredicate = qCliente.tipo.eq(Tipo.CLIENTE.getValue());
        return clienteViewModelCreator.createViewModelList(clienteRepository.findAll(soloClientiPredicate, new PageRequest(firstElementIndex / pageSize, pageSize)));
    }

    @Override
    public List<ClienteViewModel> findAllNominativi(final int firstElementIndex, final int pageSize) {
        final QCliente qCliente = QCliente.cliente;
        final Predicate soloNominativiPredicate = qCliente.tipo.eq(Tipo.NOMINATIVO.getValue());
        return clienteViewModelCreator.createViewModelList(clienteRepository.findAll(soloNominativiPredicate, new PageRequest(firstElementIndex / pageSize, pageSize)));
    }

    @Override
    public List<AmministrazioneViewModel> findAllAmministrazioni(final int firstElementIndex, final int pageSize) {
        return amministrazioneViewModelCreator.createViewModelList(amministrazioneRepository.findAll(new PageRequest(firstElementIndex / pageSize, pageSize)));
    }

    private String generateFileKeyBackup(final String tenantId, final String filename) {
        return String.join("/", tenantId, filename);
    }


    @Override
    @Transactional(rollbackFor = UnableToSaveFileException.class)
    public void saveBackupFile(final String tenantId,
                               final String filename, final byte[] content) throws UnableToSaveFileException {

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(content.length);
        final String fileKey = generateFileKeyBackup(tenantId, filename);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(AWSConfiguration.BUCKET_NAME_BACKUP, fileKey,
                byteArrayInputStream, objectMetadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.AuthenticatedRead);
        try {
            amazonS3.putObject(putObjectRequest);
        } catch (final AmazonClientException e) {
            log.error("Unable to save file on S3: " + fileKey + " " + e.getMessage());
            throw new UnableToSaveFileException();
        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
        }
    }

    @Override
    public byte[] getFileBackup(final String tenantId, final String filename) throws UnableToGetFileException {
        final String fileKey = generateFileKeyBackup(tenantId, filename);
        final GetObjectRequest getObjectRequest = new GetObjectRequest(AWSConfiguration.BUCKET_NAME_BACKUP, fileKey);
        S3ObjectInputStream objectInputStream = null;
        try {
            final S3Object s3Object = amazonS3.getObject(getObjectRequest);
            objectInputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(objectInputStream);
        } catch (final IOException e) {
            log.error("Unable to get file on S3: " + fileKey + " " + e.getMessage());
            throw new UnableToGetFileException();
        } finally {
            IOUtils.closeQuietly(objectInputStream);
        }
    }

    @Override
    public LocalDateTime getLastBackupDate() {
        return backupRepository.getOne(1L).getDateLastBackup();
    }

    @Override
    @Transactional
    public void updatePasswordBackup(final String password) {
        final Backup backup = backupRepository.getOne(1L);
        backup.setPasswordBackup(BCrypt.hashpw(password, BCrypt.gensalt()));
    }

    @Override
    @Transactional
    public LocalDateTime updateDateBackup() {
        final Backup backup = backupRepository.getOne(1L);
        backup.setDateLastBackup(LocalDateTime.now());
        return backup.getDateLastBackup();
    }

    @Override
    public boolean checkPassword(final String password) {
        final Backup backup = backupRepository.getOne(1L);
        return backup.getPasswordBackup() != null && BCrypt.checkpw(password, backup.getPasswordBackup());
    }

}
