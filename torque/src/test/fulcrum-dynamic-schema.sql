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
-- FULCRUM_DYNAMIC_PERMISSION
-- ---------------------------------------------------------------------------
drop table FULCRUM_DYNAMIC_PERMISSION if exists CASCADE;

CREATE TABLE FULCRUM_DYNAMIC_PERMISSION
(
    PERMISSION_ID INTEGER NOT NULL,
    PERMISSION_NAME VARCHAR(64) NOT NULL,
    PRIMARY KEY(PERMISSION_ID),
    UNIQUE (PERMISSION_NAME)
);


-- ---------------------------------------------------------------------------
-- FULCRUM_DYNAMIC_ROLE
-- ---------------------------------------------------------------------------
drop table FULCRUM_DYNAMIC_ROLE if exists CASCADE;

CREATE TABLE FULCRUM_DYNAMIC_ROLE
(
    ROLE_ID INTEGER NOT NULL,
    ROLE_NAME VARCHAR(64) NOT NULL,
    PRIMARY KEY(ROLE_ID),
    UNIQUE (ROLE_NAME)
);


-- ---------------------------------------------------------------------------
-- FULCRUM_DYNAMIC_GROUP
-- ---------------------------------------------------------------------------
drop table FULCRUM_DYNAMIC_GROUP if exists CASCADE;

CREATE TABLE FULCRUM_DYNAMIC_GROUP
(
    GROUP_ID INTEGER NOT NULL,
    GROUP_NAME VARCHAR(64) NOT NULL,
    PRIMARY KEY(GROUP_ID),
    UNIQUE (GROUP_NAME)
);


-- ---------------------------------------------------------------------------
-- FULCRUM_DYNAMIC_USER
-- ---------------------------------------------------------------------------
drop table FULCRUM_DYNAMIC_USER if exists CASCADE;

CREATE TABLE FULCRUM_DYNAMIC_USER
(
    USER_ID INTEGER NOT NULL,
    LOGIN_NAME VARCHAR(64) NOT NULL,
    PASSWORD_VALUE VARCHAR(16) NOT NULL,
    PRIMARY KEY(USER_ID),
    UNIQUE (LOGIN_NAME)
);


-- ---------------------------------------------------------------------------
-- DYNAMIC_ROLE_PERMISSION
-- ---------------------------------------------------------------------------
drop table DYNAMIC_ROLE_PERMISSION if exists CASCADE;

CREATE TABLE DYNAMIC_ROLE_PERMISSION
(
    ROLE_ID INTEGER NOT NULL,
    PERMISSION_ID INTEGER NOT NULL,
    PRIMARY KEY(ROLE_ID,PERMISSION_ID)
);


-- ---------------------------------------------------------------------------
-- DYNAMIC_USER_GROUP
-- ---------------------------------------------------------------------------
drop table DYNAMIC_USER_GROUP if exists CASCADE;

CREATE TABLE DYNAMIC_USER_GROUP
(
    USER_ID INTEGER NOT NULL,
    GROUP_ID INTEGER NOT NULL,
    PRIMARY KEY(USER_ID,GROUP_ID)
);


-- ---------------------------------------------------------------------------
-- DYNAMIC_GROUP_ROLE
-- ---------------------------------------------------------------------------
drop table DYNAMIC_GROUP_ROLE if exists CASCADE;

CREATE TABLE DYNAMIC_GROUP_ROLE
(
    GROUP_ID INTEGER NOT NULL,
    ROLE_ID INTEGER NOT NULL,
    PRIMARY KEY(GROUP_ID,ROLE_ID)
);


-- ---------------------------------------------------------------------------
-- DYNAMIC_USER_DELEGATES
-- ---------------------------------------------------------------------------
drop table DYNAMIC_USER_DELEGATES if exists CASCADE;

CREATE TABLE DYNAMIC_USER_DELEGATES
(
    DELEGATOR_USER_ID INTEGER NOT NULL,
    DELEGATEE_USER_ID INTEGER NOT NULL,
    PRIMARY KEY(DELEGATOR_USER_ID,DELEGATEE_USER_ID)
);









    ALTER TABLE DYNAMIC_ROLE_PERMISSION
        ADD CONSTRAINT DYNAMIC_ROLE_PERMISSION_FK_1 FOREIGN KEY (ROLE_ID)
            REFERENCES FULCRUM_DYNAMIC_ROLE (ROLE_ID)
;
    ALTER TABLE DYNAMIC_ROLE_PERMISSION
        ADD CONSTRAINT DYNAMIC_ROLE_PERMISSION_FK_2 FOREIGN KEY (PERMISSION_ID)
            REFERENCES FULCRUM_DYNAMIC_PERMISSION (PERMISSION_ID)
;


    ALTER TABLE DYNAMIC_USER_GROUP
        ADD CONSTRAINT DYNAMIC_USER_GROUP_FK_1 FOREIGN KEY (USER_ID)
            REFERENCES FULCRUM_DYNAMIC_USER (USER_ID)
;
    ALTER TABLE DYNAMIC_USER_GROUP
        ADD CONSTRAINT DYNAMIC_USER_GROUP_FK_2 FOREIGN KEY (GROUP_ID)
            REFERENCES FULCRUM_DYNAMIC_GROUP (GROUP_ID)
;


    ALTER TABLE DYNAMIC_GROUP_ROLE
        ADD CONSTRAINT DYNAMIC_GROUP_ROLE_FK_1 FOREIGN KEY (GROUP_ID)
            REFERENCES FULCRUM_DYNAMIC_GROUP (GROUP_ID)
;
    ALTER TABLE DYNAMIC_GROUP_ROLE
        ADD CONSTRAINT DYNAMIC_GROUP_ROLE_FK_2 FOREIGN KEY (ROLE_ID)
            REFERENCES FULCRUM_DYNAMIC_ROLE (ROLE_ID)
;


    ALTER TABLE DYNAMIC_USER_DELEGATES
        ADD CONSTRAINT DYNAMIC_USER_DELEGATES_FK_1 FOREIGN KEY (DELEGATOR_USER_ID)
            REFERENCES FULCRUM_DYNAMIC_USER (USER_ID)
;
    ALTER TABLE DYNAMIC_USER_DELEGATES
        ADD CONSTRAINT DYNAMIC_USER_DELEGATES_FK_2 FOREIGN KEY (DELEGATEE_USER_ID)
            REFERENCES FULCRUM_DYNAMIC_USER (USER_ID)
;


