// common tables
CREATE SEQUENCE BSM.NOTIFICATIONS_SEQ AS INTEGER
	START WITH 1
	INCREMENT BY 1
	MAXVALUE 2147483647
	MINVALUE 1
	NO CYCLE
	NO CACHE
	NO ORDER;

CREATE SEQUENCE BSM.USERCODE_SEQ AS INTEGER
	START WITH 1
	INCREMENT BY 1
	MAXVALUE 2147483647
	MINVALUE 1
	NO CYCLE
	NO CACHE
	NO ORDER;

CREATE TABLE BSM.ACCOUNTROLE  ( 
	USERCODE  	INTEGER NOT NULL,
	ROLECODE  	INTEGER NOT NULL,
	USERINSERT	VARCHAR(30) NOT NULL,
	TIMEINSERT	TIMESTAMP NOT NULL DEFAULT CURRENT TIMESTAMP,
	CONSTRAINT PK_ACCOUNTROLE PRIMARY KEY(USERCODE,ROLECODE)
);

CREATE TABLE BSM.APPLICATIONPARAMETER  ( 
	PARAMETERCODE 	INTEGER NOT NULL,
	MAPPINGTYPE   	SMALLINT NOT NULL,
	DESCRIPTION   	VARCHAR(256),
	PARAMETERVALUE	DECIMAL(15,2),
	CATEGORY      	INTEGER,
	FAILCODE      	INTEGER,
	FAILMESSAGE   	VARCHAR(256),
	VALUEFROM     	BIGINT,
	VALUETO       	BIGINT,
	ORDER         	INTEGER,
	USERINSERT    	VARCHAR(30) NOT NULL,
	TIMEINSERT    	TIMESTAMP NOT NULL,
	USERUPDATE    	VARCHAR(30),
	TIMEUPDATE    	TIMESTAMP,
	CONSTRAINT PK_APPLICATIONPARAMETER PRIMARY KEY(PARAMETERCODE)
);

CREATE TABLE BSM.MSECLEVELDEFS  ( 
	ROLECODE   	INTEGER NOT NULL,
	METHOD_NAME	VARCHAR(75) NOT NULL,
	CLASS_ID   	INTEGER NOT NULL,
	DESCRIPTION	VARCHAR(150),
	ALLOW      	SMALLINT DEFAULT 0,
	CR_DATE    	TIMESTAMP DEFAULT CURRENT TIMESTAMP,
	ED_DATE    	TIMESTAMP DEFAULT CURRENT TIMESTAMP,
	USERINSERT 	VARCHAR(30) NOT NULL,
	USERUPDATE 	VARCHAR(30) NOT NULL,
	CONSTRAINT PK_MSECLEVELDEFS PRIMARY KEY(ROLECODE,METHOD_NAME,CLASS_ID)
);

CREATE TABLE BSM.NOTIFICATIONS  ( 
	ID           	INTEGER NOT NULL,
	DISABLED     	SMALLINT NOT NULL DEFAULT 0,
	DATE_FROM    	DATE,
	DATE_TO      	DATE,
	TITLE_LOCAL  	VARCHAR(100),
	TITLE_EN     	VARCHAR(50),
	MSG_LOCAL    	VARCHAR(1000),
	MSG_EN       	VARCHAR(500),
	DELAY_SECS   	INTEGER NOT NULL DEFAULT 5,
	N_POSITION   	INTEGER NOT NULL DEFAULT 8,
	CRM_CODE     	INTEGER,
	USERNAMES    	VARCHAR(500),
	SHOW_IN_VIEWS	VARCHAR(5000),
	SEC_METHOD   	VARCHAR(75),
	SEC_CLASS    	VARCHAR(200),
	USERINSERT   	VARCHAR(30) NOT NULL,
	TIMEINSERT   	TIMESTAMP NOT NULL,
	USERUPDATE   	VARCHAR(30),
	TIMEUPDATE   	TIMESTAMP,
	CONSTRAINT NOTIFICATIONS_PK PRIMARY KEY(ID)
);

CREATE TABLE BSM.USER_LOG  ( 
	USER_NAME	VARCHAR(50) NOT NULL,
	RESULT   	VARCHAR(200) NOT NULL,
	CR_DATE  	TIMESTAMP DEFAULT CURRENT TIMESTAMP,
	BROWSER  	VARCHAR(250) DEFAULT NULL,
	MAJOR    	INTEGER DEFAULT NULL,
	MINOR    	INTEGER DEFAULT NULL,
	ADDRESS  	VARCHAR(50) DEFAULT NULL 
);

CREATE TABLE BSM.TYPECLASS  ( 
	ID        	INTEGER NOT NULL,
	CLASS_NAME	VARCHAR(200) NOT NULL,
	GRP       	INTEGER NOT NULL,
	CR_DATE   	TIMESTAMP DEFAULT CURRENT TIMESTAMP,
	USERINSERT	VARCHAR(15) NOT NULL,
	CONSTRAINT PK_TYPECLASS PRIMARY KEY(ID)
);

CREATE TABLE BSM.USERACCOUNT  ( 
	USERCODE  	INTEGER NOT NULL,
	USERNAME  	VARCHAR(60) NOT NULL,
	PASSWORD  	VARCHAR(100) NULL,
	GUID      	VARCHAR(120),
	LASTLOGIN 	TIMESTAMP,
	USERINSERT	VARCHAR(30) NOT NULL,
	TIMEINSERT	TIMESTAMP NOT NULL DEFAULT CURRENT TIMESTAMP,
	USERUPDATE	VARCHAR(30) NOT NULL,
	TIMEUPDATE	TIMESTAMP NOT NULL DEFAULT CURRENT TIMESTAMP,
	CONSTRAINT PK_USERACCOUNT PRIMARY KEY(USERCODE)
);

CREATE TABLE BSM.USERROLE  ( 
	ROLECODE   	INTEGER NOT NULL,
	ROLETYPE   	INTEGER NOT NULL,
	DESCRIPTION	VARCHAR(30) NOT NULL,
	USERINSERT 	VARCHAR(30) NOT NULL,
	TIMEINSERT 	TIMESTAMP NOT NULL DEFAULT CURRENT TIMESTAMP,
	CONSTRAINT PK_USERROLE PRIMARY KEY(ROLECODE)
);

CREATE TABLE BSM.USERS  ( 
	USERCODE  	INTEGER NOT NULL,
	GENDER    	INTEGER,
	LASTNAME  	VARCHAR(40) NOT NULL,
	FIRSTNAME 	VARCHAR(20) NOT NULL,
	USERTYPE  	INTEGER NOT NULL,
	STATUSCODE	INTEGER NULL,
	EMAIL     	VARCHAR(80) NOT NULL,
	MEMBERCODE	INTEGER,
	TELEPHONE 	VARCHAR(20),
	POSITION  	INTEGER NULL,
	USERINSERT	VARCHAR(30) NOT NULL,
	TIMEINSERT	TIMESTAMP NOT NULL DEFAULT CURRENT TIMESTAMP,
	USERUPDATE	VARCHAR(30) NOT NULL,
	TIMEUPDATE	TIMESTAMP NOT NULL DEFAULT CURRENT TIMESTAMP,
	CONSTRAINT PK_USERS PRIMARY KEY(USERCODE)
);

CREATE  TABLE BSM.APP_SESSIONS  (
    USER_CODE             int NOT NULL,
    ADDRESS            	varchar(50) NULL,
    TIMEINSERT              datetime NOT NULL,

    CONSTRAINT PK_APP_SESSIONS PRIMARY KEY(USER_CODE)
);

CREATE  TABLE BSM.APP_LOCKS  (
    LOCK_TYPE            	int NOT NULL,
    LOCK_ITEM_ID          int NOT NULL,
    USER_CODE             int NOT NULL,
    TIMEINSERT              datetime NOT NULL,

    CONSTRAINT PK_APP_LOCKS PRIMARY KEY(LOCK_TYPE, LOCK_ITEM_ID)
);

ALTER TABLE BSM.TYPECLASS
	ADD CONSTRAINT UNQ_CLASSNAME
	UNIQUE (CLASS_NAME, GRP);

ALTER TABLE BSM.USERACCOUNT
	ADD CONSTRAINT UNQ_USERNAME
	UNIQUE (UNQ_USERNAME, GRP);

