package com.woonders.lacemsjsf.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Tipo;

/**
 * Created by Emanuele on 19/06/2017.
 */
@Component
public class CheckPermissionUtils {

    public static final String NAME = "checkPermissionUtils";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private HttpSessionUtil httpSessionUtil;

    //TODO da migliorare. Perche per i clienti guardiamo operatore nominativo??? Non ha senso sta cosa...
    public boolean isAccessibleFromOperator(final ClienteViewModel selectedNominativoViewModel) {
        long currentOperatorId = httpSessionUtil.getOperatorId();
        long currentOperatorAziendaId = httpSessionUtil.getAziendaId();
        if (Tipo.CLIENTE.getValue().equalsIgnoreCase(selectedNominativoViewModel.getTipo())) {
            if (httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_SUPER)) {
                return true;
            }
            if (httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_ALL) && currentOperatorAziendaId == selectedNominativoViewModel.getOperatoreNominativo().getAzienda().getId()) {
                return true;
            }
            if (httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_OWN) && currentOperatorId
                    == selectedNominativoViewModel.getOperatoreNominativo().getId()) {
                return true;
            }
        } else if (Tipo.NOMINATIVO.getValue()
                .equalsIgnoreCase(selectedNominativoViewModel.getTipo())) {
            if (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_SUPER)) {
                return true;
            }
            if (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_ALL) && currentOperatorAziendaId == selectedNominativoViewModel.getOperatoreNominativo().getAzienda().getId()) {
                return true;
            }
            if (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_OWN) && currentOperatorId
                    == selectedNominativoViewModel.getOperatoreNominativo().getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAccessibleFromOperator(final ClientePratica selectedClienteNominativo) {
        long currentOperatorId = httpSessionUtil.getOperatorId();
        long currentOperatorAziendaId = httpSessionUtil.getAziendaId();
        if (Tipo.CLIENTE.getValue().equalsIgnoreCase(selectedClienteNominativo.getClienteViewModel().getTipo())) {
            if (httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_SUPER)) {
                return true;
            }
            if (httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_ALL) && currentOperatorAziendaId == selectedClienteNominativo.getPraticaViewModel().getOperatore().getAzienda().getId()) {
                return true;
            }
            if (httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_OWN) && currentOperatorId
                    == selectedClienteNominativo.getPraticaViewModel().getOperatore().getId()) {
                return true;
            }
        } else if (Tipo.NOMINATIVO.getValue()
                .equalsIgnoreCase(selectedClienteNominativo.getClienteViewModel().getTipo())) {
            if (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_SUPER)) {
                return true;
            }
            if (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_ALL) && currentOperatorAziendaId == selectedClienteNominativo.getClienteViewModel().getOperatoreNominativo().getAzienda().getId()) {
                return true;
            }
            if (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_OWN) && currentOperatorId
                    == selectedClienteNominativo.getClienteViewModel().getOperatoreNominativo().getId()) {
                return true;
            }
        }
        return false;
    }
}
