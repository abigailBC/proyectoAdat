package Modelo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.util.List;
import java.util.Scanner;

import org.basex.core.cmd.*;


public class BasexManager extends Manager {
	Context context;
	XMLManager manager = new XMLManager();
	Scanner sc;
	String nombre;
	String ruta;
	HashMap<String, Libro> libros = new HashMap<>();
	public void Principal_BaseX() {

		sc = new Scanner(System.in);
		nombre = "BD_Libros_XML";
		ruta = "recursos/Libros.xml";

	}
	public void crearBD() throws BaseXException {
		new CreateDB(nombre, ruta).execute(context);

	}
	public void abridBD() throws BaseXException {
		new Open(nombre).execute(context);
	}
	public void infoDB() throws BaseXException {

		System.out.println("\n* Información de la DB abierta:");

		System.out.print(new InfoDB().execute(context));

	}
	private void querySelect(String query) throws Exception {

		String datosConsulta = new XQuery(query).execute(context);

		System.out.println("DATOS DE LA CONSULTA SIN FILTRAR: \n" + datosConsulta);
		System.out.println();
		System.out.println("INTENTAMOS PROCESAR LA RESPUESTA (saltará excepción si no está bien formada la respuesta");

		SAXBuilder saxBuilder = new SAXBuilder();
		InputStream fichero = new ByteArrayInputStream(datosConsulta.getBytes());
		Document document = saxBuilder.build(fichero);
		Element classElement = document.getRootElement();

		List<Element> listaNodos = classElement.getChildren();

		for (int temp = 0; temp < listaNodos.size(); temp++) {
			Element elementoLibro = listaNodos.get(temp);

			System.out.println("-------- LIBRO " + (temp+1) + "--------");

			System.out.println(elementoLibro.getChild("titulo").getText());
			System.out.println(elementoLibro.getChild("autor").getText());
			System.out.println(elementoLibro.getChild("isbn").getText());
			System.out.println(elementoLibro.getChild("anno").getText());


			System.out.println("-------------------\n ");

		}

	}

	@Override
	public HashMap<String, Libro> recorrer() {
        try {
            crearBD();
        } catch (BaseXException e) {
            throw new RuntimeException(e);
        }
        String query = "/Libros";
        try {
            querySelect(query);
			libros=manager.recorrer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return libros;
	}

	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {
		// TODO Auto-generated method stub

		
	}
	public void terminar() {
		context.close();
	}

}
