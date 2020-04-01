package com.vluee.ddd.support.infrastructure.domainevent;

import javax.annotation.PostConstruct;

import com.vluee.ddd.support.infrastructure.repository.jpa.GenericJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DomainEventLogRepository extends GenericJpaRepository<DomainEventLog> {

	@PostConstruct
	public void info() {
		log.debug("----- Entity Manager ----- {}, ", entityManager);
	}
}
