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
-- FULCRUM_BASIC_GROUP
-- ---------------------------------------------------------------------------
drop table FULCRUM_BASIC_GROUP if exists CASCADE;

CREATE TABLE FULCRUM_BASIC_GROUP
(
    GROUP_ID INTEGER NOT NULL,
    GROUP_NAME VARCHAR(64) NOT NULL,
    PRIMARY KEY(GROUP_ID),
    UNIQUE (GROUP_NAME)
);


-- ---------------------------------------------------------------------------
-- FULCRUM_BASIC_USER
-- ---------------------------------------------------------------------------
drop table FULCRUM_BASIC_USER if exists CASCADE;

CREATE TABLE FULCRUM_BASIC_USER
(
    USER_ID INTEGER NOT NULL,
    LOGIN_NAME VARCHAR(64) NOT NULL,
    PASSWORD_VALUE VARCHAR(16) NOT NULL,
    PRIMARY KEY(USER_ID),
    UNIQUE (LOGIN_NAME)
);


-- ---------------------------------------------------------------------------
-- FULCRUM_BASIC_USER_GROUP
-- ---------------------------------------------------------------------------
drop table FULCRUM_BASIC_USER_GROUP if exists CASCADE;

CREATE TABLE FULCRUM_BASIC_USER_GROUP
(
    USER_ID INTEGER NOT NULL,
    GROUP_ID INTEGER NOT NULL,
    PRIMARY KEY(USER_ID,GROUP_ID)
);





    ALTER TABLE FULCRUM_BASIC_USER_GROUP
        ADD CONSTRAINT FULCRUM_BASIC_USER_GROUP_FK_1 FOREIGN KEY (USER_ID)
            REFERENCES FULCRUM_BASIC_USER (USER_ID)
;
    ALTER TABLE FULCRUM_BASIC_USER_GROUP
        ADD CONSTRAINT FULCRUM_BASIC_USER_GROUP_FK_2 FOREIGN KEY (GROUP_ID)
            REFERENCES FULCRUM_BASIC_GROUP (GROUP_ID)
;


