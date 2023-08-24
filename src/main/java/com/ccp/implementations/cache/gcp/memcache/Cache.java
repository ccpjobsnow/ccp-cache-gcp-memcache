package com.ccp.implementations.cache.gcp.memcache;

import com.ccp.dependency.injection.CcpInstanceProvider;

public class Cache implements CcpInstanceProvider {

	@Override
	public Object getInstance() {
		return new MemCacheGcp();
	}

}
