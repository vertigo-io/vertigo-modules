package io.vertigo.studio.mda;

import io.vertigo.kernel.component.Plugin;

import java.util.Properties;

/**
 * Plugin de génération de fichiers.
 * 
 * @author dchallas
 * @param <C> Type de configuration du générateur
 */
public interface GeneratorPlugin<C extends Configuration> extends Plugin {
	/**
	 * Positionne les propriétés.
	 * @param properties Propriétés.
	 * @return Configuration de la génération
	 */
	C createConfiguration(Properties properties);

	/**
	 * Génération d'un fichier à partir d'une source et de paramètres.
	 * @param configuration Configuration de la génération
	 * @param result Résultat de la génération
	 */
	void generate(final C configuration, final Result result);
}
