package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by ema98 on 8/9/2017.
 */
//@Controller
//@RequestMapping("/setting/*")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @RequestMapping(value = {ControllerConstants.V1 + "/setLeadIlComparatoreEnabled"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SettingViewModel setLeadIlComparatoreEnabled(final long aziendaId, final boolean value) {
        return settingService.setLeadIlComparatoreEnabled(aziendaId, value);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/save"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SettingViewModel save(final SettingViewModel settingViewModel) {
        return settingService.save(settingViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getByAziendaId"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SettingViewModel getByAziendaId(final long aziendaId) {
        return settingService.getByAziendaId(aziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findAll"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SettingViewModel> findAll() {
        return settingService.findAll();
    }
}
