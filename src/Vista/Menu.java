package Vista;

import java.util.InputMismatchException;
import java.util.Scanner;
//Clase para todos los menús que se muestran por pantalla
public class Menu {
	Scanner sc = new Scanner(System.in);
	private int cambioArchivo;

	public int menu1() {
		int tipoFichero=0;
		try {
			System.out.println("Bienvenido. Elija una opción:" +
					"\n1.Fichero de texto"
					+"\n2.Fichero Binario" +
					"\n3.Fichero XML" +
					"\n4.Base de datos" +
					"\n5.Hibernate" +
					"\n6.SQLite" +
					"\n7.PHP" +
					"\n8.Objectdb" +
					"\n9.BaseX" +
					"\n10.MongoDB" +
					"\n0.Salir");
			tipoFichero = sc.nextInt();
			return tipoFichero;

		}catch(InputMismatchException e) {
			System.out.println("Su entrada no es un número");
		}
		return tipoFichero;

	}

	public int menu2(int tipoFichero) {
		int tipoaccion=0;

		try {
			System.out.println("¿Qué quieres hacer?\n1.Leer uno" +
					"\n2.Leer muchos"
					+ "\n3.Insertar uno" +
					"\n4.Insertar muchos" +
					"\n5.Modificar" +
					"\n6.Borrar" +
					"\n7.Trasladar a otro tipo de acceso a datos" +
					"\n0.Volver");
			tipoaccion = sc.nextInt();
			return tipoaccion;
		}catch(InputMismatchException e) {
			System.out.println("Su entrada no es un número");
			tipoaccion=0;
		}
		return tipoaccion;
		
	}

	public int menu3(int tipoArchivo) {
		/*
		 * String Menu31="\n1.A texto"; String Menu32="\n2.A binario"; String
		 * Menu33="\n3.A XML"; String Menu34="\n4.Base de datos"; String
		 * Menu35="\n5.Hibernate"; String Menu36="\n6.SQLite";
		 * System.out.println("Introduce el tipo de archivo al que quieres convertir:\n"
		 * ); for(int i=1;i<7;i++) { if(i==tipoArchivo) { System.out.println(""); }
		 * System.out.println(Menu3+i); }
		 */
		System.out.println(
				"Introduce el tipo de archivo al que quieres convertir: " +
						"\n1.A texto" +
						"\n2.A binario" +
						"\n3.A XML" +
						"\n4.Base de datos" +
						"\n5.Hibernate" +
						"\n6.SQLite" +
						"\n7.PHP" +
						"\n8.Objectdb" +
						"\n9.BaseX" +
						"\n10.MongoDB" +
						"\n0.Volver");
		cambioArchivo = sc.nextInt();
		return cambioArchivo;
	}
}