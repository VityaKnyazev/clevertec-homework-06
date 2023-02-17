package ru.clevertec.knyazev.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.Address;

@Repository
public class AddressDAOJPA implements AddressDAO {
	@PersistenceContext
	EntityManager entityManager;
	
	public AddressDAOJPA() {
	}

	public AddressDAOJPA(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Address findById(Long id) {
		Address address = null;
		
		if (id != null && id > 0L) {
			address = entityManager.find(Address.class, id);
		} 

		return address;
	}

}
