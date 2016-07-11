package com.github.xzchaoo.hc.util;

import com.github.xzchaoo.hc.HC;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HCs {
	public interface HttpClientBuilderCallback {
		void onBuild(HttpClientBuilder hcb);
	}

	//默认超时30秒
	public static final int DEFAULT_TIMEOUT = 30000;

	//默认支持2个同时连接
	public static final int DEFAULT_BATCH = 2;

	public static HC makeHC() {
		return makeHC( DEFAULT_BATCH );
	}

	public static HC makeHC(int batch) {
		return makeHC( batch, DEFAULT_TIMEOUT, null, true, null, null );
	}

	public static HC makeHC(boolean ignoreCookie, BasicCookieStore bcs) {
		return makeHC( DEFAULT_BATCH, DEFAULT_TIMEOUT, null, ignoreCookie, bcs, null );
	}

	public static HC makeHC(int batch, int timeout, String httpProxy, boolean ignoreCookie, BasicCookieStore bcs,
			HttpClientBuilderCallback hcbc) {

		//构建 RequestConfig
		Builder b = RequestConfig.custom()
				.setConnectionRequestTimeout( timeout )
				.setConnectTimeout( timeout )
				.setSocketTimeout( timeout );
		if (ignoreCookie) {
			b.setCookieSpec( CookieSpecs.IGNORE_COOKIES );
		}
		RequestConfig rc = b.build();

		//构建ConnectionManager
		PoolingHttpClientConnectionManager m = new PoolingHttpClientConnectionManager();
		m.setMaxTotal( batch * 2 );
		m.setDefaultMaxPerRoute( batch );

		//处理代理
		BasicCredentialsProvider bcp = new BasicCredentialsProvider();
		HttpHost proxy = null;
		if (httpProxy != null) {
			ProxyUtil.ProxyInfo pi = ProxyUtil.parse( httpProxy );
			proxy = new HttpHost( pi.host, pi.port );
			if (pi.username != null) {
				bcp.setCredentials( new AuthScope( pi.host, pi.port ),
						new UsernamePasswordCredentials( pi.username, pi.password ) );
			}
		}

		HttpClientBuilder hcb = HttpClients.custom()
				.setDefaultCredentialsProvider( bcp )
				.setProxy( proxy )
				.setConnectionManager( m )
				.setDefaultRequestConfig( rc )
				.setDefaultCookieStore( bcs );

		if (hcbc != null)
			hcbc.onBuild( hcb );

		return new HC( hcb.build() );
	}
}
