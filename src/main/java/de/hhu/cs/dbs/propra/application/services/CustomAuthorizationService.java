package de.hhu.cs.dbs.propra.application.services;

import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.model.repo.NutzerRepository;
import de.hhu.cs.dbs.propra.domain.model.Role;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

public class CustomAuthorizationService implements AuthorizationService {
    @Inject
    private NutzerRepository nutzerRepository;

    @Override
    public boolean authorise(String name, Set<Role> rolesAllowed) {
        Optional<Nutzer> nutzer = nutzerRepository.findByName(name);
        if (nutzer.isEmpty()) return false;
        return nutzer.get().getRoles().stream().anyMatch(rolesAllowed::contains);
    }
}
