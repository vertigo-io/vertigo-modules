/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
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
package io.vertigo.geo.geocoder;

import io.vertigo.core.node.component.Manager;

/**
 * API de Geocoding d'adresses postales ou de POI.
 *
 * @author spoitrenaud, pchretien
 */
public interface GeoCoderManager extends Manager {
	/**
	 * Geocoding d'une adresse.
	 *
	 * @param address Chaine de caractères représentant une adresse.
	 * @return Liste des emplacements (latitude ; longitude) correspondant à l'adresse recherchée.
	 */
	GeoLocation findLocation(String address);

	/**
	 * Calcul de distance entre deux points
	 *
	 * @param geoLocation1 Premier point
	 * @param geoLocation2 Second point
	 * @return Distance exprimées en km.
	 */
	double distanceKm(final GeoLocation geoLocation1, final GeoLocation geoLocation2);
}
