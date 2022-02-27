CREATE TABLE Adresse
(
    Adresse_id INTEGER PRIMARY KEY CHECK ( Adresse_id > 0 )     NOT NULL,
    PLZ        VARCHAR CHECK ( PLZ NOT GLOB '*[^0-9]*' AND
                               LENGTH(PLZ) = 5) NOT NULL,
    Land       VARCHAR CHECK ( Land NOT GLOB '*[^ -~]*' AND
                               LENGTH(TRIM(Land, ' ')) != 0)    NOT NULL,
    Stadt      VARCHAR CHECK ( Stadt NOT GLOB '*[^ -~]*' AND
                               LENGTH(TRIM(Land, ' ')) != 0)   NOT NULL,
    Strasse    VARCHAR CHECK ( Strasse NOT GLOB '*[^ -~]*' AND
                               LENGTH(TRIM(Land, ' ')) != 0) NOT NULL,
    Hausnummer INTEGER CHECK ( Hausnummer > 0 )                 NOT NULL,
    UNIQUE(PLZ, Land, Stadt, Strasse, Hausnummer)
);
CREATE TABLE Nutzer
(
    Benutzername VARCHAR PRIMARY KEY CHECK (Benutzername NOT GLOB '*[^ -~]*' AND
                                            LENGTH(TRIM(Benutzername, ' ')) != 0) NOT NULL,
    Email        VARCHAR COLLATE NOCASE CHECK ( Email NOT GLOB '*[^ -~]*' AND
                                 Email GLOB '*?@?*.?*' AND
                                 LENGTH(email) - LENGTH(REPLACE(email, '@', '')) = 1 AND
                                 SUBSTR(LOWER(email), 1, INSTR(email, '.') - 1) NOT GLOB '*[^@0-9a-z]*' AND
                                 SUBSTR(LOWER(email), INSTR(email, '.') + 1) NOT GLOB '*[^a-z]*') UNIQUE NOT NULL,
    Passwort     VARCHAR CHECK ( LENGTH(Passwort) > 2 AND
                                 LENGTH(Passwort) < 9 AND
                                 Passwort NOT GLOB '*[^ -~]*' AND
                                 Passwort GLOB '*[0-9]*' AND
                                 Passwort GLOB '*[A-Z]*[A-Z]*') NOT NULL,
    Adresse_id INTEGER CHECK ( Adresse_id > 0 )     NOT NULL,
    FOREIGN KEY (Adresse_id) REFERENCES Adresse(Adresse_id) ON UPDATE RESTRICT ON DELETE RESTRICT
);
CREATE TABLE Premiumnutzer
(
    Benutzername VARCHAR PRIMARY KEY CHECK (Benutzername NOT GLOB '*[^ -~]*' AND
                                            LENGTH(TRIM(Benutzername, ' ')) != 0) NOT NULL,
    Premiumfrist DATETIME CHECK(Premiumfrist IS datetime(Premiumfrist))    NOT NULL,
    FOREIGN KEY (Benutzername) REFERENCES Nutzer(Benutzername)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Kuenstler
(
    Kuenstlername VARCHAR PRIMARY KEY CHECK (Kuenstlername not GLOB '*[^ -~]*' AND
                                             LENGTH(TRIM(Kuenstlername, ' ')) != 0) NOT NULL,
    Benutzername  VARCHAR CHECK (Benutzername NOT GLOB '*[^ -~]*' AND
                                 LENGTH(TRIM(Benutzername, ' ')) != 0) UNIQUE      NOT NULL,
    FOREIGN KEY (Benutzername) REFERENCES Premiumnutzer(Benutzername)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Album
(
    Album_id          INTEGER PRIMARY KEY CHECK ( Album_id > 0 )          NOT NULL,
    Erscheinungsdatum DATETIME CHECK(Erscheinungsdatum = DATETIME(Erscheinungsdatum))     NOT NULL,
    Bezeichnung       VARCHAR CHECK (Bezeichnung NOT GLOB '*[^ -~]*' AND
                                     LENGTH(TRIM(Bezeichnung, ' ')) != 0) NOT NULL
);

CREATE TABLE Band
(
    Bandname       VARCHAR PRIMARY KEY CHECK (Bandname NOT GLOB '*[^ -~]*' AND
                                              LENGTH(TRIM(Bandname, ' ')) != 0) NOT NULL,
    Bandgeschichte TEXT CHECK (Bandgeschichte NOT GLOB '*[^ -~]*' AND
                                    LENGTH(TRIM(Bandgeschichte, ' ')) != 0)
);

CREATE TABLE Playlist
(
    Playlist_id INTEGER CHECK(Playlist_id > 0) PRIMARY KEY NOT NULL,
    Privat BOOLEAN NOT NULL,
    Bezeichnung VARCHAR CHECK(Bezeichnung NOT GLOB '*[^ -~]*'AND
                              LENGTH(TRIM(Bezeichnung, ' ')) != 0) NOT NULL,
    Coverbild VARCHAR COLLATE NOCASE CHECK(Coverbild NOT GLOB '*[^ -~]*'AND
                            Coverbild LIKE '%_.png%'),
    Benutzername VARCHAR CHECK (Benutzername NOT GLOB '*[^ -~]*'AND
                                LENGTH( TRIM(Benutzername, ' ')) != 0) NOT NULL,
    UNIQUE(Bezeichnung, Benutzername),
    FOREIGN KEY (Benutzername) REFERENCES Premiumnutzer(Benutzername)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Titel
(
    Titel_id INTEGER CHECK(Titel_id > 0) PRIMARY KEY NOT NULL,
    Dauer TIME CHECK ( DAUER = TIME(Dauer) ) NOT NULL,
    Benennung VARCHAR CHECK(Benennung NOT GLOB '*[^ -~]*'AND
                            LENGTH(TRIM(Benennung, ' ')) != 0) NOT NULL,
    LQ VARCHAR COLLATE NOCASE CHECK(LQ NOT GLOB '*[^ -~]*'AND
                     LENGTH(TRIM(Benennung, ' ')) != 0) NOT NULL UNIQUE,
    HQ VARCHAR COLLATE NOCASE CHECK(HQ NOT GLOB '*[^ -~]*'AND
                     LENGTH(TRIM(Benennung, ' ')) != 0) NOT NULL UNIQUE,
    Bezeichnung VARCHAR CHECK(Bezeichnung NOT LIKE '%[^A-Z]%') NOT NULL,
   FOREIGN KEY (Bezeichnung) REFERENCES Gerne(Bezeichnung)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE Gerne
(
    Bezeichnung VARCHAR COLLATE NOCASE PRIMARY KEY CHECK(Bezeichnung NOT LIKE '%[^A-Z]%') NOT NULL
);

CREATE TABLE Album_bestehtAus_Titel
(
    Titel_id INTEGER CHECK(Titel_id > 0) NOT NULL,
    Album_id INTEGER CHECK(Album_id > 0) NOT NULL,
    PRIMARY KEY(Titel_id, Album_id),
    FOREIGN KEY(Titel_id) REFERENCES Titel(Titel_id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY(Album_id) REFERENCES Album(Album_id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
);

CREATE TABLE kommentiert(
        Benutzername VARCHAR CHECK (Benutzername NOT GLOB '*[^ -~]*' AND
                                LENGTH(TRIM(Benutzername, ' ')) != 0) NOT NULL,
        Titel_id INTEGER CHECK(Titel_id > 0) NOT NULL,
        Kommentar TEXT CHECK(Kommentar NOT GLOB '*[^ -~]*'AND
                                LENGTH(TRIM(Kommentar, ' ')) != 0) NOT NULL,
        PRIMARY KEY (Benutzername, Titel_id),
        FOREIGN KEY (Benutzername) REFERENCES Nutzer(Benutzername)
                 ON UPDATE CASCADE
                 ON DELETE CASCADE ,
        FOREIGN KEY (Titel_id) REFERENCES Titel(Titel_id)
                  ON UPDATE CASCADE
                  ON DELETE CASCADE
);

CREATE table bewertet
(
    Benutzername VARCHAR CHECK (Benutzername NOT GLOB '*[^ -~]*' AND
                                LENGTH(TRIM(Benutzername, ' ')) != 0) NOT NULL,
    Playlist_id INTEGER CHECK(Playlist_id > 0)  NOT NULL,
    Bewertung INTEGER CHECK(Bewertung BETWEEN 1 AND 10) NOT NULL,
    PRIMARY KEY (Benutzername, Playlist_id),
    FOREIGN KEY (Benutzername) REFERENCES Nutzer(Benutzername)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (Playlist_id) REFERENCES Playlist(Playlist_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE enthaelt
(
    Playlist_id INTEGER CHECK(Playlist_id > 0) NOT NULL,
    Titel_id INTEGER CHECK(Titel_id > 0) NOT NULL,
    PRIMARY KEY (Playlist_id, Titel_id),
    FOREIGN KEY (Playlist_id) REFERENCES Playlist(Playlist_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (Titel_id) REFERENCES Titel(Titel_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE empfehlt
(
    Bezeichnung1 VARCHAR COLLATE NOCASE PRIMARY KEY CHECK(Bezeichnung1 NOT GLOB '*[^ -~]*' AND
                                           LENGTH(TRIM(Bezeichnung1, ' ')) != 0) NOT NULL,
    Bezeichnung2 VARCHAR COLLATE NOCASE CHECK(Bezeichnung2 NOT GLOB '*[^ -~]*' AND
                               LENGTH(TRIM(Bezeichnung2, ' ')) != 0) CHECK(Bezeichnung2 not LIKE Bezeichnung1) NOT NULL,
    FOREIGN KEY(Bezeichnung1) REFERENCES GERNE (Bezeichnung)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY(Bezeichnung2) REFERENCES GERNE (Bezeichnung)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Kuenstler_veroeffentlicht_Titel
(
    Kuenstlername varchar CHECK (Kuenstlername not GLOB '*[^ -~]*' AND
                                 LENGTH(TRIM(Kuenstlername, ' ')) != 0) NOT NULL,
    Titel_id INTEGER CHECK (Titel_id > 0) NOT NULL,
    PRIMARY KEY (Kuenstlername, Titel_id),
    FOREIGN KEY (Kuenstlername) REFERENCES Kuenstler(Kuenstlername)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (Titel_id) REFERENCES Titel(Titel_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Kuenstler_veroeffentlicht_Album
(
    Kuenstlername VARCHAR CHECK (Kuenstlername NOT GLOB '*[^ -~]*'AND
                                 LENGTH(TRIM(Kuenstlername, ' ')) != 0) NOT NULL,
    Album_id INTEGER PRIMARY KEY CHECK (Album_id > 0) NOT NULL,
    FOREIGN KEY (Kuenstlername) REFERENCES Kuenstler(Kuenstlername)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (Album_id) REFERENCES Album(Album_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Band_veroeffentlicht_Album
(
    Bandname VARCHAR CHECK (Bandname NOT GLOB '*[^ -~]*'AND
                            LENGTH(TRIM(Bandname, ' ')) != 0) NOT NULL,
    Album_id INTEGER PRIMARY KEY CHECK (Album_id > 0) NOT NULL,
    FOREIGN KEY (Bandname) REFERENCES Band(Bandname)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (Album_id) REFERENCES Album(Album_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Kuenstler_bestehtAus_Band
(
    Kuenstlername VARCHAR PRIMARY KEY CHECK (Kuenstlername NOT GLOB '*[^ -~]*' AND
                                             LENGTH(TRIM(Kuenstlername, ' ')) != 0) NOT NULL,
    Bandname VARCHAR CHECK (Bandname NOT GLOB '*[^ -~]*' AND
                            LENGTH(TRIM(Bandname, ' ')) != 0) NOT NULL,
    FOREIGN KEY (Kuenstlername) REFERENCES Kuenstler(Kuenstlername)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (Bandname) REFERENCES Band(Bandname)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED
);

CREATE TRIGGER IF NOT EXISTS comment_before_rate
    BEFORE INSERT ON bewertet
    WHEN NEW.Playlist_id NOT IN (SELECT Playlist_id FROM enthaelt e
                                                             JOIN kommentiert k ON e.Titel_id = k.Titel_id)
BEGIN
    SELECT RAISE (ABORT, 'Please comment before rate!');
END;

CREATE TRIGGER IF NOT EXISTS album_released_by_artist_or_band
    BEFORE INSERT ON Album
    WHEN NEW.Album_id NOT IN (SELECT Album_id FROM Kuenstler_veroeffentlicht_Album
                              UNION
                              SELECT Album_id FROM Band_veroeffentlicht_Album)
BEGIN
    SELECT RAISE (ABORT, 'an Album must be released either by an artist or by a band!');
END;

CREATE TRIGGER IF NOT EXISTS album_released_either_by_artist_or_band1
    BEFORE INSERT ON Kuenstler_veroeffentlicht_Album
    WHEN NEW.Album_id IN (SELECT Album_id FROM Band_veroeffentlicht_Album)
BEGIN
    SELECT RAISE (ABORT, 'an Album must be released either by an artist or by a band!');
END;

CREATE TRIGGER IF NOT EXISTS album_released_either_by_artist_or_band2
    BEFORE INSERT ON Band_veroeffentlicht_Album
    WHEN NEW.Album_id IN (SELECT Album_id FROM Kuenstler_veroeffentlicht_Album)
BEGIN
    SELECT RAISE (ABORT, 'an Album must be released either by an artist or by a band!');
END;

CREATE TRIGGER IF NOT EXISTS a_band_has_min_two_artists
    BEFORE INSERT ON Band
    WHEN NEW.Bandname IN (SELECT Bandname FROM Kuenstler_bestehtAus_Band
                          GROUP by Bandname
                          HAVING COUNT(Kuenstlername) < 2)
BEGIN
    SELECT RAISE (ABORT, 'a_band_has_min_two_artists!');
END;


