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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.Container;

import com.moisespsena.vraptor.annotationscanner.AnnotationScannerResolver;
import com.moisespsena.vraptor.listenerexecution.ContainerListenerInstanceResolver;
import com.moisespsena.vraptor.listenerexecution.DefaultExecutionStack;
import com.moisespsena.vraptor.listenerexecution.DefaultExecutionStackExecution;
import com.moisespsena.vraptor.listenerexecution.ExecutionStack;
import com.moisespsena.vraptor.listenerexecution.ExecutionStackException;
import com.moisespsena.vraptor.listenerexecution.ExecutionStackExecution;
import com.moisespsena.vraptor.listenerexecution.HandlersRegistry;
import com.moisespsena.vraptor.listenerexecution.HandlersRunnerFactory;
import com.moisespsena.vraptor.listenerexecution.topological.TopologicalSortedHandlersRegistry;

/**
 * @author Moises P. Sena (http://moisespsena.com)
 * @since 1.0 16/09/2011
 */
@Component
@ApplicationScoped
public class PreValidationViewListeners {
	private final AnnotationScannerResolver annotationScannerResolver;

	private final Container container;

	private Class<? extends PreValidationViewListener>[] ordenedHandlerClasses;
	private HandlersRunnerFactory<PreValidationViewListener> runnerFactory;

	public PreValidationViewListeners(
			final AnnotationScannerResolver annotationScannerResolver,
			final Container container) {
		this.annotationScannerResolver = annotationScannerResolver;
		this.container = container;
	}

	public ExecutionStackExecution<PreValidationViewListener> createStackExecution(
			final PreValidationViewListenerExecutor executor)
			throws ExecutionStackException {
		final ExecutionStack<PreValidationViewListener> stack = new DefaultExecutionStack<PreValidationViewListener>(
				executor, runnerFactory, ordenedHandlerClasses);
		final ExecutionStackExecution<PreValidationViewListener> stackExecution = new DefaultExecutionStackExecution<PreValidationViewListener>(
				stack);
		return stackExecution;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void initialize() throws PreValidationViewListenersException {
		try {
			final Set<Class<? extends PreValidationViewListener>> handlersClasses = new HashSet<Class<? extends PreValidationViewListener>>();

			final Set<String> classesNames = annotationScannerResolver
					.typesOf(PreValidationViewRender.class);

			if (classesNames != null) {
				for (final String className : classesNames) {
					final Class<?> clazz = Class.forName(className);
					handlersClasses
							.add((Class<? extends PreValidationViewListener>) clazz);
				}
			}

			final HandlersRegistry<PreValidationViewListener> registry = new TopologicalSortedHandlersRegistry<PreValidationViewListener>();
			registry.register(handlersClasses.toArray(new Class[0]));
			ordenedHandlerClasses = registry.allArray();

			runnerFactory = new HandlersRunnerFactory<PreValidationViewListener>(
					new ContainerListenerInstanceResolver(container));
		} catch (final ClassNotFoundException e) {
			throw new PreValidationViewListenersException(e);
		}
	}
}
