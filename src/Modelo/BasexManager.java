package Modelo;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.Set;

public class BasexManager extends Manager implements CRUDDatabase {
	String nombre = "libros";
	String ruta = new String("Ficheros\\Libros2.xml");
	HashMap<String, Libro> libros = new HashMap<>();
	Context context;

	public BasexManager() {
		context = new Context();
	}

	public void borrarDB() {
		try{
			new DropDB(nombre).execute(context);

		}catch(BaseXException e){
			System.err.println("Ha habido un error borrando la bbdd: "+e.getMessage());
		}

	}
	public void cerrarContext() {
		context.close();
	}
	public void crearBD () {
		try {
			new CreateDB(nombre, ruta).execute(context);
		} catch (BaseXException e) {
			System.err.println("Ha habido un problema abriendo la bbdd: "+e.getMessage());
		}
	}
	public void abridBD() {
		try {
			new Open(nombre).execute(context);
		} catch (BaseXException e) {
			System.err.println("Ha habido un problema abriendo la bbdd: "+e.getMessage());
		}
	}
	public void cerrarBD(){
		try {
			new Close().execute(context);
		} catch (BaseXException e) {
			System.err.println("Error cerrando la base de datos: " + e.getMessage());
		}
	}

	@Override
	public HashMap<String, Libro> recorrer() {
		try {
			//borrarDB();
			//ELIMINAR ESTE COMENTARIO LA PRIMERA VEZ
			//crearBD();
			abridBD();

			String datosConsulta = new XQuery("/libros").execute(context);
			//System.out.println(datosConsulta);

			SAXBuilder saxBuilder = new SAXBuilder();
			InputStream fichero = new ByteArrayInputStream(datosConsulta.getBytes());
			Document document = saxBuilder.build(fichero);
			Element classElement = document.getRootElement();
			List<Element> listaNodos = classElement.getChildren();
			HashMap<String, Libro> librosAntiguos = new HashMap<>(); // Crear un nuevo mapa

			for (Element elementoL : listaNodos) {
				String id=elementoL.getChild("id").getText();
				String titulo = elementoL.getChild("titulo").getText();
				String autor = elementoL.getChild("autor").getText();
				String isbn = elementoL.getChild("isbn").getText();
				int anno = Integer.parseInt(elementoL.getChild("anno").getText());

				Libro libro = new Libro(id, titulo, autor, isbn, anno);
				librosAntiguos.put(id, libro);
			}

			return librosAntiguos;


		} catch (BaseXException | RuntimeException e) {

			System.out.println("Error guardando los libros: "+e.getMessage());
		} catch (JDOMException | IOException e) {

			System.out.println("Error leyendo el fichero: "+e.getMessage());
		}finally{
			cerrarBD();
			cerrarContext();
		}
		return null;

	}

	private static String formatear(Libro libro) {
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
		return xmlOut.outputString(nuevo);
	}


	@Override
	public boolean guardarLibros(HashMap<String, Libro> libros) {
		// Primero, recopilamos todos los IDs de los libros en la base de datos
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
			cerrarBD();
			cerrarContext();
			return true;
		} catch(Exception e){
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el m�todo 'annadirLibro' de la clase JSON REMOTO");
			e.printStackTrace();
			System.out.println("Fin ejecución");
			cerrarBD();
			cerrarContext();
			return false;
		}
	}
//Interfaz CRUD
	@Override
	public void annadirActualizados(HashMap<String, MapDifference.ValueDifference<Libro>> actualizados) {
		abridBD();
		try {
			for (Map.Entry<String, MapDifference.ValueDifference<Libro>> entry : actualizados.entrySet()) {

					Libro libro = entry.getValue().rightValue();
					String formateado = formatear(libro);
					String queryUpdate = "replace node /libros/libro[id='" + libro.getId() + "'] with "+formateado;
					System.out.println("Consulta XQuery: " + queryUpdate);
					System.out.println(new XQuery(queryUpdate).execute(context));

			}
			cerrarContext();
			cerrarBD();
		} catch (Exception e) {
			e.printStackTrace();

			System.err.println("Ha habido un error actualizando datos: "+e.getMessage());
		}
	}

	@Override
	public void annadirBorrados(HashMap<String, Libro> borrados) {
		abridBD();
		try {
			for (Map.Entry<String, Libro> entry : borrados.entrySet()) {
				Libro libro = entry.getValue();
				String queryDelete = "delete node /libros/libro[id='" + libro.getId() + "']";
				System.out.println("Consulta XQuery: " + queryDelete);
				new XQuery(queryDelete).execute(context);

				System.out.println("Libro eliminado: " + libro.getId());
			}
			cerrarContext();
			cerrarBD();
		} catch (Exception e) {
			e.printStackTrace();

			System.err.println("Ha habido un error eliminando datos: "+e.getMessage());
		}
	}

	@Override
	public void annadirCreados(HashMap<String, Libro> creados) {
		abridBD();
		try {

			for (Map.Entry<String, Libro> entry : creados.entrySet()) {

				String formateado = formatear(entry.getValue());
				String queryInsert = "insert node " + formateado + " into /libros ";
				System.out.println("Consulta XQuery: " + queryInsert);
				String datosconsulta = new XQuery(queryInsert).execute(context);
				System.out.println(datosconsulta);

			}
			cerrarContext();
			cerrarBD();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Ha habido un error añadiendo datos: " + e.getMessage());
		}
	}
}