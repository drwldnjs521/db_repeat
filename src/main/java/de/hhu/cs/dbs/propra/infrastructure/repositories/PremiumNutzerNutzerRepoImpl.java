package de.hhu.cs.dbs.propra.infrastructure.repositories;

import de.hhu.cs.dbs.propra.domain.model.repo.PremiumNutzerRepo;
import de.hhu.cs.dbs.propra.domain.model.Premiumnutzer;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PremiumNutzerNutzerRepoImpl implements PremiumNutzerRepo {

    @Inject
    private DataSource dataSource;

    List<Premiumnutzer> premiumnutzerList = new ArrayList<>();

    @Override
    public List<Premiumnutzer> fetchAll() {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT p.ROWID, p.Benutzername, p.Premiumfrist, n.ROWID, n.Email, n.Passwort FROM Premiumnutzer p JOIN Nutzer n ON p.Benutzername = n.Benutzername;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                premiumnutzerList.add(mappingPremiumNutzer(resultSet));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return premiumnutzerList;
    }

    @Override
    public List<Premiumnutzer> fetchByAbgelaufen(boolean abgelaufen) {
        premiumnutzerList = fetchAll();
        return fetchAll().stream()
                .filter(p -> p.isAbgelaufen() == abgelaufen)
                .collect(Collectors.toList());
    }


    public Premiumnutzer mappingPremiumNutzer(ResultSet rs) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return new Premiumnutzer("lee", LocalDateTime.now().format(myFormatObj) );

    }

}


