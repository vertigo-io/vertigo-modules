-- ============================================================
--   SGBD      		  :  PostgreSql                     
-- ============================================================




-- ============================================================
--   Sequences                                      
-- ============================================================
create sequence SEQ_TRACE
	start with 1000 cache 20; 

-- ============================================================
--   Table : TRACE                                        
-- ============================================================
create table TRACE
(
    TRA_ID      	NUMERIC     	not null,
    CATEGORY        VARCHAR(100)	not null,
    USERNAME       	VARCHAR(100)	not null,
    BUSINESS_DATE   TIMESTAMP     	,
    EXECUTION_DATE  TIMESTAMP       not null,
    ITEM_URN      	VARCHAR(250)	not null,
    MESSAGE      	TEXT            not null,
    CONTEXT         TEXT,
    constraint PK_AUDIT_TRACE primary key (TRA_ID)
);

drop index IF EXISTS IDX_TRA_USERNAME_DATE;
create index IDX_TRA_USERNAME_DATE on TRACE (USERNAME asc, EXECUTION_DATE asc);

drop index IF EXISTS IDX_TRA_ITEM_URN_DATE;
create index IDX_TRA_ITEM_URN_DATE on TRACE (ITEM_URN asc, EXECUTION_DATE asc);


comment on column TRACE.TRA_ID is
'Id Trace';

comment on column TRACE.CATEGORY is
'Category';

comment on column TRACE.USERNAME is
'Username';

comment on column TRACE.BUSINESS_DATE is
'Business date';

comment on column TRACE.EXECUTION_DATE is
'Execution date';

comment on column TRACE.ITEM_URN is
'Item urn';

comment on column TRACE.MESSAGE is
'Message';

comment on column TRACE.CONTEXT is
'Context';


