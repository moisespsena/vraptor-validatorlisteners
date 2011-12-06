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

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Lazy;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;

import com.moisespsena.vraptor.flashparameters.FlashMessages;
import com.moisespsena.vraptor.modularvalidator.SimpleMessage;

/**
 * @author Moises P. Sena (http://moisespsena.com)
 * @since 1.0 13/09/2011
 */
@Lazy
@Intercepts
@RequestScoped
public class ValidationSinalizerInterceptor implements Interceptor {

	private final FlashMessages flashMessages;

	public ValidationSinalizerInterceptor(final FlashMessages flashMessages) {
		this.flashMessages = flashMessages;
	}

	@Override
	public boolean accepts(final ResourceMethod method) {
		return true;
	}

	@Override
	public void intercept(final InterceptorStack stack,
			final ResourceMethod method, final Object resourceInstance)
			throws InterceptionException {
		try {
			stack.next(method, resourceInstance);
		} catch (final Exception e) {
			if (PreValidationViewCancelableSinalizer.isValid(e)) {
				for (final SimpleMessage message : PreValidationViewCancelableSinalizer
						.getMessages(e)) {
					flashMessages.addSimpleMessage(message);
				}
			} else if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new InterceptionException(e);
			}
		}
	}
}
