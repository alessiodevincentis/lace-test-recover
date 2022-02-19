package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.exception.UnableToGetFileException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import com.woonders.lacemscommon.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Emanuele on 14/11/2016.
 */
// inutilizzato ora, ma magari utile per test in locale
@Service
@Transactional(readOnly = true)
public class LocalFileServiceImpl extends AbstractFileServiceImpl {

    public static final String NAME = "localFileServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private boolean tenantFolderExists(final String tenantId) {
        return Files.exists(FileSystems.getDefault().getPath(tenantId));
    }

    private boolean clientePraticaFolderExists(final String tenantId, final long clientePraticaId) {
        return Files.exists(FileSystems.getDefault().getPath(tenantId, String.valueOf(clientePraticaId)));
    }

    private boolean categoryFolderExists(final String tenantId, final long clientePraticaId,
                                         final FileService.FileCategory fileCategory) {
        return Files.exists(FileSystems.getDefault().getPath(tenantId, String.valueOf(clientePraticaId), fileCategory.toString()));
    }

    private boolean fileExists(final String tenantId, final long clientePraticaId, final FileService.FileCategory fileCategory,
                               final String fileName) {
        return Files.exists(
                FileSystems.getDefault().getPath(tenantId, String.valueOf(clientePraticaId), fileCategory.toString(), fileName));
    }

    @Override
    public byte[] getFileForNoticeBoard(final String tenantId, final String filename, final long noticeId) throws UnableToGetFileException {
        return new byte[0];
    }

    @Override
    public void deleteFileForNoticeBoard(final String tenantId, final String filename, final long noticeId) throws UnableToDeleteException {

    }

    @Override
    @Transactional(rollbackFor = UnableToSaveFileException.class)
    public void save(final String tenantId, final long clientePraticaId, final FileCategory fileCategory,
                     final String filename, final byte[] content) throws UnableToSaveFileException {
        super.save(tenantId, clientePraticaId, fileCategory, filename, content);

        try {
            if (!tenantFolderExists(tenantId)) {
                Files.createDirectory(FileSystems.getDefault().getPath(tenantId));
            }
            if (!clientePraticaFolderExists(tenantId, clientePraticaId)) {
                Files.createDirectory(FileSystems.getDefault().getPath(tenantId, String.valueOf(clientePraticaId)));
            }
            if (!categoryFolderExists(tenantId, clientePraticaId, fileCategory)) {
                Files.createDirectory(
                        FileSystems.getDefault().getPath(tenantId, String.valueOf(clientePraticaId), fileCategory.toString()));
            }
            Files.write(Paths.get(tenantId, String.valueOf(clientePraticaId), fileCategory.toString(), filename), content);

        } catch (final IOException e) {
            throw new UnableToSaveFileException(e);
        }
    }

    @Override
    public byte[] getFile(final String tenantId, final long clientePraticaId, final FileCategory fileCategory,
                          final String filename) throws UnableToGetFileException {
        try {
            if (fileExists(tenantId, clientePraticaId, fileCategory, filename)) {
                return Files.readAllBytes(Paths.get(tenantId, String.valueOf(clientePraticaId), fileCategory.toString(), filename));
            }
        } catch (final IOException e) {
            throw new UnableToGetFileException(e);
        }
        throw new UnableToGetFileException();
    }

    @Override
    @Transactional(rollbackFor = UnableToDeleteException.class)
    public void delete(final String tenantId, final long clientePraticaId, final FileCategory fileCategory,
                       final String fileName) throws UnableToDeleteException {
        super.delete(tenantId, clientePraticaId, fileCategory, fileName);
        try {
            Files.delete(Paths.get(tenantId, String.valueOf(clientePraticaId), fileCategory.toString(), fileName));
        } catch (final IOException e) {
            throw new UnableToDeleteException(e);
        }
    }

}
