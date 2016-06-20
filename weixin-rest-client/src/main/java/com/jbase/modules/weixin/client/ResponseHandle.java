package com.jbase.modules.weixin.client;

import org.apache.http.client.methods.CloseableHttpResponse;

public abstract class ResponseHandle {

	protected abstract void handle(CloseableHttpResponse httpResponse);
}
