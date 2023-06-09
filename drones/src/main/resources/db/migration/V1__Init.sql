create table DRONES
(
    ID               INTEGER auto_increment,
    SERIAL_NUMBER    CHARACTER VARYING(100),
    MODEL            CHARACTER VARYING(16),
    WEIGHT_LIMIT     SMALLINT,
    BATTERY_CAPACITY TINYINT,
    STATE            CHARACTER VARYING(16),
    constraint "DRONES_pk"
        primary key (ID)
);

create table MEDICATIONS
(
    ID     INTEGER auto_increment,
    NAME   CHARACTER VARYING(256),
    WEIGHT SMALLINT,
    CODE   CHARACTER VARYING(256),
    IMAGE  VARBINARY,
    constraint MEDICATIONS_PK
        primary key (ID)
);

create table LOADINGS
(
    DRONE_ID      INTEGER not null,
    MEDICATION_ID INTEGER not null,
    QTY           INTEGER not null,
    constraint LOADINGS_PK
        primary key (DRONE_ID, MEDICATION_ID),
    constraint "LOADINGS_DRONES_null_fk"
        foreign key (DRONE_ID) references DRONES,
    constraint "LOADINGS_MEDICATIONS_null_fk"
        foreign key (MEDICATION_ID) references MEDICATIONS
);

CREATE TABLE BATTERY_LEVEL_LOG
(
    ID            BIGINT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    DRONE_ID      INTEGER NOT NULL,
    DATE          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    BATTERY_LEVEL TINYINT     NOT NULL,
    constraint "BATTERY_LEVEL_LOG_DRONES_null_fk"
        foreign key (DRONE_ID) references DRONES
);


INSERT INTO DRONES (SERIAL_NUMBER, MODEL, WEIGHT_LIMIT, BATTERY_CAPACITY, STATE)
VALUES ('1234567890', 'Lightweight', 200, 90, 'IDLE'),
       ('2345678901', 'Middleweight', 350, 80, 'IDLE'),
       ('3456789012', 'Cruiserweight', 450, 70, 'LOADING'),
       ('4567890123', 'Heavyweight', 500, 60, 'LOADED'),
       ('5678901234', 'Lightweight', 200, 90, 'DELIVERING'),
       ('6789012345', 'Middleweight', 350, 80, 'DELIVERED'),
       ('7890123456', 'Cruiserweight', 450, 70, 'RETURNING'),
       ('8901234567', 'Heavyweight', 500, 60, 'RETURNING'),
       ('9012345678', 'Lightweight', 200, 90, 'IDLE'),
       ('0123456789', 'Middleweight', 350, 80, 'IDLE');

INSERT INTO MEDICATIONS (NAME, WEIGHT, CODE, IMAGE)
VALUES ('Ibuprofen', 20, 'IBU_123', null),
       ('Aspirin', 15, 'ASP_456', null),
       ('Paracetamol', 18, 'PAR_789', null),
       ('Lisinopril', 22, 'LIS_012', null),
       ('Amlodipine', 25, 'AML_345', null),
       ('Metformin', 17, 'MET_678', null),
       ('Atorvastatin', 20, 'ATO_901', null),
       ('Omeprazole', 10, 'OME_234', null),
       ('Amoxicillin', 30, 'AMO_567', null),
       ('Gabapentin', 60, 'GAB_890', null);

INSERT INTO LOADINGS (DRONE_ID, MEDICATION_ID, QTY)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 3),
       (4, 4, 1),
       (5, 5, 2),
       (6, 6, 3),
       (7, 7, 2),
       (8, 8, 1),
       (9, 9, 1),
       (10, 10, 1);



