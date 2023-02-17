package ru.clevertec.knyazev.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ru.clevertec.knyazev.dao.CasherDAO;
import ru.clevertec.knyazev.dao.CasherDaoImpl;
import ru.clevertec.knyazev.dao.DiscountCardDAO;
import ru.clevertec.knyazev.dao.ShopDAO;
import ru.clevertec.knyazev.dao.StorageDAO;
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
import ru.clevertec.knyazev.service.discount.DiscountService;
import ru.clevertec.knyazev.view.AbstractReceiptBuilder;
import ru.clevertec.knyazev.view.ReceiptBuilderImpl;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {"ru.clevertec.knyazev.dao", "ru.clevertec.knyazev.rest.controller"})
public class AppConfig {

	@Bean
	PurchaseService purchaseServiceImpl(StorageService storageServiceImpl, CasherService casherServiceImpl, ShopService shopServiceImpl, AbstractReceiptBuilder receiptBuilderImpl) {
		return new PurchaseServiceImpl(storageServiceImpl, casherServiceImpl, shopServiceImpl, receiptBuilderImpl);
	}
	
	@Bean
	StorageService storageServiceImpl(StorageDAO storageDAOJPA) {
		return new StorageServiceImpl(storageDAOJPA);
	}
	
	@Bean
	CasherService casherServiceImpl(CasherDAO casherDAOImpl) {
		return new CasherServiceImpl(casherDAOImpl);
	}	
	
	@Bean
	ShopService shopServiceImpl(ShopDAO shopDAOJPA) {
		return new ShopServiceImpl(shopDAOJPA);
	}
	
	@Bean
	CasherDAO casherDAOImpl() {
		return new CasherDaoImpl();
	}	
	
	@Bean
	AbstractReceiptBuilder receiptBuilderImpl() {
		return new ReceiptBuilderImpl();		
	}
	
	@Bean
	DiscountService discountCardService(DiscountCardDAO discountCardDAOJPA) {
		return new DiscountCardService(discountCardDAOJPA);
	}
	
	@Bean
	DiscountService discountProductGroupService() {
		return new DiscountProductGroupService();
	}
	
}