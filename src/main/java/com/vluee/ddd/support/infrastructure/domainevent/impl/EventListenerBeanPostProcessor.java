/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vluee.ddd.support.infrastructure.domainevent.impl;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.vluee.ddd.annotations.event.EventListener;
import com.vluee.ddd.support.infrastructure.domainevent.SimpleEventPublisher;
import com.vluee.ddd.support.infrastructure.domainevent.impl.handler.AsynchronousEventHandler;
import com.vluee.ddd.support.infrastructure.domainevent.impl.handler.EventHandler;
import com.vluee.ddd.support.infrastructure.domainevent.impl.handler.SpringEventHandler;

/**
 * Registers spring beans methods as event handlers in spring event publisher
 * (if needed).
 */
@Component
public class EventListenerBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

	private BeanFactory beanFactory;
	private SimpleEventPublisher eventPublisher;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		for (Method method : bean.getClass().getMethods()) {
			EventListener listenerAnnotation = method.getAnnotation(EventListener.class);

			if (listenerAnnotation == null) {
				continue;
			}

			Class<?> eventType = method.getParameterTypes()[0];

			if (listenerAnnotation.asynchronous()) {
				// TODO just a temporary fake impl
				EventHandler handler = new AsynchronousEventHandler(eventType, beanName, method, beanFactory);
				eventPublisher.registerEventHandler(handler);
			} else {
				EventHandler handler = new SpringEventHandler(eventType, beanName, method, beanFactory);
				eventPublisher.registerEventHandler(handler);
			}
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// do nothing
		return bean;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		eventPublisher = beanFactory.getBean(SimpleEventPublisher.class);
	}
}
