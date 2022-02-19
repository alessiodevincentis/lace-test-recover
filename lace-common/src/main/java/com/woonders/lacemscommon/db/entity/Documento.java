package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Emanuele on 07/11/2016.
 */
@Entity
@Table(name = "documento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Documento {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "codicedoc", unique = true, nullable = false)
    private long codiceDoc;
    @Column(name = "tipodocumento", length = 25)
    private String tipoDocumento;
    @Column(name = "numerodoc", length = 20)
    private String numeroDoc;
    @Temporal(TemporalType.DATE)
    @Column(name = "rilasciodoc")
    private Date rilascioDoc;
    @Column(name = "luogorilasciodoc")
    private String luogoRilascioDoc;
    @Column(name = "provinciaRilascio")
    private String provinciaRilascio;
    @Column(name = "nazioneRilascio")
    private String nazioneRilascio;
    @Temporal(TemporalType.DATE)
    @Column(name = "scadenzadoc")
    private Date scadenzaDoc;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    public enum TipoDocumento {

        CARTA_IDENTITA("Carta di identità"), PATENTE("Patente"), PASSAPORTO("Passaporto");

        private final String value;

        private TipoDocumento(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
