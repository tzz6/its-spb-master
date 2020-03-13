package com.its.base.servers.api.mongodb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
/**
  * Description: MongoDb Country
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/17 14:47
  */
@Document(collection = "country")
public class Country {

	@Id
	private Integer id;
	@Indexed
	private String name;
	private String enName;
	private String code;
	private Date createDate;
	@DBRef
	private List<City> citys;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public List<City> getCitys() {
		return citys;
	}

	public void setCitys(List<City> citys) {
		this.citys = citys;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", enName=" + enName + ", code=" + code + ", createDate="
				+ createDate + ", citys=" + citys + "]";
	}

	

}
