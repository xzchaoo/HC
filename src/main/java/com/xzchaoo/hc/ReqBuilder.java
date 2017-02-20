package com.xzchaoo.hc;

import com.alibaba.fastjson.JSONObject;
import com.xzchaoo.hc.util.JsonEntity;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public class ReqBuilder {
	private final HC hc;
	private RequestBuilder rb;

	ReqBuilder(HC hc, RequestBuilder rb) {
		this.hc = hc;
		this.rb = rb;
	}

	public ReqBuilder params(Object... args) {
		if (args == null || args.length == 0) {
			return this;
		}
		if (args.length % 2 == 1) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < args.length; i += 2) {
			rb.addParameter((String) args[i], args[i + 1].toString());
		}
		return this;
	}

	public ReqBuilder params(Params params) {
		for (NameValuePair nvp : params.getList()) {
			rb.addParameter(nvp);
		}
		return this;
	}

	public ReqBuilder headers(Object... args) {
		if (args == null || args.length == 0) {
			return this;
		}
		if (args.length % 2 == 1) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < args.length; i += 2) {
			rb.addHeader((String) args[i], args[i + 1].toString());
		}
		return this;
	}

	public ReqBuilder headers(Params params) {
		for (NameValuePair nvp : params.getList()) {
			rb.addHeader(nvp.getName(), nvp.getValue());
		}
		return this;
	}

	@Deprecated
	public ReqBuilder datas(Object... args) {
		return form(args);
	}

	public ReqBuilder form(Object... args) {
		if (args == null || args.length == 0) {
			return this;
		}
		if (args.length % 2 == 1) {
			throw new IllegalArgumentException();
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(args.length / 2);
		for (int i = 0; i < args.length; i += 2) {
			nvps.add(new BasicNameValuePair((String) args[i], args[i + 1].toString()));
		}
		return form(nvps);
	}

	public ReqBuilder form(Params params) {
		return form(params.getList(), hc.getDefaultCharset());
	}

	public ReqBuilder form(Params params, Charset charset) {
		return form(params.getList(), charset);
	}

	public ReqBuilder form(List<NameValuePair> list) {
		return form(list, hc.getDefaultCharset());
	}

	public ReqBuilder form(List<NameValuePair> list, Charset charset) {
		UrlEncodedFormEntity e = new UrlEncodedFormEntity(list, charset);
		rb.setEntity(e);
		return this;
	}

	public ReqBuilder config(RequestConfig rc) {
		rb.setConfig(rc);
		return this;
	}

	public ReqBuilder entity(HttpEntity entity) {
		rb.setEntity(entity);
		return this;
	}

	public ReqBuilder json(Object target) {
		return entity(new JsonEntity(target));
	}

	public ReqBuilder cookie(String cookie) {
		rb.addHeader("Cookie", cookie);
		return this;
	}

	@Deprecated
	public ReqBuilder cookiesString(String cookie) {
		rb.addHeader("Cookie", cookie);
		return this;
	}

	@Deprecated
	public ReqBuilder cookieString(String cookie) {
		rb.addHeader("Cookie", cookie);
		return this;
	}

	public RequestBuilder raw() {
		return rb;
	}

	public HttpUriRequest build() {
		return rb.build();
	}

	public Resp execute() {
		return hc.execute(build());
	}

	public Resp execute(HttpContext ctx) {
		return hc.execute(build(), ctx);
	}

	public void consume() {
		hc.consume(build());
	}

	public ReqBuilder visit(ReqVisitor rv) {
		if (rv != null) {
			return rv.visit(this);
		}
		return this;
	}

	public ReqBuilder uri(String uri) {
		rb.setUri(uri);
		return this;
	}

	public ReqBuilder uri(URI uri) {
		rb.setUri(uri);
		return this;
	}

	public String asString() {
		return execute().asString();
	}

	public String asString(Charset charset) {
		return execute().asString(charset);
	}

	@Deprecated
	public JSONObject asJSON() {
		return execute().asJson();
	}

	public JSONObject asJson() {
		return execute().asJson();
	}

	public <T> T asPojo(Class<T> clazz) {
		return execute().asPojo(clazz);
	}
}
