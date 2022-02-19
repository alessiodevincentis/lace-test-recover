package com.woonders.lacemscommon.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.AccessLogViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.AccessLogViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.AccessLog;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.QAccessLog;
import com.woonders.lacemscommon.db.entity.QOperator;
import com.woonders.lacemscommon.db.repository.AccessLogRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by emanuele on 13/09/18.
 */
@Service
@Transactional(readOnly = true)
public class AccessLogServiceImpl implements AccessLogService {

    public static final String NAME = "accessLogServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private AccessLogViewModelCreator accessLogViewModelCreator;
    
    private InetAddress ip_host;
    private String ip_code;

    @Override
    public AccessLogViewModel getOne(long id) {
        return accessLogViewModelCreator.createViewModel(accessLogRepository.getOne(id));
    }

    @Transactional
    @Override
    public void saveLogin(String operatorName) {
    	try {
			ip_host = InetAddress.getLocalHost();
			ip_code = ip_host.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	 Operator operator = operatorRepository.findByUsername(operatorName);
	     accessLogRepository.save(new AccessLog(null, operator, LocalDateTime.now(), ip_code, null));
    }

    @Override
    public ViewModelPage<AccessLogViewModel> getAccessLogList(Long currentAziendaId, LocalDateTime startLoginDateTime, LocalDateTime endLoginDateTime,
                                                              List<String> selectedOperatorList, int firstElementIndex, int pageSize, String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters) {
        // filters non usati ora nella tabella!

        // per ora ordina sempre in base alla data di login
        QSort loginDateTimeDescSort = new QSort(QAccessLog.accessLog.loginDateTime.desc());
        Page<AccessLog> accessLogPage;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (selectedOperatorList != null && !selectedOperatorList.isEmpty()) {
            booleanBuilder.and(QOperator.operator.username.in(selectedOperatorList));
        }
        if (startLoginDateTime != null) {
            booleanBuilder.and(QAccessLog.accessLog.loginDateTime.after(startLoginDateTime));
        }
        if (endLoginDateTime != null) {
            booleanBuilder.and(QAccessLog.accessLog.loginDateTime.before(endLoginDateTime));
        }

        if (currentAziendaId != null) {
            accessLogPage = accessLogRepository.findAll(
                    booleanBuilder.and(QAccessLog.accessLog.operator.azienda.id.eq(currentAziendaId)),
                    new PageRequest(firstElementIndex / pageSize, pageSize, loginDateTimeDescSort));
        } else {
            accessLogPage = accessLogRepository
                    .findAll(booleanBuilder, new PageRequest(firstElementIndex / pageSize, pageSize, loginDateTimeDescSort));
        }

        return new ViewModelPageImpl<>(accessLogViewModelCreator.createViewModelList(accessLogPage.getContent()),
                accessLogPage.getTotalPages(), accessLogPage.getTotalElements());
    }
}
