import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.xzc.hc.util.ProxyUtil;
import org.xzc.hc.util.ProxyUtil.ProxyInfo;

public class Main {
	public static void main(String[] args) {
		String httpProxy = "a b 127.0.0.1 8080";
		ProxyInfo pi = ProxyUtil.parse( httpProxy );
		System.out.println( pi.username + " " + pi.password + " " + pi.host + " " + pi.port );
		
		
		httpProxy = "127.0.0.1 8080";
		pi = ProxyUtil.parse( httpProxy );
		System.out.println( pi.username + " " + pi.password + " " + pi.host + " " + pi.port );
		
		
		httpProxy = "admin@example.com PASSWORD@PASSWORD 127.0.0.1 8080";
		pi = ProxyUtil.parse( httpProxy );
		System.out.println( pi.username + " " + pi.password + " " + pi.host + " " + pi.port );
		
	}
}
