package com.its.model.bean;


/**
 * Column
 *
 */
@SuppressWarnings("unused")
public class Column {
	private String field;
	private String title;
	private Integer width;
	private Integer rowspan;
	private Integer colspan;
	private Boolean checkbox;
	private Integer index;
	private static final String ALIGN_LEFT = "left";
	private static final String ALIGN_CENTER = "center";
	private static final String ALIGN_RIGHT = "right";
	private String align = "left";

	public Column(Integer index, String field, String title, Integer width,
			String align) {
		this.index = index;
		this.field = field;
		this.title = title;
		this.width = width;
		this.align = align;
	}

	public String getField() {
		return this.field;
	}

	public Column setField(String field) {
		this.field = field;
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public Column setTitle(String title) {
		this.title = title;
		return this;
	}

	public Integer getWidth() {
		return this.width;
	}

	public Column setWidth(Integer width) {
		this.width = width;
		return this;
	}

	public Integer getRowspan() {
		return this.rowspan;
	}

	public Column setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
		return this;
	}

	public Integer getColspan() {
		return this.colspan;
	}

	public Column setColspan(Integer colspan) {
		this.colspan = colspan;
		return this;
	}

	public boolean isCheckbox() {
		return this.checkbox.booleanValue();
	}

	public Column setCheckbox(boolean checkbox) {
		this.checkbox = Boolean.valueOf(checkbox);
		return this;
	}

	public String getAlign() {
		return this.align;
	}

	public Column setAlign(String align) {
		this.align = align;
		return this;
	}

	public Integer getIndex() {
		return this.index;
	}

	public Column setIndex(Integer index) {
		this.index = index;
		return this;
	}

}
