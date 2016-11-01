package cn.edu.fudan.iipl.flyvar.service;


public interface CacheService {

	/**
	 * 获取缓存值
	 */
	public <T> T get(String key);

	/**
	 * 设置缓存项
	 */
	public <T> boolean set(String key, T value);

	/**
	 * 设置带失效时间的缓存项
	 */
	public boolean set(String key, Object value, Long expireTime);

	/**
	 * 删除缓存项
	 */
	public void remove(String key);

	/**
	 * 批量删除缓存项
	 */
	public void remove(String... keys);

	/**
	 * 判断缓存中是否存在该缓存项
	 */
	public boolean exists(String key);

}
