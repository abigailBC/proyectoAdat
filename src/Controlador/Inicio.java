package Controlador;
//proyecto Abigail y Geffrey
import Vista.Mensajito;
import Vista.Menu;

import java.util.HashMap;
import java.util.Objects;

import Modelo.*;
//Clase Main, ejecuta el programa
public class Inicio {

	public static void main(String[] args) {
		Mensajito mensajito = new Mensajito();
		Menu menuu = new Menu();
		int tipoFichero;
		try {

			while (true) {
				// Ejecución del primer menú (en la vista).
				tipoFichero = menuu.menu1();
				Manager miObjeto = null;
				switch (tipoFichero) {
					// Selección del tipo de fichero deseado. La variable de la clase padre se
					// convierte en la clase del fichero elegido
					case 1:
						miObjeto = new TextManager();
						break;
					case 2:
						miObjeto = new BinaryManager();
						break;
					case 3:
						miObjeto = new XMLManager();
						break;
					case 4:
						miObjeto = new BbddManager();
						break;
					case 5:
						miObjeto = new HibernateManager();
						break;
					case 6:
						miObjeto = new SqliteManager();
						break;
					case 7:
						miObjeto = new PhpManager();
						break;
					case 8:
						miObjeto = new ObjectdbManager();
						break;
					case 9:
						miObjeto = new BasexManager();
						break;
					case 10:
						miObjeto = new MongoManager();
						break;
					case 0:
						BbddManager bdm = new BbddManager();
						bdm.closeConn();
						SqliteManager.cerrarConexion();
						mensajito.cerrarSC();
						BasexManager bxm = new BasexManager();
						bxm.cerrarContext();
						System.out.println("El programa terminará");
						return;
					default:
						System.out.println("Ha habido un error, vuelve a seleccionar opción.");
						break;
				}
				// Ejecución del segundo menú
				int tipoaccion = menuu.menu2(tipoFichero);
				// Selección del tipo de acción deseada
				switch (tipoaccion) {
					case 0:
						Thread.sleep(500);
						mensajito.separador();
						break;
					// Leer uno
					case 1:
						String impreso = mensajito.viewOne();
						Libro libro = null;
						if (miObjeto != null) {
							libro = miObjeto.printOne(impreso);
						}
						if (libro != null) {
							mensajito.mostrarLibro(libro);
						} else {
							mensajito.noEncontrado();
						}
						Thread.sleep(1500);
						mensajito.separador();
						break;
					// Leer muchos
					case 2:
						HashMap<String, Libro> libros = Objects.requireNonNull(miObjeto).recorrer();
						mensajito.imprimirHashMap(libros);
						Thread.sleep(1500);
						mensajito.separador();
						break;
					// Añadir uno
					case 3:
						Libro libroNuevo=mensajito.pedirLibro();
						assert miObjeto != null;
						if (miObjeto.addOne(libroNuevo)) {
							mensajito.sobreescrito();
						} else {
							mensajito.problema();
						}
						// mensajito.imprimirHashMap(libros);
						mensajito.separador();
						Thread.sleep(1500);
						break;
					// Añadir muchos
					case 4:
						HashMap<String, Libro> librosNuevos = new HashMap<>();
						int cantidad = mensajito.cantidadNuevos();
						for (int i = 0; i < cantidad; i++) {
							Libro libroNuevos = mensajito.pedirLibro();
							librosNuevos.put(libroNuevos.getId(), libroNuevos);
						}
						if (miObjeto != null && miObjeto.addMore(librosNuevos)) {
							mensajito.sobreescrito();

						}
						Thread.sleep(1500);
						mensajito.separador();
						break;
					// Editar uno
					case 5:
						Libro libroEditado = mensajito.pedirLibro();
						if (miObjeto != null && miObjeto.editOne(libroEditado)) {
							mensajito.sobreescrito();

						}
						Thread.sleep(1500);
						mensajito.separador();

						break;
					// Borrar uno
					case 6:
						String libroBorrado = mensajito.viewDel();
						if (miObjeto != null && miObjeto.deleteOne(libroBorrado)) {
							mensajito.sobreescrito();
						}
						Thread.sleep(1500);
						mensajito.separador();
						break;
					// Pasar el archivo a otra extensión
					case 7:
						Manager miObjeto2;
						// Menú 3: selección de tipo de archivo al que se quiere trasladar el contenido
						int cambioArchivo = menuu.menu3(tipoFichero);
						switch (cambioArchivo) {
							case 0:
								break;
							case 1:
								// Se crea un objeto para controlar el volcado de contenido en el segundo archivo
								miObjeto2 = new TextManager();
								// Se guarda el hashmap creado de miObjeto1 en el archivo de miObjeto2
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
							case 2:
								miObjeto2 = new BinaryManager();
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
							case 3:
								miObjeto2 = new XMLManager();
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
							case 4:
								miObjeto2 = new BbddManager();
								assert miObjeto != null;
								miObjeto2.guardarLibros(miObjeto.recorrer());

								break;
							case 5:
								miObjeto2 = new HibernateManager();
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
							case 6:
								miObjeto2 = new SqliteManager();
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
							case 7:
								miObjeto2 = new PhpManager();
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
							case 8:
								miObjeto2 = new ObjectdbManager();
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
							case 9:
								miObjeto2 = new BasexManager();
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
							case 10:
								miObjeto2 = new MongoManager();
								if (miObjeto != null) {
									miObjeto2.guardarLibros(miObjeto.recorrer());
								}
								break;
						}
						Thread.sleep(1500);
						mensajito.separador();
						break;
					default:
						System.out.println("Ha habido un error. Vuelve a "
								+ "seleccionar las opciones");
						Thread.sleep(1500);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
