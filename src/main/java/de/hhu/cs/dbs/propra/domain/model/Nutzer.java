package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.security.Principal;
import java.sql.RowId;
import java.util.Collections;
import java.util.Set;

import static de.hhu.cs.dbs.propra.domain.model.Role.NUTZER;

@Builder
@AllArgsConstructor
@Data
@JsonPropertyOrder({"id","email","passwort","name"})
public class Nutzer implements Principal {
    @JsonProperty("nutzerid")
    private int id;
    private String email;
    private String passwort;
    @JsonIgnore
    private final Set<Role> roles;
    private String name;
    @JsonIgnore
    private int adresseId;

    public Nutzer(String name) {
        roles = Set.of(NUTZER);
        this.name = name;
    }

    public Nutzer(String benutzername, Set<Role> roles) {
        this.roles = roles;
        this.name = benutzername;
    }

    @Override
    public String getName() {
        return name;
    }
}
