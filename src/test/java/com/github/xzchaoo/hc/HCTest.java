package com.github.xzchaoo.hc;

import com.github.xzchaoo.hc.util.HCs;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.List;

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
		assertNotNull(hc.getDefaultEncoding());
		hc.close();
	}

	@Test
	public void testAll() throws UnsupportedEncodingException {
		HC hc = HCs.makeHC();
		HttpUriRequest hur = hc.post("http://www.example.com")
			.encoding("utf-8")
			.headers("a", "ha", "b", "hb")
			.params("c", "pc", "d", "pd", "dd", "中文")
			.datas("e", "de", "f", "df")
			.cookies("g", "cg", "h", "ch")
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
}
