package com.github.xzchaoo.hc;

/**
 * Created by xzchaoo on 2016/9/7.
 */
public interface RespCallback<T> {
	T process(Resp resp);
}
