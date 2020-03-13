package com.its.common.mongodb.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import com.its.common.mongodb.dao.MongoBaseDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


/**
  * Description: MongoBaseDaoImpl
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/14 17:46
  */
public abstract class AbstractMongoBaseDaoImpl<T> implements MongoBaseDao<T> {

	/** 实体类描述符 */
	private Class<T> clazz;

	@Resource
	private MongoTemplate mongoTemplate;


	@SuppressWarnings("unchecked")
	public AbstractMongoBaseDaoImpl() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	/** 创建集合 */
	@Override
	public void createCollection(String collectionName) {
		mongoTemplate.createCollection(collectionName);
	}

	/** 删除 collection */
	@Override
	public void dropCollection(String collectionName) {
		mongoTemplate.dropCollection(collectionName);
	}

	/** 添加 */
	@Override
	public void insert(T object, String collectionName) {
		mongoTemplate.insert(object, collectionName);
	}

	@Override
	public void insert(T object) {
		mongoTemplate.insert(object);
	}
	
	@Override
	public void insertAll(List<T> object) {
		mongoTemplate.insertAll(object);
	}

	/** 根据条件查找一条数据 */
	@Override
	public T findOne(Query query, String collectionName) {
		return mongoTemplate.findOne(query, clazz, collectionName);
	}

	@Override
	public T findOne(Query query) {
		return mongoTemplate.findOne(query, clazz);
	}

	/** 根据条件查找 */
	@Override
	public List<T> findByQuery(Query query, String collectionName) {
		return mongoTemplate.find(query, clazz, collectionName);
	}

	@Override
	public List<T> findByQuery(Query query) {
		return mongoTemplate.find(query, clazz);
	}
	
	/** 查找所有 */
	@Override
	public List<T> findAll(){
		return mongoTemplate.findAll(clazz);
	}
	@Override
	public List<T> findAll(String collectionName){
		return mongoTemplate.findAll(clazz, collectionName);
	}
	
	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, clazz);
	}

	/** 修改 */
	@Override
	public void update(Query query, Update update, String collectionName) {
		mongoTemplate.upsert(query, update, clazz, collectionName);
	}

	@Override
	public void update(Query query, Update update) {
		mongoTemplate.upsert(query, update, clazz);
	}

	/** 根据条件删除 */
	@Override
	public void remove(Query query, String collectionName) {
		if (query != null) {
			mongoTemplate.remove(query, clazz, collectionName);
		} else {
			mongoTemplate.remove(clazz, collectionName);
		}
	}

	@Override
	public void remove(Query query) {
		if (query != null) {
			mongoTemplate.remove(query, clazz);
		} else {
			mongoTemplate.remove(clazz);
		}
	}
}
