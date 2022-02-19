package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.NoticeBoard;
import com.woonders.lacemscommon.db.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>, QueryDslPredicateExecutor<NoticeBoard> {

    long countByOperatorSetNotContainingAndAziendaAssigned_Id(Set<Operator> operatorSet, long aziendaId);

    long countByOperatorSetNotContaining(Set<Operator> operatorSet);

    long countByOperatorSetNotContainingAndId(Set<Operator> operatorSet, long noticeId);
}
