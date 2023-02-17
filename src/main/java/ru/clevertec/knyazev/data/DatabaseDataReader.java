package ru.clevertec.knyazev.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ru.clevertec.knyazev.data.connection.DBConnection;

public class DatabaseDataReader implements DataReader {
	private DBConnection dbConnection;

	public DatabaseDataReader(DBConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	@Override
	public Map<String[], String[]> readData() throws IOException {
		String[] purchasesData;
		String[] discuountCarsdData;
		
		try {
			purchasesData = readPurchaseTable();
			discuountCarsdData = readDiscountCardTable();
		} catch (SQLException e) {
			throw new IOException("Error on reading data from databse :" + System.lineSeparator() + e.getMessage(), e);
		}
		
		return new HashMap<>() {
			private static final long serialVersionUID = 473358623541522L;
			{
				put(purchasesData, discuountCarsdData);
			}
		};
		
	}

	private String[] readPurchaseTable() throws SQLException {
		final String tableName = "product";
		final String idRowName = "id";
		final String quantityRowName = "quantity";
		final String query = "SELECT " + idRowName + ", " +  quantityRowName  + " FROM " + tableName;
		
		String[] purchases;

		try (Connection connection = dbConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = statement.executeQuery()) {
			
			int size = 0;

			if (rs != null) {
				rs.last();
				size = rs.getRow();
			}
			
			rs.beforeFirst();

			purchases = new String[size];

			int i = 0;
			while (rs.next()) {				
				Long id = rs.getLong(idRowName);
				Float quantity = rs.getFloat(quantityRowName);
				purchases[i] = id + "-" + quantity;
				i++;
			}
		}

		return purchases;
	}
	
	private String[] readDiscountCardTable() throws SQLException {
		final String tableName = "discount_card";
		final String idRowName = "id";
		final String cardNumberRowName = "number";
		final String query = "SELECT " + idRowName + ", " +  cardNumberRowName  + " FROM " + tableName;
		
		String[] discountCards;
		
		try (Connection connection = dbConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = statement.executeQuery()) {
			
			int size = 0;

			if (rs != null) {
				rs.last();
				size = rs.getRow();
			}
			
			rs.beforeFirst();

			discountCards = new String[size];

			int i = 0;
			while (rs.next()) {
				String cardNumber = rs.getString(cardNumberRowName);
				discountCards[i] = CARD_PREFIX + cardNumber;
				i++;
			}
		}
		
		return discountCards;		
	}
	
	

}
