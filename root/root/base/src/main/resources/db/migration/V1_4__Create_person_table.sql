CREATE SEQUENCE PERSON_ID_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE PERSON (
    ID bigint NOT null PRIMARY KEY,
    FIRST_NAME VARCHAR(100) NOT null,
    LAST_NAME VARCHAR(100) NOT null,
    ADDRESS_ID bigint NOT null REFERENCES ADDRESS(ID)
);
