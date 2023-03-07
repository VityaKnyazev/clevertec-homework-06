package ru.clevertec.knyazev.dao.proxy;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import ru.clevertec.knyazev.cache.Cache;
import ru.clevertec.knyazev.cache.SimpleCacheFactory;
import ru.clevertec.knyazev.dao.SellerDAO;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.util.Settings;


public class SellerDAOProxy implements InvocationHandler {	
	private SellerDAO sellerDAO;
	private Cache<Long, Seller> cache;
	
	public SellerDAOProxy(SellerDAO sellerDAO) {
		this.sellerDAO = sellerDAO;
		
		Yaml yaml = new Yaml();
		InputStream cacheFileSettingsStream = this.getClass().getClassLoader().getResourceAsStream(Settings.CACHE_FILE_SETTINGS);
		Map<String, Object> yamlProperties =  yaml.load(cacheFileSettingsStream);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> cacheProperties = (Map<String, Object>) yamlProperties.get(Settings.CACHE_OBJECT);
		String algorithm = (String) cacheProperties.get(Settings.CACHE_ALGORITHM);
		Integer cacheSize = (Integer) cacheProperties.get(Settings.CACHE_MAX_SIZE);
		cache = new SimpleCacheFactory<Long, Seller>().initCache(algorithm, cacheSize);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		
		if (methodName.equals("getSeller")) {
			return whenGetSeller(args);
		} else if (methodName.equals("getAllSellers")) {
			return method.invoke(sellerDAO, args);
		}
		
		if (methodName.equals("saveSeller")) {
			whenSaveSeller(args);
		} else if (methodName.equals("updateSeller")) {
			whenUpdateSeller(args);
		} else if(methodName.equals("deleteSeller")) {
			whenDeleteSeller(args);
		}
		
		return null;
	}
	
	private Seller whenGetSeller(Object[] args) {
		Seller seller = null;
		Long id = (Long) args[0];
		
		Seller cacheSeller = cache.get(id);
		
		if (cacheSeller != null) {
			seller = cacheSeller;
		} else {
			seller = sellerDAO.getSeller(id);
			if (seller != null) {
				cache.put(id, seller);
			}			
		}
		
		return seller;		
	}
	
	private void whenSaveSeller(Object[] args) throws DAOException {		
		Seller savingSeller = (Seller) args[0];
		Long savingSellerId = savingSeller.getId();
		
		sellerDAO.saveSeller(savingSeller);
		cache.put(savingSellerId, savingSeller);
	}
	
	private void whenUpdateSeller(Object[] args) throws DAOException {		
		Seller updatingSeller = (Seller) args[0];
		Long updatingSellererId = updatingSeller.getId();
		
		sellerDAO.updateSeller(updatingSeller);
		cache.put(updatingSellererId, updatingSeller);
	}
	
	private void whenDeleteSeller(Object[] args) throws DAOException {
		Seller deletingSeller = (Seller) args[0];
		Long deletingSellerId = deletingSeller.getId();
		
		sellerDAO.deleteSeller(deletingSeller);
		cache.remove(deletingSellerId);
	}
}
