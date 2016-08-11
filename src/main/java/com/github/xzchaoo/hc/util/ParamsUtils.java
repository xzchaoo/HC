package com.github.xzchaoo.hc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.xzchaoo.hc.util.Assert.assertTrue;

/**
 * Created by xzchaoo on 2016/6/6 0006.
 */
public class ParamsUtils {
	public static void addParams(RequestBuilder rb, Object[] paramsPair) {
		if (paramsPair == null || paramsPair.length == 0) return;
		assertTrue(paramsPair.length % 2 == 0, "参数个数必须是偶数!");
		for (int i = 0; i < paramsPair.length; i += 2) {
			String name = paramsPair[i].toString();
			String value = paramsPair[i + 1].toString();
			rb.addParameter(name, value);
		}
	}

	public static void addHeaders(RequestBuilder rb, Object[] headersPair) {
		if (headersPair == null || headersPair.length == 0) return;

		assertTrue(headersPair.length % 2 == 0, "参数个数必须是偶数!");

		for (int i = 0; i < headersPair.length; i += 2) {
			String name = headersPair[i].toString();
			String value = headersPair[i + 1].toString();
			rb.addHeader(name, value);
		}
	}

	public static JSONObject pairsToJson(List<Object> pairList) {
		JSONObject jo = new JSONObject();
		if (pairList != null) {
			for (int i = 0; i < pairList.size(); i += 2) {
				String name = pairList.get(i).toString();
				Object value = pairList.get(i + 1);
				jo.put(name, value);
			}
		}
		return jo;
	}

	public static JSONObject pairsToJson(Object[] pairArray) {
		JSONObject jo = new JSONObject();
		if (pairArray != null) {
			for (int i = 0; i < pairArray.length; i += 2) {
				String name = pairArray[i].toString();
				Object value = pairArray[i + 1];
				jo.put(name, value);
			}
		}
		return jo;
	}

	public static void jsonTo(RequestBuilder rb, List<Object> paramsPair) {
		jsonTo(rb, pairsToJson(paramsPair));
	}

	public static void jsonTo(RequestBuilder rb, JSONObject json) {
		jsonTo(rb, json.toString());
	}

	public static void jsonTo(RequestBuilder rb, String jsonString) {
		HttpEntity e = EntityBuilder.create()
			.setContentEncoding("utf-8")
			.setContentType(ContentType.APPLICATION_JSON)
			.setText(jsonString)
			.build();
		rb.setEntity(e);
	}

	public static void jsonTo(RequestBuilder rb, Object[] paramsPair) {
		jsonTo(rb, pairsToJson(paramsPair));
	}

	public static String makeCookiesString(Object[] cookiesPair) {
		if (cookiesPair == null) return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cookiesPair.length; i += 2) {
			String name = cookiesPair[i].toString();
			String value = cookiesPair[i + 1].toString();
			sb.append(name);
			sb.append('=');
			sb.append(value);
			sb.append(';');
		}
		return sb.toString();
	}

	public static String makeCookiesString(List<Object> cookiesPair) {
		if (cookiesPair == null) return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cookiesPair.size(); i += 2) {
			String name = cookiesPair.get(i).toString();
			String value = cookiesPair.get(i + 1).toString();
			sb.append(name);
			sb.append('=');
			sb.append(value);
			sb.append(';');
		}
		return sb.toString();
	}

	public static List<NameValuePair> toNameValuePairList(List<Object> pairList) {
		if (pairList == null || pairList.isEmpty()) return Collections.emptyList();
		assertTrue(pairList.size() % 2 == 0, "pairList大小必须为偶数..");
		List<NameValuePair> list = new ArrayList<NameValuePair>(pairList.size() / 2);
		for (int i = 0; i < pairList.size(); i += 2) {
			String name = pairList.get(i).toString();
			String value = pairList.get(i + 1).toString();
			list.add(new BasicNameValuePair(name, value));
		}
		return list;
	}

	public static List<NameValuePair> toNameValuePairList(Map<?, ?> map) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Map.Entry<?, ?> e : map.entrySet()) {
			if (e.getValue() != null) {
				list.add(new BasicNameValuePair(e.getKey().toString(), e.getValue().toString()));
			}
		}
		return list;
	}

	public static List<NameValuePair> toNameValuePairList(Object[] pairArray) {
		if (pairArray == null || pairArray.length == 0) return Collections.emptyList();
		assertTrue(pairArray.length % 2 == 0, "pairList大小必须为偶数..");
		List<NameValuePair> list = new ArrayList<NameValuePair>(pairArray.length / 2);
		for (int i = 0; i < pairArray.length; i += 2) {
			String name = pairArray[i].toString();
			String value = pairArray[i + 1].toString();
			list.add(new BasicNameValuePair(name, value));
		}
		return list;
	}

	public static void datasTo(RequestBuilder rb, List<Object> dataPairList) {
		try {
			List<NameValuePair> list = toNameValuePairList(dataPairList);
			UrlEncodedFormEntity e = new UrlEncodedFormEntity(list, "utf-8");
			rb.setEntity(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 默认是会跳过value==null的参数的
	 *
	 * @param map
	 * @param rb
	 */
	public static void paramsTo(RequestBuilder rb, Map<?, ?> map) {
		for (Map.Entry<?, ?> e : map.entrySet()) {
			if (e.getValue() != null) {
				rb.addParameter(e.getKey().toString(), e.getValue().toString());
			}
		}
	}

	public static Map<String, String> describe(Object bean) {
		try {
			return BeanUtils.describe(bean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void paramsTo(RequestBuilder rb, Object obj) {
		paramsTo(rb, describe(obj));
	}

	public static void datasTo(RequestBuilder rb, Map<?, ?> map) {
		List<NameValuePair> list = toNameValuePairList(map);
		UrlEncodedFormEntity e = null;
		try {
			e = new UrlEncodedFormEntity(list, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		rb.setEntity(e);
	}

	/**
	 * 目前只支持 JSONObject 注意不要传入数组
	 *
	 * @param rb
	 * @param obj
	 */
	public static void jsonTo(RequestBuilder rb, Object obj) {
		jsonTo(rb, (JSONObject) JSON.toJSON(obj));
	}
}