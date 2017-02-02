package com.xzchaoo.hc;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public class UrlUtils {
	public static URIBuilder create(String url) {
		try {
			return new URIBuilder(url);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> getQueryValues(URIBuilder ub, String name) {
		List<NameValuePair> list = ub.getQueryParams();
		List<String> values = new ArrayList<String>();
		for (NameValuePair nvp : list) {
			if (nvp.getName().equals(name)) {
				values.add(nvp.getValue());
			}
		}
		return values;
	}

	public static String getFirstQueryValue(URIBuilder ub, String name) {
		List<NameValuePair> list = ub.getQueryParams();
		for (NameValuePair nvp : list) {
			if (nvp.getName().equals(name)) {
				return nvp.getValue();
			}
		}
		return null;
	}
}
