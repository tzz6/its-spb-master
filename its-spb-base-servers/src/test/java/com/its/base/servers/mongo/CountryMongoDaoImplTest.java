package com.its.base.servers.mongo;

import com.its.base.servers.BaseTest;
import com.its.base.servers.api.mongodb.domain.City;
import com.its.base.servers.api.mongodb.domain.Country;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;
import java.util.stream.IntStream;


/**
  * Description: CountryMongoDaoImplTest
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/17 14:20
  */
public class CountryMongoDaoImplTest extends BaseTest {

	@Autowired()
	@Qualifier("countryMongoDaoImpl")
	private CountryMongoDaoImpl countryMongoDao;
	private static String collectionName = "users";


	@Test
	public void dropCollection() {
		countryMongoDao.dropCollection(collectionName);
	}

	@Test
	public void insert() {
		//新增10条数据
		int end = 10;
		IntStream.range(0, end).forEach(i -> {
			Query query = new Query(Criteria.where("id").is(i));
			countryMongoDao.remove(query, collectionName);
			Country country = new Country();
			country.setId(i);
			country.setName("cn-" + i);
			country.setEnName("en-" + i);
			country.setCreateDate(new Date());
			List<City> cityList = new ArrayList<>();
			City city = new City();
			city.setId(1);
			city.setName("深圳");
			City city2 = new City();
			city2.setId(2);
			city2.setName("广州");
			cityList.add(city);
			cityList.add(city2);
			country.setCitys(cityList);
			countryMongoDao.insert(country, collectionName);
//			countryMongoDao.insert(country);
		});
	}

	@Test
	public void findOne() {
		Map<String, Object> params = new HashMap<>(16);
		params.put("id", 0);
		params.put("name", "cn-0");
		Query query = new Query(Criteria.where("id").is(params.get("id")).and("name").is(params.get("name")));
		Country country = countryMongoDao.findOne(query);
//		Country country = countryMongoDao.findOne(query, collectionName);
		if(country!=null){
			List<City> cityList = country.getCitys();
			System.out.println(country);
			for (City city : cityList) {
				System.out.println(city.toString());
			}
		}
	}

	@Test
	public void findByQuery() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		Query query = new Query(Criteria.where("id").in(list));
		List<Country> countrys = countryMongoDao.findByQuery(query);
//		List<Country> countrys = countryMongoDao.findByQuery(query, collectionName);
		for (Country country : countrys) {
			System.out.println(country.toString());
		}
	}

	@Test
	public void update() {
		Map<String, Object> params = new HashMap<>(16);
		params.put("id", 2);
		params.put("name", "update-02");
		params.put("enName", "en-update-02");
		Query query = new Query(Criteria.where("id").is(params.get("id")));
		Update update = new Update().set("name", params.get("name")).set("enName", params.get("enName"));
		countryMongoDao.update(query, update);
//		countryMongoDao.update(query, update, collectionName);
		findByQuery();
	}

	@Test
	public void remove() {
		List<Integer> list = new ArrayList<>();
		int end = 10;
		for (int i = 0; i < end; i++) {
			list.add(i);
		}
		Query query = new Query(Criteria.where("id").in(list));
		countryMongoDao.remove(query);
//		countryMongoDao.remove(query, collectionName);
		// countryMongoDao.remove(null, collectionName);
	}

}
