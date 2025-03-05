package Modelo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Auxiliar.ApiRequests;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class PhpManager extends Manager implements CRUDDatabase {
    HashMap<String, Libro> libros = new HashMap<>();
    ApiRequests encargadoPeticiones;
    private String SERVER_PATH, GET_BOOK, SET_BOOK, DELETE_BOOK, UPDATE_BOOK;// Datos de la conexion
    JSONObject objLibro = new JSONObject();
    JSONObject objPeticion = new JSONObject();

    public PhpManager(){
        encargadoPeticiones = new ApiRequests();
//no conectamos a la bbdd aquí, somos un cliente que va a ver un json
        SERVER_PATH = "http://localhost/abigail/Libros/";
        GET_BOOK = "leerLibros.php";
        SET_BOOK = "guardarLibros.php";
        DELETE_BOOK = "borrarLibros.php";
        UPDATE_BOOK = "actualizarLibros.php";

    }
    @Override
    public HashMap<String, Libro> recorrer() {
        try {
            String url = SERVER_PATH + GET_BOOK; // Sacadas de configuracion

            String response = encargadoPeticiones.getRequest(url);
            // Parseamos la respuesta y la convertimos en un JSONObject
            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            if (respuesta == null) { // Si hay algún error de parseo (json
                // incorrecto porque hay alg�n caracter
                // raro, etc.) la respuesta ser� null
                System.out.println("El json recibido no es correcto.");
            } else { // El JSON recibido es correcto
                // Sera "ok" si todo ha ido bien o "error" si hay alg�n problema
                String estado = (String) respuesta.get("estado");
                // Si ok, obtenemos array de jugadores para recorrer y generar hashmap
                if (estado.equals("ok")) {
                    JSONArray array = (JSONArray) respuesta.get("libros");

                    if (array.size() > 0) {

                        // Declaramos variables
                        Libro libro;
                        String titulo;
                        String autor;
                        int anno;
                        String isbn;
                        String id;
                        HashMap<String, Libro> librosAntiguos = new HashMap<>(); // Crear un nuevo mapa

                        for (Object o : array) {
                            JSONObject libroJSON = (JSONObject) o;

                            id = libroJSON.get("id").toString();
                            titulo = libroJSON.get("titulo").toString();
                            autor = libroJSON.get("autor").toString();
                            isbn = libroJSON.get("isbn").toString();
                            anno = Integer.parseInt(libroJSON.get("anno").toString());

                            libro = new Libro(id, titulo, autor, isbn, anno);

                            librosAntiguos.put(id, libro);
                        }

                        System.out.println("Acceso JSON Remoto - Leidos datos correctamente y generado hashmap");
                        System.out.println();
                        return librosAntiguos;


                    } else { // El array de jugadores est� vac�o
                        System.out.println("Acceso JSON Remoto - No hay datos que tratar");
                        System.out.println();
                    }

                } else { // Hemos recibido el json pero en el estado se nos
                    // indica que ha habido alg�n error

                    System.out.println("Ha ocurrido un error en la busqueda de datos");
                    System.out.println("Error: " + (String) respuesta.get("error"));
                    System.out.println("Consulta: " + (String) respuesta.get("query"));

                }
            }
        } catch (IOException e) {
            System.out.println("error manejando el archivo PHP:"+e.getMessage());
        }

        return null;
    }
    public void annadirActualizados(HashMap<String, MapDifference.ValueDifference<Libro>> actualizados){
        for (Map.Entry<String, MapDifference.ValueDifference<Libro>> entry : actualizados.entrySet()) {
            Libro libro = entry.getValue().rightValue();
            objLibro.put("id", libro.getId());
            objLibro.put("titulo", libro.getId());
            objLibro.put("autor", libro.getAutor());
            objLibro.put("isbn", libro.getIsbn());
            objLibro.put("anno", libro.getAnno());


            objPeticion.put("peticion", "update");
            objPeticion.put("libroAnnadir", objLibro);

            String json = objPeticion.toJSONString();

            System.out.println("Lanzamos peticion JSON para actualizar un libro");

            String url = SERVER_PATH + UPDATE_BOOK;

            System.out.println("La url a la que lanzamos la petici�n es " + url);
            System.out.println("El json que enviamos es: ");
            System.out.println(json);
            //System.exit(-1);

            String response = null;
            try {
                response = encargadoPeticiones.postRequest(url, json);
            } catch (IOException e) {
                System.out.println("Error leyendo archivo: "+e.getMessage());
            }

            System.out.println("El json que recibimos es: ");

            System.out.println(response); // Traza para pruebas
            //System.exit(-1);

            // Parseamos la respuesta y la convertimos en un JSONObject


            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            if (respuesta == null) { // Si hay alg�n error de parseo (json
                // incorrecto porque hay alg�n caracter
                // raro, etc.) la respuesta ser� null
                System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
            } else { // El JSON recibido es correcto

                // Sera "ok" si todo ha ido bien o "error" si hay alg�n problema
                String estado = (String) respuesta.get("estado");
                if (estado.equals("ok")) {

                    System.out.println("Almacenado libro enviado por JSON Remoto");

                } else { // Hemos recibido el json pero en el estado se nos
                    // indica que ha habido alg�n error

                    System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
                    System.out.println("Error: " + (String) respuesta.get("error"));
                    System.out.println("Consulta: " + (String) respuesta.get("query"));


                }
            }
        }
    }
    public void annadirBorrados(HashMap<String, Libro> borrados){
        for (Map.Entry<String, Libro> entry : borrados.entrySet()){
            Libro libro = entry.getValue();
            objLibro.put("id", libro.getId());
            objLibro.put("titulo", libro.getId());
            objLibro.put("autor", libro.getAutor());
            objLibro.put("isbn", libro.getIsbn());
            objLibro.put("anno", libro.getAnno());
        }
        objPeticion.put("peticion", "delete");
        objPeticion.put("libroBorrar", objLibro);

        String json = objPeticion.toJSONString();

        System.out.println("Lanzamos peticion JSON para almacenar un libro");

        String url = SERVER_PATH + DELETE_BOOK;

        System.out.println("La url a la que lanzamos la petici�n es " + url);
        System.out.println("El json que enviamos es: ");
        System.out.println(json);
        //System.exit(-1);

        String response = null;
        try {
            response = encargadoPeticiones.postRequest(url, json);
        } catch (IOException e) {
            System.out.println("Error leyendo archivo: "+e.getMessage());
        }

        System.out.println("El json que recibimos es: ");

        System.out.println(response); // Traza para pruebas
        //System.exit(-1);

        // Parseamos la respuesta y la convertimos en un JSONObject


        JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

        if (respuesta == null) { // Si hay alg�n error de parseo (json
            // incorrecto porque hay alg�n caracter
            // raro, etc.) la respuesta ser� null
            System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
        } else { // El JSON recibido es correcto

            // Sera "ok" si todo ha ido bien o "error" si hay alg�n problema
            String estado = (String) respuesta.get("estado");
            if (estado.equals("ok")) {

                System.out.println("Almacenado libro enviado por JSON Remoto");

            } else { // Hemos recibido el json pero en el estado se nos
                // indica que ha habido alg�n error

                System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
                System.out.println("Error: " + (String) respuesta.get("error"));
                System.out.println("Consulta: " + (String) respuesta.get("query"));


            }
        }
    }
    public void annadirCreados(HashMap<String, Libro> creados){
        for (Map.Entry<String, Libro> entry : creados.entrySet()) {
            Libro libro = entry.getValue();
            objLibro.put("id", libro.getId());
            objLibro.put("titulo", libro.getId());
            objLibro.put("autor", libro.getAutor());
            objLibro.put("isbn", libro.getIsbn());
            objLibro.put("anno", libro.getAnno());


            // Tenemos el jugador como objeto JSON. Lo a�adimos a una peticion
            // Lo transformamos a string y llamamos al
            // encargado de peticiones para que lo envie al PHP

            objPeticion.put("peticion", "add");
            objPeticion.put("libroAnnadir", objLibro);

            String json = objPeticion.toJSONString();

            System.out.println("Lanzamos peticion JSON para almacenar un libro");

            String url = SERVER_PATH + SET_BOOK;

            System.out.println("La url a la que lanzamos la petici�n es " + url);
            System.out.println("El json que enviamos es: ");
            System.out.println(json);
            //System.exit(-1);

            String response = null;
            try {
                response = encargadoPeticiones.postRequest(url, json);
            } catch (IOException e) {
                System.out.println("Error leyendo archivo: "+e.getMessage());
            }

            System.out.println("El json que recibimos es: ");

            System.out.println(response); // Traza para pruebas
            //System.exit(-1);

            // Parseamos la respuesta y la convertimos en un JSONObject


            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            if (respuesta == null) { // Si hay alg�n error de parseo (json
                // incorrecto porque hay alg�n caracter
                // raro, etc.) la respuesta ser� null
                System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
            } else { // El JSON recibido es correcto

                // Sera "ok" si todo ha ido bien o "error" si hay alg�n problema
                String estado = (String) respuesta.get("estado");
                if (estado.equals("ok")) {

                    System.out.println("Almacenado libro enviado por JSON Remoto");

                } else { // Hemos recibido el json pero en el estado se nos
                    // indica que ha habido alg�n error

                    System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
                    System.out.println("Error: " + (String) respuesta.get("error"));
                    System.out.println("Consulta: " + (String) respuesta.get("query"));


                }
            }
        }
    }
    @Override
    public boolean guardarLibros(HashMap<String, Libro> libros) {
        Map<String,Libro> librosAntiguos= new HashMap<>(recorrer());
        //System.out.println("Tamaño libros antiguos: "+librosAntiguos.size());
        //System.out.println("libros antiguos: "+librosAntiguos);

        //System.out.println("Tamaño libros nuevos: "+libros.size());
        //System.out.println("libros nuevos: "+libros);
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
        } catch(Exception e){
            System.out.println(
                    "Excepcion desconocida. Traza de error comentada en el m�todo 'annadirLibro' de la clase JSON REMOTO");
            // e.printStackTrace();
            return false;
        }
        return true;
    }



}
