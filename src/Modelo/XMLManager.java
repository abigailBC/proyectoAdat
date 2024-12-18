package Modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLManager extends Manager {
	HashMap<String, Libro> libros = new HashMap<>();
	File ruta;
	
//Implementación del fichero Properties
	
	public XMLManager() {
		libros = new HashMap<>();
		String rutaProp = "Ficheros/config/proyecto.properties";
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(rutaProp));
		} catch (FileNotFoundException e) {
			System.out.println("No se ha encontrado el archivo Properties");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Ha habido un problema de entrada y/o salida en el archivo Properties");
		}
		String rutaTexto = prop.getProperty("rutaXML");

		ruta = new File(rutaTexto);
	}

	@Override
	public HashMap<String, Libro> recorrer() {
		
		try {

			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(ruta);
			Element raizLibros = document.getRootElement();

			List<Element> listaLibros = raizLibros.getChildren("libro");
			for (Element elementoLibro : listaLibros) {

				String id = elementoLibro.getChildText("id");
				String titulo = elementoLibro.getChildText("titulo");
				String autor = elementoLibro.getChildText("autor");
				String isbn = elementoLibro.getChildText("isbn");
				int anno = Integer.parseInt(elementoLibro.getChildText("anno"));
				Libro libro = new Libro(id, titulo, autor, isbn, anno);

				libros.put(id, libro);

			}
			Format f = Format.getPrettyFormat();
			f.setEncoding("gbk");
			f.setOmitDeclaration(false);

		} catch (Exception e) {
			System.out.println("Error al leer el archivo XML: "+e.getMessage());
			e.printStackTrace();
		}

		return libros;
	}

	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {
		try {
			// Crear el elemento raíz <libros>
			Element raizLibros = new Element("libros");
			Document document = new Document(raizLibros);

			// Recorrer el HashMap y añadir cada libro como un elemento <libro>
			for (Map.Entry<String, Libro> entry : libros.entrySet()) {
				Libro libro = entry.getValue();
				if (libro != null) { // Asegurarse de que el libro no sea null
					Element elementoLibro = new Element("libro");

					elementoLibro.addContent(new Element("id").setText(libro.getId()));
					elementoLibro.addContent(new Element("titulo").setText(libro.getTitulo()));
					elementoLibro.addContent(new Element("autor").setText(libro.getAutor()));
					elementoLibro.addContent(new Element("isbn").setText(libro.getIsbn()));
					elementoLibro.addContent(new Element("anno").setText(Integer.toString(libro.getAnno())));

					// Añadir el elemento <libro> al elemento raíz
					raizLibros.addContent(elementoLibro);
				}
			}

			// Configurar la salida del XML con formato legible
			Format format = Format.getPrettyFormat();
			XMLOutputter xmlOutputter = new XMLOutputter(format);

			// Guardar el XML en un archivo
			try (FileWriter fw = new FileWriter(ruta, false)) {
				xmlOutputter.output(document, fw);
				fw.close();
			}

		} catch (IOException e) {
			System.out.println("Error al sobrescribir el archivo XML: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error al procesar el documento XML: " + e.getMessage());
			e.printStackTrace();
		}

	}

}