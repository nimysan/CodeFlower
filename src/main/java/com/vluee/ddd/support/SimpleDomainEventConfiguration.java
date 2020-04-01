package com.vluee.ddd.support;

import javax.inject.Inject;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.vluee.ddd.support.infrastructure.domainevent.DomainEventLogRepository;
import com.vluee.ddd.support.infrastructure.domainevent.SimpleEventPublisher;
import com.vluee.ddd.support.infrastructure.domainevent.impl.EventListenerBeanPostProcessor;

/**
 * 
 * 
 * 
 * @author SeanYe
 *
 */
@Configuration
@Import({ SimpleEventPublisher.class, EventListenerBeanPostProcessor.class })
public class SimpleDomainEventConfiguration {

	@Inject
	private AutowireCapableBeanFactory spring;

	@Bean
	public DomainEventLogRepository create() {
		DomainEventLogRepository domainEventLogRepository = new DomainEventLogRepository();
		spring.autowireBean(domainEventLogRepository);
		return domainEventLogRepository;
	}
}
