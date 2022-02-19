package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "azienda")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Azienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "nomeazienda")
    private String nomeAzienda;
    @Column(name = "indirizzo")
    private String indirizzo;
    @Column(name = "luogo")
    private String luogo;
    @Column(name = "provincia")
    private String provincia;
    @Column(name = "cap", length = 45)
    private String cap;
    @Column(name = "telefono", length = 45)
    private String telefono;
    @Column(name = "fax", length = 45)
    private String fax;
    @Column(name = "email", length = 45)
    private String email;
    @Column(name = "pec", length = 45)
    private String pec;
    @Column(name = "piva", length = 45)
    private String piva;
    @Column(name = "cf", length = 45)
    private String cf;
    @Column(name = "oam", length = 45)
    private String oam;
    @Column(name = "sitoweb")
    private String sitoWeb;
    @Column(name = "nometitolare", length = 45)
    private String nomeTitolare;
    @Column(name = "cognometitolare", length = 45)
    private String cognomeTitolare;
    @Column(name = "giorninotifichecliente")
    private Integer giorniNotificheCliente;
    @Column(name = "giorninotifichenominativi")
    private Integer giorniNotificheNominativi;
    @Column(name = "printPdfEnabled")
    private boolean printPdfEnabled;
    @Column(unique = true)
    private String devisProxId;
    @Column(name = "cellulare", length = 45)
    private String cellulare;
    @Column(name = "giorniNotificaConteggioEstinzione")
    private Integer giorniNotificaConteggioEstinzione;
}
