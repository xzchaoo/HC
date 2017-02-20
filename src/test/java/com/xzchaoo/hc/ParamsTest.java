package com.xzchaoo.hc;

import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by zcxu on 2017/2/20.
 */
public class ParamsTest {
	@Test
	public void test_add() {
		Params p1 = Params.create("a", 1);
		Params p2 = Params.create("b", 2, "c", 3);
		p1.add(p2);
		assertEquals(3, p1.getList().size());
		assertEquals("1", p1.getList().get(0).getValue());
		assertEquals("2", p1.getList().get(1).getValue());
		assertEquals(new BasicNameValuePair("c", "3"), p1.getList().get(2));
	}
}
