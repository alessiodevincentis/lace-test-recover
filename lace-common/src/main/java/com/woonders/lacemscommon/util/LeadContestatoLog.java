package com.woonders.lacemscommon.util;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadContestatoLog {
    private String executionDateTime;
    private String commento;
    private String stato;

}
