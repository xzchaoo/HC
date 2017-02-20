package com.xzchaoo.hc;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Administrator on 2017/1/22.
 */
public class UriBuilderTest {
	@Test
	public void test1() throws URISyntaxException {
		URIBuilder ub = new URIBuilder("http://www.baidu.com/a/b/c?d=1&e=2");
		assertEquals(ub.getHost(), "www.baidu.com");
		assertEquals(ub.getPath(), "/a/b/c");
		assertNotNull(ub.getScheme(), "http");
		assertNotNull(ub.getQueryParams());
		assertEquals(ub.getQueryParams().size(), 2);
	}
}
