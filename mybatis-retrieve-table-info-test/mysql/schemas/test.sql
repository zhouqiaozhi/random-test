CREATE TABLE TEST (
                      ID INT                           NOT NULL,
                      NAME VARCHAR (20) NOT NULL,
                      NUM DECIMAL(5,5),
                      INSERT_TIME TIMESTAMP,
                      ACTIVE BOOL COMMENT 'BOOLEAN',
                      PRIMARY KEY (ID)
);

INSERT INTO TEST(ID, NAME, NUM, INSERT_TIME, ACTIVE) values (1, "test", 0.55555, CURRENT_TIMESTAMP(), true);