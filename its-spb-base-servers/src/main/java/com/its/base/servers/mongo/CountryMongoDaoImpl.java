package com.its.base.servers.mongo;

import com.its.base.servers.api.mongodb.domain.Country;
import com.its.common.mongodb.dao.impl.AbstractMongoBaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
  * Description: CountryMongoDaoImpl
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/14 17:51
  */
@Repository("countryMongoDaoImpl")
public class CountryMongoDaoImpl extends AbstractMongoBaseDaoImpl<Country> {

}
