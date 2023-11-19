package ru.clevertec.knyazev.cache.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.knyazev.cache.AbstractCacheFactory;
import ru.clevertec.knyazev.cache.Cache;
import ru.clevertec.knyazev.util.YAMLParser;

import java.util.Locale;

public class DefaultCacheFactory extends AbstractCacheFactory {

    private static final String INSTANTIATION_CACHE_ERROR = "Error when instantiating cache";

    public DefaultCacheFactory(YAMLParser yamlParser) {
        super(yamlParser);
    }

    @Override
    public <K, V> Cache<K, V> initCache() {
        CacheAlgorithm algorithm = CacheAlgorithm.valueOf(CACHE_ALGORITHM.toUpperCase(Locale.getDefault()));

        return switch (algorithm) {
            case LFU -> new LFUCache<>(DEFAULT_CACHE_SIZE);
            case LRU -> new LRUCache<>(DEFAULT_CACHE_SIZE);
            default -> throw new IllegalArgumentException(INSTANTIATION_CACHE_ERROR);
        };
    }

    private enum CacheAlgorithm {
        LRU, LFU
    }
}
