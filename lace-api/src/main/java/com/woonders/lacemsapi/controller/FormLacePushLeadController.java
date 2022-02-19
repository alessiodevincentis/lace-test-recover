package com.woonders.lacemsapi.controller;

import com.woonders.lacemsapi.model.app.Lead;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by Emanuele on 15/05/2017.
 */
@Api
@Controller
public class FormLacePushLeadController extends BaseLacePushLeadController {

    public static final String LACE_PUSH_LEAD_FORM_URL = LACE_PUSH_LEAD_URL;

    @ApiOperation(value = "Save lead into LACE")
    @Override
    @RequestMapping(value = {"/v1" + LACE_PUSH_LEAD_FORM_URL}, method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity pushLead(@Valid @ModelAttribute Lead lead, Errors errors) {
        return super.pushLead(lead, errors);
    }

    @Override
    protected ResponseEntity returnErrorMessage(java.lang.String errorMessage) {
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity returnSuccessMessage(java.lang.String successMessage) {
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

}
