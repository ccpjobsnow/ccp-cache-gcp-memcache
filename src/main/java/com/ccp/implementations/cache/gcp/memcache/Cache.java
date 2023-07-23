package com.ccp.implementations.cache.gcp.memcache;

import com.ccp.dependency.injection.CcpModuleExporter;

public class Cache implements CcpModuleExporter {

	@Override
	public Object export() {
		return new MemCacheGcp();
	}

}
