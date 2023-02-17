package ru.clevertec.knyazev.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Shop;

public class ShopDAOJPATests {
	private EntityManager entityManagerMock;
	private ShopDAOJPA shopDAOJPA;
	
	@BeforeEach
	public void setUp() {
		entityManagerMock = Mockito.mock(EntityManager.class);
		shopDAOJPA = new ShopDAOJPA(entityManagerMock);
	}
	
	
	@Test
	public void whenFindById() {
		final Address address = new Address(1L, "2512665", "Belarus", "Minsk", "Puskin prt", "11");
		final Shop shop = new Shop(1L, "testName shop", address, "+375 26 954 62 35");
		
		Mockito.when(entityManagerMock.find(Mockito.any(), Mockito.longThat(id -> (id != null) && (id > 0L)))).thenReturn(shop);
		
		final Long shopId = 1L;		
		Shop savedShop = shopDAOJPA.findById(shopId);
		
		assertNotNull(savedShop);
	}
	
	@Test
	public void whenFindByNullId() {		
		final Long shopId = null;		
		assertNull(shopDAOJPA.findById(shopId));
	}
	
	@Test
	public void whenFindByBadId() {		
		final Long shopId = 0L;		
		assertNull(shopDAOJPA.findById(shopId));
	}
}
