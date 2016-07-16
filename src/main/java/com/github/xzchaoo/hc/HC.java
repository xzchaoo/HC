package com.github.xzchaoo.hc;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 对hc的简单的封装
 *
 * @author xzchaoo
 */
public class HC {
	private static final <T> T safeRun(SafeRunner<T> sr) {
		try {
			return sr.run();
		} catch (Exception e) {
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException(e);
		}
	}

	private CloseableHttpClient chc;
	private String defaultEncoding = "utf-8";

	public HC(CloseableHttpClient chc) {
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
		return new Req(this, RequestBuilder.get(url));
	}

	public Req post(String url) {
		return new Req(this, RequestBuilder.post(url));
	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}
}
