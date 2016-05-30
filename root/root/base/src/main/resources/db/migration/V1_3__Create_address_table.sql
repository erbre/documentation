CREATE SEQUENCE ADDRESS_ID_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE ADDRESS (
    ID bigint NOT null PRIMARY KEY,
    COUNTRY_ID bigint NOT null REFERENCES COUNTRY (ID),
    STREET VARCHAR(100) NOT null,
    CITY VARCHAR(100) NOT null,
    ZIP_CODE VARCHAR(100) NOT null
);