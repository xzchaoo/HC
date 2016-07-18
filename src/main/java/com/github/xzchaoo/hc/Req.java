package com.github.xzchaoo.hc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xzchaoo.hc.util.Assert;
import com.github.xzchaoo.hc.util.ParamsUtils;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xzchaoo on 2016/6/6 0006.
 */
public class Req implements Cloneable {
	private static final Logger log = LoggerFactory.getLogger(Req.class);
	private final HC hc;
	private final RequestBuilder rb;
	private boolean json = false;
	private static final String DEFAULT_ENCODING = "utf-8";
	private String encoding = DEFAULT_ENCODING;
	private String cookieString;

	public Req(HC hc, RequestBuilder rb) {
		this(hc, rb, DEFAULT_ENCODING);
	}

	public Req(HC hc, RequestBuilder rb, String encoding) {
		this.hc = hc;
		this.rb = rb;
		this.encoding = encoding;
	}

	private MultiValuedMap<String, String> headers;
	private MultiValuedMap<String, String> params;
	private MultiValuedMap<String, String> datas;
	private MultiValuedMap<String, String> cookies;


	private MultiValuedMap<String, String> ensureHeaders() {
		if (this.headers == null) {
			this.headers = new ArrayListValuedHashMap<String, String>();
		}
		return headers;
	}

	private MultiValuedMap<String, String> ensureCookies() {
		if (this.cookies == null) {
			this.cookies = new ArrayListValuedHashMap<String, String>();
		}
		return cookies;
	}

	private MultiValuedMap<String, String> ensureParams() {
		if (this.params == null) {
			this.params = new ArrayListValuedHashMap<String, String>();
		}
		return params;
	}

	private MultiValuedMap<String, String> ensureDatas() {
		if (this.datas == null) {
			this.datas = new ArrayListValuedHashMap<String, String>();
		}
		return datas;
	}

	private Req putAll(Map<?, ?> map, MultiValuedMap<String, String> mmv) {
		for (Map.Entry<?, ?> e : map.entrySet()) {
			if (e.getValue() != null) {
				mmv.put(e.getKey().toString(), e.getValue().toString());
			}
		}
		return this;
	}

	private Req putAll(Object[] list, MultiValuedMap<String, String> mmv) {
		for (int i = 0; i < list.length; i += 2) {
			mmv.put(list[i].toString(), list[i + 1].toString());
		}
		return this;
	}

	public Req headers(Object... headers) {
		if (headers != null) {
			ensureHeaders();
			putAll(headers, this.headers);
		}
		return this;
	}

	public Req headers(Object bean) {
		return headers(ParamsUtils.describe(bean));
	}

	public Req headers(Map<?, ?> map) {
		ensureHeaders();
		return putAll(map, headers);
	}


	public Req cookies(Object... cookies) {
		if (cookies != null) {
			ensureCookies();
			putAll(cookies, this.cookies);
		}
		return this;
	}

	public Req cookies(Object bean) {
		return cookies(ParamsUtils.describe(bean));
	}

	public Req cookies(Map<?, ?> cookies) {
		ensureCookies();
		return putAll(cookies, this.cookies);
	}

	public Req params(Object... params) {
		if (params != null) {
			ensureParams();
			putAll(params, this.params);
		}
		return this;
	}

	public Req params(Object bean) {
		return params(ParamsUtils.describe(bean));
	}

	public Req params(Map<?, ?> params) {
		ensureParams();
		return putAll(params, this.params);
	}

	public Req datas(Object... datas) {
		if (datas != null) {
			ensureDatas();
			putAll(datas, this.datas);
		}
		return this;
	}

	public Req datas(Object bean) {
		return datas(ParamsUtils.describe(bean));
	}

	public Req datas(Map<?, ?> datas) {
		ensureDatas();
		return putAll(datas, this.datas);
	}

	public String asString() {
		return hc.asString(build());
	}

	public <T> T asPojo(Class<T> clazz) {
		return JSON.parseObject(asString(), clazz);
	}

	@Deprecated
	public String asString(String encoding) {
		return hc.asString(build(), encoding);
	}

	public JSONObject asJSON() {
		return JSON.parseObject(asString());
	}

	public byte[] asByteArray() {
		return hc.asByteArray(build());
	}

	public Resp asResp() {
		return hc.asResp(build());
	}

	public Req entity(HttpEntity entity) {
		rb.setEntity(entity);
		return this;
	}

	//以json的格式请求
	public Req json() {
		this.json = true;
		return this;
	}

	public Req config(RequestConfig rc) {
		rb.setConfig(rc);
		return this;
	}

	public HttpUriRequest build() {
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entries()) {
				rb.addHeader(e.getKey(), e.getValue());
			}
		}
		if (cookieString != null) {//优先级最高
			rb.addHeader("Cookie", cookieString);
		} else if (cookies != null) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> e : cookies.entries()) {
				sb.append(e.getKey());
				sb.append('=');
				sb.append(e.getValue());
				sb.append(';');
			}
			String cookie = sb.toString();
			//这里无条件直接追加
			rb.addHeader("Cookie", cookie);
		}

		if (params != null) {
			for (Map.Entry<String, String> e : params.entries()) {
				rb.addParameter(e.getKey(), e.getValue());
			}
		}
		if (datas != null) {
			if (json) {
				Map<String, String> map = new HashMap<String, String>();
				for (Map.Entry<String, String> e : datas.entries()) {
					map.put(e.getKey(), e.getValue());
				}
				String jsonString = JSON.toJSONString(map);

				HttpEntity e = EntityBuilder.create()
					.setContentEncoding(encoding)
					.setContentType(ContentType.APPLICATION_JSON)
					.setText(jsonString)
					.build();
				rb.setEntity(e);

			} else {
				List<NameValuePair> list = new ArrayList<NameValuePair>(datas.size());
				for (Map.Entry<String, String> e : datas.entries()) {
					list.add(new BasicNameValuePair(e.getKey(), e.getValue()));
				}
				UrlEncodedFormEntity e = null;
				try {
					e = new UrlEncodedFormEntity(list, encoding);
				} catch (UnsupportedEncodingException ex) {
					log.warn("UnsupportedEncodingException", ex);
				}
				rb.setEntity(e);
			}
		}
		return rb.build();
	}

	public void consume() {
		hc.consume(build());
	}

	@Deprecated
	public Req cookiesString(String cookiesString) {
		this.cookieString = cookiesString;
		return this;
	}

	public Req cookieString(String cookiesString) {
		this.cookieString = cookiesString;
		return this;
	}

	public Req encoding(String encoding) {
		Assert.notNull(encoding, "encoding can not be null");
		this.encoding = encoding;
		return this;
	}
}
