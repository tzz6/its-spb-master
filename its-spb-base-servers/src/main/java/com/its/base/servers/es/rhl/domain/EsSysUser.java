package com.its.base.servers.es.rhl.domain;

import com.its.base.servers.api.annotation.Import;
import com.its.common.es.rhl.annotation.ESID;
import com.its.common.es.rhl.annotation.ESMapping;
import com.its.common.es.rhl.annotation.ESMetaData;
import com.its.common.es.rhl.enums.DataType;
import com.its.common.utils.json.JacksonUtil;

import java.io.Serializable;
import java.util.Date;


/**
 * SysUser
 *
 * @author tzz
 */
@ESMetaData(indexName = "es_sys_user", indexType = "sys_user", numberOfShards = 5, numberOfReplicas = 1, printLog = false)
public class EsSysUser implements Serializable {

    private static final long serialVersionUID = 7523417995488977087L;

    /**
     * ID
     */
    @ESID
    @ESMapping(datatype = DataType.long_type)
    private String stId;
    /**
     * 用户名
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String stCode;
    /**
     * 用户姓名
     */
    private String stName;
    /**
     * 密码
     */
    private String stPassword;
    /**
     * 年龄
     */
	@ESMapping(datatype = DataType.long_type)
    private Long age;
    /**
     * 语言
     */
    private String language;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 编辑人
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createTm;
    /**
     * 修改时间
     */
    private Date updateTm;

    public EsSysUser() {
    }

    public EsSysUser(String stId, String stCode, String stName) {
        this.stId = stId;
        this.stCode = stCode;
        this.stName = stName;
    }

    public EsSysUser(String stId, String stCode, String stName, String stPassword, Long age, String language, String createBy, String updateBy, Date createTm, Date updateTm) {
        this.stId = stId;
        this.stCode = stCode;
        this.stName = stName;
        this.stPassword = stPassword;
        this.age = age;
        this.language = language;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.createTm = createTm;
        this.updateTm = updateTm;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getStCode() {
        return stCode;
    }

    public void setStCode(String stCode) {
        this.stCode = stCode;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getStPassword() {
        return stPassword;
    }

    public void setStPassword(String stPassword) {
        this.stPassword = stPassword;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public Date getUpdateTm() {
        return updateTm;
    }

    public void setUpdateTm(Date updateTm) {
        this.updateTm = updateTm;
    }

    @Override
    public String toString() {
        return "EsSysUser{" +
                "stId='" + stId + '\'' +
                ", stCode='" + stCode + '\'' +
                ", stName='" + stName + '\'' +
                ", stPassword='" + stPassword + '\'' +
                ", age=" + age +
                ", language='" + language + '\'' +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", createTm=" + createTm +
                ", updateTm=" + updateTm +
                '}';
    }
}