package com.its.model.bean;

/** 
 *  
 */
import java.io.Serializable;
import java.util.List;
 
public class QueryResultData<T> implements Serializable {

	private static final long serialVersionUID = -6676976596317161938L;
	private List<T> result = null;
	private long totalCount = 0;


	public QueryResultData(long totalCount, List<T> result) {
		this.totalCount = totalCount;
		this.result = result;
	}
	/**
	 * 获得页内的记录列表.
	 */
	public List<T> getResult() {
		return result;
	}
	/**
	 * 设置页内的记录列表.
	 */
	/**
	 * 获得总记录数, 默认值为-1.
	 */
	public long getTotalCount() {
		return totalCount;
	}

}
