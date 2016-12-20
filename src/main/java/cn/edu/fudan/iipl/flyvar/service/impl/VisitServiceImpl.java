/**
 * ychen. Copyright (c) 2016年10月23日.
 */
package cn.edu.fudan.iipl.flyvar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.fudan.iipl.flyvar.dao.VisitDao;
import cn.edu.fudan.iipl.flyvar.model.VisitLog;
import cn.edu.fudan.iipl.flyvar.service.VisitService;

/**
 * 
 * @author racing
 * @version $Id: VisitService.java, v 0.1 2016年10月23日 下午3:18:42 racing Exp $
 */
@Service
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitDao            visitDao;

    @Override
    public void visit(VisitLog visitLog) {
        visitDao.addVisitLog(visitLog);
        visitDao.addVisitTime();

        // int colNum = visitDao.addVisitItem(visitLog);
        // if (colNum == 0) {
        // LOGGER.warn("插入访问记录失败！");
        // }
        //
        // colNum = visitDao.addVisitOneTime();
        // if (colNum == 0) {
        // LOGGER.warn("插入访问次数失败！");
        // }
    }

    @Override
    public Long getTotalVisitTime() {
        return visitDao.getVisitTime().getTime();
        // return visitDao.getTotalVisitTime();
    }
}
