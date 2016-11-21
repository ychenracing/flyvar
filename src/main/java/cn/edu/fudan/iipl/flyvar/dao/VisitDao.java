/**
 * ychen. Copyright (c) 2016年10月24日.
 */
package cn.edu.fudan.iipl.flyvar.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import cn.edu.fudan.iipl.flyvar.model.VisitLog;
import cn.edu.fudan.iipl.flyvar.model.VisitTime;

/**
 * 
 * @author racing
 * @version $Id: MongoDao.java, v 0.1 2016年10月24日 下午10:19:39 racing Exp $
 */
@Repository
public class VisitDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public VisitTime getVisitTime() {
        return mongoTemplate.findOne(new Query(), VisitTime.class);
    }

    public VisitTime addVisitTime() {
        Query query = new Query();
        Update update = new Update();
        update.inc("time", 1);
        return mongoTemplate.findAndModify(query, update, VisitTime.class);
    }

    public List<VisitLog> getVisitLog() {
        return mongoTemplate.findAll(VisitLog.class);
    }

    public List<VisitLog> getVisitLogByIP(String ip) {
        return mongoTemplate.find(Query.query(Criteria.where("ip").is(ip)), VisitLog.class);
    }

    public void addVisitLog(VisitLog visitLog) {
        mongoTemplate.insert(visitLog);
    }

}
