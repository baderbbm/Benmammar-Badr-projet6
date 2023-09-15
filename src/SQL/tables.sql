CREATE TABLE projet.utilisateur (
    ID INT PRIMARY KEY AUTO_INCREMENT, 
    prenom VARCHAR(255),
    nom VARCHAR(255),
    adresseemail VARCHAR(255),
    motdepasse VARCHAR(100),
    soldeducompte DECIMAL(10, 2),
    CONSTRAINT uniqueemail UNIQUE (adresseemail)
);

CREATE TABLE projet.utilisateurami (
    utilisateurid INT,
    amiid INT,
    FOREIGN KEY (utilisateurid) REFERENCES projet.utilisateur(ID),
    FOREIGN KEY (amiid) REFERENCES projet.utilisateur(ID),
    CONSTRAINT pkutilisateurami PRIMARY KEY (utilisateurid, amiid)
);

CREATE TABLE projet.operation (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    IDutilisateur INT,
    Montant DECIMAL(10, 2),
    Dateetheureoperation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Natureoperation ENUM('depot', 'retrait'),
    FOREIGN KEY (IDutilisateur) REFERENCES utilisateur(ID)
);


CREATE TABLE projet.transfert  (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    IDutilisateuremetteur INT,
    IDutilisateurbeneficiaire INT,
    Montant DECIMAL(10, 2),
    Dateetheuredutransfert TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Description VARCHAR(255),
    FOREIGN KEY (IDutilisateuremetteur) REFERENCES utilisateur(ID),
    FOREIGN KEY (IDutilisateurbeneficiaire) REFERENCES utilisateur(ID)
);

