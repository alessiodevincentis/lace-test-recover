package com.woonders.lacemsapi.controller;

import com.woonders.lacemsapi.model.app.Lead;
import com.woonders.lacemsapi.service.LeadService;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.service.SmsLeadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.validation.Errors;

import java.util.stream.Collectors;

/**
 * Created by Emanuele on 15/05/2017.
 */
@Slf4j
public abstract class BaseLacePushLeadController {

    protected static final String LACE_PUSH_LEAD_URL = "/lacepushlead";

    @Autowired
    private LeadService leadService;
    @Autowired
    private SmsLeadService smsLeadService;

    public ResponseEntity pushLead(final Lead lead, final Errors errors) {
        if (errors.hasErrors()) {
            return logAndReturnErrorMessage(errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", ")));
        }

        // altri controlli custom sui campi
        if (org.springframework.util.StringUtils.isEmpty(lead.getCellulare())
                && org.springframework.util.StringUtils.isEmpty(lead.getTelefonoFisso())) {
            return logAndReturnErrorMessage("Recapiti Mancanti - cellulare o telefono fisso obbligatori");
        }
        if (!lead.getChecksum().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(StringUtils.getBytesUtf8(lead.getNome() + lead.getCognome())))) {
            return logAndReturnErrorMessage("Invalid checksum: " + lead.getChecksum());
        }

        // calcolo stato nominativo corretto
        String statoLead;
        try {
            final StatoNominativo statoNominativo = StatoNominativo.fromValue(lead.getStatoLead());
            if (statoNominativo == null) {
                statoLead = StatoNominativo.DA_LAVORARE.getValue();
            } else {
                statoLead = statoNominativo.getValue();
            }
        } catch (final IllegalArgumentException e) {
            statoLead = StatoNominativo.DA_LAVORARE.getValue();
        }

        // controlli ok, salviamo
        final ClienteViewModel clienteViewModel = leadService.save(lead.getLeadId(), lead.getImportoRichiesto(), lead.getDurata(),
                lead.getImpiego(), lead.getNettoStipendio(), lead.getBonusAnnuale(), lead.getTitolo(),
                lead.getCognome(), lead.getCognome(), lead.getNome(), lead.getDataNascita(), lead.getTelefonoFisso(),
                lead.getCellulare(), lead.getEmail(), lead.getIndirizzoResidenza(), lead.getNazioneResidenza(),
                lead.getCapResidenza(), lead.getCittaResidenza(), lead.getDataAssunzione(), lead.getProvenienza(),
                lead.getDescrizioneProvenienza(), lead.getNote(), lead.getLuogoNascita(), lead.getProvinciaNascita(),
                lead.getSesso(), lead.getProvinciaResidenza(), lead.getCodiceFiscale(), statoLead,
                lead.getCittadinanza(), lead.getNazioneNascita(), lead.getNazioneResidenza(), lead.getCivicoResidenza(),
                lead.getAziendaId());

        final String successMessage = "OK";
        log.info(successMessage);
        if (lead.getSendSms()) {
            smsLeadService.sendLeadNotificaSms(clienteViewModel, lead.getTenantId(), lead.getAziendaId());
        }
        return returnSuccessMessage(successMessage);
    }

    private ResponseEntity logAndReturnErrorMessage(final String errorMessage) {
        log.error(errorMessage);
        return returnErrorMessage(errorMessage);
    }

    protected abstract ResponseEntity returnErrorMessage(String errorMessage);

    protected abstract ResponseEntity returnSuccessMessage(String successMessage);
}
