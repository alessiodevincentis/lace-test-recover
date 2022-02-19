package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.FileViewModel;
import com.woonders.lacemscommon.exception.NoFileFoundException;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.exception.UnableToGetFileException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;

import java.util.List;

/**
 * Created by Emanuele on 14/11/2016.
 * Manages files uploaded by operators
 */
public interface FileService {

    //100 GB
    long MAXIMUM_STORAGE_SPACE_IN_BYTES = 100000000000L;

    /**
     * Save a new file to the used storage
     */
    void save(String tenantId, long clientePraticaId, FileCategory fileCategory, String filename, byte[] content) throws UnableToSaveFileException;

    /**
     * Retrieves a file displayed in the notice board
     */
    byte[] getFileForNoticeBoard(final String tenantId, final String filename, final long noticeId)
            throws UnableToGetFileException;

    /**
     * Deletes a file displayed in the notice board
     */
    void deleteFileForNoticeBoard(final String tenantId, final String filename, final long noticeId) throws UnableToDeleteException;

    /**
     * Returns how much storage space is left
     */
    long getTotalFileAvailableSpace();

    /**
     * Returns a list of all the files in a given [FileCategory]
     */
    List<FileViewModel> getFileListInCategory(String tenantId, long clientePraticaId, FileCategory fileCategory)
            throws NoFileFoundException;

    /**
     * Returns a file by its [filename]
     */
    byte[] getFile(String tenantId, long clientePraticaId, FileCategory fileCategory, String filename)
            throws UnableToGetFileException;

    /**
     * Deletes a file by its [filename]
     */
    void delete(String tenantId, long clientePraticaId, FileCategory fileCategory, String fileName)
            throws UnableToDeleteException;

    /**
     * Returns how much storage space is currently used
     */
    long getUsedStorageSpace();

    /**
     * Check if a new file can be uploaded
     */
    boolean canUploadFile(long fileSize);

    /**
     * Updates how much storage space is currently used
     */
    void modifyUsedStorageSpace(long sizeToModify, FileOperation fileOperation);

    /**
     * Deletes all files in a given [fileCategory]
     */
    void deleteAllFilesInCategory(String tenantId, long clientePraticaId, FileCategory fileCategory);

    enum FileCategory {
        ANTIRICICLAGGIO, ANAGRAFICA, REDDITO, PRATICA
    }

    enum FileOperation {
        ADD, REMOVE
    }
}
