package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.entity.Seller;

@Repository
public class SellerDAOJPA implements SellerDAO {
	private static final Logger logger = LoggerFactory.getLogger(SellerDAOJPA.class);
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Seller getSeller(Long id) {
		Seller seller = null;

		if (id != null && id > 0L) {
			seller = entityManager.find(Seller.class, id);
		}

		return seller;
	}

	@Override
	public List<Seller> getAllSellers() {
		List<Seller> sellers = new ArrayList<>();

		sellers = entityManager.createQuery("SELECT s FROM Seller s", Seller.class).getResultList();
		return sellers;
	}

	@Override
	public void saveSeller(Seller seller) throws DAOException {

		if (seller != null && seller.getId() == null) {
			try {
				entityManager.persist(seller);
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error("Error on saving seller entity:", e);
				throw new DAOException("Error on saving seller entity. " + e.getMessage(), e);
			}
		} else {
			logger.error("Error on saving. Giving seller is null or has not null id.");
			throw new DAOException("Error on saving seller entity.");
		}

	}

	@Override
	public void updateSeller(Seller seller) throws DAOException {

		if (seller != null && seller.getId() != null && seller.getId() > 0L) {
			try {
				Seller dbSeller = entityManager.find(Seller.class, seller.getId());
				if (dbSeller != null) {
					dbSeller.setName(seller.getName());
					dbSeller.setFamilyName(seller.getFamilyName());
					dbSeller.setEmail(seller.getEmail());
					dbSeller.setRole(seller.getRole());
					entityManager.flush();
				} else {
					throw new DAOException("Error on updating seller entity. Seller entity not exists with given id=" + seller.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error("Error on updating seller entity:", e);
				throw new DAOException("Error on updating seller entity. " + e.getMessage(), e);
			}
		} else {
			logger.error("Error on updating. Giving seller is null or has null or bad id.");
			throw new DAOException("Error on updating seller entity.");
		}

	}

	@Override
	public void deleteSeller(Seller seller) throws DAOException {

		if (seller != null && seller.getId() != null && seller.getId() > 0L) {
			try {
				Seller dbSeller = entityManager.find(Seller.class, seller.getId());
				if (dbSeller != null) {
					entityManager.remove(dbSeller);
					entityManager.flush();
				} else {
					throw new DAOException("Error on deleting seller entity. Seller entity not exists with given id=" + seller.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error("Error on deleting seller entity:", e);
				throw new DAOException("Error on deleting seller entity. " + e.getMessage(), e);
			}
		} else {
			logger.error("Error on deleting. Giving seller is null or has null or bad id.");
			throw new DAOException("Error on deleting seller entity.");
		}

	}

}
