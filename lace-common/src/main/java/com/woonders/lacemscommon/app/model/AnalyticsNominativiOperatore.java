package com.woonders.lacemscommon.app.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsNominativiOperatore {

    private String username;
    private long nominativiTot;
    private long nominativiToClienti;
}
