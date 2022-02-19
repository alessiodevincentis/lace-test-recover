package com.woonders.lacemsapi.controller;

import com.woonders.lacemsapi.model.app.BaseMessage;
import com.woonders.lacemsapi.model.app.Lead;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Emanuele on 15/05/2017.
 */
@Api
@Controller
public class JsonLacePushLeadController extends BaseLacePushLeadController {

    public static final String LACE_PUSH_LEAD_JSON_URL = LACE_PUSH_LEAD_URL + "/json";

    @ApiOperation(value = "Save lead into LACE")
    @RequestMapping(value = {"/v1" + LACE_PUSH_LEAD_JSON_URL}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    // purtroppo qui tenantId serve per forza perche
    // RequestTenantIdentifierResolver lo prende dai parametri della request,
    // non puo prenderlo dal json
    public ResponseEntity pushLead(@RequestParam String tenantId, @Valid @RequestBody Lead lead, Errors errors) {
        return super.pushLead(lead, errors);
    }

    @Override
    protected ResponseEntity returnErrorMessage(String errorMessage) {
        return new ResponseEntity<>(BaseMessage.builder().message(errorMessage)
                .responseCode(BaseMessage.ResponseCode.MISSING_PARAMETER).build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity returnSuccessMessage(String successMessage) {
        return new ResponseEntity<>(
                BaseMessage.builder().message(successMessage).responseCode(BaseMessage.ResponseCode.OK).build(), null,
                HttpStatus.OK);
    }

}
