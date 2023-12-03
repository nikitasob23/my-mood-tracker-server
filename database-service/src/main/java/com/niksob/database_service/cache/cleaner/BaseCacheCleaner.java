package com.niksob.database_service.cache.cleaner;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public abstract class BaseCacheCleaner implements CacheCleaner {

    protected abstract CacheManager getCacheManager();

    protected abstract String getCacheEntityName();

    @Override
    public void clearCache() {
        final Cache userInfoCache = getCacheManager().getCache(getCacheEntityName());
        if (userInfoCache != null) {
            userInfoCache.clear();
        }
    }
}
