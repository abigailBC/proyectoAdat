package Vista;

import Modelo.Libro;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//Clase para todo lo que se muestra por pantalla exceptuando los menús

public class Mensajito {
	Scanner sc = new Scanner(System.in);

	public void separador() {
		System.out.println("============MENÚ=============");
	}
	// pedir el ID para mostrar, añadir o eliminar

	public String viewOne() {
		String impreso = "";
		System.out.println("Escribe el ID del libro que quieres ver: ");
		impreso = sc.nextLine();
		return impreso;
	}

	public String viewEdit() {
		String editado = "";
		System.out.println("Elige el ID del libro que quieres editar: ");
		editado = sc.nextLine();
		return editado;
	}

	public String viewDel() {
		String libroBorrado = "";
		System.out.println("Escribe el ID del libro que quieres eliminar: ");
		libroBorrado = sc.nextLine();
		return libroBorrado;
	}

	/*
	 * public String viewOne() { String impreso="";
	 * System.out.println("Escribe el ID del libro que quieres ver: ");
	 * impreso=sc.nextLine(); return impreso; }
	 * 
	 * public String viewEdit() { String editado="";
	 * System.out.println("Elige el ID del libro que quieres editar: ");
	 * editado=sc.nextLine(); return editado; }
	 * 
	 * public String viewDel() { String libroBorrado="";
	 * System.out.println("Escribe el ID del libro que quieres eliminar: ");
	 * libroBorrado=sc.nextLine(); return libroBorrado; }
	 * 
	 */
	public void ConfirmDel() {
		System.out.println("El libro ha sido eliminado correctamente");
	}

	public void noEncontrado() {
		System.out.println("El libro escogido no ha sido encontrado");
	}

	public void mostrarLibro(Libro libro) {
		System.out.println(libro.toString());
	}

	
  public Libro pedirLibro() {
	  String editado="";
	  System.out.println("Elige el ID del libro: ");
	  editado=sc.nextLine();
	  System.out.println("Introduce el nombre del libro: ");
	  String nombre=sc.next();
	  sc.nextLine();
	  System.out.println("Introduce el autor del libro: ");
	  String autor=sc.nextLine();
	  System.out.println("Introduce el ISBN del libro: ");
	  String isbn=sc.nextLine();
	  System.out.println("Introduce el año del libro: ");
	  int anno=Integer.parseInt(sc.nextLine());
	  Libro libroEditado= new Libro(editado,nombre, autor, isbn, anno);
	  return libroEditado;
  }
 

	public void imprimirHashMap(HashMap<String, Libro> libros) {
		for (Map.Entry<String, Libro> entry : libros.entrySet()) {
			Libro valor = entry.getValue();
			System.out.println(valor.toString());
		}
	}

	public void sobreescrito() {
		System.out.println("Archivo sobrescrito exitosamente.");
	}

	public void comprobarBorrado(String libroBorrado) {
		System.out.println("El ID introducido es: " + libroBorrado);
	}

	public void problema() {
		System.out.println("Ha habido un problema, inténtelo de nuevo");
	}

	public int cantidadNuevos() {
		System.out.println("¿Cuántos libros nuevos quiere añadir?: ");
		int cantidad = sc.nextInt();
		sc.nextLine();
		return cantidad;
	}

	public void cerrarSC() {
		sc.close();
	}
}