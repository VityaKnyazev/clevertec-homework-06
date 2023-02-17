package ru.clevertec.knyazev.service;

import ru.clevertec.knyazev.dao.ShopDAO;
import ru.clevertec.knyazev.entity.Shop;

public class ShopServiceImpl implements ShopService {
	private ShopDAO shopDAO;
	
	public ShopServiceImpl(ShopDAO shopDAO) {
		this.shopDAO = shopDAO;
	}

	@Override
	public Shop showShop() {
		return shopDAO.findById(1L);
	}

}
