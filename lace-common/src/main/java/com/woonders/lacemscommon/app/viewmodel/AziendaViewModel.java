package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class AziendaViewModel extends AbstractBaseViewModel {

    private long id;
    private String nomeAzienda;
    private String indirizzo;
    private String luogo;
    private String provincia;
    private String cap;
    private String telefono;
    private String fax;
    private String email;
    private String pec;
    private String piva;
    private String cf;
    private String oam;
    private String sitoWeb;
    private String nomeTitolare;
    private String cognomeTitolare;
    private Integer giorniNotificheCliente;
    private Integer giorniNotificheNominativi;
    private boolean printPdfEnabled;
    private String devisProxId;
    private String cellulare;
    private Integer giorniNotificaConteggioEstinzione;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
