package com.its.model.bean;

import java.io.Serializable;

/**
  * Description: PageEntity
  * Company: tzz
  * @Author: tzz
  * Date: 2019/07/10 18:32
  */
public class PageEntity implements Serializable {
	
	private static final long serialVersionUID = -6049458356491622982L;
	
	private static final int DEFAULT_PAGE_SIZE = 100;

    /** curr page */
    private int page = 1;

    private int totalPage;

    /** total records per pager */
    private int rows = DEFAULT_PAGE_SIZE;

    private int total;

    /** page records start index */
    private int from;

    /** page records end index */
    private int to;
	
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
