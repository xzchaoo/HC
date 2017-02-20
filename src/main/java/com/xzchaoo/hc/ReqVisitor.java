package com.xzchaoo.hc;

/**
 * Created by Administrator on 2016/10/25.
 */
public interface ReqVisitor {
	ReqBuilder visit(ReqBuilder req);
}
