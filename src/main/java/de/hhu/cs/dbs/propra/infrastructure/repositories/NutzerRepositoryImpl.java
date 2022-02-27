package de.hhu.cs.dbs.propra.infrastructure.repositories;

import de.hhu.cs.dbs.propra.application.exceptions.ResourceNotFoundException;
import de.hhu.cs.dbs.propra.application.exceptions.SQLExceptionMapper;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.model.repo.NutzerRepository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


public class NutzerRepositoryImpl implements NutzerRepository {
    @Inject
    private DataSource dataSource;

    public List<Nutzer> fetchAll() {
        List<Nutzer> nutzerList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT ROWID, * FROM Nutzer;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                nutzerList.add(mappingNutzer(resultSet));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return nutzerList;
    }

    @Override
    public Optional<Nutzer> findByName(String name) {
        Optional<Nutzer> optionalNutzer = Optional.empty();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT ROWID, * FROM Nutzer n WHERE n.Benutzername LIKE :name"; // TODO: Dem Benutzer 'name' die entsprechenden Berechtigungen geben (siehe enum Role), um diese per RolesAllowed-Annotation zu nutzen. Ein Ergebnistupel besteht aus E-Mail-Adresse bzw. Benutzernamen und Berechtigung.
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            Nutzer nutzer = mappingNutzer(resultSet);
            optionalNutzer = Optional.ofNullable(nutzer);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
       return optionalNutzer;
    }

    @Override
    public List<Nutzer> findByEmail(String email) {
        List<Nutzer> nutzerList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT ROWID, * FROM Nutzer n WHERE n.Email LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + email + "%");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                nutzerList.add(mappingNutzer(resultSet));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return nutzerList;
    }
/*
    @Override
    public Optional<Nutzer> findByNameAndEmail(String name, String email){
        Optional<Nutzer> optionalNutzer = Optional.empty();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT ROWID, * FROM Nutzer n WHERE n.Benutzername = ? AND n.Email = ? ;"; // TODO: Dem Benutzer 'name' die entsprechenden Berechtigungen geben (siehe enum Role), um diese per RolesAllowed-Annotation zu nutzen. Ein Ergebnistupel besteht aus E-Mail-Adresse bzw. Benutzernamen und Berechtigung.
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, email);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            Nutzer nutzer = mappingNutzer(resultSet);
            optionalNutzer = Optional.ofNullable(nutzer);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return optionalNutzer;
    }


 */
    @Override
    public void save(Nutzer nutzer){
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO Nutzer VALUES (?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nutzer.getName());
            preparedStatement.setString(2, nutzer.getEmail());
            preparedStatement.setString(3, nutzer.getPasswort());
            preparedStatement.setInt(4, nutzer.getAdresseId());
            preparedStatement.executeUpdate();
            preparedStatement.closeOnCompletion();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s\n", e.getSQLState(), e.getMessage(), e.getErrorCode());
        }

    }
    public int findRowId(String name) {
        Nutzer nutzer = findByName(name).get();
        if(nutzer == null) return 0;
        return nutzer.getId();
    }


    @Override
    public long countByNameAndPassword(String name, String password) {
        return 0;
    }

    private Nutzer mappingNutzer(ResultSet resultSet) {
        Nutzer nutzer = null;
        try {
            nutzer = Nutzer.builder()
                    .id(resultSet.getInt(1))
                    .name(resultSet.getString(2))
                    .email(resultSet.getString(3))
                    .passwort(resultSet.getString(4))
                    .adresseId(resultSet.getInt(5))
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nutzer;

    }
}
