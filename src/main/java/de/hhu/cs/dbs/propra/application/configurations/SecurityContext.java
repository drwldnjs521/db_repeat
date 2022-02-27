package de.hhu.cs.dbs.propra.application.configurations;

import de.hhu.cs.dbs.propra.domain.model.Nutzer;

import de.hhu.cs.dbs.propra.domain.model.Role;

import java.security.Principal;

public class SecurityContext implements javax.ws.rs.core.SecurityContext {
    private Nutzer nutzer;
    @Override
    public Principal getUserPrincipal() {
        return nutzer;
    }

    @Override
    public boolean isUserInRole(String role) {
        return nutzer != null && nutzer.getRoles().contains(Role.valueOf(role));
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return BASIC_AUTH;
    }

    public void setUser(Nutzer nutzer) {
        this.nutzer = nutzer;
    }
}
