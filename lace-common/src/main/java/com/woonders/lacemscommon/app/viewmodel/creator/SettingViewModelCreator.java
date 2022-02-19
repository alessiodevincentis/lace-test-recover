package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.db.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Emanuele on 30/01/2017.
 */
@Component
public class SettingViewModelCreator extends AbstractBaseViewModelCreator<Setting, SettingViewModel> {

    @Autowired
    private AziendaViewModelCreator aziendaViewModelCreator;

    @Override
    public Setting createModel(SettingViewModel viewModel) {
        if (viewModel != null) {
            return Setting.builder().id(viewModel.getId())
                    .leadIlComparatoreEnabled(viewModel.isLeadIlComparatoreEnabled())
                    .simulatorEnabled(viewModel.isSimulatorEnabled())
                    .agenziaSmsBalance(viewModel.getNotificaLeadSmsBalance()).sendSmsLead(viewModel.isSendSmsLead())
                    .mittenteSms(viewModel.getMittenteSms()).testoSmsLead(viewModel.getTestoSmsLead())
                    .testoSmsAppuntamentoClienteNominativo(viewModel.getTestoSmsAppuntamentoClienteNominativo())
                    .testoSmsAppuntamentoOperatoreAssegnato(viewModel.getTestoSmsAppuntamentoOperatoreAssegnato())
                    .azienda(aziendaViewModelCreator.createModel(viewModel.getAziendaViewModel()))
                    .alias(viewModel.getAlias()).build();
        }
        return null;
    }

    @Override
    public SettingViewModel createViewModel(Setting model) {
        if (model != null) {
            return SettingViewModel.builder().id(model.getId())
                    .leadIlComparatoreEnabled(model.isLeadIlComparatoreEnabled())
                    .simulatorEnabled(model.isSimulatorEnabled())
                    .notificaLeadSmsBalance(model.getAgenziaSmsBalance()).sendSmsLead(model.isSendSmsLead())
                    .mittenteSms(model.getMittenteSms()).testoSmsLead(model.getTestoSmsLead())
                    .testoSmsAppuntamentoClienteNominativo(model.getTestoSmsAppuntamentoClienteNominativo())
                    .testoSmsAppuntamentoOperatoreAssegnato(model.getTestoSmsAppuntamentoOperatoreAssegnato())
                    .aziendaViewModel(aziendaViewModelCreator.createViewModel(model.getAzienda()))
                    .alias(model.getAlias()).build();
        }
        return null;
    }
}
