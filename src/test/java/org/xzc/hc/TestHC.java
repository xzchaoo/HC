package org.xzc.hc;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.xzc.hc.util.HCs;

/**
 * Created by xzchaoo on 2016/6/6 0006.
 */
public class TestHC {

	@Test
	public void testJSON() {
		HC hc = HCs.makeHC();
		String s = hc.post("http://api.bilibili.com/x/reply/add")
			.cookies()
			.datas("oid", 4569, "message", "asdf", "type", 1, "plat", 3)
			.asString();
		System.out.println(s);
		hc.close();
	}
}
