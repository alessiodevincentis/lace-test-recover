package com.woonders.lacemscommon.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.woonders.lacemscommon.db.tenantrepository.Tenant;
import com.woonders.lacemscommon.service.TenantService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by emanuele on 23/12/16.
 */
@Component
@Getter
@Slf4j
public class TenantManager {

	public static final String NAME = "tenantManager";
	public static final String JSF_NAME = "#{" + NAME + "}";
	private static final String TENANT_LIST_KEY = "tenantList";
	private static final String DEVIS_PROX_ID_MAP_KEY = "devisProxIdMap";
	@Autowired
	private DataSource multiTenantDataSource;
	@Autowired
	private TenantService tenantService;
	private Cache<String, List<String>> tenantCache;
	private Cache<String, Map<String, DevisProxTenantDto>> devisProxCache;

	// http://stackoverflow.com/questions/8686507/how-to-add-a-hook-to-the-application-context-initialization-event
	@EventListener({ ContextRefreshedEvent.class })
	void contextRefreshedEvent() {
		tenantCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
		devisProxCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
	}

	private Callable<Map<String, DevisProxTenantDto>> generateTenantDevisProxMap() {
		return () -> {
			log.info("Refreshing devis prox id map...");
			Map<String, DevisProxTenantDto> tenantDevisProxMap = new HashMap<>();
			try {
				for (final String tenantId : tenantCache.get(TENANT_LIST_KEY, getTenantListFromDb())) {
					Connection connection = null;
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						connection = multiTenantDataSource.getConnection();

						PreparedStatement switchDbStatement = connection.prepareStatement("use " + tenantId);
						switchDbStatement.execute();

						preparedStatement = connection.prepareStatement("select * from azienda");
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							final String currentDevisProxId = resultSet.getString("devisProxId");
							final long aziendaId = resultSet.getLong("id");
							if (!StringUtils.isEmpty(currentDevisProxId)) {
								tenantDevisProxMap.put(currentDevisProxId,
										DevisProxTenantDto.builder().tenantName(tenantId).aziendaId(aziendaId)
												.devisProxTenantId(currentDevisProxId).build());
							}
						}
					} catch (final SQLException e) {
						log.error("Unable to get tenant id", e);
					} finally {
						if (connection != null) {
							try {
								connection.close();
							} catch (final SQLException e) {
								log.error("Unable to close", e);
							}
						}
						if (preparedStatement != null) {
							try {
								preparedStatement.close();
							} catch (final SQLException e) {
								log.error("Unable to close", e);
							}
						}
						if (resultSet != null) {
							try {
								resultSet.close();
							} catch (final SQLException e) {
								log.error("Unable to close", e);
							}
						}
					}
				}
			} catch (ExecutionException e) {
				throw new RuntimeException("ERROR creating devis prox map!");
			}
			return tenantDevisProxMap;
		};
	}

	private Callable<List<String>> getTenantListFromDb() {
		return () -> {
			log.info("Refreshing tenant list...");
			return tenantService.getActiveTenantList().stream().map(Tenant::getName).collect(Collectors.toList());
		};
	}

	public boolean isTenantAvailable(final String tenantName) {
		try {
			return tenantCache.get(TENANT_LIST_KEY, getTenantListFromDb()).contains(tenantName);
		} catch (ExecutionException e) {
			return false;
		}
	}

	public DevisProxTenantDto getTenantDtoFromDevisProxId(final String devisProxId) {
		try {
			Map<String, DevisProxTenantDto> tenantDevisProxMap = devisProxCache.get(DEVIS_PROX_ID_MAP_KEY,
					generateTenantDevisProxMap());
			if (!StringUtils.isEmpty(devisProxId) && tenantDevisProxMap.containsKey(devisProxId)) {
				return tenantDevisProxMap.get(devisProxId);
			}
		} catch (ExecutionException e) {
			throw new RuntimeException("No tenant with the following devisProxId exists: " + devisProxId);
		}
		throw new RuntimeException("No tenant with the following devisProxId exists: " + devisProxId);
	}

}
