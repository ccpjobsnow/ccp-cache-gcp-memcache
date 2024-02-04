package com.ccp.implementations.cache.gcp.memcache;

import com.ccp.dependency.injection.CcpInstanceProvider;

public class CcpGcpMemCache implements CcpInstanceProvider {

	
	public Object getInstance() {
		return new GcpMemCache();
	}

}
