package cn.edu.fudan.iipl.flyvar.service;

import java.util.List;
import java.util.Set;

public interface SampleNameService {
	
	/**
	 * 获取sample name列表
	 */
	public List<String> getSampleNames();
	
	/**
	 * 获取sample name的Set
	 */
	public Set<String> getSampleNameSet();

}
