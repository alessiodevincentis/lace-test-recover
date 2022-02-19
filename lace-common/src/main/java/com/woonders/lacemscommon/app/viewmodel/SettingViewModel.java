package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entityenum.PreferenzaNumeroMittente;
import lombok.*;

/**
 * Created by Emanuele on 30/01/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class SettingViewModel extends AbstractBaseViewModel {

    private long id;
    private boolean leadIlComparatoreEnabled;
    private boolean simulatorEnabled;
    private int notificaLeadSmsBalance;
    private boolean sendSmsLead;
    private String mittenteSms;
    private String testoSmsLead;
    private String testoSmsAppuntamentoClienteNominativo;
    private String testoSmsAppuntamentoOperatoreAssegnato;
    private String alias;
    private AziendaViewModel aziendaViewModel;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
