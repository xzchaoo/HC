package com.xzchaoo.hc;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/28.
 */
public class Params {
	private List<NameValuePair> list = new ArrayList<NameValuePair>();

	public Params() {
	}

	public Params(Object... args) {
		if (args == null || args.length == 0) {
			return;
		}
		if (args.length % 2 == 1) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < args.length; i += 2) {
			list.add(new BasicNameValuePair((String) args[i], args[i + 1].toString()));
		}
	}

	public Params add(String name, Object value) {
		list.add(new BasicNameValuePair(name, value.toString()));
		return this;
	}

	public Params add(Params p) {
		return add(p.getList());
	}

	public Params add(List<NameValuePair> nvps) {
		list.addAll(nvps);
		return this;
	}

	public Params add(Object... args){
		if (args == null || args.length == 0) {
			return this;
		}
		if (args.length % 2 == 1) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < args.length; i += 2) {
			list.add(new BasicNameValuePair((String) args[i], args[i + 1].toString()));
		}
		return this;
	}

	public List<NameValuePair> getList() {
		return list;
	}

	public boolean contains(String name) {
		for (NameValuePair nvp : list) {
			if (nvp.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static Params create(Object... args) {
		return new Params(args);
	}
}
