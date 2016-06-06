package org.xzc.hc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.xzc.hc.util.Assert;
import org.xzc.hc.util.ParamsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzchaoo on 2016/6/6 0006.
 */
public class Req implements Cloneable {
	private final HC hc;
	private final RequestBuilder rb;
	private StringBuilder cookieBuilder;
	private List<Object> dataPairList;
	private boolean json = false;

	public Req(HC hc, RequestBuilder rb) {
		this.hc = hc;
		this.rb = rb;
	}

	public Req headers(Object... headers) {
		if (headers != null)
			ParamsUtils.addHeaders(rb, headers);
		return this;
	}


	public Req cookies(Object... cookies) {
		if (cookies != null) {
			if (this.cookieBuilder == null)
				this.cookieBuilder = new StringBuilder();
			Assert.assertTrue(cookies.length % 2 == 0, "cookies个数必须是偶数.");
			for (int i = 0; i < cookies.length; i += 2) {
				String name = cookies[i].toString();
				String value = cookies[i + 1].toString();
				cookieBuilder.append(name);
				cookieBuilder.append('=');
				cookieBuilder.append(value);
				cookieBuilder.append(';');
			}
		}
		return this;
	}

	public Req params(Object... params) {
		if (params != null)
			ParamsUtils.addParams(rb, params);
		return this;
	}

	public Req data(Object... dataPairList) {
		if (dataPairList != null) {
			if (this.dataPairList == null)
				this.dataPairList = new ArrayList<Object>(dataPairList.length);
			for (Object o : dataPairList) this.dataPairList.add(o);
		}
		return this;
	}

	public String asString() {
		return hc.asString(build());
	}

	public String asString(String encoding) {
		return hc.asString(build(), encoding);
	}

	public JSONObject asJSON() {
		return JSON.parseObject(asString());
	}

	public byte[] asByteArray() {
		return hc.asByteArray(build());
	}

	public Resp asRes() {
		return hc.asResp(build());
	}

	//以json的格式请求
	public Req json() {
		this.json = true;
		return this;
	}

	public HttpUriRequest build() {
		if (cookieBuilder != null) {
			rb.addHeader("Cookie", cookieBuilder.toString());
			cookieBuilder = null;
		}
		if (dataPairList != null) {
			if (json) ParamsUtils.jsonTo(rb, dataPairList);
			else ParamsUtils.formTo(rb, dataPairList);
		}
		return rb.build();
	}

	public Req clone() {
		try {
			return (Req) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public void consume() {
		hc.consume(build());
	}

	public Req cookiesString(String cookiesString) {
		rb.addHeader("Cookie", cookiesString);
		return this;
	}
}
