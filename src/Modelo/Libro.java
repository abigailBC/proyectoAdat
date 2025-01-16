package Modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;


//Marcas de hibernate en el fichero de la clase. Así no hace falta crear un fichero Hibernate por cada clase.
@Entity
@Table(name = "libro")

public class Libro implements Serializable {
	
//PRIMARY KEY
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "autor")
	private String autor;
	
	@Column(name = "isbn")
	private String isbn;
	
	@Column(name = "anno")
	private int anno;
	
	private static final long serialVersionUID = 1L;

	public Libro(String id, String titulo, String autor, String isbn, int anno) {
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.anno = anno;
	}

	public Libro() {
	}

	public String getId() {
		return id;
	}

	public String setId(String id) {
		this.id = id;
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	@Override
	public String toString() {
		return "Libro [id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", isbn=" + isbn + ", año=" + anno + "]";
	}

}