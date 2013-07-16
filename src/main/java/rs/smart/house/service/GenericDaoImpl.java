package rs.smart.house.service;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.id.IdentityGenerator.GetGeneratedKeysDelegate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.smart.house.domain.AbstractEntity;


@Service("genericDao")
@Transactional
public class GenericDaoImpl<T extends AbstractEntity> {

	
	//protected static Logger logger = Logger.getLogger(GenericDaoImpl.class);
	protected static Logger logger = Logger.getLogger("controller");
	
	/**
	 * INFO ! OVO JE HIBERNATE SESIJA, NE SESSION FACTORY! Nije preporucivo
	 * koristiti depricated factoty metode kao reconect nad ovim vec se pozove
	 * hibernate.getSessionFactory() pa nad tim nesto vec ...
	
	 */
	private Session hibernate = null;
	
	
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
		
	private void refreshHibernateSession(){
//		if (hibernate == null){
//			hibernate = sessionFactory.getCurrentSession();
//			logger.info("Inicijalizovana hibernate sesija... " );
//		}else{
//			logger.debug("Hibernate sesija vec inicijalizovana");
//		}
		
		hibernate = sessionFactory.getCurrentSession();
		//logger.info("Nova Hibernate sesija!");
		if (hibernate.isConnected() == false) {
			//logger.info("Hibernate nije bio konektovan, pozvan reconnect()");
			hibernate.reconnect();
		}
	}
	
	
	public void persist(T obj) {
		refreshHibernateSession();
		hibernate.persist(obj);
	}


	public List<T> findByExample(T obj, Class klasa) {
		refreshHibernateSession();
		
		Criteria criteria = hibernate.createCriteria(klasa);
		Example example = Example.create(obj);
		example.excludeZeroes();
		example.ignoreCase();
		example.enableLike(MatchMode.ANYWHERE);
		criteria.add(example);
		return criteria.list();
	}

	
	public T saveOrUpdate(T obj) {

		refreshHibernateSession();
		hibernate.saveOrUpdate(obj);
		hibernate.flush();
		
		return findByExample(obj, obj.getClass()).get(0);

	}


	public T save(T obj) {
		return saveOrUpdate(obj);
	}

	public List<T> loadAll(Class klasa) {
		return loadAllActive(klasa);
	}

	
	public List<T> loadAllActive(Class klasa) {
		
		refreshHibernateSession();
		
		List<T> listaGenerika;
		
		listaGenerika = (List<T>) hibernate.createCriteria(klasa).add(Restrictions.isNotNull("id")).list();
		return listaGenerika;
	}

	
	public List<T> loadByStatus(Class klasa, short statusSh) {
		refreshHibernateSession();
		
		List<T> listaGenerika;
		
		listaGenerika = (List<T>) hibernate.createCriteria(klasa).add(Restrictions.isNotNull("id")).list();
		return listaGenerika;
	}

	/**
	 * Metoda pretrazuje podatke u tabeli Prosledjujemo ime kolone i vrednost po
	 * kojoj vrsimo restrikciju
	 * 
	 * @param klasa
	 *            - Class entiteta
	 * @param imeKolone
	 * @param kriterijum
	 *            - Object vrednost, tip mora da se poklopi sa kolonom u bazi
	 * @return Lista koje??ega
	 */
	public List<T> loadByColumnRestriction(Class klasa, String imeKolone, Object kriterijum) {
		refreshHibernateSession();
		
		List<T> listaGenerika;
		listaGenerika = (List<T>) hibernate.createCriteria(klasa).add(Restrictions.eq(imeKolone, kriterijum)).list();
		return listaGenerika;
	}

	public List<T> loadAllRowsFromTable(Class klasa) {
		refreshHibernateSession();
		return (List<T>) hibernate.createCriteria(klasa).list();

	}

	public T delete(Long idOfObj, Class klasa) {
		refreshHibernateSession();
		AbstractEntity tmpEntity = (AbstractEntity) hibernate.createCriteria(klasa).add(Restrictions.eq("id", idOfObj)).list().get(0);
		hibernate.delete((T) tmpEntity);
		return (T) tmpEntity;
	}


	public T delete(T obj) {
		refreshHibernateSession();
		AbstractEntity tmpEntity = (AbstractEntity) obj;
		hibernate.delete((T) tmpEntity);
		return (T) tmpEntity;
	}

	public void flush() {
		refreshHibernateSession();
		hibernate.flush();
	}

	public T loadById(Class klasa, long id) {
		refreshHibernateSession();
		return (T) hibernate.createCriteria(klasa).add(Restrictions.eq("id", id)).uniqueResult();
	}

	public T merge(T obj) {
		refreshHibernateSession();
		return (T) hibernate.merge(obj);
	}
	
	public Session getHibernateSession() {
		refreshHibernateSession();
		return hibernate;
	}
	
	public Query executeQuery(String query){
		refreshHibernateSession();
		return hibernate.createQuery(query);
	}
	
	public List executeQueryAndReturnList(String query){
		return executeQuery(query).list();
//		return hibernate.createQuery(query);
	}
	

}
