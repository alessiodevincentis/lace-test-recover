package com.woonders.lacemsjsf.util;

import com.woonders.lacemscommon.exception.UnableToSendEmailException;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorConverter {

    public static final String NAME = "errorConverter";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PropertiesUtil propertiesUtil;

    public String convertSendEmailError(UnableToSendEmailException.UnableToSendEmailError unableToSendEmailError) {
        if (unableToSendEmailError == null) {
            return propertiesUtil.getMsgErrorGeneric();
        } else {
            switch (unableToSendEmailError) {
                case MISSING_RECIPIENT:
                    return propertiesUtil.getMsgErrorMailRecipientMissing();
                case MISSING_AGENCY_NAME:
                    return propertiesUtil.getMsgErrorMailAgencyNameMissing();
                case MISSING_REPLY_TO:
                    return propertiesUtil.getMsgErrorMailReplyToMissing();
                case MISSING_SUBJECT:
                    return propertiesUtil.getMsgErrorMailSubjectMissing();
                case MISSING_BODY:
                    return propertiesUtil.getMsgErrorMailBodyMissing();
                default:
                    return propertiesUtil.getMsgErrorGeneric();
            }
        }
    }
}
