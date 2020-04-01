package com.vluee.ddd.support.infrastructure.domainevent;

import com.vluee.ddd.support.domain.AggregateId;

public interface DomainEvent extends java.io.Serializable {
	public Class<?> getDomainClass();

	public AggregateId getDomainId();
}
