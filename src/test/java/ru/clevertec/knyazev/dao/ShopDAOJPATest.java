package ru.clevertec.knyazev.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Shop;

@ExtendWith(MockitoExtension.class)
public class ShopDAOJPATest {
	@Mock
	private EntityManager entityManagerMock;
	@InjectMocks
	private ShopDAOJPA shopDAOJPA;

	@Test
	public void checkFindByIdShouldReturnShop() {
		Address address = Address.builder()
				.id(1L)
				.postalCode("2512665")
				.country("Belarus")
				.city("Minsk")
				.street("Puskin prt")
				.buildingNumber("11")
				.build();
		Shop expectedShop = Shop.builder()
				.id(1L)
				.name("testName shop")
				.address(address)
				.phone("+375 26 954 62 35")
				.build();

		Mockito.when(entityManagerMock.find(Mockito.any(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(expectedShop);

		Long inputShopId = 1L;
		Shop actualShop = shopDAOJPA.findById(inputShopId);
		
		ArgumentCaptor<Long> lArg = ArgumentCaptor.forClass(Long.class);
		Mockito.verify(entityManagerMock).find(Mockito.any(), lArg.capture());
		
		assertAll(() -> {
			assertThat(actualShop).isNotNull();
			assertThat(actualShop.getId()).isGreaterThan(0L);
			assertThat(actualShop.getId()).isEqualTo(lArg.getValue());
		});		
	}

	@Test
	public void checkFindByIdOnNullIdShouldReturnNull() {
		Long inputShopId = null;
		assertThat(shopDAOJPA.findById(inputShopId)).isNull();
	}

	@Test
	public void checkFindByIdOnBadIdShouldReturnNull() {
		Long inputShopId = 0L;
		assertThat(shopDAOJPA.findById(inputShopId)).isNull();
	}
}
