package com.ccp.implementations.cache;

import com.ccp.dependency.injection.CcpImplementationProvider;

public class ImplementationProvider implements CcpImplementationProvider {

	@Override
	public Object getImplementation() {
		return new MemCacheGcp();
	}

}
