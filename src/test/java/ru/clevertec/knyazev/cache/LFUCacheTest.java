package ru.clevertec.knyazev.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LFUCacheTest {
	private LFUCache<Long, String> lfuCache;
	
	@BeforeEach
	public void setUp() {
		lfuCache = new LFUCache<>(3);
	}
	
	@Test
	public void checkPutShouldAddToCache() {		
		assertThatCode(() -> lfuCache.put(1L, "First cashed element")).doesNotThrowAnyException();
	}
	
	@Test
	public void checkPutShouldReplaceValueOnExistingKey() {	
		lfuCache.put(1L, "First cashed element");
		lfuCache.put(2L, "Second cashed element");
		lfuCache.put(3L, "Third cashed element");
		assertThatCode(() -> lfuCache.put(2L, "Change Second cashed element")).doesNotThrowAnyException();
	}
	
	@Test
	public void checkPutShouldAddValueOnMaxHashSize() {	
		lfuCache.put(1L, "First cashed element");
		lfuCache.put(2L, "Second cashed element");
		lfuCache.put(3L, "Third cashed element");
		assertThatCode(() -> lfuCache.put(4L, "Four cashed element")).doesNotThrowAnyException();
	}
	
	@Test
	public void checkGetShouldReturnValueOnKey() {
		lfuCache.put(1L, "First cashed element");
		lfuCache.put(2L, "Second cashed element");
		lfuCache.put(3L, "Third cashed element");
		
		Long inputKey = 2L;
		
		String expectedValue = "Second cashed element";
		String actualValue = lfuCache.get(inputKey);
		
		assertThat(actualValue).isEqualTo(expectedValue);
	}
	
	@Test
	public void checkGetShouldReturnNullOnNonExistingKey() {
		lfuCache.put(1L, "First cashed element");
		lfuCache.put(2L, "Second cashed element");
		lfuCache.put(3L, "Third cashed element");
		
		Long inputKey = 5L;
		
		String actualValue = lfuCache.get(inputKey);
		
		assertThat(actualValue).isNull();
	}
	
	@Test
	public void checkRemoveShouldRemoveFromeCache() {
		lfuCache.put(1L, "First cashed element");
		lfuCache.put(2L, "Second cashed element");
		lfuCache.put(3L, "Third cashed element");
		
		assertThatCode(() -> lfuCache.remove(3L)).doesNotThrowAnyException();
	}
}
