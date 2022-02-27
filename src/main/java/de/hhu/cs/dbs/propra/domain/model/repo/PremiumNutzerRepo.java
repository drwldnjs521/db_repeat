package de.hhu.cs.dbs.propra.domain.model.repo;

import de.hhu.cs.dbs.propra.domain.model.Premiumnutzer;

import java.util.List;

public interface PremiumNutzerRepo {

    List<Premiumnutzer> fetchAll();

    List<Premiumnutzer> fetchByAbgelaufen(boolean abgelaufen);

}
