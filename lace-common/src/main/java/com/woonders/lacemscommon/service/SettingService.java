package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.GeneralSettingViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;

import java.util.List;

/**
 * Created by Emanuele on 30/01/2017.
 */

/**
 * Manages settings
 * [setting] and [general_setting] table
 */
public interface SettingService {

    /**
     * set if you want enable to receive lead from IlComparatore
     */
    SettingViewModel setLeadIlComparatoreEnabled(long aziendaId, boolean value);

    /**
     * save value of [settings] table
     */
    SettingViewModel save(SettingViewModel settingViewModel);

    /**
     * get settings by agency info id
     */
    SettingViewModel getByAziendaId(long aziendaId);

    /**
     * return all settings, could be different when we have more than 1 agency (See MultiAgency feature)
     */
    List<SettingViewModel> findAll();

    /**
     * return values of [general_setting] table
     */
    GeneralSettingViewModel getGeneralSetting();

    /**
     * save general settings
     */
    GeneralSettingViewModel save(GeneralSettingViewModel generalSettingViewModel);

    /**
     * return boolean useful to know if Simulator feature is enable
     */
    boolean isSimulatorEnabledByAziendaId(final long aziendaId);
}
