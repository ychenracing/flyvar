package cn.edu.fudan.iipl.flyvar.service.impl;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import cn.edu.fudan.iipl.flyvar.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {

	private static final Logger logger = LoggerFactory
			.getLogger(CacheServiceImpl.class);

	@Autowired
	private RedisTemplate<Serializable, Object> redisTemplate;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		ValueOperations<Serializable, Object> operations = redisTemplate
				.opsForValue();
		Object result = operations.get(key);
		return (T) result;
	}

	@Override
	public <T> boolean set(String key, T value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			logger.error("缓存设置失败, key=" + key + ", value" + value, e);
		}
		return result;
	}

	@Override
	public void remove(String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	@Override
	public void remove(String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	@Override
	public boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}

	@Override
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
