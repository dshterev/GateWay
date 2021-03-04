package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JPAController {

	@Autowired
	private EntityManagerFactory emf;

	public JPAController() {
	}

	public final synchronized EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public void add(Object obj) throws Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();
			em.refresh(obj);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void update(Object obj) throws Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			obj = em.merge(obj);
			em.getTransaction().commit();
			em.refresh(obj);			
		} catch (Exception ex) {			
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void remove(Long id, Object objClass) throws Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			Object obj;
			try {
				obj = em.getReference(objClass.getClass(), id);
				assert (obj != null);
			} catch (EntityNotFoundException ex) {
				throw ex;
			}
			em.remove(obj);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public Object findById(Class objClass, Long id) {
		EntityManager em = getEntityManager();
		try {
			return em.find(objClass, id);
		} finally {
			em.close();
		}
	}
	
	public Object findById(Class objClass, String id) {
		EntityManager em = getEntityManager();
		try {
			return em.find(objClass, id);
		} finally {
			em.close();
		}
	}

	public Object executeSingleResultNamedQuery(Class objClass, String queryName, Map<String, Object> paramMap,
			EntityManager em) {
		Query q = em.createNamedQuery(queryName, objClass);
		for (String key : paramMap.keySet()) {
			q.setParameter(key, paramMap.get(key));
		}
		q.setMaxResults(1);
		Object result = null;
		try {
		    result = q.getSingleResult();
		} catch (NoResultException e) {
		    System.out.println("No result found for... ");
		}
		return result;
	}
	
	public List executeNamedQuery(Class objClass, String queryName, Map<String, Object> paramMap, EntityManager em) {
		Query q = em.createNamedQuery(queryName, objClass);
		for (String key : paramMap.keySet()) {
			q.setParameter(key, paramMap.get(key));
		}
		try {
			return q.getResultList();
		} catch (NoResultException e) {
			throw new NoResultException("No result found");
		} 
	}
}




