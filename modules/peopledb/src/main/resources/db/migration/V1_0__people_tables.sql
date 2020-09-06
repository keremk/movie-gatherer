CREATE TABLE people
(
    urn                     varchar(30) PRIMARY KEY,
    external_urns           varchar(30)[],
    birthday                date,
    deathday                date,
    known_for_department    varchar(256),
    name                    varchar(256) NOT NULL CHECK (name <> ''),
    also_known_as           varchar(256)[],
    gender                  varchar(64),
    biography               text,
    popularity              real,
    place_of_birth          text,
    profile_picture_path    varchar(256),
    homepage                varchar(256)
);

CREATE TABLE pictures
(
    id                  serial  PRIMARY KEY,
    person_urn          varchar(30) REFERENCES people(urn),
    path                varchar(256),
    width               integer,
    height              integer
);

