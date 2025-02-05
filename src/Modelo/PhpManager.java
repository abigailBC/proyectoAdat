package Modelo;

import java.util.HashMap;

public class PhpManager extends Manager{
    HashMap<String, Libro> libros = new HashMap<>();

    @Override
    public HashMap<String, Libro> recorrer() {

        return libros;
    }

    @Override
    public void guardarLibros(HashMap<String, Libro> libros) {

    }
}
