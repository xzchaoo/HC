package com.xzchaoo.hc.util;

import com.alibaba.fastjson.JSON;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * Created by Administrator on 2016/10/22.
 */
public class JsonEntity extends StringEntity {
	public JsonEntity(Object obj) {
		super(JSON.toJSONString(obj), ContentType.APPLICATION_JSON);
	}
}
