package com.vluee.ddd.support.infrastructure.domainevent;

import com.vluee.ddd.support.domain.AggregateId;

import lombok.Getter;

public abstract class AbstractDomainEvent implements DomainEvent {

	private static final long serialVersionUID = -8179599351528628426L;

	@Getter
	private Class<?> domainClass;

	@Getter
	private AggregateId domainId;

	public AbstractDomainEvent(Class<?> domainClass, AggregateId domainId) {
		super();
		this.domainClass = domainClass;
		this.domainId = domainId;
	}

}
