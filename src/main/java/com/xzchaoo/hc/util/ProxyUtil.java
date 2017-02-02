package com.xzchaoo.hc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyUtil {
	public static class ProxyInfo {
		public String username;
		public String password;
		public String host;
		public int port;
	}

	private static final Pattern PATTERN = Pattern.compile( "^(([^ ]+) ([^ ]+) )?([^ ]+) (\\d+)$" );

	public static ProxyInfo parse(String httpProxy) {
		ProxyInfo pi = new ProxyInfo();
		Matcher m = PATTERN.matcher( httpProxy );
		if (!m.matches())
			throw new IllegalArgumentException( "请给出形如 \"username password host port\" 的代理格式." );
		pi.username = m.group( 2 );
		pi.password = m.group( 3 );
		pi.host = m.group( 4 );
		pi.port = Integer.parseInt( m.group( 5 ) );
		return pi;
	}
}
