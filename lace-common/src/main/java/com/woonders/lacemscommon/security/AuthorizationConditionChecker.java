package com.woonders.lacemscommon.security;

import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Emanuele on 23/05/2017.
 */
@Slf4j
@Component
@Transactional(readOnly = true)
public class AuthorizationConditionChecker {

    @Autowired
    private PraticaRepository praticaRepository;

    /**
     * Non capisco perche se gli passo #authentication o #principal dal
     * PreAuthorize sono sempre null.....
     *
     * @param aziendaId
     * @return
     */
    public boolean isSameAziendaId(final Long aziendaId) {
        return aziendaId != null
                && ((CustomSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getAziendaId() == aziendaId;
    }

    public Long getAziendaIfFromCodicePratica(final Long codicePratica) {
        final Pratica pratica = praticaRepository.findOne(codicePratica);
        if (pratica != null) {
            return pratica.getOperatore().getAzienda().getId();
        }
        return null;
    }

    public boolean isPraticaAssignedTo(final String username, final Long codicePratica) {
        final Pratica pratica = praticaRepository.findOne(codicePratica);
        return pratica != null && pratica.getOperatore().getUsername().equals(username);
    }
}
