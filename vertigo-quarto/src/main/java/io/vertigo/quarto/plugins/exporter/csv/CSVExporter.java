/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.quarto.plugins.exporter.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.commons.codec.Encoder;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;
import io.vertigo.datamodel.data.definitions.DtField;
import io.vertigo.datamodel.data.model.DtObject;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.quarto.exporter.model.Export;
import io.vertigo.quarto.exporter.model.ExportField;
import io.vertigo.quarto.exporter.model.ExportSheet;
import io.vertigo.quarto.impl.exporter.util.ExporterUtil;

/**
 * Export avec ETAT.
 *
 * @author pchretien, npiedeloup
 */
final class CSVExporter {
	/**
	 * Séparateur csv : par défaut ";".
	 */
	private static final String SEPARATOR = ";";

	/**
	 * Caractère de fin de ligne
	 */
	private static final String END_LINE = "" + (char) 13 + (char) 10;

	/**
	 * Encoder CSV
	 */
	private final Encoder<String, String> csvEncoder;

	private final Map<DtField, Map<Object, String>> referenceCache = new HashMap<>();
	private final Map<Class, BasicTypeAdapter> exportAdapters;
	private final Map<DtField, Map<Object, String>> denormCache = new HashMap<>();
	private final EntityStoreManager entityStoreManager;
	private final SmartTypeManager smartTypeManager;
	private final Charset charset;

	/**
	 * Constructeur.
	 *
	 * @param codecManager Manager des codecs
	 */
	CSVExporter(final Charset charset,
			final CodecManager codecManager,
			final EntityStoreManager entityStoreManager,
			final SmartTypeManager smartTypeManager) {
		Assertion.check()
				.isNotNull(charset)
				.isNotNull(codecManager)
				.isNotNull(entityStoreManager)
				.isNotNull(smartTypeManager);
		//-----
		csvEncoder = codecManager.getCsvEncoder();
		this.entityStoreManager = entityStoreManager;
		this.smartTypeManager = smartTypeManager;
		exportAdapters = smartTypeManager.getTypeAdapters("export");
		this.charset = charset;
	}

	/**
	 * Méthode principale qui gère l'export d'un tableau vers un fichier CVS. On
	 * ajoute le BOM UTF8 si le fichier est généré en UTF-8 pour une bonne
	 * ouverture dans Excel.
	 *
	 * @param documentParameters  Paramètres du document à exporter
	 * @param out Flux de sortie
	 * @throws IOException Exception d'ecriture
	 */
	void exportData(final Export documentParameters, final OutputStream out) throws IOException {
		try (final Writer writer = new OutputStreamWriter(out, charset.name())) {
			if (StandardCharsets.UTF_8.equals(charset)) {
				// on met le BOM UTF-8 afin d'avoir des ouvertures correctes avec
				// excel
				writer.append('\uFEFF');
			}
			final boolean isMultiData = documentParameters.sheets().size() > 1;
			for (final ExportSheet exportSheet : documentParameters.sheets()) {
				exportHeader(exportSheet, writer);
				exportData(exportSheet, writer);
				if (isMultiData) {
					writer.write("\"\"");
					writer.write(END_LINE);
				}
			}
		}
	}

	/**
	 * Réalise l'export des données d'en-tête.
	 *
	 * @param parameters de cet export
	 * @param out Le flux d'écriture des données exportées.
	 * @throws IOException Exception lors de l'écriture dans le flux.
	 */
	private void exportHeader(final ExportSheet parameters, final Writer out) throws IOException {
		final String title = parameters.getTitle();
		if (title != null) {
			out.write(encodeString(title));
			out.write(END_LINE);
		}

		String sep = "";
		for (final ExportField exportColumn : parameters.getExportFields()) {
			out.write(sep);
			out.write(encodeString(exportColumn.getLabel().getDisplay()));
			sep = SEPARATOR;
		}
		out.write(END_LINE);
	}

	/**
	 * Réalise l'export des données de contenu.
	 *
	 * @param parameters de cet export
	 * @param out Le flux d'écriture des données exportées.
	 * @throws IOException Exception lors de l'écriture dans le flux.
	 */
	private void exportData(final ExportSheet parameters, final Writer out) throws IOException {
		// Parcours des DTO de la DTC
		if (parameters.hasDtObject()) {
			exportLine(parameters.getDtObject(), parameters, out);
		} else {
			for (final DtObject dto : parameters.getDtList()) {
				exportLine(dto, parameters, out);
			}
		}
	}

	private void exportLine(final DtObject dto, final ExportSheet parameters, final Writer out) throws IOException {
		String sep = "";
		String sValue;
		for (final ExportField exportColumn : parameters.getExportFields()) {
			final DtField dtField = exportColumn.getDtField();
			out.write(sep);
			sValue = ExporterUtil.getText(entityStoreManager, smartTypeManager, exportAdapters, referenceCache, denormCache, dto, exportColumn);
			if (dtField.smartTypeDefinition().getScope().isBasicType() && dtField.smartTypeDefinition().getBasicType() == BasicType.BigDecimal) {
				out.write(encodeNumber(sValue));
			} else {
				out.write(encodeString(sValue));
			}
			sep = SEPARATOR;
		}
		out.write(END_LINE);
	}

	/**
	 * Encode la chaîne exportée en csv.
	 *
	 * @param str La chaîne à encoder.
	 * @return La chaîne encodée.
	 */
	private String encodeString(final String str) {
		return '\"' + csvEncoder.encode(str) + '\"';

	}

	/**
	 * Encode la chaîne exportée en csv.
	 *
	 * @param str La chaîne à encoder.
	 * @return La chaîne encodée.
	 */
	private String encodeNumber(final String str) {
		return encodeString(str).replace('.', ',');
	}

}
