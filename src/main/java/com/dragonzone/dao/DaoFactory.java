package com.dragonzone.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class DaoFactory {

	private static final int MAX_RESULT_SIZE = 128;

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
	public <T> List<T> list(Class<T> classOfT,
			List<? extends Criterion> criteriaList, int maxResults) {
		if (null == classOfT) {
			throw new IllegalArgumentException("classOfT");
		}
		if (maxResults < 1) {
			throw new IllegalArgumentException("maxResults must be >= 1");
		}
		Session session = getSession();

		org.hibernate.Criteria criteria = session.createCriteria(classOfT);
		criteria.setMaxResults(Math.min(maxResults, MAX_RESULT_SIZE));

		if (null != criteriaList) {
			for (Criterion criterion : criteriaList) {
				criteria.add(criterion);
			}
		}

		@SuppressWarnings("unchecked")
		List<T> list = criteria.list();

		return list;
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
	public <T> T findById(Class<T> classOfT, long id) {
		@SuppressWarnings("unchecked")
		T result = (T) getSession().get(classOfT, id);
		return result;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public <T> T write(T instance) {
		@SuppressWarnings("unchecked")
		T savedInstance = (T) getSession().merge(instance);
		return savedInstance;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public <T> void delete(T instance) {
		getSession().delete(instance);
	}
}
