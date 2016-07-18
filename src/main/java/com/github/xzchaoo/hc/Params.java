package com.github.xzchaoo.hc;

import com.alibaba.fastjson.JSONObject;
import com.github.xzchaoo.hc.util.ParamsUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Params {
	private List<NameValuePair> params = new ArrayList<NameValuePair>();

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
			String value = args[i + 1].toString();
			add(name, value);
		}
	}


	public Params add(String name, Object value) {
		params.add(new BasicNameValuePair(name, value.toString()));
		return this;
	}

	public boolean isEmpty() {
		return params.isEmpty();
	}

	private String encoding = "utf-8";

	public Params encoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public UrlEncodedFormEntity toEntity() {
		try {
			return new UrlEncodedFormEntity(params, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public Params paramsTo(RequestBuilder rb) {
		for (NameValuePair nvp : params)
			rb.addParameter(nvp);
		return this;
	}

	public Params datasTo(RequestBuilder rb) {
		rb.setEntity(toEntity());
		return this;
	}

	public Params set(String name, Object value) {
		Iterator<NameValuePair> iter = params.iterator();
		while (iter.hasNext()) {
			NameValuePair nvp = iter.next();
			if (nvp.getName().equals(name))
				iter.remove();
		}
		params.add(new BasicNameValuePair(name, value.toString()));
		return this;
	}

	public Params headersTo(RequestBuilder rb) {
		for (NameValuePair nvp : params) {
			rb.addHeader(nvp.getName(), nvp.getValue());
		}
		return this;
	}

	public List<NameValuePair> getParamList() {
		return params;
	}

	public void jsonTo(RequestBuilder rb) {
		JSONObject jo = new JSONObject();
		for (NameValuePair nvp : params) {
			jo.put(nvp.getName(), nvp.getValue());
		}
		ParamsUtils.jsonTo(rb, jo);
	}
}
