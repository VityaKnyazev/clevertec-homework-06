package ru.clevertec.knyazev.cache;

import ru.clevertec.knyazev.util.YAMLParser;

public abstract class AbstractCacheFactory {

    private static final String PROPERTY_FILE = "application.yaml";

    protected String CACHE_ALGORITHM;
    protected Integer DEFAULT_CACHE_SIZE;

    public AbstractCacheFactory(YAMLParser yamlParser) {
        CACHE_ALGORITHM = yamlParser.getProperty("cache", "algorithm");
        DEFAULT_CACHE_SIZE = Integer.valueOf(
                yamlParser.getProperty("cache", "size"));
    }

    public abstract <K, V> Cache<K,V> initCache();
}
