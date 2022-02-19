package com.woonders.lacemscommon.network;

import java.util.Map;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;

/**
 * Created by Emanuele on 21/02/2017.
 */
public abstract class BaseClient {

	private final String ACCEPT_HEADER = "accept";
	private final String APPLICATION_JSON_HEADER = "application/json";
	private final String CONTENT_TYPE_HEADER = "Content-Type";

	private void addJsonContentAccept(HttpRequest httpRequest) {
		httpRequest.header(ACCEPT_HEADER, APPLICATION_JSON_HEADER).header(CONTENT_TYPE_HEADER, APPLICATION_JSON_HEADER);
	}

	/**
	 * parameterMap usata per passare parametri come query string. Se non si
	 * devono passare, usare null. L'avevo fatto per fatture in cloud ma in
	 * realta non serve poiche bisogna mettere le loro apiKey direttamente nel
	 * body. Comunque lo lascio, potrebbe essere utile in futuro
	 * 
	 * @param url
	 * @param body
	 * @param parameterMap
	 * @return
	 */
	protected RequestBodyEntity createPostRequest(String url, Object body, Map<String, Object> parameterMap) {
		HttpRequestWithBody httpRequestWithBody = Unirest.post(url);
		addAuthorization(httpRequestWithBody);
		addJsonContentAccept(httpRequestWithBody);
		return httpRequestWithBody.queryString(parameterMap).body(body);
	}

	protected RequestBodyEntity createPostRequest(String url, Object body) {
		return createPostRequest(url, body, null);
	}

	protected HttpRequest createGetRequest(String url, Map<String, Object> parameter) {
		GetRequest getRequest = Unirest.get(url);
		addAuthorization(getRequest);
		addJsonContentAccept(getRequest);
		return getRequest.queryString(parameter);
	}

	protected abstract void addAuthorization(HttpRequest httpRequest);
}
