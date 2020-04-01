package com.vluee.ddd.support.infrastructure.domainevent;

import com.vluee.ddd.support.domain.AggregateId;

import lombok.Getter;

public abstract class AbstractDomainEvent implements DomainEvent {

	private static final long serialVersionUID = -8179599351528628426L;

	@Getter
	private Class<?> domainClass;

	@Getter
	private AggregateId domainId;

	@Getter
	private AggregateId operatorId;

	@Getter
	private String payload;

	public AbstractDomainEvent(Class<?> domainClass, AggregateId domainId, AggregateId operator, String payload) {
		super();
		this.domainClass = domainClass;
		this.domainId = domainId;
		this.operatorId = operator;
		this.payload = payload;
	}

}
