package de.hhu.cs.dbs.propra.domain.model.repo;

import de.hhu.cs.dbs.propra.application.exceptions.ResourceNotFoundException;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;

import java.util.List;
import java.util.Optional;

public interface NutzerRepository {

    List<Nutzer> fetchAll();

    Optional<Nutzer> findByName(String name);

    List<Nutzer> findByEmail(String email);

    int findRowId(String name);

    void save(Nutzer nutzer);

    long countByNameAndPassword(String name, String password);
}
