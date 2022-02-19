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
 * Created by Giulio on 06/08/2019.
 */
// http://stackoverflow.com/questions/26471225/spring-3-1-or-later-requestmapping-consumes-produces
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-using-type-converters-with-spring-mvc/
@Controller
public class DevisProxPushLeadDisputeController {

    public static final String PUSH_LEAD_DISPUTE_URL = "/esito-contestazione";

    // grazie java che non mi fai fare la enum per usarla nelle annotation :/
    public static final String DEVISPROX_TENANT_ID = "id_agents";
    private static final Logger logger = LoggerFactory.getLogger(DevisProxPushLeadDisputeController.class);
    private final String DEVISPROX_LEAD_ID = "qid";
    private final String DEVISPROX_PRODUCT_ID = "idq";

    private final String LACE_LEAD_ID = "laceLeadID";
    private final String LACE_TENANT_NAME = "laceTenantName";
    private final String RESULT = "result";
    private final String REASON = "reason";

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

    @RequestMapping(value = {PUSH_LEAD_DISPUTE_URL, "/v1" + PUSH_LEAD_DISPUTE_URL}, method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String pushLeadDespute(
//            @RequestParam(value = DEVISPROX_TENANT_ID) final String devisProxTenantId,
//            @RequestParam(value = DEVISPROX_LEAD_ID) final String devisProxLeadId,
//            @RequestParam(value = DEVISPROX_PRODUCT_ID) final String devisProxProductId,

            @RequestParam(value = LACE_LEAD_ID) final String laceLeadID,
            @RequestParam(value = LACE_TENANT_NAME) final String laceTenantName,
            @RequestParam(value = RESULT) final Boolean desputeResult,
            @RequestParam(value = REASON) final String reason) {


//            @RequestParam(value = USERNAME) final String username,
//            @RequestParam(value = PASSWORD) final String password,
//            @RequestParam(value = CHECKSUM) final String checksum) {

        try {
//            final DevisProxTenantDto devisProxTenantDto = tenantManager.getTenantDtoFromDevisProxId(devisProxTenantId);
//            tenantName = devisProxTenantDto.getTenantName();
//            final long tenantAziendaId = devisProxTenantDto.getAziendaId();
//            if (!USERNAME_VALUE.equals(username) || !PASSWORD_VAL            @RequestParam(value = DEVISPROX_TENANT_ID) final String devisProxTenantId,
//            @RequestParam(value = DEVISPROX_LEAD_ID) final String devisProxLeadId,
//            @RequestParam(value = DEVISPROX_PRODUCT_ID) final String devisProxProductId,UE.equals(password)) {
//                throw new RuntimeException("Invalid username or password");
//            }
//            if (surname == null && name == null && city == null) {
//                throw new RuntimeException("Unable to check hash: surname, name and city are null!");
//            }
//            if (!checksum.equalsIgnoreCase(DigestUtils.md5DigestAsHex(StringUtils
//                    .getBytesUtf8(surname + name + org.apache.commons.lang3.StringUtils.stripToEmpty(city))))) {
//                throw new RuntimeException("Invalid checksum: " + checksum);
//            }
//            final ClienteViewModel clienteViewModel = leadService.save(devisProxLeadId, loanAmount, loanLength, clientJobType,
//                    monthlyNetIncome, annualBonus, title, surname, singleSurname, name, dateOfBirth, landlineNumber,
//                    mobileNumber, email, address, country, postCode, city, recruitmentDate, Provenienza.LEAD,
//                    SOURCE_DEVIS_PROX, null, null, null, null, null, null, StatoNominativo.DA_LAVORARE.getValue(), null,
//                    null, null, null, tenantAziendaId);



            final String successMessage = "OK - laceLeadID=" + laceLeadID + " laceTenantName="+laceTenantName+" desputeResult="+desputeResult+" reason="+reason;
//            final String moreSuccessMessage = successMessage + " - DevisProxPartnerId: " + devisProxTenantId + " - TenantName: " + tenantName;
//            logger.info(moreSuccessMessage);
//            smsLeadService.sendLeadNotificaSms(clienteViewModel, tenantName, tenantAziendaId);
            return successMessage;
        } catch (final RuntimeException e) {
//            final String errorMessage = "KO LeadId: " + devisProxLeadId + " - DevisProxPartnerId: " + devisProxTenantId  + " - TenantName: " + tenantName + " - Error: " + e.getMessage();
            final String errorMessage = "ERROR in PUSH_LEAD_DISPUTE api";
            logger.error(errorMessage, e);
            return errorMessage;
        }
    }

}
