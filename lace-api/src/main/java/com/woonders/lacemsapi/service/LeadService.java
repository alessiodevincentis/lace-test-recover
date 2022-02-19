package com.woonders.lacemsapi.service;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.Provenienza;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LeadService {

    /**
     * @param leadId           - leadId
     * @param loanAmount       - importoRichiesto
     * @param loanLength       - durataRichiestaMesi
     * @param clientJobType    - impiego
     * @param monthlyNetIncome - nettoMensileNominativo
     * @param annualBonus      - NOT USED
     * @param title            - NOT USED
     * @param surname          - cognome
     * @param singleSurname    - cognome da nubile, usato in base a cosa arriva dal Lead
     * @param name             - nome
     * @param dateOfBirth      - dataNascita
     * @param landlineNumber   - telefonoFisso
     * @param mobileNumber     - telefono
     * @param email            - email
     * @param address          - indirizzoResidenza
     * @param country          - TO BE ADDED
     * @param postCode         - capResidenza
     * @param city             - luogoResidenza
     * @param recruitmentDate  - dataInizio (occupazione)
     * @param provenienza      - tipo provenienza
     * @param source           - provenienza/fornitore lead
     * @param message          - note
     * @param birthPlace       - luogo nascita
     * @param birthProvince    - provincia nascita
     * @param sex              - sesso
     * @param addressProvince  - provincia residenza
     * @param aziendaId        - aziendaId per il multiagenzia
     */
    ClienteViewModel save(String leadId, BigDecimal loanAmount, Integer loanLength, Impiego clientJobType,
                          BigDecimal monthlyNetIncome, BigDecimal annualBonus, String title, String surname, String singleSurname,
                          String name, LocalDate dateOfBirth, String landlineNumber, String mobileNumber, String email,
                          String address, String country, String postCode, String city, LocalDate recruitmentDate,
                          Provenienza provenienza, String source, String message, String birthPlace, String birthProvince,
                          Cliente.Sesso sex, String addressProvince, String cf, String statoLead, String cittadinanza,
                          String nazioneNascita, String nazioneResidenza, String civicoResidenza, long aziendaId);
}
