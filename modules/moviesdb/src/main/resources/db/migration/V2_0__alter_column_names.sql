ALTER TABLE movies
RENAME COLUMN production_companies TO production_company_urns;

ALTER TABLE releases
ADD COLUMN country_code varchar(2);