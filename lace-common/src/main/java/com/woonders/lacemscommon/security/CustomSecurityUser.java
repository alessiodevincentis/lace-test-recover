package com.woonders.lacemscommon.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by Emanuele on 14/09/2016.
 */
@Getter
@Setter
public class CustomSecurityUser extends User {

    private long id;
    private boolean storageEnabled;
    private boolean printPdfEnabled;
    private long aziendaId;

    public CustomSecurityUser(final String username, final String password,
                              final Collection<? extends GrantedAuthority> authorities, final long id,
                              final boolean storageEnabled,
                              final boolean printPdfEnabled, final long aziendaId) {
        super(username, password, authorities);
        init(id, storageEnabled, printPdfEnabled, aziendaId);
    }

    public CustomSecurityUser(final String username, final String password, final boolean enabled,
                              final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
                              final Collection<? extends GrantedAuthority> authorities, final long id,
                              final boolean storageEnabled,
                              final boolean printPdfEnabled, final long aziendaId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        init(id, storageEnabled, printPdfEnabled, aziendaId);
    }

    private void init(final long id, final boolean storageEnabled, final boolean printPdfEnabled, final long aziendaId) {
        this.id = id;
        this.storageEnabled = storageEnabled;
        this.printPdfEnabled = printPdfEnabled;
        this.aziendaId = aziendaId;
    }

}
