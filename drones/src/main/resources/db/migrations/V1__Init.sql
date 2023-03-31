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
    id            integer generated by default as identity (exhausted),
    drone_id      integer not null,
    medication_id integer not null,
    qty           integer not null,
    constraint "LOADINGS_DRONES_null_fk"
        foreign key (drone_id) references DRONES (ID),
    constraint "LOADINGS_MEDICATIONS_null_fk"
        foreign key (medication_id) references MEDICATIONS (ID)
);


INSERT INTO PUBLIC.DRONES ( SERIAL_NUMBER, MODEL, WEIGHT_LIMIT, BATTERY_CAPACITY, STATE) VALUES ( 'qwerty123', 'Lightweight', 12, 99, 'IDLE');




