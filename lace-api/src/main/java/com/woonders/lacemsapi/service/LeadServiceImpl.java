package com.woonders.lacemsapi.service;

import com.woonders.lacemsapi.model.app.Lead;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Residenza;
import com.woonders.lacemscommon.db.entityenum.FornitoreLead;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.Provenienza;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.db.repository.ResidenzaRepository;
import com.woonders.lacemscommon.db.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
@Slf4j
public class LeadServiceImpl implements LeadService {

    public final static String NAME = "leadServiceImpl";

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ResidenzaRepository residenzaRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;

    @Override
    public ClienteViewModel save(final String leadId, final BigDecimal loanAmount, final Integer loanLength,
                                 final Impiego clientJobType, final BigDecimal monthlyNetIncome, final BigDecimal annualBonus,
                                 final String title, final String surname, final String singleSurname, final String name,
                                 final LocalDate dateOfBirth, final String landlineNumber, final String mobileNumber, final String email,
                                 final String address, final String country, final String postCode, final String city,
                                 final LocalDate recruitmentDate, final Provenienza provenienza, final String source, final String message,
                                 final String birthPlace, final String birthProvince, final Cliente.Sesso sex, final String addressProvince,
                                 final String cf, final String statoLead, final String cittadinanza, final String nazioneNascita,
                                 final String nazioneResidenza, final String civicoResidenza, final long aziendaId) {

        // qui decidiamo a quale operatore assegnare il lead e poi aumentiamo il
        // suo counter
        final Operator operator = operatorRepository
                .findTop1ByReceiveLeadEnabledTrueAndAzienda_IdOrderByLeadRicevutiAsc(aziendaId);
        operator.setLeadRicevuti(operator.getLeadRicevuti() + 1);
        operatorRepository.save(operator);

        // non funziona poi in lace se del lead salviamo il toString(), misteri!
        // TODO quando si convertono i field in enum nelle entity, questi vanno
        // cambiati!!!
        Cliente cliente = Cliente.builder().leadId(leadId).importoRichiesto(loanAmount)
                .durataRichiestaMesi(loanLength).nettoMensileNominativo(monthlyNetIncome).nome(name)
                .telefonoFisso(landlineNumber).telefono(mobileNumber).email(email).tipo(Tipo.NOMINATIVO.toString())
                .provenienza(provenienza.getValue()).provenienzaDesc(source).dataIns(new Date())
                .statoNominativo(statoLead).note(message).luogoNascita(birthPlace).provNascita(birthProvince).sesso(sex)
                .cf(cf).cittadinanza(cittadinanza).nazioneNascita(nazioneNascita).build();

        cliente.setOperatoreNominativo(operator);

        if (clientJobType != null) {
            cliente.setImpiego(clientJobType.getValue());
        }

        if (dateOfBirth != null) {
            cliente.setDataNascita(Date.from(Instant.from(dateOfBirth.atStartOfDay(ZoneId.of("GMT")))));
        }

        if (recruitmentDate != null) {
            cliente.setDataInizio(Date.from(Instant.from(recruitmentDate.atStartOfDay(ZoneId.of("GMT")))));
        }

        if (monthlyNetIncome == null) {
            cliente.setNettoMensileNominativo(BigDecimal.ZERO);
        }

        if (loanAmount == null) {
            cliente.setImportoRichiesto(BigDecimal.ZERO);
        }
        if (Lead.Title.SIG_RA.getValue().equals(title) && singleSurname != null && !singleSurname.isEmpty()) {
            cliente.setCognome(singleSurname);
        } else {
            cliente.setCognome(surname);
        }

        try {
            cliente.setFornitoreLead(FornitoreLead.fromValue(source).getValue());
        } catch (final IllegalArgumentException e) {
            log.info("Unable to get fornitore lead " + source);
        }

        try {
            cliente = clienteRepository.save(cliente);

            if (address != null || postCode != null || city != null || addressProvince != null) {
                final Residenza residenza = Residenza.builder().indirizzoResidenza(address).capResidenza(postCode)
                        .luogoResidenza(city).provResidenza(addressProvince).civicoResidenza(civicoResidenza)
                        .nazioneResidenza(nazioneResidenza).build();
                residenza.setCliente(cliente);
                residenzaRepository.save(residenza);
            }
            final ClienteViewModel clienteViewModel = clienteViewModelCreator.createViewModel(cliente);
            log.info("Lead stored: " + clienteViewModel);

            return clienteViewModel;
        } catch (final DataAccessException e) {
            log.error("Unable to store Lead: " + clienteViewModelCreator.createViewModel(cliente), e);
            throw new RuntimeException(e.getRootCause().getMessage());
        }

    }

}
