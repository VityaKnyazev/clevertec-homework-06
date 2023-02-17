package ru.clevertec.knyazev.data.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnectionImpl implements DBConnection {
	private final String DB_PROPERTIES = "/applicationDataInput.properties";
	private DataSource dataSource;
	
	private static DBConnection dbConnection;
	
	private DBConnectionImpl() {
		HikariConfig hikariConfig = new HikariConfig(DB_PROPERTIES);
		dataSource = new HikariDataSource(hikariConfig);
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		Connection connection = dataSource.getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		return connection;
	}
	
	public static DBConnection getDBConnectionInstance() {
		if (dbConnection == null) {
			synchronized (DBConnection.class) {
				if (dbConnection == null) {
					dbConnection = new DBConnectionImpl();
				}
			}			
		}
				
		return dbConnection;
	}
}
