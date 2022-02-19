package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.GeneralSettingViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.GeneralSettingViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.SettingViewModelCreator;
import com.woonders.lacemscommon.db.entity.GeneralSetting;
import com.woonders.lacemscommon.db.entity.Setting;
import com.woonders.lacemscommon.db.repository.GeneralSettingRepository;
import com.woonders.lacemscommon.db.repository.SettingRepository;
import com.woonders.lacemscommon.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Emanuele on 30/01/2017.
 */
@Service
@Transactional(readOnly = true)
public class SettingServiceImpl implements SettingService {

    public static final String NAME = "settingServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";
    @Autowired
    private SettingViewModelCreator settingViewModelCreator;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private GeneralSettingRepository generalSettingRepository;

    @Autowired
    private GeneralSettingViewModelCreator generalSettingViewModelCreator;

    @Override
    @Transactional
    public SettingViewModel setLeadIlComparatoreEnabled(final long aziendaId, final boolean value) {
        final Setting setting = settingRepository.findOne(aziendaId);
        setting.setLeadIlComparatoreEnabled(value);
        return settingViewModelCreator.createViewModel(settingRepository.save(setting));
    }

    //Esplodeva per gli sms di notifica lead
    //@PreAuthorize("hasAuthority('PREFERENZE_WRITE_SUPER') or (hasAuthority('PREFERENZE_WRITE') and @authorizationConditionChecker.isSameAziendaId(#settingViewModel.id))")
    @Override
    @Transactional
    public SettingViewModel save(final SettingViewModel settingViewModel) {
        final Setting setting = settingViewModelCreator.createModel(settingViewModel);
        return settingViewModelCreator.createViewModel(settingRepository.save(setting));
    }

    @Override
    public SettingViewModel getByAziendaId(final long aziendaId) {
        return settingViewModelCreator.createViewModel(settingRepository.findByAzienda_Id(aziendaId));
    }

    @Override
    public List<SettingViewModel> findAll() {
        return settingViewModelCreator.createViewModelList(settingRepository.findAll());
    }

    @Override
    public GeneralSettingViewModel getGeneralSetting() {
        return generalSettingViewModelCreator.createViewModel(generalSettingRepository.findOne(1L));
    }

    @Override
    public GeneralSettingViewModel save(final GeneralSettingViewModel generalSettingViewModel) {
        final GeneralSetting generalSetting = generalSettingViewModelCreator.createModel(generalSettingViewModel);
        return generalSettingViewModelCreator.createViewModel(generalSettingRepository.save(generalSetting));
    }

    @Override
    public boolean isSimulatorEnabledByAziendaId(final long aziendaId) {
        final Setting setting = settingRepository.findByAzienda_Id(aziendaId);
        if (setting != null) {
            return setting.isSimulatorEnabled();
        }
        return false;
    }
}
