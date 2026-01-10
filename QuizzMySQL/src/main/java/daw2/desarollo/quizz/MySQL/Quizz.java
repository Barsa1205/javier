package daw2.desarollo.quizz.MySQL;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Quizz {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String nombre;
	private double puntuacion;
	private String fecha;
	
	@Enumerated(EnumType.STRING)
    private Categoria categoria;
	
	@OneToMany(mappedBy = "quizz", cascade = CascadeType.ALL)
	private List<DetalleRespuesta> detalles = new ArrayList<>();
	
	public List<DetalleRespuesta> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleRespuesta> detalles) {
		this.detalles = detalles;
	}

	protected Quizz() {}

	public Quizz(String nombre, double puntuacion, String fecha, Categoria categoria) {
		super();
		this.nombre = nombre;
		this.puntuacion = puntuacion;
		this.fecha = fecha;
		this.categoria = categoria;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Quizz [id=" + id + ", nombre=" + nombre + ", puntuacion=" + puntuacion + ", fecha=" + fecha
				+ ", categoria=" + categoria + ", detalles=" + detalles + "]";
	}
	
}
