package com.ccp.implementations.cache.gcp.memcache;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.cache.CcpCache;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

class GcpMemCache implements CcpCache {
	
	private static MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

	@SuppressWarnings("unchecked")
	public Object get(String key) {

		Object object = memcacheService.get(key);

		boolean isNotMap = object instanceof Map == false;

		if (isNotMap) {
			return object;
		}

		Map<String, Object> map = (Map<String, Object>) object;

		CcpJsonRepresentation jr = new CcpJsonRepresentation(map);
		return jr;
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
