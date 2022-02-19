package com.woonders.lacemsjsf.util;

import com.woonders.lacemsjsf.config.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by ema98 on 7/31/2017.
 */
@Component
public class ExceptionResolverUtil {

    public static final String NAME = "exceptionResolverUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PropertiesUtil propertiesUtil;

    //serviva prima per le eccezioni catchate direttamente nei bean, ma non ha piu molto senso.
    // Anche perche nemmeno lo usiamo piu. Per ora comunque lo segno deprecato e lo lascio, vediamo...
    @Deprecated
    public String getMessageToShowFromException(DataAccessException e) {
        String throwableClassName = e.getMostSpecificCause().getClass().getName().toLowerCase();
        String throwableExceptionMessage = e.getMostSpecificCause().getMessage();
        return getMessageToShowFromException(throwableClassName, throwableExceptionMessage);
    }

    public String getMessageToShowFromException(Throwable throwable) {
        String throwableClassName = throwable.getClass().getName().toLowerCase();
        String throwableExceptionMessage = throwable.getMessage();
        return getMessageToShowFromException(throwableClassName, throwableExceptionMessage);
    }

    private String getMessageToShowFromException(String throwableClassName, String throwableExceptionMessage) {
        if(throwableClassName.contains("integrity")) {
            String violatedKey = StringUtils.substringBetween(throwableExceptionMessage, "key '", "_UNIQUE'");
            if("cf".equalsIgnoreCase(violatedKey)) {
                return propertiesUtil.getMsgCfErrorGiaUsato();
            } else if("name".equalsIgnoreCase(violatedKey)) {
                return propertiesUtil.getMsgTableNameAlreadyUsed();
            }
        } else if(throwableClassName.contains("truncation")) {
            String column = StringUtils.substringBetween(throwableExceptionMessage, "'", "'");
            return MessageFormat.format(propertiesUtil.getMsgErrorColumnTruncated(), column);
        }
        return propertiesUtil.getMsgErrorGeneric();
    }
}
