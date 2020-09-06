CREATE TABLE movies
(
    urn                     varchar(30) PRIMARY KEY,
    external_urns           varchar(30)[],
    title                   text NOT NULL CHECK (title <> ''),
    tagline                 text,
    overview                text,
    genres                  integer[],
    original_title          text,
    original_language       char(2),
    spoken_languages        char(2)[],
    backdrop_path           varchar(256),
    poster_path             varchar(256),
    homepage                varchar(256),
    runtime                 integer,
    budget                  real,
    revenue                 real,
    release_date            date,
    production_status       varchar(20),
    production_countries    char(2)[],
    production_companies    varchar(30)[],
    popularity              real,
    vote_average            real,
    vote_count              integer
);

CREATE INDEX idx_genres ON movies USING GIN(genres);
CREATE INDEX idx_countries ON movies USING GIN(production_countries);

CREATE TABLE releases
(
    id                  serial  PRIMARY KEY,
    movie_urn           varchar(30) REFERENCES movies(urn),
    release_date        date,
    spoken_language     char(2),
    certification       varchar(20),
    release_type        integer
);

CREATE TABLE casting
(
    id                  serial  PRIMARY KEY,
    movie_urn           varchar(30) REFERENCES movies(urn),
    person_urn          varchar(30) NOT NULL CHECK (person_urn <> ''),
    character_name      varchar(256),
    ordinal             integer
);

CREATE TABLE crew
(
    id                  serial  PRIMARY KEY,
    movie_urn           varchar(30) REFERENCES movies(urn),
    person_urn          varchar(30) NOT NULL CHECK (person_urn <> ''),
    job_role            varchar(128),
    department          varchar(128)
);
