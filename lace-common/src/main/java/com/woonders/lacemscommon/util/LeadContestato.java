package com.woonders.lacemscommon.util;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadContestato {

    private long laceLeadID;                // id del nominativo in Lace
    private String laceTenantName;          // tenant name in Lace
    private String devisProxTenantId;       // tenantID in DevisProx

    private String devisProxLeadId;         // id del nominativo in DevisProx

    private int requestReason;              // motivo della contestazione
    private String requestNote;             // informazioni aggiuntive (commento o log)

    private Boolean resultResponse;         // esito della contestazione
    private String  resultReason;           // motivo del rifiuto, se la contestazione viene rifiutata

    private ArrayList<LeadContestatoLog> logs;
}

