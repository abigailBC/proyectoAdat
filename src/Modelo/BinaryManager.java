package Modelo;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BinaryManager extends Manager implements Serializable {
	
//Implementación del fichero Properties
	public BinaryManager() {
		libros = new HashMap<>();
		String rutaProp = "Ficheros/config/proyecto.properties";
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(rutaProp));
		} catch (FileNotFoundException e) {
			System.out.println("No se ha encontrado el archivo Properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String rutaBinary = prop.getProperty("rutaBinario");
		ruta = new File(rutaBinary);
	}

	private static final long serialVersionUID = 1L;
	HashMap<String, Libro> libros = new HashMap<>();

	//Código no ejecutado. Serializa el fichero binario para meter los objetos para cuando se necesite resetear el fichero.
	public void inicioBinario() {
		try {
			Libro libro1 = new Libro("001", "Una noche de niebla y furia", "Sarah J. Maas", "978-84-08-29096-4", 2016);
			libros.put("001", libro1);
			Libro libro2 = new Libro("002", "El nombre del viento", "Patrick Rothfuss", "978-84-01-33720-8", 2007);
			libros.put("002", libro2);
			Libro libro3 = new Libro("003", "Cien años de soledad", "Gabriel García Márquez", "978-84-376-0494-7",
					1967);
			libros.put("003", libro3);
			Libro libro4 = new Libro("004", "1984", "George Orwell", "978-84-376-0495-4", 1949);
			libros.put("004", libro4);
			Libro libro5 = new Libro("005", "Los pilares de la tierra", "Ken Follett", "978-84-01-33153-4", 1989);
			libros.put("005", libro5);
			Libro libro6 = new Libro("006", "La sombra del viento", "Carlos Ruiz Zafón", "978-84-08-11100-9", 2001);
			libros.put("006", libro6);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta));
			for (Map.Entry<String, Libro> entry : libros.entrySet()) {
				Libro valor = entry.getValue();
				oos.writeObject(valor);
			}
			oos.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public HashMap<String, Libro> recorrer() {
		try {
			FileInputStream fis = new FileInputStream(ruta);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Libro libro;
			while (true) {
				try {
					libro = (Libro) ois.readObject();
					String clave = libro.getId();
					libros.put(clave, libro);
				} catch (EOFException e) {
					System.out.println("El fichero binario está vacío");
					break;
				}catch(Exception e) {
					System.out.println("Error al leer el fichero binario: "+e.getMessage());
				}

			}

			ois.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return libros;
	}

	// sobreescribir el fichero binario
	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {
		try {
			// Abrimos el archivo en modo de escritura, sobrescribe el archivo existente
			FileOutputStream fos = new FileOutputStream(ruta);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			// Iteramos sobre el HashMap y escribimos cada libro en el archivo
			for (Map.Entry<String, Libro> entry : libros.entrySet()) {
				oos.writeObject(entry.getValue()); // Escribimos el objeto 'Libro'
			}

			// Cerramos el ObjectOutputStream para asegurarnos de que todo se ha escrito
			oos.close();

		} catch (IOException e) {
			System.out.println("Error al sobrescribir el archivo binario: " + e.getMessage());
			e.printStackTrace();
		}
	}
}