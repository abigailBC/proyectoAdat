package Modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SqliteManager extends Manager {
	HashMap<String, Libro> libros = new HashMap<>();
	private static Connection conn;
	String url;
//Aún no está implementado el fichero Properties en SQlite
	public SqliteManager() {
		libros = new HashMap<>();
		String rutaProp = "Ficheros/config/proyecto.properties";
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(rutaProp));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		url = prop.getProperty("rutaSqlite");
	}

	// Método para conectarse a la base de datos SQLite
	public static void conectarBD() {
		try {
			// Ruta de la base de datos SQLite (puede ser relativa o absoluta)
			String url = "jdbc:sqlite:C:\\Users\\mixol\\segundoCurso\\ProyectoDefinitivo\\Ficheros\\libros.db";

			conn = DriverManager.getConnection(url);

			if (conn != null) {
				System.out.println("Conexión a SQLite establecida.");
			}

		} catch (SQLException e) {
			System.err.println("Error al conectar con SQLite.");
			e.printStackTrace();
		}
	}

	@Override
	public HashMap<String, Libro> recorrer() {
		conectarBD();
		String query = "SELECT * FROM Libros";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				String id = rs.getString("id");
				String titulo = rs.getString("título");
				String autor = rs.getString("autor");
				String isbn = rs.getString("isbn");
				int anno = rs.getInt("anno");
				Libro libro = new Libro(id, titulo, autor, isbn, anno);
				libros.put(id, libro);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			System.err.println("Error al leer con SQLite: "+e.getMessage());
			e.printStackTrace();
		}

		return libros;
	}

	// Método para cerrar la conexión
	public static void cerrarConexion() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.err.println("Error al cerrar la conexión con SQLite: "+e.getMessage());
		}
	}

	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {
		conectarBD();

		try {
			Statement pstmt = conn.createStatement();
			String query0 = "delete from Libros";
			pstmt.execute(query0);
			for (Map.Entry<String, Libro> entry : libros.entrySet()) {
				Libro libro = entry.getValue();
				String id ="'" + libro.getId() + "'";
				String titulo = "'" + libro.getTitulo() + "'";
				String autor = "'" + libro.getAutor() + "'";
				String isbn = "'" + libro.getIsbn() + "'";
				int anno = libro.getAnno();
				String query = "INSERT INTO Libros(id, título, autor, isbn, anno) VALUES( "+ id + ", " + titulo + ", "
						+ autor + ", " + isbn + ", " + anno + ")";
				pstmt.executeUpdate(query);
			}
			pstmt.close();

		} catch (SQLException e) {
			System.err.println("Error al sobrescribir los datos con SQLite: "+e.getMessage());
		}
	}

}
