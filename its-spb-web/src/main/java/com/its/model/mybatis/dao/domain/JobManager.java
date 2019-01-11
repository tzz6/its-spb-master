package com.its.model.mybatis.dao.domain;

import java.io.Serializable;

/**
 * 定时任务业务数据管理
 *
 */
public class JobManager implements Serializable {

	private static final long serialVersionUID = 1727194932633062516L;
	
	/** ID */
	private Long jobId;
	/** 业务数据类型 */
	private String serviceType;
	/** 业务数据ID */
	private String serviceId;
	/** 状态0:定时任务未处理 1:定时任务已处理 */
	private String status;
	/** JOB调用次数 */
	private Integer jobCount;
	/** 数据IP */
	private String ip;
	/** JOB执行IP */
	private String jobIp;
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getJobCount() {
		return jobCount;
	}
	public void setJobCount(Integer jobCount) {
		this.jobCount = jobCount;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getJobIp() {
		return jobIp;
	}
	public void setJobIp(String jobIp) {
		this.jobIp = jobIp;
	}

}