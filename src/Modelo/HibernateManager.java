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
	HashMap <String, Libro> libros = new HashMap<>();

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
		Session s = sf.openSession();

		try {
			s.beginTransaction();

			// Obtener todos los libros de la base de datos
			TypedQuery<Libro> q = s.createQuery("from Libro", Libro.class);
			List<Libro> results = q.getResultList();

			// Recorrer la lista de libros de la base de datos
			for (Libro libro : results) {
				if (!libros.containsKey(libro.getId())) {
					s.delete(libro);
				}
			}

			// Guardar los libros del HashMap en la base de datos
			for (Libro libro : libros.values()) {
				s.saveOrUpdate(libro);
			}

			s.getTransaction().commit();
		} catch (Exception e) {
			if (s.getTransaction() != null) {
				s.getTransaction().rollback();
			}
			System.err.println("Error al borrar los datos con Hibernate: " + e.getMessage());
		} finally {
			s.close();
			sf.close();
		}
	}

}