package com.xzchaoo.hc;

import com.xzchaoo.hc.util.Assert;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * 对hc的简单的封装
 *
 * @author xzchaoo
 */
public class HC implements Closeable {
	public static final HttpEntity EMPTY_ENTITY = CommonEntity.EMPTY_URL_ENCODED_FORM_ENTITY;
	private Charset defaultCharset = CharsetUtils.UTF8;
	private CloseableHttpClient chc;

	public HC(CloseableHttpClient chc) {
		this(chc, CharsetUtils.UTF8);
	}

	public HC(CloseableHttpClient chc, Charset defaultCharset) {
		Assert.notNull(chc, "CloseableHttpClient can not be null");
		this.chc = chc;
		this.defaultCharset = defaultCharset;
	}

	public RequestConfig getRequestConfig() {
		return chc instanceof Configurable ? ((Configurable) chc).getConfig() : null;
	}

	public Charset getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(Charset defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	public Resp execute(HttpUriRequest hur) {
		CloseableHttpResponse chr = null;
		try {
			chr = chc.execute(hur);
			return new Resp(hur, chr);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			HttpClientUtils.closeQuietly(chr);
		}
	}

	public void consume(HttpUriRequest hur) {
		CloseableHttpResponse chr = null;
		try {
			chr = chc.execute(hur);
			EntityUtils.consume(chr.getEntity());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			HttpClientUtils.closeQuietly(chr);
		}
	}

	/**
	 * 关闭
	 */
	public void close() {
		HttpClientUtils.closeQuietly(chc);
	}

	public CloseableHttpClient getCHC() {
		return chc;
	}

	public ReqBuilder get() {
		return new ReqBuilder(this, RequestBuilder.get());
	}

	public ReqBuilder get(String url) {
		return new ReqBuilder(this, RequestBuilder.get(url));
	}

	public ReqBuilder get(URI uri) {
		return new ReqBuilder(this, RequestBuilder.get(uri));
	}

	public ReqBuilder post() {
		return new ReqBuilder(this, RequestBuilder.post()).entity(EMPTY_ENTITY);
	}

	public ReqBuilder post(String url) {
		return new ReqBuilder(this, RequestBuilder.post(url)).entity(EMPTY_ENTITY);
	}

	public ReqBuilder post(URI url) {
		return new ReqBuilder(this, RequestBuilder.post(url)).entity(EMPTY_ENTITY);
	}

	public ReqBuilder put() {
		return new ReqBuilder(this, RequestBuilder.put());
	}

	public ReqBuilder put(String url) {
		return new ReqBuilder(this, RequestBuilder.put(url));
	}

	public ReqBuilder put(URI url) {
		return new ReqBuilder(this, RequestBuilder.put(url));
	}

	public ReqBuilder delete() {
		return new ReqBuilder(this, RequestBuilder.delete());
	}

	public ReqBuilder delete(URI url) {
		return new ReqBuilder(this, RequestBuilder.delete(url));
	}

	public ReqBuilder delete(String url) {
		return new ReqBuilder(this, RequestBuilder.delete(url));
	}

	public ReqBuilder head() {
		return new ReqBuilder(this, RequestBuilder.head());
	}

	public ReqBuilder head(String url) {
		return new ReqBuilder(this, RequestBuilder.head(url));
	}

	public ReqBuilder head(URI url) {
		return new ReqBuilder(this, RequestBuilder.head(url));
	}

	public ReqBuilder options() {
		return new ReqBuilder(this, RequestBuilder.options());
	}

	public ReqBuilder options(String url) {
		return new ReqBuilder(this, RequestBuilder.options(url));
	}

	public ReqBuilder options(URI url) {
		return new ReqBuilder(this, RequestBuilder.options(url));
	}

	public ReqBuilder patch() {
		return new ReqBuilder(this, RequestBuilder.patch());
	}

	public ReqBuilder patch(String url) {
		return new ReqBuilder(this, RequestBuilder.patch(url));
	}

	public ReqBuilder patch(URI url) {
		return new ReqBuilder(this, RequestBuilder.patch(url));
	}

	public ReqBuilder trace() {
		return new ReqBuilder(this, RequestBuilder.trace());
	}

	public ReqBuilder trace(String url) {
		return new ReqBuilder(this, RequestBuilder.trace(url));
	}

	public ReqBuilder trace(URI url) {
		return new ReqBuilder(this, RequestBuilder.trace(url));
	}

}
