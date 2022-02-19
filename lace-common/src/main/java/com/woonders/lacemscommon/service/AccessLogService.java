package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.AccessLogViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Manages access to Lace saving/retrieving login events done by operators
 * [access_log] table
 */
public interface AccessLogService {

    AccessLogViewModel getOne(long id);

    /**
     * Store a login event done by operatorName
     */
    void saveLogin(String operatorName);

    /**
     * Returns a list of login events to be displayed filtered and paged
     */
    ViewModelPage<AccessLogViewModel> getAccessLogList(Long currentAziendaId, LocalDateTime startLoginDateTime, LocalDateTime endLoginDateTime,
                                                       List<String> selectedOperatorList, int firstElementIndex, int pageSize,
                                                       String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);
}
