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
package com.vluee.ddd.support.infrastructure.domainevent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vluee.ddd.support.domain.AggregateId;
import com.vluee.ddd.support.domain.DomainEventPublisher;
import com.vluee.ddd.support.infrastructure.domainevent.impl.handler.EventHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleEventPublisher implements DomainEventPublisher {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventPublisher.class);

	public static SimpleEventPublisher INSTANCE;
	private Set<EventHandler> eventHandlers = new HashSet<EventHandler>();

	@Autowired(required = false)
	private DomainEventLogRepository domainEventLogRepository;

	public void registerEventHandler(EventHandler handler) {
		eventHandlers.add(handler);
		// new SpringEventHandler(eventType, beanName, method));
	}

	@Override
	public void publish(Serializable event) {
		saveDomainEventLog(event);
		doPublish(event);
	}

	private void saveDomainEventLog(Serializable event) {
		if (domainEventLogRepository != null) {
			if (event instanceof DomainEvent) {
				DomainEvent domainEvent = ((DomainEvent) event);
				domainEventLogRepository
						.save(new DomainEventLog(AggregateId.generate(), null, domainEvent.getClass().getName(),
								domainEvent.getDomainClass().getName(), domainEvent.getDomainId()));
			}
		}
	}

	protected void doPublish(Object event) {
		for (EventHandler handler : new ArrayList<EventHandler>(eventHandlers)) {
			if (handler.canHandle(event)) {
				try {
					handler.handle(event);
				} catch (Exception e) {
					LOGGER.error("event handling error", e);
				}
			}
		}
	}

	@PostConstruct
	public void afterSetup() {
		INSTANCE = this;

		if (domainEventLogRepository == null) {
			log.warn(
					"The Simple Event Publisher does not have a DomainEventLogRepository Implementation. Will not log the domain event.");
		}
	}
}
