package Modelo;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import java.util.HashMap;

public interface CRUDDatabase {
    public void annadirActualizados(HashMap<String, MapDifference.ValueDifference<Libro>> actualizados);

    public void annadirBorrados(HashMap<String, Libro> borrados);

    public void annadirCreados(HashMap<String, Libro> creados);
    }
