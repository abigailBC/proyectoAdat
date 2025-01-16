package Modelo;
import Modelo.Libro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
public class ObjectdbManager extends Manager {
	HashMap<String, Libro> libros = new HashMap<>();

	@Override
	public HashMap<String, Libro> recorrer() {
		// Creamos el entitymanager (que vendría a ser lo mismo que la sesión) mediante "factoria"
		// Introducimos la ruta en la que se va a crear la BD orientada a objetos. En
		// este caso es dentro del mismo proyecto
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/address-book.odb");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("db/libros.odb");
		EntityManager em = emf.createEntityManager();
		TypedQuery<Libro> query = em.createQuery("SELECT b FROM Libro b", Libro.class);
		List<Libro> results = query.getResultList();
		results = query.getResultList();
		for (Libro bb : results) {
			libros.put(bb.getId(), bb);
		}
		em.close();
		return libros;
	}

	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("db/libros.odb");
		EntityManager em = emf.createEntityManager();
		// Creamos objetos (si lo ejecutamos varias veces se repiten puesto que no
		// estamos controlando los id porque son generados)
		for(Map.Entry<String, Libro>entry : libros.entrySet()) {
			Libro libro=entry.getValue();
			String id="'"+libro.getId()+"'";
			String titulo="'"+libro.getTitulo()+"'";
			String autor="'"+libro.getAutor()+"'";
			String isbn="'"+libro.getIsbn()+"'";
			String anno="'"+libro.getAnno()+"'";
		// Guardamos objetos (en lugar de session.save se usa entityManager.persist)
		// Abrimos transaccion
			em.getTransaction().begin();
		//se pasa por parámetro el objeto que se quiere grabar persist==save
			em.persist(libro);
		// Hacemos commit de la transaccion
			em.getTransaction().commit();
			em.close();
		}
	}
}
