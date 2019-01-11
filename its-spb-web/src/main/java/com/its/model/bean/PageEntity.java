package com.its.model.bean;

import java.io.Serializable;

public class PageEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6049458356491622982L;
	
	private static final int DEFAULT_PAGE_SIZE = 100;

	private int page = 1;//curr page
	
	private int totalPage;
	
	private int rows = DEFAULT_PAGE_SIZE;//total records per pager
	
	private int total;
	
	private int from; //page records start index
	
	private int to;//page records end index
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page <= 0 ? 1 : page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		this.from = (this.page - 1) * this.rows;
		this.to = rows;
		if(this.total % this.rows == 0){
			this.totalPage = this.total / this.rows;
		}else{
			this.totalPage = (this.total / this.rows) + 1;
		}
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage < 0 ? 0 : totalPage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

}
