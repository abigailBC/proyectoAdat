package Modelo;

//Esta clase no está implementada aún
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "autor")
public class Autor implements Serializable {
	/**
	 * Clase sin implementar.
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "fecha_nac")
	private String fecha_nac;

	@Column(name = "pais")
	private String pais;

	public Autor() {

	}

	public Autor(String id, String nombre, String fecha_nac, String pais) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fecha_nac = fecha_nac;
		this.pais = pais;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFecha_nac() {
		return fecha_nac;
	}

	public void setFecha_nac(String fecha_nac) {
		this.fecha_nac = fecha_nac;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	@Override
	public String toString() {
		return "Autor [id=" + id + ", nombre=" + nombre + ", fecha_nac=" + fecha_nac + ", pais=" + pais + "]";
	}

}
