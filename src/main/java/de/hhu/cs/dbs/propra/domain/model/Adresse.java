package de.hhu.cs.dbs.propra.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Adresse {
    private int id;
    private String plz;
    private String land;
    private String stadt;
    private String strasse;
    private int hausnummer;

}
