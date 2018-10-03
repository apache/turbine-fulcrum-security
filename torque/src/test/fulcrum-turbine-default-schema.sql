-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.

-- ---------------------------------------------------------------------------
-- FULCRUM_TURBINE_PERMISSION
-- ---------------------------------------------------------------------------
drop table TURBINE_PERMISSION if exists CASCADE;

CREATE TABLE TURBINE_PERMISSION
(
    PERMISSION_ID INTEGER IDENTITY NOT NULL,
    PERMISSION_NAME VARCHAR(64) NOT NULL,
    PRIMARY KEY(PERMISSION_ID),
    UNIQUE (PERMISSION_NAME)
);


-- ---------------------------------------------------------------------------
-- TURBINE_ROLE
-- ---------------------------------------------------------------------------
drop table TURBINE_ROLE if exists CASCADE;

CREATE TABLE TURBINE_ROLE
(
    ROLE_ID INTEGER IDENTITY NOT NULL,
    ROLE_NAME VARCHAR(64) NOT NULL,
    PRIMARY KEY(ROLE_ID),
    UNIQUE (ROLE_NAME)
);


-- ---------------------------------------------------------------------------
-- TURBINE_GROUP
-- ---------------------------------------------------------------------------
drop table TURBINE_GROUP if exists CASCADE;

CREATE TABLE TURBINE_GROUP
(
    GROUP_ID INTEGER IDENTITY NOT NULL,
    GROUP_NAME VARCHAR(64) NOT NULL,
    PRIMARY KEY(GROUP_ID),
    UNIQUE (GROUP_NAME)
);


-- ---------------------------------------------------------------------------
-- TURBINE_USER
-- ---------------------------------------------------------------------------
drop table TURBINE_USER if exists CASCADE;

CREATE TABLE TURBINE_USER
(
    USER_ID INTEGER IDENTITY NOT NULL,
    LOGIN_NAME VARCHAR(64) NOT NULL,
    PASSWORD_VALUE VARCHAR(16) NOT NULL,
    FIRST_NAME VARCHAR(64),
    LAST_NAME VARCHAR(64),
    EMAIL VARCHAR(64),
    OBJECTDATA VARBINARY(800),
    PRIMARY KEY(USER_ID),
    UNIQUE (LOGIN_NAME)
);


-- ---------------------------------------------------------------------------
-- TURBINE_ROLE_PERMISSION
-- ---------------------------------------------------------------------------
drop table TURBINE_ROLE_PERMISSION if exists CASCADE;

CREATE TABLE TURBINE_ROLE_PERMISSION
(
    ROLE_ID INTEGER NOT NULL,
    PERMISSION_ID INTEGER NOT NULL,
    PRIMARY KEY(ROLE_ID,PERMISSION_ID)
);


-- ---------------------------------------------------------------------------
-- TURBINE_USER_GROUP_ROLE
-- ---------------------------------------------------------------------------
drop table TURBINE_USER_GROUP_ROLE if exists CASCADE;

CREATE TABLE TURBINE_USER_GROUP_ROLE
(
    USER_ID INTEGER NOT NULL,
    GROUP_ID INTEGER NOT NULL,
    ROLE_ID INTEGER NOT NULL,
    PRIMARY KEY(USER_ID,GROUP_ID,ROLE_ID)
);









    ALTER TABLE TURBINE_ROLE_PERMISSION
        ADD CONSTRAINT TURBINE_ROLE_PERMISSION_FK_1 FOREIGN KEY (ROLE_ID)
            REFERENCES TURBINE_ROLE (ROLE_ID)
;
    ALTER TABLE TURBINE_ROLE_PERMISSION
        ADD CONSTRAINT TURBINE_ROLE_PERMISSION_FK_2 FOREIGN KEY (PERMISSION_ID)
            REFERENCES TURBINE_PERMISSION (PERMISSION_ID)
;


    ALTER TABLE TURBINE_USER_GROUP_ROLE
        ADD CONSTRAINT TURBINE_USER_GROUP_ROLE_FK_1 FOREIGN KEY (USER_ID)
            REFERENCES TURBINE_USER (USER_ID)
;
    ALTER TABLE TURBINE_USER_GROUP_ROLE
        ADD CONSTRAINT TURBINE_USER_GROUP_ROLE_FK_2 FOREIGN KEY (GROUP_ID)
            REFERENCES TURBINE_GROUP (GROUP_ID)
;
    ALTER TABLE TURBINE_USER_GROUP_ROLE
        ADD CONSTRAINT TURBINE_USER_GROUP_ROLE_FK_3 FOREIGN KEY (ROLE_ID)
            REFERENCES TURBINE_ROLE (ROLE_ID)
;


