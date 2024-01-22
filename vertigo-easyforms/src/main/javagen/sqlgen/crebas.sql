-- ============================================================
--   SGBD      		  :  PotsgreSql                     
-- ============================================================

-- ============================================================
--   Drop                                       
-- ============================================================
drop table IF EXISTS EASY_FORM cascade;
drop sequence IF EXISTS SEQ_EASY_FORM;




-- ============================================================
--   Sequences                                      
-- ============================================================
create sequence SEQ_EASY_FORM
	start with 1000 cache 1; 


-- ============================================================
--   Table : EASY_FORM                                        
-- ============================================================
create table EASY_FORM
(
    EFO_ID      	 NUMERIC     	not null,
    TEMPLATE    	 JSONB       	,
    constraint PK_EASY_FORM primary key (EFO_ID)
);

comment on column EASY_FORM.EFO_ID is
'Id';

comment on column EASY_FORM.TEMPLATE is
'Template';



