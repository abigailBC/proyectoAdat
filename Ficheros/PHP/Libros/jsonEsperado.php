<?php

/*  Formato JSON esperado */

$arrEsperado = array();
$arrLibroEsperado = array();

$arrEsperado["peticion"] = "add";

$arrLibroEsperado["id"] = "(string)";
$arrLibroEsperado["titulo"] = "(Un string)";
$arrLibroEsperado["autor"] = "(Un string)";
$arrLibroEsperado["isbn"] = "2 (Un string)";
$arrLibroEsperado["anno"] = "2 (Un int)";


$arrEsperado["libroAnnadir"||"libroActualizar"||"libroBorrar"] = $arrLibroEsperado;


/* Funcion para comprobar si el recibido es igual al esperado */

function JSONCorrectoAnnadir($recibido){
	
	$auxCorrecto = false;
	
	if(isset($recibido["peticion"]) && $recibido["peticion"] && isset($recibido["libroAnnadir"])||($recibido["libroActualizar"])||($recibido["libroBorrar"])){
		
		$auxLibro = $recibido["libroAnnadir"];
		if(isset($auxLibro["id"]) && isset($auxLibro["titulo"]) && isset($auxLibro["autor"]) && isset($auxLibro["isbn"]) && isset($auxLibro["anno"])){
			$auxCorrecto = true;
		}
		
	}
	
	
	return $auxCorrecto;
	
}
function JSONCorrectoActualizar($recibido){
	
	$auxCorrecto = false;
	
	if(isset($recibido["peticion"]) && $recibido["peticion"] && isset($recibido["libroActualizar"])){
		
		$auxLibro = $recibido["libroActualizar"];
		if(isset($auxLibro["id"]) && isset($auxLibro["titulo"]) && isset($auxLibro["autor"]) && isset($auxLibro["isbn"]) && isset($auxLibro["anno"])){
			$auxCorrecto = true;
		}
		
	}
	
	
	return $auxCorrecto;
	
}
function JSONCorrectoBorrar($recibido){
	
	$auxCorrecto = false;
	
	if(isset($recibido["peticion"]) && $recibido["peticion"] && isset($recibido["libroBorrar"])){
		
		$auxLibro = $recibido["libroBorrar"];
		if(isset($auxLibro["id"]) && isset($auxLibro["titulo"]) && isset($auxLibro["autor"]) && isset($auxLibro["isbn"]) && isset($auxLibro["anno"])){
			$auxCorrecto = true;
		}
		
	}
	
	
	return $auxCorrecto;
	
}
