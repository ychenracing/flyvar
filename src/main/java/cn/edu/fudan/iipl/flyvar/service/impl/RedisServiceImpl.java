package cn.edu.fudan.iipl.flyvar.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import cn.edu.fudan.iipl.flyvar.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {

	private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	@Autowired
	private ShardedJedisPool shardedJedisPool;

	@Override
	public <T> T get(String key) {
		try (ShardedJedis shardedJedis = shardedJedisPool.getResource(); Jedis jedis = shardedJedis.getShard(key);) {
			byte[] values = jedis.get(key.getBytes());
			return getObjectFromBytes(values);
		}
		catch (Exception ex) {
			logger.error("获取jedis分片错误！", ex);
		}
		return null;
	}

	@Override
	public <T> void set(String key, T value) {
		try (ShardedJedis shardedJedis = shardedJedisPool.getResource(); Jedis jedis = shardedJedis.getShard(key);) {
			byte[] values = getBytesFromObject(value);
			jedis.set(key.getBytes(), values);
		}
		catch (Exception ex) {
			logger.error("获取jedis分片错误！", ex);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T getObjectFromBytes(byte[] bytes) {
		T result = null;
		try (ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
				ObjectInputStream in = new ObjectInputStream(bytesIn);) {
			result = (T) in.readObject();
		}
		catch (Exception ex) {
			logger.error("字节数组获取反序列化对象失败！", ex);
		}
		return result;
	}

	private <T> byte[] getBytesFromObject(T object) {
		try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bytesOut);) {
			out.writeObject(object);
			return bytesOut.toByteArray();
		}
		catch (Exception ex) {
			logger.error("序列化对象为字节数组失败！", ex);
		}
		return null;
	}

}
