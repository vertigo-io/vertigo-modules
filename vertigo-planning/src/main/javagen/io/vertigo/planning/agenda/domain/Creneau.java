package io.vertigo.planning.agenda.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;
import io.vertigo.datamodel.data.stereotype.Field;
import io.vertigo.datamodel.data.util.DataModelUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class Creneau implements Entity {
	private static final long serialVersionUID = 1L;

	private Long creId;

	@io.vertigo.datamodel.data.stereotype.Association(
			name = "ACreneauTrancheHoraire",
			fkFieldName = "trhId",
			primaryDtDefinitionName = "DtTrancheHoraire",
			primaryIsNavigable = true,
			primaryRole = "TrancheHoraire",
			primaryLabel = "Tranche horaire",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtCreneau",
			foreignIsNavigable = false,
			foreignRole = "Creneau",
			foreignLabel = "Creneau",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.planning.agenda.domain.TrancheHoraire> trhIdAccessor = new StoreVAccessor<>(io.vertigo.planning.agenda.domain.TrancheHoraire.class, "TrancheHoraire");

	@io.vertigo.datamodel.data.stereotype.Association(
			name = "AReservationCreneauCreneau",
			fkFieldName = "recId",
			primaryDtDefinitionName = "DtReservationCreneau",
			primaryIsNavigable = true,
			primaryRole = "ReservationCreneau",
			primaryLabel = "Réservation",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DtCreneau",
			foreignIsNavigable = false,
			foreignRole = "Creneau",
			foreignLabel = "Creneau",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.planning.agenda.domain.ReservationCreneau> recIdAccessor = new StoreVAccessor<>(io.vertigo.planning.agenda.domain.ReservationCreneau.class, "ReservationCreneau");

	/** {@inheritDoc} */
	@Override
	public UID<Creneau> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return Long creId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPId", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id")
	public Long getCreId() {
		return creId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id'.
	 * @param creId Long <b>Obligatoire</b>
	 */
	public void setCreId(final Long creId) {
		this.creId = creId;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Tranche horaire'.
	 * @return Long trhId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.data.stereotype.ForeignKey(smartType = "STyPId", label = "Tranche horaire", fkDefinition = "DtTrancheHoraire", cardinality = io.vertigo.core.lang.Cardinality.ONE )
	public Long getTrhId() {
		return (Long) trhIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Tranche horaire'.
	 * @param trhId Long <b>Obligatoire</b>
	 */
	public void setTrhId(final Long trhId) {
		trhIdAccessor.setId(trhId);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Réservation'.
	 * @return Long recId
	 */
	@io.vertigo.datamodel.data.stereotype.ForeignKey(smartType = "STyPId", label = "Réservation", fkDefinition = "DtReservationCreneau" )
	public Long getRecId() {
		return (Long) recIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Réservation'.
	 * @param recId Long
	 */
	public void setRecId(final Long recId) {
		recIdAccessor.setId(recId);
	}

 	/**
	 * Association : Tranche horaire.
	 * @return l'accesseur vers la propriété 'Tranche horaire'
	 */
	public StoreVAccessor<io.vertigo.planning.agenda.domain.TrancheHoraire> trancheHoraire() {
		return trhIdAccessor;
	}

 	/**
	 * Association : Réservation.
	 * @return l'accesseur vers la propriété 'Réservation'
	 */
	public StoreVAccessor<io.vertigo.planning.agenda.domain.ReservationCreneau> reservationCreneau() {
		return recIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
