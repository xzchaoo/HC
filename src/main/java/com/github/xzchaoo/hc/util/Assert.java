package com.github.xzchaoo.hc.util;

/**
 * Created by xzchaoo on 2016/6/6 0006.
 */
public class Assert {
	public static void notNull(Object obj, String text) {
		if (obj == null) throw new NullPointerException(text);
	}

	public static void notEmpty(String str, String text) {
		if (str == null || str.trim().length() > 0) throw new IllegalArgumentException(text);
	}

	public static void assertTrue(boolean result, String text) {
		if (!result) throw new IllegalStateException(text);
	}
}
