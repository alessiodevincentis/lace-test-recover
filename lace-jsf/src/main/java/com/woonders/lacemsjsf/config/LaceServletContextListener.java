package com.woonders.lacemsjsf.config;

import com.woonders.lacemscommon.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.SessionCookieConfig;
import javax.servlet.annotation.WebListener;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Emanuele on 19/02/2017.
 */
@Slf4j
@WebListener
public class LaceServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		setCookieHttps(servletContextEvent);
		cleanWorkingDir();
	}

	private void cleanWorkingDir() {
		Path workingDir = Paths.get(Constants.APP_WORKING_DIR);
		if (!workingDir.toFile().getName().endsWith("LACEtmp")) {
			log.error("La cartella DEVE chiamarsi LACEtmp per evitare danni!!!!!!");
			throw new RuntimeException("La cartella DEVE chiamarsi LACEtmp per evitare danni!!!!!!");
		}
		if (workingDir.toFile().exists()) {
			boolean deleted = FileSystemUtils.deleteRecursively(workingDir.toFile());
			if (!deleted) {
				log.error("Working dir not deleted!!");
				throw new RuntimeException("Working dir not deleted!!");
			}
		}
		boolean created = workingDir.toFile().mkdirs();
		if (!created) {
			log.error("Working dir not created!!");
			throw new RuntimeException("Working dir not created!!");
		}
	}

	private void setCookieHttps(ServletContextEvent servletContextEvent) {
		SessionCookieConfig sessionCookieConfig = servletContextEvent.getServletContext().getSessionCookieConfig();
		sessionCookieConfig.setHttpOnly(true);
		sessionCookieConfig.setSecure(Constants.HTTPS_ENABLED);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}
