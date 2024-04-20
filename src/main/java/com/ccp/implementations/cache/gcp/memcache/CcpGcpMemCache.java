package com.ccp.implementations.cache.gcp.memcache;

import com.ccp.dependency.injection.CcpInstanceProvider;
import com.ccp.especifications.cache.CcpCache;

public class CcpGcpMemCache implements CcpInstanceProvider<CcpCache> {

	
	public CcpCache getInstance() {
		return new GcpMemCache();
	}

}
