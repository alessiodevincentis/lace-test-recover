package com.woonders.lacemsapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.woonders.lacemscommon.config.TenantManager;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.service.*;
import com.woonders.lacemscommon.util.LeadContestato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Created by Giulio on 06/08/2019.
 */
@Slf4j
@RestController
public class DevisProxPushLeadDisputeControllerV2 {
    public static final String PUSH_LEAD_DISPUTE_URL = "/esito-contestazione2";
    public static final String DEVISPROX_TENANT_ID = "id_agents";
    private final String DEVISPROX_LEAD_ID = "qid";

    @Autowired
    private TenantManager tenantManager;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private LogService logService;

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST,
                    value = PUSH_LEAD_DISPUTE_URL,
                    produces = "application/json",
                    consumes = {"application/json","multipart/form-data", "multipart/mixed"})
    public String esitoContestazioneLead(HttpServletResponse response,
                              @RequestBody String leadContestatoString,
                              @RequestHeader(value = "Authorization", defaultValue = "") String authToken,
                              @RequestHeader(value = "Accept-Encoding", defaultValue = "") String contentHeader) throws Exception {


        log.info("---------------------------------------------  in DevisProxPushLeadDisputeControllerV2");
        log.info("---------------------------------------------- esitoContestazioneLead - LEAD CONTESTATO BODY "+leadContestatoString);
        JsonObject leadContestatoJson = new JsonParser().parse(leadContestatoString).getAsJsonObject();
                
        String devisProxLeadID = String.valueOf(leadContestatoJson.get("qid").getAsInt());
        Cliente nominativoContestato = clienteRepository.findByLeadId(devisProxLeadID); // findById(leadContestato.getLaceLeadID());
        if (nominativoContestato == null) {
            log.error("nominativo inestistente");
        return "KO - ERROR: LEAD NOT FOUND";
        }
        if (nominativoContestato.getStatoNominativo().equalsIgnoreCase(StatoNominativo.CONTESTATO.getValue()) == false){
            log.error("LEAD NON IN FASE DI CONTESTAZIONE: stato corrente "+ nominativoContestato.getStatoNominativo());
            return "KO - ERROR: LEAD NOT DISPUTED";
        }
        if (leadContestatoJson.get("resultResponse").getAsBoolean()){
            log.info("contestazione accettata");
            nominativoContestato.setStatoNominativo(StatoNominativo.CONTESTAZIONE_ACCETTATA.getValue());

            logService.logCustomAction(nominativoContestato.getId(), LogService.TipoLog.NOMINATIVO, "La contestazione del nominativo è stata accettata dal fornitore DevisProx",
                    LogService.CustomAction.RICEZ_CONTESTAZIONE_OK, nominativoContestato.getOperatoreNominativo().getId());
        }
        else{
            log.info("contestazione rifiutata");
            nominativoContestato.setStatoNominativo(StatoNominativo.CONTESTAZIONE_RIFIUTATA.getValue());
            JsonElement resultReason = leadContestatoJson.get("resultReason");
            String reason = "Nessuna ragione ricevuta";
            if (resultReason != null)
            	reason = resultReason.getAsString();
            logService.logCustomAction(nominativoContestato.getId(), LogService.TipoLog.NOMINATIVO, "Contestazione rifiutata, motivazione: "+reason,
                    LogService.CustomAction.RICEZ_CONTESTAZIONE_KO, nominativoContestato.getOperatoreNominativo().getId());
        }
        clienteRepository.save(nominativoContestato);

        return "OK";
        
//        Gson gsonBuilder = new GsonBuilder().create();
//        log.info(gsonBuilder.toJson(leadContestato));
//        OutputStream out = response.getOutputStream();
//        out.write(gsonBuilder.toJson(leadContestato).getBytes());
//        out.flush();
//        out.close();
    }
}