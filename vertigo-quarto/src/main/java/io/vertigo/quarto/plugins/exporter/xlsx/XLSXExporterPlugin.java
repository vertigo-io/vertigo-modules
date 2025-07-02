package io.vertigo.quarto.plugins.exporter.xlsx;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.quarto.exporter.model.Export;
import io.vertigo.quarto.exporter.model.ExportFormat;
import io.vertigo.quarto.impl.exporter.ExporterPlugin;

public class XLSXExporterPlugin implements ExporterPlugin {
	private final EntityStoreManager storeManager;
	private final SmartTypeManager smartTypeManager;

	@Inject
	public XLSXExporterPlugin(final EntityStoreManager storeManager, final SmartTypeManager smartTypeManager) {
		this.storeManager = storeManager;
		this.smartTypeManager = smartTypeManager;
	}

	/** {@inheritDoc} */
	@Override
	public void exportData(final Export export, final OutputStream out) throws IOException {
		new XLSXExporter(storeManager, smartTypeManager).exportData(export, out);
	}

	/** {@inheritDoc} */
	@Override
	public boolean accept(final ExportFormat exportFormat) {
		return ExportFormat.XLSX.equals(exportFormat);
	}

}
