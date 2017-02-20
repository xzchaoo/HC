package com.xzchaoo.hc.test;

import com.alibaba.fastjson.JSONObject;
import com.xzchaoo.hc.HC;
import com.xzchaoo.hc.util.HCs;
import com.xzchaoo.hc.util.ProxyUtil;
import com.xzchaoo.hc.util.ProxyUtil.ProxyInfo;

public class Main {
	public static void main(String[] args) {
		String httpProxy = "a b 127.0.0.1 8080";
		ProxyInfo pi = ProxyUtil.parse(httpProxy);
		System.out.println(pi.username + " " + pi.password + " " + pi.host + " " + pi.port);


		httpProxy = "127.0.0.1 8080";
		pi = ProxyUtil.parse(httpProxy);
		System.out.println(pi.username + " " + pi.password + " " + pi.host + " " + pi.port);


		httpProxy = "admin@example.com PASSWORD@PASSWORD 127.0.0.1 8080";
		pi = ProxyUtil.parse(httpProxy);
		System.out.println(pi.username + " " + pi.password + " " + pi.host + " " + pi.port);

		HC hc = HCs.makeHC();
		String s = hc.get("http://www.baidu.com").execute().asString();
		System.out.println(s);

		JSONObject r = hc.get("http://api.bilibili.com/x/video?aid=1").execute().asJson();
		System.out.println(r);
	}
}
