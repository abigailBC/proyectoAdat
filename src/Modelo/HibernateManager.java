package Modelo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import com.google.common.collect.*;
import com.google.common.collect.MapDifference;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateManager extends Manager implements CRUDDatabase{
	HashMap <String, Libro> libros = new HashMap<>();

	@Override
	public HashMap<String, Libro> recorrer() {
		//clase que se especializa en crear una session.
		SessionFactory sf = new Configuration().configure().buildSessionFactory();

		Session s=sf.openSession();
		try {
			HashMap<String, Libro> librosAntiguos = new HashMap<>(); // Crear un nuevo mapa
	    	TypedQuery<Libro> q=s.createQuery("from Libro", Libro.class);
	    	List <Libro> results = q.getResultList();
	        Iterator librosIterator= results.iterator();
	        while (librosIterator.hasNext()){
	            Libro libro = (Libro)librosIterator.next();
	            librosAntiguos.put(libro.getId(), libro);
	        }
			return librosAntiguos;

		}catch(Exception e) {
			System.out.println("Error al leer con Hibernate: "+e.getMessage());

		}finally {
			s.close();
			sf.close();
		}
		return null;
    }

	@Override
	public boolean guardarLibros(HashMap<String, Libro> libros) {
		Map<String,Libro> librosAntiguos= new HashMap<>(recorrer());
		MapDifference<String,Libro> md= Maps.difference(librosAntiguos, libros);
		HashMap<String, MapDifference.ValueDifference<Libro>> actualizados = new HashMap<>(md.entriesDiffering());
		HashMap<String, Libro> borrados = new HashMap<>(md.entriesOnlyOnLeft());
		HashMap<String, Libro> creados = new HashMap<>(md.entriesOnlyOnRight());

		try {

			if (!creados.isEmpty()) {
				annadirCreados(creados);

			}
			if(!borrados.isEmpty()){
				annadirBorrados(borrados);
			}

			if(!actualizados.isEmpty()){
				annadirActualizados(actualizados);
			}
		} catch(Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el m�todo 'annadirLibro' de la clase JSON REMOTO");
			// e.printStackTrace();
			return false;
		}
		return true;
	}



	//Interfaz CRUD

	@Override
	public void annadirActualizados(HashMap<String, MapDifference.ValueDifference<Libro>> actualizados) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		try (sf; Session s = sf.openSession()) {
			// Guardar los libros del HashMap en la base de datos
			for (Map.Entry<String, MapDifference.ValueDifference<Libro>> entry : actualizados.entrySet()) {
				Libro libro = entry.getValue().rightValue();
				s.beginTransaction();
				s.merge(libro);
				s.getTransaction().commit();
			}
		} catch (Exception e) {
			System.out.println("Error actualizando: " + e.getMessage());
		}
	}

	@Override
	public void annadirBorrados(HashMap<String, Libro> borrados) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
        try (sf; Session s = sf.openSession()) {

            s.beginTransaction();
            for (Map.Entry<String, Libro> entry : borrados.entrySet()) {
                s.delete(entry.getValue());
                s.getTransaction().commit();
            }
        } catch (Exception e) {
            System.out.println("Error borrando: " + e.getMessage());
        }
	}


	@Override
	public void annadirCreados(HashMap<String, Libro> creados) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
        try (sf; Session s = sf.openSession()) {
            s.beginTransaction();
            // Guardar los libros del HashMap en la base de datos
            for (Map.Entry<String, Libro> entry : creados.entrySet()) {
                s.saveOrUpdate(entry.getValue());
                s.getTransaction().commit();
            }
        } catch (Exception e) {
            System.out.println("Error creando: " + e.getMessage());
        }
	}
}