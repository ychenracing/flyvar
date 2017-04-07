package cn.edu.fudan.iipl.flyvar.service;

public interface RedisService {
	
	<T> T get(String key);
	
	<T> void set(String key, T value);

}
