package com.github.xzchaoo.hc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xzchaoo on 2016/6/6 0006.
 */
public class Resp {
	private final CloseableHttpResponse resp;
	private final HC hc;
	private String stringCache;

	public Resp(HC hc, CloseableHttpResponse resp) {
		this.hc = hc;
		this.resp = resp;
	}

	public JSONObject asJSON() {
		return JSON.parseObject(asString(hc.getDefaultEncoding()));
	}

	public <T> T asPojo(Class<T> clazz) {
		return JSON.parseObject(asString(), clazz);
	}

	public String asString() {
		return asString(hc.getDefaultEncoding());
	}

	public CloseableHttpResponse raw() {
		return resp;
	}

	public void close() {
		HttpClientUtils.closeQuietly(resp);
	}

	public void consume() {
		try {
			EntityUtils.consume(resp.getEntity());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			HttpClientUtils.closeQuietly(resp);
		}
	}

	public byte[] asByteArray() {
		try {
			return EntityUtils.toByteArray(resp.getEntity());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			HttpClientUtils.closeQuietly(resp);
		}
	}

	public String asString(String encoding) {
		if (stringCache != null) return stringCache;
		try {
			return (stringCache = EntityUtils.toString(resp.getEntity(), encoding));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			HttpClientUtils.closeQuietly(resp);
		}
	}

	public String getFirstHeader(String name) {
		Header h = resp.getFirstHeader(name);
		return h == null ? null : h.getValue();
	}

	public static String toCookieString(Header h) {
		String v = h.getValue();
		return v.substring(0, v.indexOf(';') + 1);
	}

	private static final Pattern COOKIE_PATTERN = Pattern.compile("(\\w+)=(\\w+);");

	public String getCookie(String name) {
		Header[] hs = resp.getHeaders("Set-Cookie");
		for (Header h : hs) {
			String cs = h.getValue();
			Matcher m = COOKIE_PATTERN.matcher(cs);
			if (m.find()) {
				String key = m.group(1);
				String value = m.group(2);
				if (key.equals(name)) {
					return value;
				}
			}
		}
		return null;
	}

	public String getCookieString() {
		Header[] hs = resp.getHeaders("Set-Cookie");
		StringBuilder sb = new StringBuilder();
		for (Header h : hs) {
			sb.append(toCookieString(h));
		}
		return sb.toString();
	}

	public int getStatusCode() {
		return resp.getStatusLine().getStatusCode();
	}
}
