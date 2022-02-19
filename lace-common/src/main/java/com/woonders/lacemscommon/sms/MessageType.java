package com.woonders.lacemscommon.sms;

/**
 * Created by Emanuele on 18/02/2017.
 */
public enum MessageType {

    SMS_CAMPAGNA("smsCampagna"), SMS_NOTIFICA_LEAD("smsNotificaLead"), SMS_CALENDARIO("smsCalendario"), SMS_SINGLE("smsSingle");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public static MessageType fromValue(final String value) {
        if (value == null) {
            return null;
        }
        for (MessageType messageType : MessageType.values()) {
            if (messageType.value.equalsIgnoreCase(value)) {
                return messageType;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value);
    }

    public String getValue() {
        return value;
    }
}
