package org.xzc.hc;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 对hc的简单的封装
 * @author xzchaoo
 *
 */
public class HC {
	private static final <T> T safeRun(SafeRunner<T> sr) {
		try {
			return sr.run();
		} catch (Exception e) {
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException( e );
		}
	}

	private CloseableHttpClient chc;

	private String encoding = "utf-8";

	public HC(CloseableHttpClient chc) {
		this.chc = chc;
	}

	public byte[] asByteArray(final HttpUriRequest req) {
		return safeRun( new SafeRunner<byte[]>() {
			public byte[] run() throws Exception {
				CloseableHttpResponse res = null;
				try {
					res = chc.execute( req );
					return EntityUtils.toByteArray( res.getEntity() );
				} finally {
					HttpClientUtils.closeQuietly( res );
				}
			}
		} );
	}

	public byte[] asByteArray(Req req) {
		return asByteArray( req.build() );
	}

	public CloseableHttpResponse asRes(final HttpUriRequest req) {
		return safeRun( new SafeRunner<CloseableHttpResponse>() {
			public CloseableHttpResponse run() throws Exception {
				return chc.execute( req );
			}
		} );
	}

	public CloseableHttpResponse asRes(Req req) {
		return asRes( req.build() );
	}

	public String asString(HttpUriRequest req) {
		return asString( req, encoding );
	}

	public String asString(final HttpUriRequest req, final String encoding) {
		return safeRun( new SafeRunner<String>() {
			public String run() throws Exception {
				CloseableHttpResponse res = null;
				try {
					res = chc.execute( req );
					return EntityUtils.toString( res.getEntity(), encoding );
				} finally {
					HttpClientUtils.closeQuietly( res );
				}
			}
		} );
	}

	public String asString(Req req) {
		return asString( req.build(), encoding );
	}

	public String asString(Req req, String encoding) {
		return asString( req.build(), encoding );
	}

	public void close() {
		HttpClientUtils.closeQuietly( chc );
	}

	public void consume(final HttpUriRequest req) {
		safeRun( new SafeRunner<Void>() {
			public Void run() throws Exception {
				CloseableHttpResponse res = null;
				try {
					res = chc.execute( req );
					EntityUtils.consumeQuietly( res.getEntity() );
				} finally {
					HttpClientUtils.closeQuietly( res );
				}
				return null;
			}
		} );
	}

	public void consume(Req req) {
		consume( req.build() );
	}

	public byte[] getAsByteArray(String url) {
		return asByteArray( RequestBuilder.get( url ).build() );
	}

	public String getAsString(String url) {
		return getAsString( url, encoding );
	}

	public String getAsString(String url, String encoding) {
		return asString( RequestBuilder.get( url ).build(), encoding );
	}

	public CloseableHttpClient getCHC() {
		return chc;
	}

}
