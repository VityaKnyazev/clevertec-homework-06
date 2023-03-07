package ru.clevertec.knyazev.cache;

import java.util.Locale;

public class SimpleCacheFactory<K, V> {

	public Cache<K, V> initCache(String cacheAlgorithm, Integer cacheSize) {
		CacheAlgorithm algorithm = CacheAlgorithm.valueOf(cacheAlgorithm.toUpperCase(Locale.getDefault()));

		return switch (algorithm) {
		case LFU -> new LFUCache<>(cacheSize);
		case LRU -> new LRUCache<>(cacheSize);
		default -> null;
		};
	}

	public enum CacheAlgorithm {
		LRU, LFU
	}
}
