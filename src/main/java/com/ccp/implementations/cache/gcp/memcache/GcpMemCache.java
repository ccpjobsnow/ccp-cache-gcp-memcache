package com.ccp.implementations.cache.gcp.memcache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.cache.CcpCache;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

class GcpMemCache implements CcpCache {
	private static MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

	@SuppressWarnings("unchecked")
	private Object get(String key) {

		Object object = memcacheService.get(key);

		boolean isNotMap = object instanceof Map == false;

		if (isNotMap) {
			return object;
		}

		Map<String, Object> map = (Map<String, Object>) object;

		CcpJsonRepresentation jr = new CcpJsonRepresentation(map);
		return jr;
	}

	@SuppressWarnings("unchecked")
	public <V> V get(String key, CcpJsonRepresentation json, Function<CcpJsonRepresentation, V> taskToGetValue, int cacheSeconds) {

		Object object = this.get(key);

		if (object != null) {
			return (V) object;
		}
		V value = taskToGetValue.apply(json);
		this.put(key, value, cacheSeconds);

		return value;
	}

	@SuppressWarnings("unchecked")
	
	public <V> V getOrDefault(String key, V defaultValue) {
		Object object = this.get(key);
		
		if(object == null) {
			return defaultValue;
		}
		return (V) object;
		
	}

	@SuppressWarnings("unchecked")
	
	public <V> V getOrThrowException(String key, RuntimeException e) {
		Object object = this.get(key);
		
		if(object == null) {
			throw e;
		}
		
		return (V) object;
	}

	
	public boolean isPresent(String key) {
		boolean isPresent = this.get(key) != null;
		return isPresent;
	}

	
	public void put(String key, Object value, int secondsDelay) {
		Expiration arg2 = Expiration.byDeltaSeconds(secondsDelay);
		if(value instanceof CcpJsonRepresentation) {
			CcpJsonRepresentation jr = (CcpJsonRepresentation)value;
			value = new LinkedHashMap<>(jr.content);
		}
		memcacheService.put(key, value, arg2);

	}

	@SuppressWarnings("unchecked")
	
	public <V> V delete(String key) {
		
		V t = (V) this.get(key);
		
		memcacheService.delete(key);
		
		return t;
	}

}
