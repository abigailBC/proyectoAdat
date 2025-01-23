-- TABLA DE DATOS APROBADA POR JAIME ###########################################################################################################
DROP DATABASE IF EXISTS bdproyectoIntegrador;
CREATE DATABASE IF NOT EXISTS bdproyectoIntegrador;
USE bdproyectoIntegrador;

/*LIBRO 1---N PREMIO */ 
-- Crear tabla Libro 
-- cREAR TABLA PREMIO 
-- un <libro> tiene muchos <premios>, PERO cada premio pertenece a un único libro-- Ok 
CREATE TABLE Libro (
    id VARCHAR(255) PRIMARY KEY, -- clave de mi Libro 
    titulo VARCHAR(255) NOT NULL, -- no pueden contener valor nulo 
    autor VARCHAR(255) NOT NULL, -- no puede contener valor nulo
    isbn VARCHAR(13) NOT NULL, -- no puede contener valor nulo 
    anno INT NOT NULL, -- no puede contener valor nulo 
    categoria VARCHAR(50) NULL -- opcional de incluir / puede valor nulo 
);

CREATE TABLE Premio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libro_id VARCHAR(255) NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(122) NULL,
    anno INT NOT NULL,
    FOREIGN KEY (libro_id) REFERENCES Libro(id) ON DELETE SET NULL
);


-- ok ########################################################################################################################
commit; 
-- EJEMPLOS DE INSERTSSSSSSSSSSSSSSSSSSSSSSSSSSSS
INSERT INTO Libro (id, titulo, autor, isbn, anno, categoria) VALUES ('K1', 'El Quijote', 'Miguel de Cervantes', '9781234567890', 1605, 'Novela');
INSERT INTO Libro (id, titulo, autor, isbn, anno, categoria) VALUES ('B02', 'Cien Años de Soledad', 'Gabriel García Márquez', '9789876543210', 1967, 'Realismo Mágico');
INSERT INTO Libro (id, titulo, autor, isbn, anno, categoria) VALUES ('IB03', '1984', 'George Orwell', '9781122334455', 1949, 'Distopía');
INSERT INTO Libro (id, titulo, autor, isbn, anno, categoria) VALUES ('LIB04', 'Orgullo y Prejuicio', 'Jane Austen', '9789988776655', 1813, 'Romance');
INSERT INTO Libro (id, titulo, autor, isbn, anno, categoria) VALUES ('LIB05', 'Matar a un Ruiseñor', 'Harper Lee', '9785566778899', 1960, 'Drama');
INSERT INTO Libro (id, titulo, autor, isbn, anno, categoria) VALUES ('GSZZ', 'MADRIZ IN SPAIN', 'Geoff zz', '9785566778899', 1960, 'Drama');
INSERT INTO Libro (id, titulo, autor, isbn, anno, categoria) VALUES ('D45K', 'SPAIN IN MADRID', 'Gkind', '9785566778899', 1930, 'Drama');
-- LIBRO Y PREMIO 
INSERT INTO Premio (libro_id, nombre, descripcion, anno) VALUES ('GSZZ', 'Premio Nobel de Literatura', 'Premio internacional a la mejor obra de literatura', 1905);
INSERT INTO Premio (libro_id, nombre, descripcion, anno) VALUES ('LIB05', 'Premio Nobel de Literatura', 'Premio internacional a la mejor obra de literatura', 1982);
INSERT INTO Premio (libro_id, nombre, descripcion, anno) VALUES ('B02', 'Premio Hugo', 'Premio de novela de ciencia ficcion ', 1950);
INSERT INTO Premio (libro_id, nombre, descripcion, anno) VALUES ('GSZZ', 'Premio Booker', 'Premio otorgado por Abigail ', 1970);
INSERT INTO Premio (libro_id, nombre, descripcion, anno) VALUES ('IB03', 'Premio Pulitzer','Premio internacional otorgado por geff', 1961);

select * from Premio;
select * from libro;
-- ####################################################################################################################### ok



-- PROBAR Y COMPROBAR RELACIONES 
SELECT Libro.id, Libro.titulo, Premio.nombre AS premio, Premio.anno, premio.descripcion
FROM Libro
JOIN Premio ON Libro.id = Premio.libro_id;
 -- Este resultado muestra los libros que tienen premios, junto con los nombres de los premios y los años en que fueron otorgados.




commit;