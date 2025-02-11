package Modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

		try {
			em.getTransaction().begin();

			// Obtener todos los libros de la base de datos
			TypedQuery<Libro> query = em.createQuery("SELECT b FROM Libro b", Libro.class);
			List<Libro> results = query.getResultList();
			Set<String> idsLibrosHM = libros.keySet();

			// Eliminar libros que no están en el HashMap
			for (Libro libroBbdd : results) {
				if (!idsLibrosHM.contains(libroBbdd.getId())) {
					Libro libroAEliminar = em.find(Libro.class, libroBbdd.getId());
					if (libroAEliminar != null) {
						em.remove(libroAEliminar); // Elimina el libro
					}
				}
			}

			// Actualizar o insertar libros del HashMap
			for (Libro libro : libros.values()) {
				Libro libroExiste = em.find(Libro.class, libro.getId());
				if (libroExiste != null) {
					em.merge(libro); // Actualiza si existe
				} else {
					em.persist(libro); // Inserta si no existe
				}
			}

			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
	}
}

	/*clases de Jaime
	public class Jaimeclases() {
		try{
			// Creamos el entitymanager (que vendría a ser lo mismo que la sesión) mediante
			// "factoria"
			// Introducimos la ruta en la que se va a crear la BD orientada a objetos. En
			// este caso es dentro del mismo proyecto
			//EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/address-book.odb");
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("db/address-book.odb");
			EntityManager em = emf.createEntityManager();
			// En primer lugar "resetamos" borrando todos los objetos
			// Abrimos transaccion
			System.out.println("RESETEANDO BD. BORRANDO TODOS LOS OBJETOS");
			em.getTransaction().begin();
			em.createQuery("DELETE FROM AddressBook a").executeUpdate();
			em.getTransaction().commit();
			// Creamos objetos (si lo ejecutamos varias veces se repiten puesto que no
			// estamos controlando los id porque son generados)
			System.out.println("CREANDO Y ALMACENANDO OBJETOS");
			AddressBook b1 = new AddressBook("Jaime", "Luna 27");
			AddressBook b2 = new AddressBook("Paco", "Sol 32");
			AddressBook b3 = new AddressBook("Luis", "Luna 1");
			AddressBook b4 = new AddressBook("Ana", "Saturno 23");
			AddressBook b5 = new AddressBook("Maria", "Postigo 45");
			// Guardamos objetos (en lugar de session.save se usa entityManager.persist)
			// Abrimos transaccion
			em.getTransaction().begin();
			em.persist(b1);
			em.persist(b2);
			em.persist(b3);
			em.persist(b4);
			em.persist(b5);
			// Hacemos commit de la transaccion
			em.getTransaction().commit();
			// Lanzamos query para comprobar que se han almacenado los objetos
			// Al igual que pasaba cuando utilizabamos hibernate con ficheros de mapeo, se
			// crea una lista de objetos
			TypedQuery<AddressBook> query = em.createQuery("SELECT b FROM AddressBook b", AddressBook.class);
			List<AddressBook> results = query.getResultList();
			System.out.println("MOSTRAMOS TODOS LOS OBJETOS");
			for (AddressBook bb : results) {
				System.out.println(bb);
			}
			// Modificamos un objeto (volvemos a abrir transacción)
			b1.setAddress("Java 87");
			em.getTransaction().begin();
			em.merge(b1);
			em.getTransaction().commit();
			// Volvemos a lanzar query para comprobar que se ha modificado el objeto
			System.out.println("MOSTRAR TODOS DESPUES DE MODIFICACION");
			query = em.createQuery("SELECT b FROM AddressBook b", AddressBook.class);
			results = query.getResultList();
			for (AddressBook bb : results) {
				System.out.println(bb);
			}
			// Se podría hacer la query con algún criterio de búsqueda
			query = em.createQuery("SELECT b FROM AddressBook b WHERE b.name='Jaime'", AddressBook.class);
			results = query.getResultList();
			System.out.println("RESULTADOS DE QUERY CON BÚSQUEDA DE JAIME");
			for (AddressBook bb : results) {
				System.out.println(bb);
			}
			// Borramos un objeto (volvemos a abrir transacción)
			System.out.println("BORRANDO OBJETO JAIME");
			em.getTransaction().begin();
			em.remove(b1);
			em.getTransaction().commit();
			// Volvemos a lanzar query para comprobar que se ha modificado el objeto
			query = em.createQuery("SELECT b FROM AddressBook b", AddressBook.class);
			results = query.getResultList();
			for (AddressBook bb : results) {
				System.out.println(bb);
			}
			// Cerramos conexion
			em.close();
			emf.close();
		} catch(
		javax.persistence.PersistenceException ex){
			System.err.println("Se ha producido un error an intentar acceder a la base de datos\n"
					+ "Probablemente sea porque está abierta por el ObjectDB explorer");

		} catch(Exception ex){
			System.err.println("Se ha producido una excepción deconocida");
			System.err.println(ex.getMessage());
			System.err.println("Descomenta la traza para más detalles");
			// ex.printStackTrace();
		}

	}
}*/
