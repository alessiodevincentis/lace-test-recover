package com.woonders.lacemscommon.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.woonders.lacemscommon.config.AWSConfiguration;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.exception.UnableToGetFileException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.woonders.lacemscommon.service.impl.NoticeBoardServiceImpl.NOTICE_BOARD_FOLDER_NAME;

/**
 * Created by emanuele on 28/11/16.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class AmazonS3FileServiceImpl extends AbstractFileServiceImpl {

    public static final String NAME = "amazonS3FileServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String CLIENTI_FOLDER_NAME = "clienti";


    @Autowired
    private AmazonS3 amazonS3;

    private String generateFileKey(final String tenantId, final long clientePraticaId,
                                   final FileCategory fileCategory, final String filename) {
        String clientePraticaIdToUse = String.valueOf(clientePraticaId);
        switch (fileCategory) {
            case ANAGRAFICA:
                return String.join("/", tenantId, CLIENTI_FOLDER_NAME, clientePraticaIdToUse, fileCategory.toString(), filename);
            default:
                return String.join("/", tenantId, clientePraticaIdToUse, fileCategory.toString(), filename);
        }
    }

    @Override
    @Transactional(rollbackFor = UnableToSaveFileException.class)
    public void save(final String tenantId, final long clientePraticaId, final FileCategory fileCategory,
                     final String filename, final byte[] content) throws UnableToSaveFileException {
        super.save(tenantId, clientePraticaId, fileCategory, filename, content);

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(content.length);
        final String fileKey = generateFileKey(tenantId, clientePraticaId, fileCategory, filename);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(AWSConfiguration.BUCKET_NAME, fileKey,
                byteArrayInputStream, objectMetadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.AuthenticatedRead);
        try {
            amazonS3.putObject(putObjectRequest);
        } catch (final AmazonClientException e) {
            log.error("Unable to save file on S3: " + fileKey);
            throw new UnableToSaveFileException();
        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
        }
    }

    @Override
    public byte[] getFile(final String tenantId, final long clientePraticaId, final FileCategory fileCategory,
                          final String filename) throws UnableToGetFileException {
        final String fileKey = generateFileKey(tenantId, clientePraticaId, fileCategory, filename);
        final GetObjectRequest getObjectRequest = new GetObjectRequest(AWSConfiguration.BUCKET_NAME, fileKey);
        final S3ObjectInputStream objectInputStream = null;
        return getFileFromS3(objectInputStream, getObjectRequest, fileKey);
    }

    private byte[] getFileFromS3(S3ObjectInputStream objectInputStream, final GetObjectRequest getObjectRequest, final String fileKey)
            throws UnableToGetFileException {
        try {
            final S3Object s3Object = amazonS3.getObject(getObjectRequest);
            objectInputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(objectInputStream);
        } catch (final AmazonClientException | IOException e) {
            log.error("Unable to retrieve file from S3: " + fileKey);
            throw new UnableToGetFileException();
        } finally {
            IOUtils.closeQuietly(objectInputStream);
        }
    }

    @Override
    public byte[] getFileForNoticeBoard(final String tenantId, final String filename, final long noticeId)
            throws UnableToGetFileException {
        final String fileKey = generateFileKeyNoticeBoard(tenantId, filename, noticeId);
        final GetObjectRequest getObjectRequest = new GetObjectRequest(AWSConfiguration.BUCKET_NAME, fileKey);
        final S3ObjectInputStream objectInputStream = null;
        return getFileFromS3(objectInputStream, getObjectRequest, fileKey);
    }

    private String generateFileKeyNoticeBoard(final String tenantId, final String filename, final long noticeId) {
        return String.join("/", tenantId, NOTICE_BOARD_FOLDER_NAME, String.valueOf(noticeId), filename);
    }

    @Override
    @Transactional(rollbackFor = UnableToDeleteException.class)
    public void delete(final String tenantId, final long clientePraticaId, final FileCategory fileCategory,
                       final String fileName) throws UnableToDeleteException {
        super.delete(tenantId, clientePraticaId, fileCategory, fileName);
        final String fileKey = generateFileKey(tenantId, clientePraticaId, fileCategory, fileName);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(AWSConfiguration.BUCKET_NAME, fileKey);
        deleteFileFromS3(deleteObjectRequest, fileKey);
    }


    @PreAuthorize("hasAuthority('NOTICE_BOARD_WRITE') or hasAuthority('NOTICE_BOARD_WRITE_SUPER')")
    @Override
    @Transactional(rollbackFor = UnableToDeleteException.class)
    public void deleteFileForNoticeBoard(final String tenantId, final String filename, final long noticeId) throws UnableToDeleteException {
        final String fileKey = generateFileKeyNoticeBoard(tenantId, filename, noticeId);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(AWSConfiguration.BUCKET_NAME, fileKey);
        deleteFileFromS3(deleteObjectRequest, fileKey);
    }

    private void deleteFileFromS3(final DeleteObjectRequest deleteObjectRequest, final String fileKey)
            throws UnableToDeleteException {
        try {
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (final AmazonClientException e) {
            log.error("Unable to delete file from S3: " + fileKey);
            throw new UnableToDeleteException();
        }
    }

}
