package com.woonders.lacemscommon.config.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import com.woonders.lacemscommon.AsyncTenantStorage;
import com.woonders.lacemscommon.util.RequestUtil;

/**
 * Created by Emanuele on 30/03/2017. http://www.baeldung.com/spring-events
 * https://spring.io/guides/gs/async-method/
 * http://howtodoinjava.com/spring/spring-core/how-to-publish-and-listen-application-events-in-spring/
 * http://zoltanaltfatter.com/2016/05/11/application-events-with-spring/
 * https://www.keyup.eu/en/blog/101-synchronous-and-asynchronous-spring-events-in-one-application
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	public static final String SINGLE_TASK_EXECUTOR = "singleTaskExecutor";
	@Autowired
	private RequestUtil requestUtil;
	@Autowired
	private AsyncTenantStorage asyncTenantStorage;

	/**
	 * Used by asynchronous event listener. Marcato @Primary perche cosi viene
	 * usato questo da Spring per i metodi @Async.
	 */
	@Primary
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(20);
		executor.initialize();
		return executor;
	}

	/**
	 * Usato nel codice dove vogliamo usare il multithread
	 */
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(20);
		executor.initialize();
		return executor;
	}

	/**
	 * Usato solo per gli import per fare in modo che si incodino uno dopo l
	 * altro. MAX POOL = 1, NON CAMBIARE!!!
	 *
	 * In realta anche aumentando la PoolSize Spring li esegue uno dopo l
	 * altro....vorrei capire perche
	 */
	@Bean
	public Executor singleTaskExecutor() {
		ThreadPoolTaskExecutor executor = new TenantAwareThreadPoolExecutor();
		executor.setMaxPoolSize(1);
		executor.initialize();
		return executor;
	}

	/**
	 * Questo thread pool crea callable/runnable che si ricordano il tenant
	 * prelevato dalla request che li ha creati. In questo modo, prima di
	 * eseguirli (DA ESEGUIRE SEMPRE SINGOLARMENTE, METTERE POOLSIZE = 1),
	 * possiamo settare il tenant sul quale far eseguire la transazione
	 *
	 * In realta anche aumentando la PoolSize Spring li esegue uno dopo l
	 * altro....vorrei capire perche
	 */
	private class TenantAwareThreadPoolExecutor extends ThreadPoolTaskExecutor {

		@Override
		public <T> Future<T> submit(Callable<T> task) {
			return super.submit(new TenantAwareCallable<>(task, requestUtil.getTenantName()));
		}

		@Override
		public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
			return super.submitListenable(new TenantAwareCallable<>(task, requestUtil.getTenantName()));
		}

		@Override
		public Future<?> submit(Runnable task) {
			return super.submit(new TenantAwareRunnable(task, requestUtil.getTenantName()));
		}

		@Override
		public ListenableFuture<?> submitListenable(Runnable task) {
			return super.submitListenable(new TenantAwareRunnable(task, requestUtil.getTenantName()));
		}
	}

	/**
	 * Prima di eseguire il call del callable imposta il tenant su cui eseguire
	 * eventuali transazioni, dopodiche lo resetta
	 */
	private class TenantAwareCallable<T> implements Callable<T> {
		private Callable<T> callable;
		private String tenantName;

		private TenantAwareCallable(Callable<T> callable, String tenantName) {
			this.callable = callable;
			this.tenantName = tenantName;
		}

		@Override
		public T call() throws Exception {
			asyncTenantStorage.setTenantToUseName(tenantName);
			try {
				return callable.call();
			} finally {
				asyncTenantStorage.setTenantToUseName(null);
			}
		}
	}

	/**
	 * Prima di eseguire il run del runnable imposta il tenant su cui eseguire
	 * eventuali transazioni, dopodiche lo resetta
	 */
	private class TenantAwareRunnable implements Runnable {
		private Runnable runnable;
		private String tenantName;

		public TenantAwareRunnable(Runnable runnable, String tenantName) {
			this.runnable = runnable;
			this.tenantName = tenantName;
		}

		@Override
		public void run() {
			asyncTenantStorage.setTenantToUseName(tenantName);
			try {
				runnable.run();
			} finally {
				asyncTenantStorage.setTenantToUseName(null);
			}
		}
	}

}
