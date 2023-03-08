CREATE TABLE IF NOT EXISTS t_user
(
    username varchar(32)  NOT NULL PRIMARY KEY,
    password varchar(256) NOT NULL,
    nickname varchar(64)
);

CREATE TABLE IF NOT EXISTS t_token
(
    token    varchar(128) NOT NULL PRIMARY KEY,
    username varchar(32)  NOT NULL,
    exp      timestamp    NOT NULL
);

-- NOT WORK FOR TIMEZONE
-- CREATE TRIGGER IF NOT EXISTS clean_token
--     AFTER INSERT
--     ON t_token
--     FOR EACH ROW
-- BEGIN
--     DELETE FROM t_token WHERE exp < CURRENT_TIMESTAMP;
-- END;