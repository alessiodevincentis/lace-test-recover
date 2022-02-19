package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.exception.UnableToGetFileException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Used for the backup functionality
 * [backup] table
 */
public interface BackupService {

    /**
     * Returns a list with details of all procedure and their related customers
     */
    List<ClienteViewModel> findAllPratiche(int firstElementIndex, int pageSize);

    /**
     * Returns a list with details of all leads
     */
    List<ClienteViewModel> findAllNominativi(int firstElementIndex, int pageSize);

    /**
     * Returns a list with details of all "amministrazione"
     */
    List<AmministrazioneViewModel> findAllAmministrazioni(int firstElementIndex, int pageSize);

    /**
     * Save the backup file once it's being populated with all the necessary info
     */
    void saveBackupFile(final String tenantId,
                        final String filename, final byte[] content) throws UnableToSaveFileException;

    /**
     * Retrieves the backup file
     */
    byte[] getFileBackup(final String tenantId, final String filename) throws UnableToGetFileException;

    /**
     * Returns last date a backup was done
     */
    LocalDateTime getLastBackupDate();

    /**
     * Updates the password needed to download a backup file
     */
    void updatePasswordBackup(final String password);

    /**
     * Updates the last backup date
     */
    LocalDateTime updateDateBackup();

    /**
     * Check the password given is correct to be able to access the backup file
     */
    boolean checkPassword(final String password);
}
