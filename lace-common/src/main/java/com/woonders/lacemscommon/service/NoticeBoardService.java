package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.NoticeBoardViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.exception.UnableToSaveException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Manages notice history used for internal communications between operators
 * [notice_board] table
 */
public interface NoticeBoardService {
    /**
     * save new notice
     */
    @PreAuthorize("hasAuthority('NOTICE_BOARD_WRITE') or hasAuthority('NOTICE_BOARD_WRITE_SUPER')")
    @Transactional(rollbackFor = {UnableToSaveFileException.class, UnableToSaveException.class})
    NoticeBoardViewModel save(NoticeBoardViewModel noticeBoardViewModel, long aziendaId,
                              Role.RoleName noticeBoardWriteRolename, long operatorId,
                              String tenantId, String fileName, byte[] content)
            throws UnableToSaveFileException, UnableToSaveException;

    /**
     * add operator that can be read the notice
     */
    @Transactional
    void addOperatorIntoOperatorSet(long noticeBoardId, long operatorId);


    /**
     * delete notice
     */
    @PreAuthorize("hasAuthority('NOTICE_BOARD_WRITE') or hasAuthority('NOTICE_BOARD_WRITE_SUPER')")
    @Transactional(rollbackFor = UnableToDeleteException.class)
    void delete(long noticeBoardId) throws UnableToDeleteException;

    /**
     * return boolean useful to know if there is file attached to the notice
     */
    boolean isFileAttached(long noticeBoardId);

    /**
     * return a paged list of all notices
     */
    ViewModelPage<NoticeBoardViewModel> getAllNotices(Long currentAziendaId, long aziendaId, boolean haveReadSuperAuthority,
                                                      int firstElementIndex, int pageSize, String sortField,
                                                      QueryDSLHelper.SortOrder sortOrder);

    /**
     * return notice by id
     */
    NoticeBoardViewModel getNotice(long noticeId);

    /**
     * delete file attached to the notice
     */
    void setFileNameNull(long noticeId);

    /**
     * return the number of notices to be read
     */
    long countNoticesToRead(Long currentAziendaId, long aziendaId, boolean haveReadSuperAuthority,
                            long operatorId);

    /**
     * return a boolean useful to know if a specific notice is already been read
     */
    boolean isNoticeToRead(long operatorId, long noticeId);

    /**
     * return list of the operator who read the notice
     */
    List<OperatorViewModel> getAllOperatorsByNoticeIdExcludeCreatorId(long noticeId, long creatorId);
}
