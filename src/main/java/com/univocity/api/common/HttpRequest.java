/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.api.common;

import java.io.*;
import java.util.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public class HttpRequest implements Cloneable{

	private String url;
	private int timeout = 0;
	private boolean followRedirects = false;
	private boolean validateSSL = true;
	private HttpMethodType httpMethodType = HttpMethodType.GET;
	private LinkedHashMap<String, String> headers = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> cookies = new LinkedHashMap<String, String>();
	private List<Object[]> data = new ArrayList<Object[]>();

	private String proxyUser;
	private String proxyHost;
	private String proxyPassword;
	private int proxyPort = -1;

	public HttpRequest(String url) {
		this.url = url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUserAgent(String userAgent) {
		setHeader("User-Agent", userAgent);
	}

	public void setReferrer(String referrer) {
		setHeader("Referer", referrer);
	}

	public String getUserAgent() {
		return headers.get("User-Agent");
	}

	public String getReferrer() {
		return headers.get("Referer");
	}

	public void setHeader(String header, String value) {
		set(headers, header, value);
	}

	public void setCookie(String cookie, String value) {
		set(cookies, cookie, value);
	}

	private void set(Map<String, String> map, String key, String value) {
		if (value == null) {
			map.remove(key);
		} else {
			map.put(key, value);
		}
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;

	}

	public void setHttpMethodType(HttpMethodType httpMethodType) {
		this.httpMethodType = httpMethodType;
	}

	public String getUrl() {
		return url;
	}

	public int getTimeout() {
		return timeout;
	}

	public boolean getFollowRedirects() {
		return followRedirects;
	}

	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	public boolean getValidateSSL() {
		return validateSSL;
	}

	public void setValidateSSL(boolean validateSSL) {
		this.validateSSL = validateSSL;
	}

	public HttpMethodType getHttpMethodType() {
		return httpMethodType;
	}

	public void setDataParameter(String paramName, String value) {
		this.data.add(new Object[]{paramName, value});
	}

	public void setDataStreamParameter(String paramName, String filename, ResourceProvider<InputStream> dataProvider) {
		this.data.add(new Object[]{paramName, filename, dataProvider});
	}

	public void setFileParameter(String paramName, String filename, FileProvider file) {
		this.data.add(new Object[]{paramName, filename, file});
	}

	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	public Map<String, String> getCookies() {
		return Collections.unmodifiableMap(cookies);
	}

	public List<Object[]> getData() {
		return Collections.unmodifiableList(data);
	}

	public void setProxy(String host, int port) {
		setProxy(host, port, null, null);
	}

	public void setProxy(String host, int port, String user) {
		setProxy(host, port, user, "");
	}

	public void setProxy(String host, int port, String user, String password) {
		Args.positive(port, "Proxy port");
		Args.notBlank(host, "Proxy host");
		this.proxyHost = host;
		this.proxyPort = port;
		this.proxyUser = user;
		this.proxyPassword = password;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public HttpRequest clone() {
		try {
			HttpRequest clone = (HttpRequest) super.clone();
			clone.headers = (LinkedHashMap<String, String>) this.headers.clone();
			clone.cookies = (LinkedHashMap<String, String>) this.cookies.clone();
			clone.data = new ArrayList<Object[]>();
			for (Object[] object : this.data) {
				clone.data.add(object.clone());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Could not clone",e);
		}
	}
}
