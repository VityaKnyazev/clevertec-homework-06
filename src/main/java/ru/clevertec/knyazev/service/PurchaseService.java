package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.util.Map;

import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.view.Receipt;

public interface PurchaseService {
	Receipt buyPurchases(Map<ProductDTO, BigDecimal> purchases) throws ServiceException;
}