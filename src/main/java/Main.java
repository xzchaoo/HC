import com.github.xzchaoo.hc.HC;
import com.github.xzchaoo.hc.util.HCs;
import com.github.xzchaoo.hc.util.ProxyUtil;
import com.github.xzchaoo.hc.util.ProxyUtil.ProxyInfo;

import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

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
		String s = hc.get("http://www.baidu.com").asString();
		System.out.println(s);

		Map<String, String> map = new HashMap<String, String>();
		map.put("username", "zs");
		try {
			System.out.println(BeanUtils.describe(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
