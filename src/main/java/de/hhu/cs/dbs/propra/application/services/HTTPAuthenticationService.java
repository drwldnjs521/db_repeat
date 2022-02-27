package de.hhu.cs.dbs.propra.application.services;

import de.hhu.cs.dbs.propra.domain.model.repo.NutzerRepository;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public abstract class HTTPAuthenticationService implements AuthenticationService {
    public static final String REALM = "Restricted access";

    @Inject
    protected NutzerRepository nutzerRepository;

    public abstract boolean isSecure();

    public abstract boolean validateHeader(String header);

    public String getRealm() {
        return REALM;
    }

    public String getWWWAuthenticateHeader() {
        return StringUtils.capitalize(getAuthenticationScheme().toLowerCase()) + " realm=\"" + getRealm() + "\"";
    }
}
