package com.xzchaoo.hc;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.util.Collections;

/**
 * Created by zcxu on 2017/2/8.
 */
public class CommonEntity {
	public static final HttpEntity EMPTY_URL_ENCODED_FORM_ENTITY = new UrlEncodedFormEntity(Collections.<NameValuePair>emptyList(), CharsetUtils.UTF8);
}
