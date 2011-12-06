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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.caelum.vraptor.validator.Message;

import com.moisespsena.vraptor.modularvalidator.MessagesLevel;
import com.moisespsena.vraptor.modularvalidator.SimpleMessage;
import com.moisespsena.vraptor.modularvalidator.SimpleMessageImpl;

/**
 * @author Moises P. Sena (http://moisespsena.com)
 * @since 1.0 13/09/2011
 */
public class PreValidationViewCancelableSinalizer {
	public static class ValidationSinalizerException extends RuntimeException {
		private static final long serialVersionUID = -2918211849358891644L;
		private final List<Message> errors;

		public ValidationSinalizerException(final List<Message> errors) {
			this.errors = errors;
		}

		public List<SimpleMessage> getErrors() {
			final List<SimpleMessage> messages = new ArrayList<SimpleMessage>(
					errors.size());

			for (final Message error : errors) {
				final SimpleMessage simpleMessage = new SimpleMessageImpl();
				simpleMessage.setCategory(error.getCategory());
				simpleMessage.setMessage(error.getMessage());
				simpleMessage.setLevel(MessagesLevel.ERROR);

				messages.add(simpleMessage);
			}

			return messages;
		}
	}

	public static List<SimpleMessage> getMessages(final Throwable e) {
		if (isValid(e)) {
			final ValidationSinalizerException ex = (e instanceof ValidationSinalizerException) ? (ValidationSinalizerException) e
					: (ValidationSinalizerException) e.getCause();
			return ex.getErrors();
		} else {
			return null;
		}
	}

	public static boolean isValid(final Throwable e) {
		final Set<Throwable> visited = new HashSet<Throwable>();
		Throwable cause = e;

		while ((cause != null) && !visited.contains(cause)
				&& !(cause instanceof ValidationSinalizerException)) {
			cause = cause.getCause();
			visited.add(cause);
		}

		if ((cause != null) && (cause instanceof ValidationSinalizerException)) {
			return true;
		}

		return false;
	}

	public static void sinalize(final List<Message> errors) {
		throw new ValidationSinalizerException(errors);
	}
}
