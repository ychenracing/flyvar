package cn.edu.fudan.iipl.flyvar.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.fudan.iipl.flyvar.dao.SampleNameDao;
import cn.edu.fudan.iipl.flyvar.model.constants.Constants;
import cn.edu.fudan.iipl.flyvar.service.CacheService;
import cn.edu.fudan.iipl.flyvar.service.SampleNameService;

import com.google.common.collect.Sets;

@Service
public class SampleNameServiceImpl implements SampleNameService {

	@Autowired
	private CacheService cacheService;

	@Autowired
	private SampleNameDao sampleNameDao;

	@Override
	public List<String> getSampleNames() {
		String key = Constants.CACHE_SAMPLE_NAME_LIST;
		List<String> result = cacheService.get(key);
		if (result == null) {
			result = sampleNameDao.getSampleNameList();
			cacheService.set(key, result);
		}
		return result;
	}

	@Override
	public Set<String> getSampleNameSet() {
		String key = Constants.CACHE_SAMPLE_NAME_SET;
		Set<String> result = cacheService.get(key);
		if (result == null) {
			result = Sets.newHashSet(sampleNameDao.getSampleNameList());
			cacheService.set(key, result);
		}
		return result;
	}

}
