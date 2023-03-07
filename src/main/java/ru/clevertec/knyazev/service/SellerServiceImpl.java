package ru.clevertec.knyazev.service;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.clevertec.knyazev.dao.SellerDAO;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.dao.proxy.SellerDAOProxy;
import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.service.exception.ServiceException;

@Service
public class SellerServiceImpl implements SellerService {
	private static final Logger logger = LoggerFactory.getLogger(SellerServiceImpl.class);
	private SellerDAO sellerDAOJPA;

	@Autowired
	public SellerServiceImpl(SellerDAO sellerDAOJPA) {
		SellerDAOProxy sellerDAOProxy = new SellerDAOProxy(sellerDAOJPA);
		this.sellerDAOJPA =  (SellerDAO) Proxy.newProxyInstance(SellerDAO.class.getClassLoader(),
				new Class[] { SellerDAO.class }, sellerDAOProxy);
	}

	@Transactional
	@Override
	public Optional<Seller> showSeller(Long id) {
		Seller seller = sellerDAOJPA.getSeller(id);
		return (seller != null) ? Optional.of(seller) : Optional.empty();
	}
	
	@Transactional
	@Override
	public List<Seller> showAllSellers() {
		return sellerDAOJPA.getAllSellers();
	}
	
	@Transactional(rollbackOn = {DAOException.class, ServiceException.class})
	@Override
	public void addSeller(Seller seller) throws ServiceException {
		try {
			sellerDAOJPA.saveSeller(seller);
		} catch (DAOException e) {
			logger.error("Error on adding seller:", e);
			throw new ServiceException("Error on adding seller! " + e.getMessage(), e);
		}
	}

	@Transactional(rollbackOn = {DAOException.class, ServiceException.class})
	@Override
	public void changeSeller(Seller seller) throws ServiceException {
		try {
			sellerDAOJPA.updateSeller(seller);
		} catch (DAOException e) {
			logger.error("Error on changing seller:", e);
			throw new ServiceException("Error on changing seller! " + e.getMessage(), e);
		}
	}

	@Transactional(rollbackOn = {DAOException.class, ServiceException.class})
	@Override
	public void removeSeller(Seller seller) throws ServiceException {
		try {
			sellerDAOJPA.deleteSeller(seller);
		} catch (DAOException e) {
			logger.error("Error on deleting seller:", e);
			throw new ServiceException("Error on deleting seller! " + e.getMessage(), e);
		}
	}

}
