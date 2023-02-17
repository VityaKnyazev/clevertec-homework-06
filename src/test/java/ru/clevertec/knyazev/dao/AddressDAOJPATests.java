package ru.clevertec.knyazev.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.entity.Address;

public class AddressDAOJPATests {
	private EntityManager entityManagerMock;
	private AddressDAOJPA addressDAOJPA;
		
	@BeforeEach
	public void setUp() {
		entityManagerMock = Mockito.mock(EntityManager.class);
		addressDAOJPA = new AddressDAOJPA(entityManagerMock);
	}
	
	@Test
	public void whenFindById() {
		final Address address = new Address(2L, "2512665", "Belarus", "Minsk", "Puskin prt", "11");
		
		Mockito.when(entityManagerMock.find(Mockito.any(), Mockito.longThat(id -> (id != null) && (id > 0L)))).thenReturn(address);
		
		final Long id = 2L;		
		Address savedAddress = addressDAOJPA.findById(id);
		
		assertNotNull(savedAddress);
	}
	
	@Test
	public void whenFindByNullId() {		
		final Long id = null;		
		assertNull(addressDAOJPA.findById(id));
	}
	
	@Test
	public void whenFindByBadId() {		
		final Long id = 0L;		
		assertNull(addressDAOJPA.findById(id));
	}
	
}