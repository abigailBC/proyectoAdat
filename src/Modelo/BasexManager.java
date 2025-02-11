package Modelo;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.XQuery;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class BasexManager extends Manager {
	Context context;

	public BasexManager() {
		// Database context.
		context = new Context();
	}

	public void cerrarContext() {

		// Close the database context
		context.close();
	}

	String nombrel = "Libros.xml";
	File rutal = new File("Ficheros\\Libros2.xml");
	String ruta = "C:\\Users\\mixol\\basex\\data\\Libros";
	HashMap<String, Libro> libros = new HashMap<>();

	@Override
	public HashMap<String, Libro> recorrer() {
		String datosConsulta = "";
		try {
			//ELIMINAR ESTE COMENTARIO LA PRIMERA VEZ
			crearBD();
			abridBD();

			datosConsulta = new XQuery("/Libros2.xml").execute(context);
			System.out.println(datosConsulta);
		} catch (BaseXException | RuntimeException e) {
			// throw new RuntimeException(e);
			System.out.println(e.getMessage());
		}


		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = null;
		try {
			document = saxBuilder.build(rutal);
		} catch (JDOMException | IOException e) {
			throw new RuntimeException(e);
		}
		Element classElement = document.getRootElement();

		List<Element> listaNodos = classElement.getChildren();

		for (Element elementoL : listaNodos) {
			String id=elementoL.getChild("id").getText();
			String titulo = elementoL.getChild("titulo").getText();
			String autor = elementoL.getChild("autor").getText();
			String isbn = elementoL.getChild("isbn").getText();
			int anno = Integer.parseInt(elementoL.getChild("anno").getText());

			Libro libro = new Libro(id, titulo, autor, isbn, anno);
			libros.put(id, libro);
		}
		cerrarContext();
		return libros;
	}

	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {
		// Primero, recopilamos todos los IDs de los libros en la base de datos
		Set<String> idsEnBaseDeDatos = new HashSet<>();
		try {
			String queryGetIds = "for $id in /Libros2.xml/libro/id return string($id)";
			String result = new XQuery(queryGetIds).execute(context);
			String[] ids = result.split("\n");
			for (String id : ids) {
				idsEnBaseDeDatos.add(id.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Luego, actualizamos o insertamos los libros del HashMap
		for (Map.Entry<String, Libro> entry : libros.entrySet()) {
			Libro libro = entry.getValue();

			// Creamos nodo XML (incluyendo "hijos" y nodos de texto)
			Element nuevo = new Element("libro");
			Element elem_id = new Element("id");
			elem_id.setText(libro.getId());
			Element elem_titulo = new Element("titulo");
			elem_titulo.setText(libro.getTitulo());
			Element elem_autor = new Element("autor");
			elem_autor.setText(libro.getAutor());
			Element elem_isbn = new Element("isbn");
			elem_isbn.setText(libro.getIsbn());
			Element elem_anno = new Element("anno");
			elem_anno.setText(String.valueOf(libro.getAnno()));

			nuevo.addContent(elem_id);
			nuevo.addContent(elem_titulo);
			nuevo.addContent(elem_autor);
			nuevo.addContent(elem_isbn);
			nuevo.addContent(elem_anno);

			// Formateamos como string y lo añadimos a la query de inserción o actualización
			XMLOutputter xmlOut = new XMLOutputter();
			String formateado = xmlOut.outputString(nuevo);

			// Verificamos si el libro ya existe en la base de datos
			String queryCheck = "count(/Libros2.xml/libro[id='" + libro.getId() + "'])";
			String queryUpdate = "replace node /Libros2.xml/libro[id='" + libro.getId() + "'] with " + formateado;
			String queryInsert = "insert node " + formateado + " into /Libros2.xml";

			try {
				String count = new XQuery(queryCheck).execute(context);
				if (Integer.parseInt(count) > 0) {
					// Si el libro existe, lo actualizamos
					queryOtra(queryUpdate);
					System.out.println("Libro actualizado: " + libro.getId());
				} else {
					// Si el libro no existe, lo insertamos
					queryOtra(queryInsert);
					System.out.println("Libro insertado: " + libro.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Finalmente, eliminamos los libros que no están en el HashMap
		Set<String> idsEnHashMap = libros.keySet();
		idsEnBaseDeDatos.removeAll(idsEnHashMap); // Quedan solo los IDs que no están en el HashMap

		for (String id : idsEnBaseDeDatos) {
			try {
				String queryDelete = "delete node /Libros2.xml/libro[id='" + id + "']";
				queryOtra(queryDelete);
				System.out.println("Libro eliminado: " + id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("== LIBROS ACTUALIZADOS CORRECTAMENTE ==");
	}

	public void crearBD () {
		try {
			new CreateDB(nombrel, ruta).execute(context);
		} catch (BaseXException e) {
			throw new RuntimeException(e);
		}

	}
	public void abridBD () {
		try {
			new Open(nombrel, ruta).execute(context);
		} catch (BaseXException e) {
			throw new RuntimeException(e);
		}
	}
	public void queryOtra(String query) throws Exception {

		String datosConsulta = new XQuery(query).execute(context);

		System.out.println(datosConsulta);

	}

}
	/*para examen:
	public void pruebaBusqueda() throws Exception {
			String query = "<respuesta> { //Libro[Autor='YO'] } </respuesta>";
			querySelect(query);
		}
	 */