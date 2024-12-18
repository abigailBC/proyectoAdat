DROP DATABASE IF EXISTS bdproyectoIntegrador;
CREATE DATABASE IF NOT EXISTS bdproyectoIntegrador;
USE bdproyectoIntegrador;

CREATE TABLE Libro (
    id VARCHAR(255) PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    anno INT NOT NULL
);

INSERT INTO Libro (id, titulo, autor, isbn, anno) VALUES
('1', 'Cien años de soledad', 'Gabriel García Márquez', '9780307474728', 1967),
('2', 'Don Quijote de la Mancha', 'Miguel de Cervantes', '9780060934347', 1605),
('3', '1984', 'George Orwell', '9780451524935', 1949),
('4', 'El principito', 'Antoine de Saint-Exupéry', '9780156012195', 1943),
('5', 'Crimen y castigo', 'Fiódor Dostoyevski', '9780140449136', 1866),
('6', 'Orgullo y prejuicio', 'Jane Austen', '9780141439518', 1813),
('7', 'Matar a un ruiseñor', 'Harper Lee', '9780061120084', 1960),
('8', 'La Odisea', 'Homero', '9780140268867', -800),
('9', 'En busca del tiempo perdido', 'Marcel Proust', '9780142437964', 1913),
('10', 'Ulises', 'James Joyce', '9780141182803', 1922);
