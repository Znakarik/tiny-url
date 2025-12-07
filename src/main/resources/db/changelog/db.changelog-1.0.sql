--changeset znakarik:1
CREATE TABLE urls (
id VARCHAR(100) PRIMARY KEY,
short_url VARCHAR(100) NOT NULL,
long_url VARCHAR(200) NOT NULL,
create_date_time VARCHAR(100) NOT NULL
);

CREATE TABLE url_redirects (
id VARCHAR(100) PRIMARY KEY,
url_id VARCHAR(100) NOT NULL,
create_date_time VARCHAR(100) NOT NULL
);