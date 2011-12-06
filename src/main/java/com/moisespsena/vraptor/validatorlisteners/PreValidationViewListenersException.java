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

/**
 * @author Moises P. Sena (http://moisespsena.com)
 * @since 1.0 20/09/2011
 */
public class PreValidationViewListenersException extends Exception {
	private static final long serialVersionUID = 6959993937345625218L;

	/**
	 * 
	 */
	public PreValidationViewListenersException() {
	}

	/**
	 * @param message
	 */
	public PreValidationViewListenersException(final String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PreValidationViewListenersException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public PreValidationViewListenersException(final Throwable cause) {
		super(cause);
	}

}
