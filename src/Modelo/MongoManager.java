package Modelo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.*;

public class MongoManager extends Manager {
    HashMap<String, Libro> libros = new HashMap<>();
    MongoCollection<Document> miColeccion;

    public MongoManager() {

        MongoClient mongoClient = new MongoClient("localhost", 27017);

            MongoDatabase database = mongoClient.getDatabase("Libros");

            miColeccion = database.getCollection("libro");

    }

    @Override
    public HashMap<String, Libro> recorrer() {
        try (MongoCursor<Document> resultado = miColeccion.find().iterator()) {

            while (resultado.hasNext()) {

                Document doc = (Document) resultado.next();
                Libro l = new Libro(doc.getString("id"), doc.getString("titulo"), doc.getString("autor"), doc.getString("isbn"), doc.getInteger("anno"));
                libros.put(doc.getString("id"), l);

            }
        }catch(Exception e){

            System.out.println("Ha habido un error leyendo el archivo: "+e.getMessage());
        }
        return libros;
    }


    @Override
    public boolean guardarLibros(HashMap<String, Libro> libros) {
        Set<String> idsBbdd;
        try (MongoCursor<Document> resultado = miColeccion.find().iterator()) {
            idsBbdd = new HashSet<>();

            // Primero, recopilamos todos los IDs de los documentos en la base de datos
            while (resultado.hasNext()) {
                Document doc = resultado.next();
                String id = doc.getString("id");
                idsBbdd.add(id);
            }


            // Luego, actualizamos o insertamos los libros del HashMap
            for (Map.Entry<String, Libro> entry : libros.entrySet()) {
                Libro l = entry.getValue();
                Document filtro = new Document("id", l.getId()); // Filtro para buscar por id
                Document nuevo = new Document();
                nuevo.put("id", l.getId());
                nuevo.put("titulo", l.getTitulo());
                nuevo.put("autor", l.getAutor());
                nuevo.put("isbn", l.getIsbn());
                nuevo.put("anno", l.getAnno());

                // Opciones para la operación de actualización
                Document updateOperationDocument = new Document("$set", nuevo);

                // Realiza la operación de upsert
                miColeccion.updateOne(filtro, updateOperationDocument, new UpdateOptions().upsert(true));
            }

            // Finalmente, eliminamos los documentos que no están en el HashMap
            Set<String> idsEnHashMap = libros.keySet();
            idsBbdd.removeAll(idsEnHashMap); // Quedan solo los IDs que no están en el HashMap

            for (String id : idsBbdd) {
                Document filtro = new Document("id", id);
                miColeccion.deleteOne(filtro);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Ha habido un error guardando los datos: "+e.getMessage());
            System.out.println("Vigile si el servidor MongoDB está abierto");
            return false;
        }
    }
}

