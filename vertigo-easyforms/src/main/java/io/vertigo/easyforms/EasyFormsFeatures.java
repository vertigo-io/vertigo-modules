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
package io.vertigo.easyforms;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import io.vertigo.core.impl.analytics.trace.TraceAspect;
import io.vertigo.core.node.config.DefinitionProviderConfig;
import io.vertigo.datamodel.impl.smarttype.ModelDefinitionProvider;
import io.vertigo.easyforms.domain.DtDefinitions;
import io.vertigo.easyforms.impl.easyformsrunner.library.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldTypeDefinitionProvider;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldValidatorDefinitionProvider;
import io.vertigo.ui.impl.springmvc.config.DefaultUiModuleFeatures;

@Configuration
public class EasyFormsFeatures extends DefaultUiModuleFeatures<EasyFormsFeatures> {

	public EasyFormsFeatures() {
		super("vertigo-easyforms");
	}

	@Override
	protected String getPackageRoot() {
		return this.getClass().getPackage().getName();
	}

	@Override
	protected void buildFeatures() {
		super.buildFeatures();
		getModuleConfigBuilder()
				.addAspect(TraceAspect.class)
				.addDefinitionProvider(DefinitionProviderConfig.builder(ModelDefinitionProvider.class)
						.addDefinitionResource("smarttypes", EasyFormsSmartTypes.class.getName())
						.addDefinitionResource("dtobjects", DtDefinitions.class.getName())
						.build())
				.addDefinitionProvider(FieldTypeDefinitionProvider.class)
				.addDefinitionProvider(FieldValidatorDefinitionProvider.class);
	}

	@Override
	protected List<String> getControllerPackages() {
		return List.of(".impl.easyformsdesigner.controllers", ".impl.easyformsrunner.controllers");
	}

}
