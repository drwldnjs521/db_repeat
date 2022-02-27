package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;



@Data
public class Premiumnutzer {
    private String benutzername;
    private String ablaufDatum;
    private boolean abgelaufen;

    public Premiumnutzer(String benutzername, String ablaufDatum) {
        this.benutzername = benutzername;
        this.ablaufDatum = ablaufDatum;
        LocalDateTime dateTime = LocalDateTime.parse(ablaufDatum);
        this.abgelaufen = LocalDateTime.now().isAfter(dateTime);

    }
}
