DROP SEQUENCE PERSON_SEQ;
CREATE SEQUENCE PERSON_SEQ;

DROP TABLE PERSON;
CREATE TABLE PERSON (
    ID INT PRIMARY KEY,
    NAME VARCHAR NOT NULL
);

ALTER TABLE PERSON
    ADD CONSTRAINT PERSON_UX UNIQUE(NAME);

INSERT INTO PERSON (ID, NAME) VALUES(PERSON_SEQ.NEXTVAL, 'Mickey');
INSERT INTO PERSON (ID, NAME) VALUES(PERSON_SEQ.NEXTVAL, 'Donald');
