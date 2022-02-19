package com.woonders.lacemsapi.model.app;

import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.Provenienza;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ApiModel(value = "Lead", description = "Class which represent the lead to be received by LACE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead {

    @NotNull(message = "Name required")
    private String nome;
    @NotNull(message = "Surname required")
    private String cognome;
    private String titolo;
    private String codiceFiscale;
    private Cliente.Sesso sesso;
    @ApiModelProperty(dataType = "String", example = "dd/MM/yyyy")
    private LocalDate dataNascita;
    private String luogoNascita;
    private String provinciaNascita;
    private String nazioneNascita;
    private String indirizzoResidenza;
    private String civicoResidenza;
    private String cittaResidenza;
    private String provinciaResidenza;
    private String capResidenza;
    private String nazioneResidenza;
    private String cittadinanza;
    private String email;
    private String cellulare;
    private String telefonoFisso;
    private Impiego impiego;
    @ApiModelProperty(dataType = "String", example = "dd/MM/yyyy")
    private LocalDate dataAssunzione;
    @ApiModelProperty(example = "5000", notes = "Don't use thousand separator")
    private BigDecimal nettoStipendio;
    @ApiModelProperty(example = "5000", notes = "Don't use thousand separator")
    private BigDecimal importoRichiesto;
    @ApiModelProperty(example = "5000", notes = "Don't use thousand separator")
    private BigDecimal bonusAnnuale;
    private Integer durata;
    private String note;
    @ApiModelProperty(notes = "Not required, LEAD will be saved into a 'New lead' state")
    private String statoLead;
    @NotNull(message = "Source required")
    private Provenienza provenienza;
    @NotNull(message = "Source description required")
    private String descrizioneProvenienza;
    @ApiModelProperty(notes = "Unique lead id sent by the third party service to identify the lead")
    private String leadId;
    // altri parametri
    @NotNull(message = "TenantId required")
    @ApiModelProperty(dataType = "String", notes = "It is the name/id of the tenant as represented into LACE")
    private String tenantId;
    @NotNull(message = "Checksum required")
    @ApiModelProperty(dataType = "String", notes = "Generated with name + surname concatenated")
    private String checksum;
    @ApiModelProperty(dataType = "boolean", notes = "If true, LACE will send an SMS to the LEAD to notify him their request was accepted by a consultant")
    private Boolean sendSms;
    @ApiModelProperty(dataType = "long", notes = "This is only to be used if the tenant which receives the lead has more sub-accounts")
    private long aziendaId;

    // se aziendaId = 0 significa che non lo abbiamo passato al webservice,
    // perche magari non ha multiagenzia,
    // quindi ritorniamo 1L
    public long getAziendaId() {
        if (aziendaId == 0) {
            return 1L;
        }
        return aziendaId;
    }

    public Boolean getSendSms() {
        if (sendSms == null) {
            return true;
        }
        return sendSms;
    }

    public enum ClientJobType {

        DIPENDENTE_STATALE("1"), DIPENDENTE_PUBBLICO("2"), DIPENDENTE_PRIVATO_SPA("3"), DIPENDENTE_PARAPUBBLICO(
                "4"), PENSIONATO("5"), ALTRO("6"), DIPENDENTE_PRIVATO_SRL(
                "7"), DIPENDENTE_COOPERATIVA("8"), MILITARE("9"), LIBERO_PROFESSIONISTA("10");

        private final String value;

        ClientJobType(final String value) {
            this.value = value;
        }

        public static Optional<ClientJobType> fromValue(final String value) {
            if (value == null) {
                return Optional.empty();
            }
            for (final Lead.ClientJobType clientJobType : Lead.ClientJobType.values()) {
                if (clientJobType.getValue().equalsIgnoreCase(value)) {
                    return Optional.of(clientJobType);
                }
            }
            throw new IllegalArgumentException("No constant with value " + value);
        }

        public String getValue() {
            return value;
        }

    }

    public enum Title {

        SIG("1"), SIG_INA("2"), SIG_RA("3");

        private final String value;

        private Title(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Country {

    }

}
