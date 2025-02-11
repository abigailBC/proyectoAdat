package Modelo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.*;

public class MongoManager extends Manager{
    HashMap<String,Libro> libros = new HashMap<>();
    MongoCollection<Document> miColeccion;

    public MongoManager() {

        MongoClient mongoClient = new MongoClient("localhost", 27017);

        MongoDatabase database = mongoClient.getDatabase("Libros");

        miColeccion = database.getCollection("libro");

    }

    @Override
    public HashMap<String, Libro> recorrer() {
        MongoCursor resultado = miColeccion.find().iterator();

        while (resultado.hasNext()) {

            Document doc = (Document) resultado.next();
            Libro l = new Libro(doc.getString("id"), doc.getString("titulo"), doc.getString("autor"), doc.getString("isbn"), doc.getInteger("anno"));
            libros.put(doc.getString("id"), l);

        }
        return libros;
    }


    @Override
    public void guardarLibros(HashMap<String, Libro> libros) {
        MongoCursor<Document> resultado = miColeccion.find().iterator();
        Set<String> idsBbdd = new HashSet<>();

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
    }
    /*
    Document query=new Document();
    Document mayor=new Document();
    mayor.put("$gt", 100);


    db.deposito.update(
        {
        "valor":100
        },
        {
        $set:{
        "cantidad":100
        }
       })
     */

//    @Override
//    public void guardarLibros(HashMap<String, Libro> libros) {
//        Document nuevo = new Document();
//
//        for (Map.Entry<String, Libro> entry : libros.entrySet()) {
//            Libro l = entry.getValue();
//            nuevo.put("id", l.getId());
//            nuevo.put("titulo", l.getTitulo());
//            nuevo.put("autor", l.getAutor());
//            nuevo.put("isbn", l.getIsbn());
//            nuevo.put("anno", l.getAnno());
//            miColeccion.insertOne(nuevo);
//        }
//    }



/*clase del profesor para métodos específicos
    public class PruebaMongo {



        public void leerDatos() {

            try (MongoCursor<Document> resultado = miColeccion.find().iterator()) {
                System.out.println("\n VER DATOS \n");
                while (resultado.hasNext()) {
                    Document doc = (Document) resultado.next();
                    System.out.println(doc.getString("nombre") + " - " + doc.getInteger("valor"));

                }
            }

        }

        public void insercionPrueba() {

            System.out.println("\n PRUEBA INSERCION \n Nombre Prueba y valor 500 \n ");

            Document nuevo = new Document();
            nuevo.put("valor", 500);
            nuevo.put("nombre", "prueba");

            miColeccion.insertOne(nuevo);

            System.out.println("\n INSERCION CORRECTA \n");

        }

        public void insercionPrueba2() {

            System.out.println("\n PRUEBA INSERCION CON FIND DE OTRA COLECCION ");

            Document busqueda = new Document();
            busqueda.put("clave", "coca");

            Document proyeccion = new Document();
            proyeccion.put("_id", 0);

            MongoCursor<Document> resultado = miColeccion2.find(busqueda).projection(proyeccion).iterator();
            Document doc = resultado.next();


            Document nuevo = new Document();
            nuevo.put("valor", 5);
            nuevo.put("nombre", "prueba insert find sin _id");
            nuevo.put("dispensadorIntruso", doc);

            miColeccion.insertOne(nuevo);

            System.out.println("\n INSERCION CORRECTA \n");

        }

        public void pruebaBusqueda() {

            System.out.println("\n BUSQUEDA VALOR 500 \n");

            Document searchQuery = new Document();
            searchQuery.put("valor", 500);

            MongoCursor resultado = miColeccion.find(searchQuery).iterator();

            while (resultado.hasNext()) {

                Document doc = (Document) resultado.next();
                System.out.println(doc.getString("nombre") + doc.getInteger("valor"));

            }

        }
        public void pruebaBorrado() {
            Document QueryDelete = new Document();
            QueryDelete.put("valor", 500);

            miColeccion.deleteMany(QueryDelete);
            System.out.println("Se ha borrado el documento");
        }
        public void pruebaModificar() {
            Document queryModify = new Document();
            queryModify.put("valor", 5);
//{$set:{"cantidad":250}}
            Document modificado = new Document();
            modificado.put("cantidad", 100);

            Document father = new Document();
            father.put("$set", modificado);

            miColeccion.updateOne(queryModify, father);
            System.out.println("Se ha modificado el documento");
        }

    }
*/
}
