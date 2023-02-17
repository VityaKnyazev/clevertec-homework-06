package ru.clevertec.knyazev;

import java.util.Set;

import ru.clevertec.knyazev.dao.AddressDAOImpl;
import ru.clevertec.knyazev.dao.CasherDaoImpl;
import ru.clevertec.knyazev.dao.DiscountCardDAOImpl;
import ru.clevertec.knyazev.dao.ProductDAOImpl;
import ru.clevertec.knyazev.dao.ShopDAOImpl;
import ru.clevertec.knyazev.dao.StorageDAOImpl;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.service.CasherService;
import ru.clevertec.knyazev.service.CasherServiceImpl;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.PurchaseServiceImpl;
import ru.clevertec.knyazev.service.ShopService;
import ru.clevertec.knyazev.service.ShopServiceImpl;
import ru.clevertec.knyazev.service.StorageService;
import ru.clevertec.knyazev.service.StorageServiceImpl;
import ru.clevertec.knyazev.service.discount.DiscountCardService;
import ru.clevertec.knyazev.service.discount.DiscountProductGroupService;
import ru.clevertec.knyazev.service.discount.DiscountServiceComposite;
import ru.clevertec.knyazev.view.AbstractReceiptBuilder;
import ru.clevertec.knyazev.view.ReceiptBuilderImpl;

public class ContextInitializer {
	
	private ContextInitializer() {}
	
	public static PurchaseService createProductContext() {
		StorageService storageService = new StorageServiceImpl(new StorageDAOImpl(new ProductDAOImpl()));
		CasherService casherService = new CasherServiceImpl(new CasherDaoImpl());
		ShopService shopService = new ShopServiceImpl(new ShopDAOImpl(new AddressDAOImpl()));
		
		AbstractReceiptBuilder consoleReceiptBuilder = new ReceiptBuilderImpl();
				
		PurchaseService purchaseService = new PurchaseServiceImpl(storageService, casherService, shopService, consoleReceiptBuilder);
		
		return purchaseService;
	}
	
	public static void createDiscountContext(Set<DiscountCardDTO> discountCardsDTO) {
		DiscountCardService discountCardService = new DiscountCardService(new DiscountCardDAOImpl());
		discountCardService.setDiscountCardsDTO(discountCardsDTO);
		
		DiscountProductGroupService discountProductGroupService = new DiscountProductGroupService();
		
		DiscountServiceComposite discountServiceComposite = DiscountServiceComposite.getInstance();
		discountServiceComposite.addDiscountService(discountCardService);
		discountServiceComposite.addDiscountService(discountProductGroupService);
	}
}
