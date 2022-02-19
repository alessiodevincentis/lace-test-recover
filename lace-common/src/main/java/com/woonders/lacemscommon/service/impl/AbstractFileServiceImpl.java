package com.woonders.lacemscommon.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.woonders.lacemscommon.app.viewmodel.FileViewModel;
import com.woonders.lacemscommon.app.viewmodel.GeneralSettingViewModel;
import com.woonders.lacemscommon.db.entity.ClientePraticaFile;
import com.woonders.lacemscommon.db.entity.QClientePraticaFile;
import com.woonders.lacemscommon.db.repository.ClientePraticaFileRepository;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.exception.NoFileFoundException;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import com.woonders.lacemscommon.service.FileService;
import com.woonders.lacemscommon.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by ema98 on 9/14/2017.
 */
@Slf4j
@Transactional(readOnly = true)
public abstract class AbstractFileServiceImpl implements FileService {

    @Autowired
    private SettingService settingService;
    @Autowired
    private ClientePraticaFileRepository clientePraticaFileRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PraticaRepository praticaRepository;

    @Override
    public long getTotalFileAvailableSpace() {
        return FileService.MAXIMUM_STORAGE_SPACE_IN_BYTES - getUsedStorageSpace();
    }

    /**
     * @return used storage space in bytes
     */
    @Override
    public long getUsedStorageSpace() {
        return settingService.getGeneralSetting().getUsedStorageSpace();
    }

    @Override
    public boolean canUploadFile(long fileSize) {
        return getUsedStorageSpace() + fileSize < FileService.MAXIMUM_STORAGE_SPACE_IN_BYTES;
    }

    @Override
    @Transactional
    public void modifyUsedStorageSpace(long sizeToModify, FileService.FileOperation fileOperation) {
        GeneralSettingViewModel generalSettingViewModel = settingService.getGeneralSetting();
        long newUsedStorageSpaceSize;
        switch (fileOperation) {
            case ADD:
                newUsedStorageSpaceSize = generalSettingViewModel.getUsedStorageSpace() + sizeToModify;
                break;
            case REMOVE:
                newUsedStorageSpaceSize = generalSettingViewModel.getUsedStorageSpace() - sizeToModify;
                break;
            default:
                throw new IllegalArgumentException("FileOperation MUST be specified!");
        }
        //security check but should never happen
        if (newUsedStorageSpaceSize < 0) {
            newUsedStorageSpaceSize = 0;
        }
        generalSettingViewModel.setUsedStorageSpace(newUsedStorageSpaceSize);
        settingService.save(generalSettingViewModel);
    }

    @Override
    @Transactional(rollbackFor = UnableToSaveFileException.class)
    public void save(final String tenantId, final long clientePraticaId, final FileCategory fileCategory,
                     String filename, final byte[] content) throws UnableToSaveFileException {

        BooleanBuilder booleanBuilder = getPredicateToRetrieveClientePraticaFile(clientePraticaId, fileCategory, filename);

        Iterable<ClientePraticaFile> clientePraticaFileList = clientePraticaFileRepository.findAll(booleanBuilder);
        try {
            ClientePraticaFile clientePraticaFile = Iterables.getOnlyElement(clientePraticaFileList);
            modifyUsedStorageSpace(clientePraticaFile.getLength(), FileOperation.REMOVE);
            clientePraticaFileRepository.delete(clientePraticaFile.getId());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            //nothing to do if it doesn-t exist
        }

        ClientePraticaFile clientePraticaFile = ClientePraticaFile.builder()
                .fileCategory(fileCategory)
                .fileName(filename)
                .length(content.length)
                .build();

        if (FileCategory.ANAGRAFICA.equals(fileCategory)) {
            clientePraticaFile.setCliente(clienteRepository.getOne(clientePraticaId));
        } else {
            clientePraticaFile.setPratica(praticaRepository.getOne(clientePraticaId));
        }

        clientePraticaFileRepository.save(clientePraticaFile);
        modifyUsedStorageSpace(content.length, FileOperation.ADD);
    }

    private BooleanBuilder getPredicateToRetrieveClientePraticaFile(long clientePraticaId, FileCategory fileCategory, String fileName) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QClientePraticaFile qClientePraticaFile = QClientePraticaFile.clientePraticaFile;
        booleanBuilder.and(qClientePraticaFile.fileCategory.eq(fileCategory));

        if (FileCategory.ANAGRAFICA.equals(fileCategory)) {
            booleanBuilder.and(qClientePraticaFile.cliente.id.eq(clientePraticaId));
        } else {
            booleanBuilder.and(qClientePraticaFile.pratica.codicePratica.eq(clientePraticaId));
        }

        if (!StringUtils.isEmpty(fileName)) {
            booleanBuilder.and(qClientePraticaFile.fileName.eq(fileName));
        }

        return booleanBuilder;
    }

    private BooleanBuilder getPredicateToRetrieveClientePraticaFile(long clientePraticaId, FileCategory fileCategory) {
        return getPredicateToRetrieveClientePraticaFile(clientePraticaId, fileCategory, null);
    }


    @Override
    @Transactional(rollbackFor = UnableToDeleteException.class)
    public void delete(final String tenantId, final long clientePraticaId, final FileCategory fileCategory,
                       final String fileName) throws UnableToDeleteException {
        BooleanBuilder booleanBuilder = getPredicateToRetrieveClientePraticaFile(clientePraticaId, fileCategory, fileName);

        Iterable<ClientePraticaFile> clientePraticaFileList = clientePraticaFileRepository.findAll(booleanBuilder);
        try {
            ClientePraticaFile clientePraticaFile = Iterables.getOnlyElement(clientePraticaFileList);
            modifyUsedStorageSpace(clientePraticaFile.getLength(), FileOperation.REMOVE);
            clientePraticaFileRepository.delete(clientePraticaFile.getId());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new UnableToDeleteException(e);
        }
    }

    @Override
    public List<FileViewModel> getFileListInCategory(String tenantId, long clientePraticaId, FileCategory fileCategory) throws NoFileFoundException {
        Iterable<ClientePraticaFile> clientePraticaFileIterable = clientePraticaFileRepository.findAll(getPredicateToRetrieveClientePraticaFile(clientePraticaId, fileCategory));
        List<FileViewModel> fileViewModelList = new LinkedList<>();
        List<ClientePraticaFile> clientePraticaFileList = Lists.newArrayList(clientePraticaFileIterable);
        for (ClientePraticaFile clientePraticaFile : clientePraticaFileList) {
            FileViewModel fileViewModel = FileViewModel.builder()
                    .tenantId(tenantId)
                    .fileCategory(fileCategory)
                    .fileName(clientePraticaFile.getFileName())
                    .build();
            if (FileCategory.ANAGRAFICA.equals(fileCategory)) {
                fileViewModel.setClientePraticaId(String.valueOf(clientePraticaFile.getCliente().getId()));
            } else {
                fileViewModel.setClientePraticaId(String.valueOf(clientePraticaFile.getPratica().getCodicePratica()));
            }
            fileViewModelList.add(fileViewModel);
        }
        if (fileViewModelList.size() > 0) {
            return fileViewModelList;
        } else {
            throw new NoFileFoundException();
        }
    }

    @Override
    public void deleteAllFilesInCategory(String tenantId, long clientePraticaId, FileCategory fileCategory) {
        try {
            List<FileViewModel> fileViewModelList = getFileListInCategory(tenantId, clientePraticaId, fileCategory);
            for (FileViewModel fileViewModel : fileViewModelList) {
                try {
                    delete(tenantId, clientePraticaId, fileCategory, fileViewModel.getFileName());
                } catch (UnableToDeleteException e) {
                    //speriamo non esploda mai :D
                    log.error("Unable to delete file from S3", e);
                }
            }
        } catch (NoFileFoundException e) {
            //if no file, nothing to delete
        }
    }
}
