package Modelo;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TextManager extends Manager{
	
//Implementación del fichero Properties
	HashMap <String, Libro> libros;
	File ruta;
	
	public TextManager() {
		libros=new HashMap<>();
		String rutaProp="Ficheros/config/proyecto.properties";
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(rutaProp));
		} catch (FileNotFoundException e) {
			System.out.println("No se ha encontrado el archivo Properties");
		} catch (IOException e) {
			System.out.println("Ha habido un problema de entrada y/o salida en el archivo Properties");
		}
		String rutaTexto= prop.getProperty("rutaTexto");
				
		
		ruta=new File(rutaTexto);
	}
	
	@Override
	public HashMap<String, Libro> recorrer() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(ruta));
			String linea = new String();
			
			while((linea=br.readLine())!=null) {
				//System.out.println(linea); (de comprobación)
				String[] atribs=linea.split(",");
				Libro libro = new Libro(atribs[0], atribs[1], atribs[2], atribs[3], Integer.parseInt(atribs[4]));
				libros.put(libro.getId(),libro);
			}
			br.close();
		}catch(Exception e) {
			System.out.println("Error al leer el archivo txt: "+e.getMessage());
		}
		
		return libros;
	}
	
	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
			for(Map.Entry<String, Libro>entry : libros.entrySet()) {
				Libro libro=entry.getValue();
				bw.write(libro.getId()+","+libro.getTitulo()+","+libro.getAutor()+","+libro.getIsbn()+","+libro.getAnno()+"\n");
			}
			bw.close();
		}catch(Exception e) {
			System.err.println("Error al sobrescribir el archivo txt: "+e.getMessage());
		}
		
	}
}
	