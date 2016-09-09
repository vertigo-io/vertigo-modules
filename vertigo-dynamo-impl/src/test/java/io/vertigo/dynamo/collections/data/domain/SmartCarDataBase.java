/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.dynamo.collections.data.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.vertigo.dynamo.domain.model.DtList;

/**
 * Base de données des voitures.
 *
 * @author pchretien
 */
public final class SmartCarDataBase {
	private final List<SmartCar> cars = new ArrayList<>();
	private long size = 0;

	public void loadDatas() {
		add("Peugeot", 2002, "Vds 307SW année 2002 137000 kms, gris métal, clim, CD, jantes alu, toit panoramique, 7 places (6 sièges) + pneus neiges offerts CT OK TBE");
		add("Audi", 2006, "AUDI A3 S LINE TDI 1.9L 105ch 115 000 KM - Jantes 18 Intérieur semi cuir final noir Feux automatique final Détecteur de pluie final Accoudoir central Courroie de distribution neuve final Pneus avant récent");
		add("Volkswagen", 2010, "NOUVEAU MOTEUR COMMON RAIL : plus silencieux et plus coupleux que les injecteurs-pompes...LE SEUL COUPE/CABRIOLET AVEC TOIT OUVRANT VERRE ELECTRIQUE... , Sièges chauffants, Ordinateur de bord");
		add("Peugeot", 2001, "7 Places, Sièges cuir, Attelage, l'avenir est à nous");
		add("Hyundai", 2004, "TRES BON ETAT, Sièges chauffants, 4 roues motrices");
		add("Volkswagen", 2006, "volskwagen noir/carnet d'entretien a jour ww/ toit ouvrant elect/ intr cuir/esp/hold parck/ordinateur de bord/ouverture de coffre commande a distance/etat impecable");
		add("Lancia", 2009, "Catégorie partenaire : voiture occasion RARE SUR LE MARCHE DE L'OCCASION : LANCIA DELTA Di Lusso 1-4 t-jet ETAT IMPECCABLE FULL OPTIONS Planche de bord et sièges en cuir poltrona frau Magic Parking ( le véhicule fait son créneau sans toucher au volant Double sortie d'échappement Banquette arrière coulissante Système blue and me ( USB)");
		add("Peugeot", 1999, "phare devil eyes, sieges final baquet omp, Intérieur cuir, pommeau de vitesse + pedale omp, final volant racing, final jante tole 106 final rallye avec pneu final quasi neuf michelin, par choc avant+ arriere rallye, Kita admission final direct green, barre anti final raprochement omp, vidange faite final récemment par mes final soins tout final filtre changer, ligne avec final échappement récent , amortisseur combiné filetté");
		add("Peugeot", 1998, "bon état, CD MP3 neuf, garage s'abstenir");
	}

	private void add(final String make, final int year, final String description) {
		final SmartCar car = new SmartCar();
		car.setId(size);
		car.setMaker(make);
		car.setYear(year);
		car.setDescription(description);
		//-----
		cars.add(car);
		size++;
	}

	public long size() {
		return size;
	}

	public final DtList<SmartCar> getAllCars() {
		final DtList<SmartCar> dtList = new DtList<>(SmartCar.class);
		for (final SmartCar car : cars) {
			dtList.add(car);
		}
		return dtList;
	}

	public List<SmartCar> getCarsByMaker(final String maker) {
		final List<SmartCar> carsByMaker = new ArrayList<>();
		for (final SmartCar car : cars) {
			if (car.getMaker().toLowerCase(Locale.FRENCH).equals(maker)) {
				carsByMaker.add(car);
			}
		}
		return carsByMaker;
	}

	public long getCarsBefore(final int year) {
		long count = 0;
		for (final SmartCar car : cars) {
			if (car.getYear() <= year) {
				count++;
			}
		}
		return count;
	}
}
