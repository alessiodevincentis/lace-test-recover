package com.woonders.lacemscommon.config;


import java.util.List;
import java.util.stream.Collectors;

import com.woonders.lacemscommon.db.tenantrepository.SmsTenant;
import com.woonders.lacemscommon.db.tenantrepository.SmsTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Giulio on 31/07/19.
 */
@Component
@Getter
@Slf4j
public class SmsTenantManager {

    public static final String NAME = "smsTenantManager";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String SMSLIST_KEY = "smsList";

    @Autowired
    private SmsTenantRepository smsTenantRepository;


    private List<String> getIdSmsListFromDb() {
        return smsTenantRepository.findAll().stream().map(SmsTenant::getIdSms).collect(Collectors.toList());
    }

}
