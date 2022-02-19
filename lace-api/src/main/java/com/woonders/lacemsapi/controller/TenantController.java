package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.db.tenantrepository.SmsTenant;
import com.woonders.lacemscommon.db.tenantrepository.SmsTenantRepository;
import com.woonders.lacemscommon.db.tenantrepository.Tenant;
import com.woonders.lacemscommon.db.tenantrepository.TenantRepository;
import com.woonders.lacemscommon.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ema98 on 8/9/2017.
 */
@Controller
@RequestMapping(ControllerConstants.V1 + "/tenant/*")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private SmsTenantRepository smsTenantRepo;


    @RequestMapping(value = {"/isTenantAvailable"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean isTenantAvailable(@RequestParam final String tenantName) {
        final List<String> tenantNameList = tenantService.getActiveTenantList().stream()
                .map(Tenant::getName)
                .collect(Collectors.toList());

        smsTenantRepo.findAll().forEach( SmsTenant::getTenantName );
        return tenantNameList.contains(tenantName);
    }
}
