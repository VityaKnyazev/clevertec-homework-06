package ru.clevertec.knyazev.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LRUCacheTest {
	private LRUCache<Long, String> lruCache;
	
	@BeforeEach
	public void setUp() {
		lruCache = new LRUCache<>(3);
	}
	
	@Test
	public void checkPutShouldAddToCache() {		
		assertThatCode(() -> lruCache.put(1L, "First cashed element")).doesNotThrowAnyException();
	}
	
	@Test
	public void checkPutShouldReplaceValueOnExistingKey() {	
		lruCache.put(1L, "First cashed element");
		lruCache.put(2L, "Second cashed element");
		assertThatCode(() -> lruCache.put(2L, "Change Second cashed element")).doesNotThrowAnyException();
	}
	
	@Test
	public void checkPutShouldAddValueOnMaxHashSize() {	
		lruCache.put(1L, "First cashed element");
		lruCache.put(2L, "Second cashed element");
		lruCache.put(3L, "Third cashed element");
		assertThatCode(() -> lruCache.put(4L, "Four cashed element")).doesNotThrowAnyException();
	}
	
	@Test
	public void checkGetShouldReturnValueOnKey() {
		lruCache.put(1L, "First cashed element");
		lruCache.put(2L, "Second cashed element");
		lruCache.put(3L, "Third cashed element");
		
		Long inputKey = 2L;
		
		String expectedValue = "Second cashed element";
		String actualValue = lruCache.get(inputKey);
		
		assertThat(actualValue).isEqualTo(expectedValue);
	}
	
	@Test
	public void checkGetShouldReturnNullOnNonExistingKey() {
		lruCache.put(1L, "First cashed element");
		lruCache.put(2L, "Second cashed element");
		lruCache.put(3L, "Third cashed element");
		
		Long inputKey = 5L;
		
		String actualValue = lruCache.get(inputKey);
		
		assertThat(actualValue).isNull();
	}
	
	@Test
	public void checkRemoveShouldRemoveFromeCache() {
		lruCache.put(1L, "First cashed element");
		lruCache.put(2L, "Second cashed element");
		lruCache.put(3L, "Third cashed element");
		
		assertThatCode(() -> lruCache.remove(3L)).doesNotThrowAnyException();
	}
	
}
