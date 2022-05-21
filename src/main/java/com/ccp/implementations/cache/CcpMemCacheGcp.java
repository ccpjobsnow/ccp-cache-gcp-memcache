package com.ccp.implementations.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpImplementation;
import com.ccp.especifications.cache.CcpCache;
import com.ccp.especifications.cache.CcpTaskCache;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@CcpImplementation
public class CcpMemCacheGcp implements CcpCache {
	private static MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

	@SuppressWarnings("unchecked")
	private Object get(String key) {

		Object object = memcacheService.get(key);

		boolean isNotMap = object instanceof Map == false;

		if (isNotMap) {
			return object;
		}

		Map<String, Object> map = (Map<String, Object>) object;

		CcpMapDecorator jr = new CcpMapDecorator(map);
		return jr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V get(String key, CcpTaskCache<V> taskToGetValue, int cacheSeconds) {

		Object object = this.get(key);

		if (object != null) {
			return (V) object;
		}

		CcpMapDecorator values = new CcpMapDecorator(new CcpMapDecorator());// TODO ???
		V value = taskToGetValue.getValue(values);
		this.put(key, value, cacheSeconds);

		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getOrDefault(String key, V defaultValue) {
		Object object = this.get(key);
		
		if(object == null) {
			return defaultValue;
		}
		return (V) object;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getOrThrowException(String key, RuntimeException e) {
		Object object = this.get(key);
		
		if(object == null) {
			throw e;
		}
		
		return (V) object;
	}

	@Override
	public boolean isPresent(String key) {
		boolean isPresent = this.get(key) != null;
		return isPresent;
	}

	@Override
	public void put(String key, Object value, int secondsDelay) {
		Expiration arg2 = Expiration.byDeltaSeconds(secondsDelay);
		if(value instanceof CcpMapDecorator) {
			CcpMapDecorator jr = (CcpMapDecorator)value;
			value = new LinkedHashMap<>(jr.content);
		}
		memcacheService.put(key, value, arg2);

	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V remove(String key) {
		
		V t = (V) this.get(key);
		
		memcacheService.delete(key);
		
		return t;
	}

}
