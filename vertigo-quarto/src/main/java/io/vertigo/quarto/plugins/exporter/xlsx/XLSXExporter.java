/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2025, Vertigo.io, team@vertigo.io
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
package io.vertigo.quarto.plugins.exporter.xlsx;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;
import io.vertigo.datamodel.data.definitions.DataField;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.quarto.exporter.model.Export;
import io.vertigo.quarto.exporter.model.ExportField;
import io.vertigo.quarto.exporter.model.ExportSheet;
import io.vertigo.quarto.impl.exporter.util.ExporterUtil;

/**
 *
 * Export XLSX.
 * Uses POI.
 *
 * @author pchretien, npiedeloup, mlaroche
 */
public class XLSXExporter {
	private static final int MAX_COLUMN_WIDTH = 50;

	private final Map<DataField, Map<Object, String>> referenceCache = new HashMap<>();
	private final Map<DataField, Map<Object, String>> denormCache = new HashMap<>();

	private final Map<BasicType, XSSFCellStyle> evenXssfStyleCache = new EnumMap<>(BasicType.class);
	private final Map<BasicType, XSSFCellStyle> oddXssfStyleCache = new EnumMap<>(BasicType.class);

	private final EntityStoreManager storeManager;

	private final SmartTypeManager smartTypeManager;
	private final Map<Class, BasicTypeAdapter> exportAdapters;

	/**
	 * Constructor.
	 *
	 * @param storeManager Store manager
	 * @param smartTypeManager SmartType manager
	 */
	XLSXExporter(final EntityStoreManager storeManager, final SmartTypeManager smartTypeManager) {
		Assertion.check()
				.isNotNull(storeManager)
				.isNotNull(smartTypeManager);
		//-----
		this.storeManager = storeManager;
		this.smartTypeManager = smartTypeManager;
		exportAdapters = smartTypeManager.getTypeAdapters("export");
	}

	private static XSSFCellStyle createHeaderCellStyle(final XSSFWorkbook workbook) {
		final var cellStyle = workbook.createCellStyle();
		final var font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBold(true);
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		return cellStyle;
	}

	private static XSSFCellStyle createRowCellStyle(final XSSFWorkbook workbook, final boolean odd) {
		final var cellStyle = workbook.createCellStyle();
		final var font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cellStyle.setFillForegroundColor(odd ? IndexedColors.WHITE.getIndex() : IndexedColors.GREY_25_PERCENT.getIndex());

		return cellStyle;
	}

	/**
	 * Réalise l'export des données de contenu et de la ligne d'en-tête.
	 *
	 * @param parameters Paramètre de cet export
	 * @param workbook Document excel
	 * @param sheet Feuille Excel
	 * @param forceLandscape Indique si le parametrage force un affichage en paysage
	 */
	private void exportData(final ExportSheet parameters, final XSSFWorkbook workbook, final XSSFSheet sheet, final boolean forceLandscape) {
		// Column width
		final Map<Integer, Double> maxWidthPerColumn = new HashMap<>();
		if (parameters.hasDtObject()) {
			exportObject(parameters, workbook, sheet, maxWidthPerColumn);
		} else {
			exportList(parameters, workbook, sheet, maxWidthPerColumn);
		}
		// On definit la largeur des colonnes:
		var totalWidth = 0D;
		int cellIndex;
		for (final Map.Entry<Integer, Double> entry : maxWidthPerColumn.entrySet()) {
			cellIndex = entry.getKey();
			final var maxLength = entry.getValue();
			final var usesMaxLength = Double.valueOf(Math.min(maxLength.doubleValue(), MAX_COLUMN_WIDTH)).intValue();
			sheet.setColumnWidth(cellIndex, usesMaxLength * 256);
			totalWidth += usesMaxLength;
		}
		/**
		 * @todo ne serait-il pas plus simple d'utilisersheet.autoSizeColumn(i); de poi 3.0.1 ?
		 */

		// note: il ne semble pas simple de mettre title et author dans les propriétés du document
		final var title = parameters.getTitle();
		if (title != null) {
			final var header = sheet.getHeader();
			header.setLeft(title);
		}
		sheet.setHorizontallyCenter(true);
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
		if (forceLandscape || totalWidth > 85) {
			sheet.getPrintSetup().setLandscape(true);
		}

		// On définit le footer
		final var footer = sheet.getFooter();
		footer.setRight("Page " + HeaderFooter.page() + " / " + HeaderFooter.numPages());
	}

	private void initHssfStyle(final XSSFWorkbook workbook) {

		final var df = workbook.createDataFormat();

		// default:
		final var oddCellStyle = createRowCellStyle(workbook, true);
		final var evenCellStyle = createRowCellStyle(workbook, true);
		oddXssfStyleCache.put(BasicType.Boolean, oddCellStyle);
		oddXssfStyleCache.put(BasicType.String, oddCellStyle);
		evenXssfStyleCache.put(BasicType.Boolean, evenCellStyle);
		evenXssfStyleCache.put(BasicType.String, evenCellStyle);

		// Nombre sans décimal
		final var oddLongCellStyle = createRowCellStyle(workbook, true);
		final var evenLongCellStyle = createRowCellStyle(workbook, true);
		oddLongCellStyle.setDataFormat(df.getFormat("#,##0"));
		evenLongCellStyle.setDataFormat(df.getFormat("#,##0"));
		oddXssfStyleCache.put(BasicType.Long, oddLongCellStyle);
		oddXssfStyleCache.put(BasicType.Integer, oddLongCellStyle);
		evenXssfStyleCache.put(BasicType.Long, evenLongCellStyle);
		evenXssfStyleCache.put(BasicType.Integer, evenLongCellStyle);

		// Nombre a décimal
		final var oddDoubleCellStyle = createRowCellStyle(workbook, true);
		final var evenDoubleCellStyle = createRowCellStyle(workbook, true);
		oddDoubleCellStyle.setDataFormat(df.getFormat("#,##0.00"));
		evenDoubleCellStyle.setDataFormat(df.getFormat("#,##0.00"));
		oddXssfStyleCache.put(BasicType.Double, oddDoubleCellStyle);
		oddXssfStyleCache.put(BasicType.BigDecimal, oddDoubleCellStyle);
		evenXssfStyleCache.put(BasicType.Double, evenDoubleCellStyle);
		evenXssfStyleCache.put(BasicType.BigDecimal, evenDoubleCellStyle);

		// Date
		final var oddDateCellStyle = createRowCellStyle(workbook, true);
		final var evenDateCellStyle = createRowCellStyle(workbook, true);
		oddDateCellStyle.setDataFormat(df.getFormat("m/d/yy" /* "m/d/yy h:mm" */));
		evenDateCellStyle.setDataFormat(df.getFormat("m/d/yy" /* "m/d/yy h:mm" */));
		oddXssfStyleCache.put(BasicType.LocalDate, oddDateCellStyle);
		evenXssfStyleCache.put(BasicType.LocalDate, evenDateCellStyle);

	}

	private void exportList(final ExportSheet parameters, final XSSFWorkbook workbook, final XSSFSheet sheet, final Map<Integer, Double> maxWidthPerColumn) {
		// exporte le header
		final var headerRow = sheet.createRow(0);
		var cellIndex = 0;
		for (final ExportField exportColumn : parameters.getExportFields()) {
			final var cell = headerRow.createCell(cellIndex);
			final var displayedLabel = exportColumn.getLabel().getDisplay();
			cell.setCellValue(new XSSFRichTextString(displayedLabel));
			cell.setCellStyle(createHeaderCellStyle(workbook));

			updateMaxWidthPerColumn(displayedLabel, 1.2, cellIndex, maxWidthPerColumn); // +20% pour les majuscules
			cellIndex++;
		}
		//La premiere ligne est répétable
		sheet.setRepeatingRows(new CellRangeAddress(0, 0, -1, -1));

		var rowIndex = 1;
		for (final DataObject dto : parameters.getDtList()) {
			final var row = sheet.createRow(rowIndex);
			cellIndex = 0;
			Object value;
			for (final ExportField exportColumn : parameters.getExportFields()) {
				final var cell = row.createCell(cellIndex);

				final var smartTypeDefinition = exportColumn.getDataField().smartTypeDefinition();
				if (smartTypeDefinition.getScope().isBasicType()) {
					value = ExporterUtil.getValue(storeManager, smartTypeManager, exportAdapters, referenceCache, denormCache, dto, exportColumn);
				} else {
					value = ExporterUtil.getText(storeManager, smartTypeManager, exportAdapters, referenceCache, denormCache, dto, exportColumn);
				}
				putValueInCell(value, cell, rowIndex % 2 == 0 ? evenXssfStyleCache : oddXssfStyleCache, cellIndex, maxWidthPerColumn, smartTypeDefinition);

				cellIndex++;
			}
			rowIndex++;
		}
	}

	private void exportObject(final ExportSheet parameters, final XSSFWorkbook workbook, final XSSFSheet sheet, final Map<Integer, Double> maxWidthPerColumn) {
		var rowIndex = 0;
		final var labelCellIndex = 0;
		final var valueCellIndex = 1;
		final var dto = parameters.getDtObject();
		Object value;
		for (final ExportField exportColumn : parameters.getExportFields()) {
			final var row = sheet.createRow(rowIndex);

			final var cell = row.createCell(labelCellIndex);
			final var label = exportColumn.getLabel();
			cell.setCellValue(new XSSFRichTextString(label.getDisplay()));
			cell.setCellStyle(createHeaderCellStyle(workbook));
			updateMaxWidthPerColumn(label.getDisplay(), 1.2, labelCellIndex, maxWidthPerColumn); // +20% pour les majuscules

			final var valueCell = row.createCell(valueCellIndex);
			final var smartTypeDefinition = exportColumn.getDataField().smartTypeDefinition();
			if (smartTypeDefinition.getScope().isBasicType()) {
				value = ExporterUtil.getValue(storeManager, smartTypeManager, exportAdapters, referenceCache, denormCache, dto, exportColumn);
			} else {
				value = ExporterUtil.getText(storeManager, smartTypeManager, exportAdapters, referenceCache, denormCache, dto, exportColumn);
			}
			putValueInCell(value, valueCell, oddXssfStyleCache, valueCellIndex, maxWidthPerColumn, smartTypeDefinition);
			rowIndex++;
		}

	}

	private void putValueInCell(final Object value, final XSSFCell cell, final Map<BasicType, XSSFCellStyle> rowCellStyle, final int cellIndex, final Map<Integer, Double> maxWidthPerColumn,
			final SmartTypeDefinition domain) {
		String stringValueForColumnWidth;
		if (domain.getScope().isBasicType()) {
			cell.setCellStyle(rowCellStyle.get(domain.getBasicType()));
		} else {
			cell.setCellStyle(rowCellStyle.get(BasicType.String));
		}
		if (value != null) {
			stringValueForColumnWidth = String.valueOf(value);

			if (value instanceof final String stringValue) {
				cell.setCellValue(new XSSFRichTextString(stringValue));
			} else if (value instanceof final Integer integerValue) {
				cell.setCellValue(integerValue.doubleValue());
			} else if (value instanceof final Double dValue) {
				cell.setCellValue(dValue.doubleValue());
				stringValueForColumnWidth = String.valueOf(Math.round(dValue.doubleValue() * 100) / 100D);
			} else if (value instanceof final Long lValue) {
				cell.setCellValue(lValue.doubleValue());
			} else if (value instanceof final BigDecimal bigDecimalValue) {
				cell.setCellValue(bigDecimalValue.doubleValue());
				stringValueForColumnWidth = String.valueOf(Math.round(bigDecimalValue.doubleValue() * 100) / 100D);
			} else if (value instanceof final Boolean bValue) {
				//cell.setCellValue(bValue.booleanValue() ? "Oui" : "Non");
				cell.setCellValue(smartTypeManager.valueToString(domain, bValue.booleanValue()));
			} else if (value instanceof final LocalDate dateValue) {
				// sans ce style "date" les dates apparaîtraient au format
				// "nombre"
				cell.setCellValue(dateValue);
				stringValueForColumnWidth = "DD/MM/YYYY";
				// ceci ne sert que pour déterminer la taille de la cellule, on a pas besoin de la vrai valeur
			} else if (value instanceof final Instant instantValue) {
				cell.setCellValue(LocalDateTime.ofInstant(instantValue, ZoneId.of("UTC")));
				stringValueForColumnWidth = "DD/MM/YYYY HH:mm";
				// ceci ne sert que pour déterminer la taille de la cellule, on a pas besoin de la vrai valeur
			} else {
				throw new UnsupportedOperationException("Type " + domain.getBasicType() + " not supported by this Excel exporter");
			}
			updateMaxWidthPerColumn(stringValueForColumnWidth, 1, cellIndex, maxWidthPerColumn); // +20% pour les majuscules
		}
	}

	private static void updateMaxWidthPerColumn(final String value, final double textSizeCoeff, final int cellIndex, final Map<Integer, Double> maxWidthPerColumn) {
		// Calcul de la largeur des colonnes
		final var newLenght = value != null ? value.length() * textSizeCoeff + 2 : 0; // +textSizeCoeff% pour les majuscules, et +2 pour les marges
		final var oldLenght = maxWidthPerColumn.get(cellIndex);
		if (oldLenght == null || oldLenght.doubleValue() < newLenght) {
			maxWidthPerColumn.put(cellIndex, newLenght);
		}
	}

	/**
	 * Méthode principale qui gère l'export d'un tableau vers un fichier ODS.
	 *
	 * @param documentParameters Paramètres du document à exporter
	 * @param out Flux de sortie
	 * @throws IOException Io exception
	 */
	void exportData(final Export documentParameters, final OutputStream out) throws IOException {
		// Workbook
		final var forceLandscape = Export.Orientation.Landscape == documentParameters.orientation();
		try (final var workbook = new XSSFWorkbook()) {
			initHssfStyle(workbook);
			for (final ExportSheet exportSheet : documentParameters.sheets()) {
				final var title = exportSheet.getTitle();
				final var sheet = title == null ? workbook.createSheet() : workbook.createSheet(title);
				exportData(exportSheet, workbook, sheet, forceLandscape);

			}
			workbook.write(out);
		}
	}
}
