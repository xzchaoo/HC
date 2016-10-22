package com.github.xzchaoo.hc;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Params {
	private MultiValuedMap<String, String> map = new ArrayListValuedHashMap<String, String>();
	private String encoding = "utf-8";


	public Params() {
	}

	public Params(String name, Object value) {
		add(name, value);
	}

	public Params(Object... args) {
		if (args.length % 2 != 0)
			throw new IllegalArgumentException("参数个数必须是偶数!");
		for (int i = 0; i < args.length; i += 2) {
			String name = args[i].toString();
			Object value = args[i + 1];
			add(name, value);
		}
	}


	public Params add(String name, Object value) {
		map.put(name, value.toString());
		return this;
	}

	public Params set(String name, Object value) {
		map.remove(name);
		map.put(name, value.toString());
		return this;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean contains(String name) {
		return map.containsKey(name);
	}

	public Params encoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public UrlEncodedFormEntity toEntity() {
		try {
			return new UrlEncodedFormEntity(getParamList(), encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public Params to(MultiValuedMap<String, String> mvm) {
		for (Map.Entry<String, String> e : map.entries())
			map.put(e.getKey(), e.getValue());
		return this;
	}

	public Params paramsTo(RequestBuilder rb) {
		for (Map.Entry<String, String> e : map.entries())
			rb.addParameter(e.getKey(), e.getValue());
		return this;
	}

	public Params headersTo(RequestBuilder rb) {
		for (Map.Entry<String, String> e : map.entries())
			rb.addHeader(e.getKey(), e.getValue());
		return this;
	}

	public Params datasTo(RequestBuilder rb) {
		rb.setEntity(toEntity());
		return this;
	}

	public Params paramsTo(Req req) {
		return to(req.getParams());
	}

	public Params headersTo(Req req) {
		return to(req.getHeaders());
	}

	public Params datasTo(Req req) {
		return to(req.getDatas());
	}

	public Params cookiesTo(Req req) {
		return to(req.getCookies());
	}

	@Deprecated
	public List<NameValuePair> getParamList() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> e : map.entries()) {
			list.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}
		return list;
	}

	public Map<String, String> asMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (Map.Entry<String, String> e : this.map.entries()) {
			map.put(e.getKey(), e.getValue());
		}
		return map;
	}
}