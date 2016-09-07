package com.github.xzchaoo.hc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xzchaoo.hc.util.Assert;

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

import static javafx.scene.input.KeyCode.T;

/**
 * 对hc的简单的封装
 *
 * @author xzchaoo
 */
public class HC implements Closeable {
	protected static <T> T safeRun(SafeRunner<T> sr) {
		try {
			return sr.run();
		} catch (Exception e) {
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException(e);
		}
	}

	private static final String DEFAULT_ENCODING = "utf-8";
	private final CloseableHttpClient chc;
	private String defaultEncoding = DEFAULT_ENCODING;

	public HC(CloseableHttpClient chc) {
		Assert.notNull(chc, "CloseableHttpClient can not be null");
		this.chc = chc;
	}

	public RequestConfig getRequestConfig() {
		return chc instanceof Configurable ? ((Configurable) chc).getConfig() : null;
	}

	public byte[] asByteArray(final HttpUriRequest req) {
		return safeRun(new SafeRunner<byte[]>() {
			public byte[] run() throws Exception {
				CloseableHttpResponse res = null;
				try {
					res = chc.execute(req);
					return EntityUtils.toByteArray(res.getEntity());
				} finally {
					HttpClientUtils.closeQuietly(res);
				}
			}
		});
	}
	public Resp asResp(final HttpUriRequest req) {
		return safeRun(new SafeRunner<Resp>() {
			public Resp run() throws Exception {
				return new Resp(HC.this, chc.execute(req));
			}
		});
	}

	public String asString(HttpUriRequest req) {
		return asString(req, defaultEncoding);
	}

	public CloseableHttpResponse execute(HttpUriRequest hur) throws IOException {
		return chc.execute(hur);
	}

	public JSONObject asJSON(HttpUriRequest hur) {
		return asJSON(hur, defaultEncoding);
	}

	public JSONObject asJSON(HttpUriRequest hur, String encoding) {
		return JSON.parseObject(asString(hur, encoding));
	}

	public String asString(final HttpUriRequest req, final String encoding) {
		return safeRun(new SafeRunner<String>() {
			public String run() throws Exception {
				CloseableHttpResponse res = null;
				try {
					res = chc.execute(req);
					return EntityUtils.toString(res.getEntity(), encoding);
				} finally {
					HttpClientUtils.closeQuietly(res);
				}
			}
		});
	}

	/**
	 * 关闭
	 */
	public void close() {
		HttpClientUtils.closeQuietly(chc);
	}

	/**
	 * 消耗掉这个请求, 不需要返回值
	 *
	 * @param req
	 */
	public void consume(final HttpUriRequest req) {
		safeRun(new SafeRunner<Void>() {
			public Void run() throws Exception {
				CloseableHttpResponse res = null;
				try {
					res = chc.execute(req);
					EntityUtils.consumeQuietly(res.getEntity());
				} finally {
					HttpClientUtils.closeQuietly(res);
				}
				return null;
			}
		});
	}

	public CloseableHttpClient getCHC() {
		return chc;
	}

	public Req get(String url) {
		return new Req(this, RequestBuilder.get(url), defaultEncoding);
	}

	public Req post(String url) {
		return new Req(this, RequestBuilder.post(url), defaultEncoding);
	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String encoding) {
		Assert.notNull(encoding, "encoding can not be null");
		this.defaultEncoding = encoding;
	}
}
