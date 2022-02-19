package com.woonders.lacemsapi.controller;

import com.woonders.lacemsapi.service.LeadService;
import com.woonders.lacemsapi.service.LeadServiceImpl;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.config.DevisProxTenantDto;
import com.woonders.lacemscommon.config.TenantManager;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.Provenienza;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.service.SmsLeadService;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Emanuele on 23/10/2016.
 */
// http://stackoverflow.com/questions/26471225/spring-3-1-or-later-requestmapping-consumes-produces
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-using-type-converters-with-spring-mvc/
@Controller
public class DevisProxPushLeadController {

    public static final String PUSH_LEAD_URL = "/pushlead";

    // grazie java che non mi fai fare la enum per usarla nelle annotation :/
    public static final String DEVISPROX_TENANT_ID = "id_agents";
    private static final Logger logger = LoggerFactory.getLogger(DevisProxPushLeadController.class);
    private final String DEVISPROX_LEAD_ID = "qid";
    private final String DEVISPROX_PRODUCT_ID = "idq";
    private final String LOAN_AMOUNT = "montant_emprunt";
    private final String LOAN_LENGTH = "duree_emprunt";
    private final String CLIENT_JOB_TYPE = "type_secteur";
    private final String MONTHLY_NET_INCOME = "salaire";
    private final String ANNUAL_BONUS = "prime";
    private final String TITLE = "civilite";
    private final String SURNAME = "nom";
    private final String MARRIED_SURNAME = "nom_jf";
    private final String NAME = "prenom";
    private final String DATE_OF_BIRTH = "dob";
    private final String LANDLINE_NUMBER = "tel_domicile";
    private final String MOBILE_NUMBER = "tel_mobile";
    private final String EMAIL = "email";
    private final String ADDRESS = "adresse";
    private final String COUNTRY = "pays";
    private final String POST_CODE = "cp";
    private final String CITY = "ville";
    private final String RECRUITMENT_DATE = "date_embauche";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String CHECKSUM = "checksum";
    private final String SOURCE_DEVIS_PROX = "DevisProx";
    private final String USERNAME_VALUE = "";
    private final String PASSWORD_VALUE = "";
    @Autowired
    @Qualifier(LeadServiceImpl.NAME)
    private LeadService leadService;
    @Autowired
    private SmsLeadService smsLeadService;

    @Autowired
    private TenantManager tenantManager;

    private String tenantName;

    @RequestMapping(value = {PUSH_LEAD_URL, "/v1" + PUSH_LEAD_URL}, method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String pushLead(@RequestParam(value = DEVISPROX_TENANT_ID) final String devisProxTenantId,
                           @RequestParam(value = DEVISPROX_LEAD_ID) final String devisProxLeadId,
                           @RequestParam(value = DEVISPROX_PRODUCT_ID) final String devisProxProductId,
                           @RequestParam(value = LOAN_AMOUNT, required = false) final BigDecimal loanAmount,
                           @RequestParam(value = LOAN_LENGTH, required = false) final Integer loanLength,
                           @RequestParam(value = CLIENT_JOB_TYPE, required = false) final Impiego clientJobType,
                           @RequestParam(value = MONTHLY_NET_INCOME, required = false) final BigDecimal monthlyNetIncome,
                           @RequestParam(value = ANNUAL_BONUS, required = false) final BigDecimal annualBonus,
                           @RequestParam(value = TITLE, required = false) final String title,
                           @RequestParam(value = SURNAME, required = false) final String surname,
                           @RequestParam(value = MARRIED_SURNAME, required = false) final String singleSurname,
                           @RequestParam(value = NAME, required = false) final String name,
                           @RequestParam(value = DATE_OF_BIRTH, required = false) final LocalDate dateOfBirth,
                           @RequestParam(value = LANDLINE_NUMBER, required = false) final String landlineNumber,
                           @RequestParam(value = MOBILE_NUMBER, required = false) final String mobileNumber,
                           @RequestParam(value = EMAIL, required = false) final String email,
                           @RequestParam(value = ADDRESS, required = false) final String address,
                           @RequestParam(value = COUNTRY, required = false) final String country,
                           @RequestParam(value = POST_CODE, required = false) final String postCode,
                           @RequestParam(value = CITY, required = false) final String city,
                           @RequestParam(value = RECRUITMENT_DATE, required = false) final LocalDate recruitmentDate,
                           @RequestParam(value = USERNAME) final String username,
                           @RequestParam(value = PASSWORD) final String password,
                           @RequestParam(value = CHECKSUM) final String checksum) {

        try {
            final DevisProxTenantDto devisProxTenantDto = tenantManager.getTenantDtoFromDevisProxId(devisProxTenantId);
            tenantName = devisProxTenantDto.getTenantName();
            final long tenantAziendaId = devisProxTenantDto.getAziendaId();
            if (!USERNAME_VALUE.equals(username) || !PASSWORD_VALUE.equals(password)) {
                throw new RuntimeException("Invalid username or password");
            }
            if (surname == null && name == null && city == null) {
                throw new RuntimeException("Unable to check hash: surname, name and city are null!");
            }
            if (!checksum.equalsIgnoreCase(DigestUtils.md5DigestAsHex(StringUtils
                    .getBytesUtf8(surname + name + org.apache.commons.lang3.StringUtils.stripToEmpty(city))))) {
                throw new RuntimeException("Invalid checksum: " + checksum);
            }
            final ClienteViewModel clienteViewModel = leadService.save(devisProxLeadId, loanAmount, loanLength, clientJobType,
                    monthlyNetIncome, annualBonus, title, surname, singleSurname, name, dateOfBirth, landlineNumber,
                    mobileNumber, email, address, country, postCode, city, recruitmentDate, Provenienza.LEAD,
                    SOURCE_DEVIS_PROX, null, null, null, null, null, null, StatoNominativo.DA_LAVORARE.getValue(), null,
                    null, null, null, tenantAziendaId);

            final String successMessage = "OK " + devisProxLeadId;
            final String moreSuccessMessage = successMessage + " - DevisProxPartnerId: " + devisProxTenantId + " - TenantName: " + tenantName;
            logger.info(moreSuccessMessage);
            smsLeadService.sendLeadNotificaSms(clienteViewModel, tenantName, tenantAziendaId);
            return successMessage;
        } catch (final RuntimeException e) {
            final String errorMessage = "KO LeadId: " + devisProxLeadId + " - DevisProxPartnerId: " + devisProxTenantId  + " - TenantName: " + tenantName + " - Error: " + e.getMessage();
            logger.error(errorMessage, e);
            return errorMessage;
        }
    }

}
