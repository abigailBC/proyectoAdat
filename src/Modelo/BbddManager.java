package Modelo;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BbddManager extends Manager{
	HashMap<String, Libro> libros = new HashMap<>();
	Connection con;
	
	public Connection AccesoBD() {
		String driver = "com.mysql.cj.jdbc.Driver";

		String database = "bdproyectointegrador";

		String hostname = "localhost";

		String port = "3306";

		String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";

		String username = "root";

		String password = "";
		try {
			
			System.out.println(url);
			con = DriverManager.getConnection(url, username, password);
	
		} catch (SQLException e) {
			System.out.println("No se ha podido establecer la conexión con la base de datos");
		}
		return con;

	}
	
	@Override
	public HashMap<String, Libro> recorrer() {

		try {
			con=AccesoBD();

			String query = "SELECT * FROM libro";

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				String id = rs.getString("id");
				String titulo = rs.getString("titulo");
				String autor = rs.getString("autor");
				String isbn = rs.getString("isbn");
				int anno = rs.getInt("anno");
				Libro libro = new Libro(id, titulo, autor, isbn, anno);
				libros.put(id, libro);
				
			}
			
			st.close();
		} catch (Exception e) {
			System.err.println("Error al leer la base de datos: "+e.getMessage());
		}
		return libros;
	}


	@Override
	public void guardarLibros(HashMap<String, Libro> libros) {
		try {
			AccesoBD();
			//Se elimina la información dentro de la tabla
			String query1 = "TRUNCATE TABLE libro";
			Statement st = con.createStatement();
			st.executeUpdate(query1);
			//Se rellena la tabla con la nueva información
			for(Map.Entry<String, Libro>entry : libros.entrySet()) {
				Libro libro=entry.getValue();
				String id="'"+libro.getId()+"'";
				String titulo="'"+libro.getTitulo()+"'";
				String autor="'"+libro.getAutor()+"'";
				String isbn="'"+libro.getIsbn()+"'";
				String anno="'"+libro.getAnno()+"'";
				String query2 = "INSERT INTO libro (id, titulo, autor, isbn, anno) VALUES ("+id+", "+titulo+", "+autor+", "+isbn+", "+anno+")";
				st.executeUpdate(query2);

			}
			st.close();	

		} catch (Exception e) {
			System.err.println("Error al sobrescribir la base de datos: "+e.getMessage());
		}
		
	}

	public void closeConn() {
		try {
			if(con!=null) {
				con.commit();
				con.close();
			}
		}catch(SQLException e) {
			System.out.println("No se ha podido cerrar la conexión a base de datos"+e.getMessage());
			
		}	
	}
}
