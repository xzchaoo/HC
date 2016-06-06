package org.xzc.hc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by xzchaoo on 2016/6/6 0006.
 */
public class Resp {
	private final CloseableHttpResponse resp;
	private final HC hc;

	public Resp(HC hc, CloseableHttpResponse resp) {
		this.hc = hc;
		this.resp = resp;
	}

	public JSONObject asJSON() {
		return JSON.parseObject(asString(hc.getDefaultEncoding()));
	}

	public String asString() {
		return asString(hc.getDefaultEncoding());
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
		try {
			return EntityUtils.toString(resp.getEntity(), encoding);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			HttpClientUtils.closeQuietly(resp);
		}
	}
}
