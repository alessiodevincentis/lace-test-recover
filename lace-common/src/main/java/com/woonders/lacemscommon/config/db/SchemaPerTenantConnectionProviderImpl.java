package com.woonders.lacemscommon.config.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by emanuele on 23/12/16.
 */
@Slf4j
@Component
public class SchemaPerTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

	@Autowired
	private DataSource multiTenantDataSource;

	@Override
	public Connection getAnyConnection() throws SQLException {
		return multiTenantDataSource.getConnection();
	}

	@Override
	public void releaseAnyConnection(final Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	@Override
	public Connection getConnection(final String tenantIdentifier) throws SQLException {
		final Connection connection = getAnyConnection();
		try {
			connection.createStatement().execute("USE " + tenantIdentifier);
		} catch (final SQLException e) {
			log.error("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
		}
		return connection;
	}

	@Override
	public void releaseConnection(final String tenantIdentifier, final Connection connection) throws SQLException {
		log.debug("Releasing connection from " + tenantIdentifier);
		releaseAnyConnection(connection);
		log.debug("Connection released from " + tenantIdentifier);
	}

	@Override
	public boolean supportsAggressiveRelease() {
		// TODO da vedere
		return true;
	}

	@Override
	public boolean isUnwrappableAs(final Class aClass) {
		// probabilmente mai usato
		return false;
	}

	@Override
	public <T> T unwrap(final Class<T> aClass) {
		// probabilmente mai usato
		return null;
	}
}
