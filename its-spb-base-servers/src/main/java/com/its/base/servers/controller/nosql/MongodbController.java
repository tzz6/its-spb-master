package com.its.base.servers.controller.nosql;

import com.its.base.servers.api.mongodb.domain.City;
import com.its.base.servers.api.mongodb.domain.Country;
import com.its.base.servers.mongo.CountryMongoDaoImpl;
import com.its.common.dto.ItsResponse;
import com.its.common.model.Datagrid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
  * Description: MongoDB
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/17 15:22
  */
@Controller
@RequestMapping("/mongoDB")
public class MongodbController {

	private static final Log log = LogFactory.getLog(MongodbController.class);

	private CountryMongoDaoImpl countryMongoDao;

	@Autowired
	public void setCountryMongoDao(CountryMongoDaoImpl countryMongoDao) {
		this.countryMongoDao = countryMongoDao;
	}

	/**
	 * mongoDB列表数据
	 *
	 *@param name name
	 * @param enName enName
	 * @param page 页
	 * @param rows 行数
	 * @return 列表数据
	 */
	@RequestMapping("/mongoDBManager")
	public @ResponseBody
	Datagrid<Country> mongoDBManager(@RequestParam(value = "name", required = false) String name,
									 @RequestParam(value = "enName", required = false) String enName, @RequestParam(value = "page") Integer page,
									 @RequestParam(value = "rows") Integer rows) {

		int startNum = (page - 1) * rows;
		Query query = new Query();
		if ((name != null && !"".equals(name))) {
			if(enName != null && !"".equals(enName)){
				Criteria criteria = Criteria.where("name").regex(name).and("enName").regex(enName);
				query.addCriteria(criteria);
			}else{
				Criteria criteria = Criteria.where("name").regex(name);
				query.addCriteria(criteria);
			}
		}else{
			if (enName != null && !"".equals(enName)) {
				Criteria criteria = Criteria.where("enName").regex(enName);
				query.addCriteria(criteria);
			}
		}
		long total = countryMongoDao.count(query);
		query.skip(startNum);
		query.limit(rows);
		List<Country> result = countryMongoDao.findByQuery(query);
		return new Datagrid<>(total, result);
	}

	/**
	 * 查询对应ID
	 * @param id id
	 * @return Map
	 */
	@RequestMapping(value = "/getById")
	public @ResponseBody Map<String, Object> getById(@RequestParam(value = "id") Integer id) {
		Query query = new Query(Criteria.where("id").is(id));
		Country country = countryMongoDao.findOne(query);
		Map<String, Object> map = new HashMap<>(0);
		map.put("id", country.getId());
		map.put("name", country.getName());
		map.put("enName", country.getEnName());
		map.put("code", country.getCode());
		return map;
	}

	/** 保存 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ItsResponse<String> save(Country country) {
		ItsResponse<String> response = new ItsResponse<String>().success();
		try {
			if (country.getId() != null) {
				// 编辑
				Query query = new Query(Criteria.where("id").is(country.getId()));
				Update update = new Update().set("name", country.getName()).set("enName", country.getEnName())
						.set("code", country.getCode());
				countryMongoDao.update(query, update);
			} else {
				// 新增
				Integer id = 0;
				Query query = new Query();
				query.with(Sort.by(Sort.Order.desc("id")));
				List<Country> list = countryMongoDao.findByQuery(query);
				if (list != null && list.size() > 0) {
					id = list.get(0).getId() + 1;
				}
				country.setId(id);
				setCountry(country, id);
				countryMongoDao.insert(country);
			}
		} catch (Exception e) {
			log.error("mongodb save error：" + e.getMessage(), e);
			response = new ItsResponse<String>().success();
		}
		return response;
	}

	private static void setCountry(Country country, Integer id) {
		country.setCreateDate(new Date());
		List<City> cityList = new ArrayList<>();
		City city = new City();
		city.setId(id);
		city.setName("深圳");
		cityList.add(city);
		country.setCitys(cityList);
	}

	/** 删除 */
	@RequestMapping("/delete")
	public @ResponseBody ItsResponse<String> delete(@RequestParam(value = "ids") String ids) {
		ItsResponse<String> response = new ItsResponse<String>().success();
		try {
			Query query = null;
			String empty = " ";
			if (ids != null && !empty.equals(ids)) {
				String[] idArr = ids.split(",");
				List<Integer> list = new ArrayList<>();
				for (String id : idArr) {
					list.add(Integer.parseInt(id));
				}
				query = new Query(Criteria.where("id").in(list));
			}
			countryMongoDao.remove(query);
		} catch (Exception e) {
			log.error("mongodb delete error：" + e.getMessage(), e);
			response = new ItsResponse<String>().success();
		}
		return response;
	}
}