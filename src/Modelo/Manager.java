package Modelo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//Clase padre. Contiene los métodos comunes a todos los Managers

public abstract class Manager {
//variable heredable
	File ruta;

//Métodos abstractos. Se sobreescriben en todas las clases
	
	public abstract HashMap<String, Libro> recorrer();
	
	public abstract boolean guardarLibros(HashMap<String, Libro> libros);

//Métodos comunes. Se heredan.
	
	public Libro printOne(String impreso) {
		HashMap <String, Libro> libros=recorrer();
        return libros.get(impreso);
				
	}
	
	public Boolean addOne(Libro libro) {
		HashMap <String, Libro> libros=recorrer();
		boolean todoOk=false;
		try {
			libros.put(libro.getId(), libro);
			if(guardarLibros(libros)){
				todoOk=true;
			}
		}catch(Exception e) {
			System.out.println("Ha habido un problema añadiendo el dato: "+e.getMessage());
		}
		return todoOk;
	}
	
	public Boolean addMore(HashMap<String, Libro> librosNuevos) {
		HashMap <String, Libro> libros=recorrer();
		boolean todoOk=false;
		try {
			for(Map.Entry<String, Libro>entry : librosNuevos.entrySet()) {
				String clave = entry.getKey();
				Libro valor= entry.getValue();
				libros.put(clave, valor);
			}
			if(guardarLibros(libros)){
				todoOk=true;
			}
		}catch(Exception e) {
			System.out.println("Ha habido un problema añadiendo los datos: "+e.getMessage());
		}
		
		return todoOk;
	}

	
	public Boolean editOne(Libro libroEditado) {
		HashMap <String, Libro> libros=recorrer();
		boolean todoOk=false;
		try {
			libros.put(libroEditado.getId(), libroEditado);
			if(guardarLibros(libros)){
				todoOk=true;
			}
		}catch(Exception e) {
			System.out.println("Ha habido un problema editando el dato: "+e.getMessage());
		}
		return todoOk;
	}

	
	public Boolean deleteOne(String libroBorrado) {
		HashMap <String, Libro> libros=recorrer();
		boolean todoOk=false;
		try {
			if(libros.containsKey(libroBorrado)) {
				libros.remove(libroBorrado);
				guardarLibros(libros);
				todoOk=true;
			}
		}catch(Exception e) {
			System.out.println("Ha habido un problema borrando el dato: "+e.getMessage());
		}
		return todoOk;

	}

}