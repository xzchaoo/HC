package com.xzchaoo.hc;

import com.xzchaoo.hc.util.HCs;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by xiangfeng.xzc on 2016/7/18.
 */
public class HCTest {
	@Test
	public void testBuild() {
		HC hc = HCs.makeHC();
		assertNotNull(hc.getCHC());
		assertNotNull(hc.getRequestConfig());
		assertNotNull(hc.getDefaultCharset());
		Resp resp = hc.get("https://passport.bilibili.com/login").execute();
		CloseableHttpResponse resp0 = resp.raw();
		for (Header h : resp0.getHeaders("Set-Cookie")) {
			for (HeaderElement he : h.getElements()) {
				System.out.println(he.getName() + " " + he.getValue());
				System.out.println(he.getParameterCount());
			}
		}
		hc.close();
	}

	@Test
	public void testAll() throws UnsupportedEncodingException {
		//HC hc = HCs.makeHC();
		HttpUriRequest hur = RequestBuilder.post("http://www.example.com")
			.addHeader("a", "ha")
			.addHeader("b", "hb")
			.addParameter("c", "pc")
			.addParameter("d", "pd")
			.addParameter("dd", "中文")
			.setEntity(new UrlEncodedFormEntity(Collections.<NameValuePair>emptyList()))
			.addHeader("Cookie", "g=cg;h=ch;")
			//.datas("e", "de", "f", "df")
			.build();

		assertEquals(hur.getMethod(), "POST");
		List<NameValuePair> queryParams = new URIBuilder(hur.getURI()).getQueryParams();
		assertNotNull(queryParams);
		assertEquals(queryParams.size(), 3);

		for (NameValuePair nvp : queryParams) {
			if (nvp.getName().equals("c")) {
				assertEquals(nvp.getValue(), "pc");
			} else if (nvp.getName().equals("d")) {
				assertEquals(nvp.getValue(), "pd");
			} else if (nvp.getName().equals("dd")) {
				assertEquals(nvp.getValue(), "中文");
			}
		}

		assertNotNull(hur.getFirstHeader("a"));
		assertEquals(hur.getFirstHeader("a").getValue(), "ha");
		assertNotNull(hur.getFirstHeader("b"));
		assertEquals(hur.getFirstHeader("b").getValue(), "hb");

		assertNotNull(hur.getFirstHeader("Cookie"));
		assertEquals(hur.getFirstHeader("Cookie").getValue(), "g=cg;h=ch;");
	}

	@Test
	public void testCookie() {
		HC hc = HCs.makeHC();
		BasicCookieStore bcs = new BasicCookieStore();
		HttpClientContext hcc = HttpClientContext.create();
		hcc.setCookieStore(bcs);
		Resp resp = hc.get("https://account.bilibili.com/ajax/miniLogin/minilogin")
			.config(RequestConfig.copy(hc.getRequestConfig())
				.setCookieSpec(CookieSpecs.DEFAULT)
				.build())
			.execute(hcc);
		List<Cookie> cookies = bcs.getCookies();
		for (Cookie c : cookies) {
			System.out.println(c);
		}
	}

	public void ceshi() throws IOException, InterruptedException {
		HC hc = HCs.makeHC();
		final CloseableHttpClient chc = hc.getCHC();
		ExecutorService es = Executors.newFixedThreadPool(16);
		for (int i = 0; i < 1; ++i) {
			es.submit(new Runnable() {
				public void run() {
					try {
						CloseableHttpResponse resp = chc.execute(
							RequestBuilder.get("http://api.bilibili.com/x/video?aid=1").build()
						);
						HttpEntity he = resp.getEntity();
						Thread.sleep(1000);
						//EntityUtils.consume(he);
						resp.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		es.shutdown();
		es.awaitTermination(1, TimeUnit.MINUTES);
		System.out.println("我在这里");
		for (int i = 0; i < 100; ++i) {
			Thread.sleep(1000);
		}
	}
}
