package com.xzchaoo.hc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by xzchaoo on 2016/6/6 0006.
 */
public class Resp {
	private static final Charset UTF8 = Charset.forName("utf-8");
	private Charset defaultCharset = UTF8;
	private final CloseableHttpResponse resp;
	private String stringCache;
	private BufferedHttpEntity bhe;
	private HttpUriRequest hur;

	Resp(HttpUriRequest hur, CloseableHttpResponse resp) {
		this(hur, resp, UTF8);
	}

	Resp(HttpUriRequest hur, CloseableHttpResponse resp, Charset defaultCharset) {
		this.hur = hur;
		this.resp = resp;
		this.defaultCharset = defaultCharset;
		HttpEntity he = resp.getEntity();
		if (he != null) {
			if (he instanceof BufferedHttpEntity) {
				bhe = (BufferedHttpEntity) he;
			} else {
				try {
					bhe = new BufferedHttpEntity(he);
					resp.setEntity(bhe);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public JSONObject asJson() {
		return JSON.parseObject(asString(defaultCharset));
	}

	public <T> T asPojo(Class<T> clazz) {
		return JSON.parseObject(asString(defaultCharset), clazz);
	}

	/**
	 * @deprecated typo. Please use asPojo instead. This method will be removed in future
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	@Deprecated
	public <T> T asJso(Class<T> clazz) {
		return asPojo(clazz);
	}

	public CloseableHttpResponse raw() {
		return resp;
	}

	public byte[] asByteArray() {
		if (bhe == null) {
			return null;
		}
		try {
			return EntityUtils.toByteArray(bhe);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转成字符串
	 *
	 * @return
	 */
	public String asString() {
		return asString(defaultCharset);
	}

	/**
	 * 转成字符串
	 *
	 * @return
	 */
	public String asString(Charset charset) {
		if (bhe == null) {
			return null;
		}
		if (stringCache == null) {
			try {
				stringCache = EntityUtils.toString(bhe, charset);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return stringCache;
	}

	public Charset getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(Charset defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	/**
	 * 状态码
	 *
	 * @return
	 */
	public int getStatusCode() {
		return resp.getStatusLine().getStatusCode();
	}

	public String getFirstHeaderValue(String name) {
		Header header = resp.getFirstHeader(name);
		return header == null ? null : header.getValue();
	}

	public HttpUriRequest request() {
		return hur;
	}
}
