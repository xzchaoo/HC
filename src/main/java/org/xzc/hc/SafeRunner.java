package org.xzc.hc;

public interface SafeRunner<T> {
	T run() throws Exception;
}
