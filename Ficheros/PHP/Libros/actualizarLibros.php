<?php

require 'bbdd.php'; // Incluimos fichero con la conexión a la BBDD
require 'jsonEsperado.php';

$arrMensaje = array(); // Este array lo codificaremos como JSON

$parameters = file_get_contents("php://input");

if(isset($parameters)){

    $mensajeRecibido = json_decode($parameters, true);

    if(JSONCorrectoActualizar($mensajeRecibido)){

        $libro = $mensajeRecibido["libroActualizar"]; 

        $id = $libro["id"];
        $titulo = $libro["titulo"];
        $autor = $libro["autor"];
        $isbn = $libro["isbn"];
        $anno = $libro["anno"];

        // Consulta SQL corregida
        $query  = "UPDATE libro SET titulo='$titulo', autor='$autor', isbn='$isbn', anno='$anno' WHERE id='$id'";

        $result = $conn->query($query);

        if ($result) { // Si la query fue exitosa
            
            $arrMensaje["estado"] = "ok";
            $arrMensaje["mensaje"] = "Libro actualizado correctamente";

        } else { // Si hubo un error al ejecutar la consulta
            
            $arrMensaje["estado"] = "error";
            $arrMensaje["mensaje"] = "SE HA PRODUCIDO UN ERROR AL ACCEDER A LA BASE DE DATOS";
            $arrMensaje["error"] = $conn->error;
            $arrMensaje["query"] = $query;
        }

    } else { // JSON incorrecto

        $arrMensaje["estado"] = "error";
        $arrMensaje["mensaje"] = "EL JSON NO CONTIENE LOS CAMPOS ESPERADOS";
        $arrMensaje["recibido"] = $mensajeRecibido;
        $arrMensaje["esperado"] = $arrEsperado;
    }

} else { // No se ha recibido el JSON correctamente

    $arrMensaje["estado"] = "error";
    $arrMensaje["mensaje"] = "EL JSON NO SE HA ENVIADO CORRECTAMENTE";
}

echo json_encode($arrMensaje, JSON_PRETTY_PRINT);

$conn->close();
die();

?>
