-- Création de la base de données
CREATE DATABASE netflix;

-- Utilisation de la base de données
USE netflix;

-- Création de la table "utilisateurs" pour stocker les informations de compte
CREATE TABLE utilisateurs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom_utilisateur VARCHAR(50) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(50) NOT NULL
);

-- Création de la table "videos" pour stocker les informations sur les vidéos
CREATE TABLE videos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(100) NOT NULL,
    resume TEXT,
    teaser VARCHAR(255),
    duree INT,
    annee INT,
    realisateur VARCHAR(100),
    acteurs TEXT,
    categorie VARCHAR(50),
    est_vue BOOL DEFAULT FALSE,
    note INT
);

-- Création de la table "listes_de_lecture" pour stocker les listes de lecture
CREATE TABLE listes_de_lecture (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    utilisateur_id INT NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

-- Création de la table "videos_liste" pour associer les vidéos aux listes de lecture
CREATE TABLE videos_liste (
    id INT PRIMARY KEY AUTO_INCREMENT,
    video_id INT NOT NULL,
    liste_id INT NOT NULL,
    FOREIGN KEY (video_id) REFERENCES videos(id),
    FOREIGN KEY (liste_id) REFERENCES listes_de_lecture(id)
);

-- Création de la table "parametres_compte" pour stocker les paramètres du compte utilisateur
CREATE TABLE parametres_compte (
    id INT PRIMARY KEY AUTO_INCREMENT,
    utilisateur_id INT NOT NULL,
    reprise_lecture BOOL DEFAULT TRUE,
    qualite_video VARCHAR(50) DEFAULT 'SD',
    sous_titres VARCHAR(50) DEFAULT 'Aucun',
    historique BOOL DEFAULT TRUE,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

-- Création de la table "visionnages" pour stocker l'historique des visionnages
CREATE TABLE visionnages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    utilisateur_id INT NOT NULL,
    video_id INT NOT NULL,
    date_visionnage DATETIME NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id),
    FOREIGN KEY (video_id) REFERENCES videos(id)
);

-- Création de la table "categories" pour stocker les catégories de vidéos
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL UNIQUE
);

-- Création de la table "videos_categories" pour associer les vidéos aux catégories
CREATE TABLE videos_categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    video_id INT NOT NULL,
    categorie_id INT NOT NULL,
    FOREIGN KEY (video_id) REFERENCES videos(id),
    FOREIGN KEY (categorie_id) REFERENCES categories(id)
);