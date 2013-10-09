package com.mnaleshterav3.dao;

import java.sql.Blob;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnaleshterav3.controller.DocumentController;
import com.mnaleshterav3.model.Document;
import com.mnaleshterav3.model.Owner;


 
@Repository
public class DocumentDAO {
	DocumentController dco =new DocumentController();
     
    @Autowired
    private SessionFactory sessionFactory;     
    public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
    public void save(Document document) {
		Session session = sessionFactory.getCurrentSession();
		Blob blob=Hibernate.getLobCreator(session).createBlob(DocumentController.in,DocumentController.fs);
		
		
        document.setContent(blob);
      
        //Hibernate.getLobCreator(session.createBlob(dco.in,dco.fs));*/
        

        session.save(document);
    }

	@SuppressWarnings("unchecked")
	@Transactional
    public List<Document> list() {
        Session session = sessionFactory.getCurrentSession();
        List<Document> documents = null;
        
        try {
            documents = (List<Document>)session.createQuery("from Document").list();
 
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return documents;
    }
     
    @Transactional
    public Document get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (Document)session.get(Document.class, id);
    }
    @Transactional
    public void addOwner(Owner owner){
    	Session session=sessionFactory.getCurrentSession();
    	session.save(owner);
    }
 
    @Transactional
    public void remove(Integer id) {
        Session session = sessionFactory.getCurrentSession();
         
        Document document = (Document)session.get(Document.class, id);
         
        session.delete(document);
    }
}