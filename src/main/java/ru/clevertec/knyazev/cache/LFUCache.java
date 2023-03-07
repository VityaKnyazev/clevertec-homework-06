package ru.clevertec.knyazev.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Current class is a realization of LFU caching mechanism. Value is in cache
 * while quantity of using of current value is bigger than quantity of using of
 * others values. When frequency of using becomes the lowest, value will be
 * deleted from cache. will be deleted from cache.
 * 
 * For correct algorithm work in classes of types K,V that are used in LFUCache
 * must be overriding equals and hashcode methods.
 * 
 * @author Vitya Knyazev
 *
 * @param <K> key on which V value storing in cache.
 * @param <V> value that storing in cache.
 */
public class LFUCache<K, V> implements Cache<K, V> {
	private final Integer maxCacheSize;

	private Map<K, V> lfuCache;
	private Map<V, Integer> cacheElCounter;

	public LFUCache(Integer maxCacheSize) {
		this.maxCacheSize = maxCacheSize;

		lfuCache = new HashMap<>();
		cacheElCounter = new HashMap<>();
	}

	@Override
	public void put(K key, V value) {
		Integer lfuCacheSize = lfuCache.size();

		if (lfuCache.containsKey(key)) {
			V oldValue = lfuCache.get(key);

			if (!oldValue.equals(value)) {
				lfuCache.replace(key, value);
				replaceInCacheElCounter(oldValue, value);
			}
		} else {
			if (lfuCacheSize < maxCacheSize) {
				lfuCache.put(key, value);
				cacheElCounter.put(value, 0);
			} else {
				V removingValue = cacheElCounter.entrySet().stream()
						.sorted((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).findFirst().get().getKey();
				K removingKey = lfuCache.entrySet().stream().filter(entry -> entry.getValue().equals(removingValue))
						.findFirst().get().getKey();

				if (lfuCache.remove(removingKey, removingValue)) {
					cacheElCounter.remove(removingValue);
					lfuCache.put(key, value);
					cacheElCounter.put(value, 0);
				}
			}
		}
	}

	@Override
	public V get(K key) {
		V value = null;

		if (lfuCache.containsKey(key)) {
			value = lfuCache.get(key);
			increaseCacheElCounter(value);
		}

		return value;
	}

	@Override
	public void remove(K key) {
		if (lfuCache.containsKey(key)) {
			V removedValue = lfuCache.remove(key);
			cacheElCounter.remove(removedValue);
		}
	}

	private void replaceInCacheElCounter(V oldValue, V newValue) {
		if (cacheElCounter.containsKey(oldValue)) {
			cacheElCounter.remove(oldValue);
			cacheElCounter.put(newValue, 0);
		}
	}

	private void increaseCacheElCounter(V value) {
		Integer usageQuantity = cacheElCounter.get(value);
		usageQuantity++;
		cacheElCounter.replace(value, usageQuantity);
	}
}
