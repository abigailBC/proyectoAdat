package Modelo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateManager extends Manager{
	HashMap<String, Libro>libros = new HashMap<>();

	@Override
	public HashMap<String, Libro> recorrer() {
		//clase que se especializa en crear una session.
		try {
			SessionFactory sf = new Configuration().configure().buildSessionFactory();

	        Session s=sf.openSession();
	        
	    	TypedQuery<Libro> q=s.createQuery("from Libro", Libro.class);
	    	List <Libro> results = q.getResultList();
	        Iterator librosIterator= results.iterator();
	        while (librosIterator.hasNext()){
	            Libro libro = (Libro)librosIterator.next();
	            libros.put(libro.getId(), libro);            
	        }
	        s.close();
		}catch(Exception e) {
			System.out.println("Error al leer con Hibernate: "+e.getMessage());

		}
        
		return libros;
    }

	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session s=sf.openSession();
        try {
			for(Map.Entry<String, Libro>entry : libros.entrySet()) {
				Libro libro=entry.getValue();
		        s.beginTransaction();
				s.saveOrUpdate(libro);
			    s.getTransaction().commit();
			}
	        s.close();
	        
        }catch(Exception e) {
			System.err.println("Error al sobrescribir los datos con Hibernate: "+e.getMessage());
        }
		
	}

}