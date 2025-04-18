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
package io.vertigo.easyforms;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import io.vertigo.core.node.config.DefinitionProviderConfig;
import io.vertigo.core.node.config.Feature;
import io.vertigo.core.param.Param;
import io.vertigo.datamodel.impl.smarttype.ModelDefinitionProvider;
import io.vertigo.easyforms.domain.DtDefinitions;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.runner.pack.provider.FieldTypeDefinitionProvider;
import io.vertigo.easyforms.runner.pack.provider.FieldValidatorTypeDefinitionProvider;
import io.vertigo.easyforms.runner.pack.provider.UiComponentDefinitionProvider;
import io.vertigo.ui.impl.springmvc.config.DefaultUiModuleFeatures;

@Configuration
public final class EasyFormsFeatures extends DefaultUiModuleFeatures<EasyFormsFeatures> {

	public EasyFormsFeatures() {
		super("vertigo-easyforms");
	}

	@Override
	protected String getPackageRoot() {
		return this.getClass().getPackage().getName();
	}

	@Feature("easyforms")
	public EasyFormsFeatures withConfig(final Param... params) {
		getModuleConfigBuilder()
				.addComponent(EasyFormsRunnerManager.class, params);
		return this;
	}

	@Override
	protected void buildFeatures() {
		super.buildFeatures();
		getModuleConfigBuilder()
				.addDefinitionProvider(DefinitionProviderConfig.builder(ModelDefinitionProvider.class)
						.addDefinitionResource("smarttypes", EasyFormsSmartTypes.class.getName())
						.addDefinitionResource("dtobjects", DtDefinitions.class.getName())
						.build())
				// add
				.addDefinitionProvider(FieldTypeDefinitionProvider.class)
				.addDefinitionProvider(FieldValidatorTypeDefinitionProvider.class)
				.addDefinitionProvider(UiComponentDefinitionProvider.class);
	}

	@Override
	protected List<String> getControllerPackages() {
		return List.of(".designer.controllers", ".runner.controllers");
	}

}
