package org.xzc.hc;

public interface SafeRunner<T> {
	public T run() throws Exception;
}
