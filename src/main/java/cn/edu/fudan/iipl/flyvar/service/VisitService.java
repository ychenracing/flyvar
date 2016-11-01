/**
 * ychen. Copyright (c) 2016年10月23日.
 */
package cn.edu.fudan.iipl.flyvar.service;

import cn.edu.fudan.iipl.flyvar.model.VisitLog;

/**
 * 访问记录服务类
 * @author racing
 * @version $Id: VisitService.java, v 0.1 2016年10月23日 下午3:18:42 racing Exp $
 */
public interface VisitService {

	/**
	 * 访问。增加访问记录和访问次数
	 */
	public void visit(VisitLog visitLog);

	/**
	 * 获取总访问次数
	 */
	public Long getTotalVisitTime();
}
