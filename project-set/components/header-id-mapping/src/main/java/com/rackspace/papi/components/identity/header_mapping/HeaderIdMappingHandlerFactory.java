package com.rackspace.papi.components.identity.header_mapping;

import com.rackspace.papi.commons.config.manager.UpdateListener;
import com.rackspace.papi.components.identity.header_mapping.config.HeaderIdMappingConfig;
import com.rackspace.papi.components.identity.header_mapping.config.HttpHeader;
import com.rackspace.papi.filter.logic.AbstractConfiguredFilterHandlerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderIdMappingHandlerFactory extends AbstractConfiguredFilterHandlerFactory<HeaderIdMappingHandler> {

    private List<HttpHeader> sourceHeaders;

    public HeaderIdMappingHandlerFactory() {
        sourceHeaders = new ArrayList<HttpHeader>();
    }

    @Override
    protected Map<Class, UpdateListener<?>> getListeners() {

        return new HashMap<Class, UpdateListener<?>>() {
            {
                put(HeaderIdMappingConfig.class, new HeaderIdMappingConfigurationListener());
            }
        };
    }

    private class HeaderIdMappingConfigurationListener implements UpdateListener<HeaderIdMappingConfig> {

        private boolean isInitialized = false;

        @Override
        public void configurationUpdated(HeaderIdMappingConfig configurationObject) {

            sourceHeaders = configurationObject.getSourceHeaders().getHeader();
            isInitialized = true;
        }

        @Override
        public boolean isInitialized() {
            return isInitialized;
        }
    }

    @Override
    protected HeaderIdMappingHandler buildHandler() {
        if (!this.isInitialized()) {
            return null;
        }
        return new HeaderIdMappingHandler(sourceHeaders);
    }
}
