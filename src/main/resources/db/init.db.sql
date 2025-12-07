
CREATE TABLE urls (
id VARCHAR(100) PRIMARY KEY,
short_url VARCHAR(100) NOT NULL,
long_url VARCHAR(200) NOT NULL,
redirects_count integer NOT NULL,
create_date_time VARCHAR(100) NOT NULL
);