package com.woonders.lacemsjsf.view.pdf;

public enum FinanziariaEnum {

    ITALCREDI("italcredi"), MEDIOLANUM("mediolanum");

    private final String fieldName;

    FinanziariaEnum(final String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
