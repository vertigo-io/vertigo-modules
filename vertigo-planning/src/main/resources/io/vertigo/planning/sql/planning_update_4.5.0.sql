-- ============================================================
--   SGBD      		  :  PostgreSql
--   Update vertigo-planning schema to 4.5.0 : performance indexes and unique constraints
-- ============================================================

-- Adding the unique constraints fails if the data already contains duplicates. Check them first :
--   select REC_ID, count(*) from CRENEAU where REC_ID is not null group by REC_ID having count(*) > 1;
--   select AGE_ID, DATE_LOCALE, MINUTES_DEBUT, count(*) from PLAGE_HORAIRE group by AGE_ID, DATE_LOCALE, MINUTES_DEBUT having count(*) > 1;
-- The script is rerunnable (each run rebuilds the unique constraints : lock on CRENEAU and PLAGE_HORAIRE).

-- a reservation occupies at most one creneau (the unique constraint also indexes REC_ID)
alter table CRENEAU
	drop constraint IF EXISTS A_RESERVATION_CRENEAU_CRENEAU_RESERVATION_CRENEAU_UNIQUE_FK;
alter table CRENEAU
	add constraint A_RESERVATION_CRENEAU_CRENEAU_RESERVATION_CRENEAU_UNIQUE_FK unique (REC_ID);
-- the former REC_ID index is dropped only once the unique constraint indexes it
drop index IF EXISTS A_RESERVATION_CRENEAU_CRENEAU_RESERVATION_CRENEAU_FK;

-- two plages of an agenda never start at the same time on the same day
alter table PLAGE_HORAIRE
	drop constraint IF EXISTS A_PLAGE_HORAIRE_UNIQUE;
alter table PLAGE_HORAIRE
	add constraint A_PLAGE_HORAIRE_UNIQUE unique (DATE_LOCALE, MINUTES_DEBUT, AGE_ID);

create index IF NOT EXISTS IDX_TRH_AGE_DATE_PUBLICATION_MINUTE on TRANCHE_HORAIRE (AGE_ID, DATE_LOCALE asc, INSTANT_PUBLICATION asc, MINUTES_DEBUT asc);

create index IF NOT EXISTS IDX_PLH_AGE_DATE on PLAGE_HORAIRE (AGE_ID, DATE_LOCALE asc, MINUTES_DEBUT asc, MINUTES_FIN asc);

create index IF NOT EXISTS IDX_REC_AGE_DATE on RESERVATION_CRENEAU (AGE_ID, DATE_LOCALE asc);
