package ru.clevertec.knyazev.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.knyazev.data.connection.DBConnection;

@ExtendWith(MockitoExtension.class)
public class DatabaseConnectionTest {
	@Mock
	private DBConnection dbConnectionMock;
	@Mock
	private Connection connectionMock;
	@Mock
	private PreparedStatement preparedStatementMock;
	@Mock
	private ResultSet resultSetMock;
	@InjectMocks
	private DatabaseDataReader databaseDataReader;

	@Test
	public void whenReadData() throws SQLException, IOException {
		int purchasesQuantity = 3;
		int discountCardsQuantity = 1;
		
		Mockito.when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
		Mockito.when(connectionMock.prepareStatement(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(preparedStatementMock);
		Mockito.when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		Mockito.when(resultSetMock.last()).thenReturn(true);
		Mockito.doNothing().when(resultSetMock).beforeFirst();
		Mockito.when(resultSetMock.getRow()).thenReturn(purchasesQuantity, discountCardsQuantity);
		Mockito.when(resultSetMock.next()).thenReturn(true, true, true, false, true, false);
		Mockito.when(resultSetMock.getLong(Mockito.anyString())).thenReturn(1L, 2L, 3L);
		Mockito.when(resultSetMock.getFloat(Mockito.anyString())).thenReturn(3.256F, 5.258F, 4F);
				
		
		String dbDiscountCardNumber = "256987f45";	
		Mockito.when(resultSetMock.getString(Mockito.anyString())).thenReturn(dbDiscountCardNumber);
		
		String[] expectedPurchases = {"1-3.256", "2-5.258", "3-4.0"};
		String[] expectedDiscountCards = {"card-256987f45"};
		
		Map<String[], String[]> actualPurchaseDiscountMap = databaseDataReader.readData();
		
		assertAll(() -> {
			assertThat(actualPurchaseDiscountMap).isNotNull();
			assertThat(Arrays.equals(actualPurchaseDiscountMap.keySet().iterator().next(), expectedPurchases)).isTrue();
			assertThat(Arrays.equals(actualPurchaseDiscountMap.values().iterator().next(), expectedDiscountCards)).isTrue();
		});
	}
}
