package ru.clevertec.knyazev.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ru.clevertec.knyazev.data.connection.DBConnection;

public class DatabaseConnectionTests {
	private DBConnection dbConnectionMock;
	private Connection connectionMock;
	private PreparedStatement preparedStatementMock;
	private ResultSet resultSetMock;
	
	private DatabaseDataReader databaseDataReader;
	
	@BeforeEach
	public void setUp() throws SQLException {
		dbConnectionMock = Mockito.mock(DBConnection.class);
		connectionMock = Mockito.mock(Connection.class);
		preparedStatementMock = Mockito.mock(PreparedStatement.class);
		resultSetMock = Mockito.mock(ResultSet.class);
		
		databaseDataReader = new DatabaseDataReader(dbConnectionMock);
		
		Mockito.when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
		Mockito.when(connectionMock.prepareStatement(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(preparedStatementMock);
		Mockito.when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		Mockito.when(resultSetMock.last()).thenReturn(true);
		Mockito.doNothing().when(resultSetMock).beforeFirst();
		
	}
	
	@Test
	public void whenReadData() throws SQLException, IOException {
		final int purchasesQuantity = 3;
		final int discountCardsQuantity = 1;
		
		Mockito.when(resultSetMock.getRow()).thenReturn(purchasesQuantity, discountCardsQuantity);
		Mockito.when(resultSetMock.next()).thenReturn(true, true, true, false, true, false);
		Mockito.when(resultSetMock.getLong(Mockito.anyString())).thenReturn(1L, 2L, 3L);
		Mockito.when(resultSetMock.getFloat(Mockito.anyString())).thenReturn(3.256F, 5.258F, 4F);
				
		
		final String actualDiscountCardNumber = "256987f45";	
		Mockito.when(resultSetMock.getString(Mockito.anyString())).thenReturn(actualDiscountCardNumber);
		
		final String[] expectedPurchases = {"1-3.256", "2-5.258", "3-4.0"};
		final String[] expectedDiscountCards = {"card-256987f45"};
		
		Map<String[], String[]> actualPurchaseDiscountMap = databaseDataReader.readData();
		
		assertAll(() -> {
			assertNotNull(actualPurchaseDiscountMap);
			assertTrue(Arrays.equals(actualPurchaseDiscountMap.keySet().iterator().next(), expectedPurchases));
			assertTrue(Arrays.equals(actualPurchaseDiscountMap.values().iterator().next(), expectedDiscountCards));
		});
	}
}
