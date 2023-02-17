package ru.clevertec.knyazev.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.Product;

@Repository
public class ProductDAOJPA implements ProductDAO {
	@PersistenceContext
	EntityManager entityManager;
	
	public ProductDAOJPA() {}

	public ProductDAOJPA(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Product getProductById(Long id) {
		Product product = null;
		
		if (id != null && id > 0L) {
			product = entityManager.find(Product.class, id);
		}
		
		return product;
	}

	@Override
	public Product saveProduct(Product product) {
		if (product.getId() == null) {
			entityManager.persist(product);
			return product;
		}
		
		return null;
	}

}
