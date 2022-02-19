package com.woonders.lacemscommon.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.NoticeBoardViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.NoticeBoardViewModelCreator;
import com.woonders.lacemscommon.config.AWSConfiguration;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.NoticeBoard;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.repository.AziendaRepository;
import com.woonders.lacemscommon.db.repository.NoticeBoardRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.exception.UnableToSaveException;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import com.woonders.lacemscommon.service.NoticeBoardService;
import com.woonders.lacemscommon.util.PredicateHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class NoticeBoardServiceImpl implements NoticeBoardService {

    public static final String NAME = "noticeBoardServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    public static final String NOTICE_BOARD_FOLDER_NAME = "notice_board";


    @Autowired
    private NoticeBoardRepository noticeBoardRepository;
    @Autowired
    private NoticeBoardViewModelCreator noticeBoardViewModelCreator;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private PredicateHelper predicateHelper;


    @PreAuthorize("hasAuthority('NOTICE_BOARD_WRITE') or hasAuthority('NOTICE_BOARD_WRITE_SUPER')")
    @Override
    @Transactional(rollbackFor = {UnableToSaveFileException.class, UnableToSaveException.class})
    public NoticeBoardViewModel save(final NoticeBoardViewModel noticeBoardViewModel, final long aziendaId,
                                     final Role.RoleName noticeBoardWriteRolename, final long operatorId,
                                     final String tenantId, final String fileName, final byte[] content)
            throws UnableToSaveFileException, UnableToSaveException {

        final NoticeBoard noticeBoard = noticeBoardViewModelCreator.createModel(noticeBoardViewModel);

        if (noticeBoard.getId() == 0) {
            switch (noticeBoardWriteRolename) {
                case NOTICE_BOARD_WRITE:
                    noticeBoard.setAziendaAssigned(aziendaRepository.getOne(aziendaId));
                    break;
                case NOTICE_BOARD_WRITE_SUPER:
                    break;
            }

            final Operator operatorCreator = operatorRepository.getOne(operatorId);
            final Set<Operator> operatorSet = new HashSet<>();
            operatorSet.add(operatorCreator);

            noticeBoard.setCreatorOperator(operatorCreator);
            noticeBoard.setOperatorSet(operatorSet);
            noticeBoard.setDateTimeCreation(LocalDateTime.now());
        }

        if (content != null && fileName != null) {
            noticeBoard.setFileNameAttached(fileName);
        }

        final NoticeBoard noticeBoardSaved = noticeBoardRepository.save(noticeBoard);

        if (content != null && fileName != null) {
            saveFile(tenantId, fileName, noticeBoardSaved.getId(), content);
        }

        return noticeBoardViewModelCreator.createViewModel(noticeBoardSaved);
    }

    @Override
    @Transactional
    public void addOperatorIntoOperatorSet(final long noticeBoardId, final long operatorId) {
        final Operator operatorToAdd = operatorRepository.getOne(operatorId);
        final NoticeBoard noticeBoard = noticeBoardRepository.getOne(noticeBoardId);
        noticeBoard.getOperatorSet().add(operatorToAdd);
    }

    @PreAuthorize("hasAuthority('NOTICE_BOARD_WRITE') or hasAuthority('NOTICE_BOARD_WRITE_SUPER')")
    @Override
    @Transactional(rollbackFor = UnableToDeleteException.class)
    public void delete(final long noticeBoardId) throws UnableToDeleteException {
        noticeBoardRepository.delete(noticeBoardId);
    }

    @Override
    public boolean isFileAttached(final long noticeBoardId) {
        return noticeBoardId != 0 && !StringUtils.isEmpty(noticeBoardRepository.getOne(noticeBoardId).getFileNameAttached());
    }

    private String generateFileKeyNoticeBoard(final String tenantId, final String filename, final long noticeId) {
        return String.join("/", tenantId, NOTICE_BOARD_FOLDER_NAME, String.valueOf(noticeId), filename);
    }

    private void saveFile(final String tenantId, final String filename, final long noticeId,
                          final byte[] content) throws UnableToSaveFileException {

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(content.length);
        final String fileKey = generateFileKeyNoticeBoard(tenantId, filename, noticeId);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(AWSConfiguration.BUCKET_NAME, fileKey,
                byteArrayInputStream, objectMetadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.AuthenticatedRead);
        try {
            amazonS3.putObject(putObjectRequest);
        } catch (final AmazonClientException e) {
            log.error("Unable to save file of notice board on S3: " + fileKey + " " + e.getMessage());
            throw new UnableToSaveFileException();
        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
        }
    }

    @Override
    public ViewModelPage<NoticeBoardViewModel> getAllNotices(final Long currentAziendaId, final long aziendaId, final boolean haveReadSuperAuthority,
                                                             final int firstElementIndex, final int pageSize, final String sortField,
                                                             final QueryDSLHelper.SortOrder sortOrder) {


        final Page<NoticeBoard> noticeBoardPage;
        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        noticeBoardPage = noticeBoardRepository.findAll(
                predicateHelper.getPredicateForNoticeBoardByAziendaId(currentAziendaId, aziendaId, haveReadSuperAuthority),
                new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

        return new ViewModelPageImpl<>(noticeBoardViewModelCreator.createViewModelList(noticeBoardPage.getContent()),
                noticeBoardPage.getTotalPages(), noticeBoardPage.getTotalElements());
    }

    @Override
    public NoticeBoardViewModel getNotice(final long noticeId) {
        return noticeBoardViewModelCreator.createViewModel(noticeBoardRepository.findOne(noticeId));
    }

    @Override
    @Transactional
    public void setFileNameNull(final long noticeId) {
        noticeBoardRepository.getOne(noticeId).setFileNameAttached(null);
    }

    @Override
    public long countNoticesToRead(final Long currentAziendaId, final long aziendaId, final boolean haveReadSuperAuthority,
                                   final long operatorId) {
        final Operator operator = operatorRepository.findOne(operatorId);
        final Set<Operator> operatorSet = new HashSet<>();
        operatorSet.add(operator);

        if (haveReadSuperAuthority) {
            if (currentAziendaId != null) {
                return noticeBoardRepository.countByOperatorSetNotContainingAndAziendaAssigned_Id(operatorSet, currentAziendaId);
            }
            return noticeBoardRepository.countByOperatorSetNotContaining(operatorSet);

        } else {
            return noticeBoardRepository.countByOperatorSetNotContainingAndAziendaAssigned_Id(operatorSet, aziendaId);

        }
    }

    @Override
    public boolean isNoticeToRead(final long operatorId, final long noticeId) {
        final Operator operator = operatorRepository.findOne(operatorId);
        final Set<Operator> operatorSet = new HashSet<>();
        operatorSet.add(operator);
        return noticeBoardRepository.countByOperatorSetNotContainingAndId(operatorSet, noticeId) != 0;
    }

    @Override
    public List<OperatorViewModel> getAllOperatorsByNoticeIdExcludeCreatorId(final long noticeId, final long creatorId) {
        final NoticeBoardViewModel noticeBoardViewModel = noticeBoardViewModelCreator.createViewModel(noticeBoardRepository.getOne(noticeId));
        return noticeBoardViewModel.getOperatorViewModelList()
                .stream().filter(operatorViewModel -> operatorViewModel.getId() != creatorId)
                .collect(Collectors.toList());
    }
}
