/***
 * Copyright (c) 2011 Moises P. Sena - www.moisespsena.com
 * All rights reserved.
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
/**
 * 
 */
package com.moisespsena.vraptor.validatorlisteners;

import java.util.List;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.DefaultValidationViewsFactory;

import com.moisespsena.vraptor.listenerexecution.ExecutionStackException;

/**
 * @author Moises P. Sena (http://moisespsena.com)
 * @since 1.0 13/09/2011
 */
@Component
@RequestScoped
public class CustomValidationViewsFactory extends DefaultValidationViewsFactory {
	private final PreValidationViewListeners listeners;
	private final RequestInfo requestInfo;
	private final Result result;

	/**
	 * @param result
	 * @param proxifier
	 */
	public CustomValidationViewsFactory(final Result result,
			final Proxifier proxifier,
			final PreValidationViewListeners listeners,
			final RequestInfo requestInfo) {
		super(result, proxifier);
		this.result = result;
		this.listeners = listeners;
		this.requestInfo = requestInfo;
	}

	@Override
	public <T extends View> T instanceFor(final Class<T> view,
			final List<Message> errors) {
		final PreValidationViewListenerExecutor executor = new PreValidationViewListenerExecutor(
				requestInfo, errors);

		try {
			listeners.createStackExecution(executor).execute();
		} catch (final ExecutionStackException e) {
			throw new CustomValidationViewsFactoryException(e);
		}

		if (!result.used() && executor.isViewRender()) {
			return super.instanceFor(view, errors);
		} else {
			PreValidationViewCancelableSinalizer.sinalize(errors);
			return null;
		}
	}
}
