package daw2.desarollo.quizz.MySQL;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class DetalleRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pregunta;
    private String respuesta;

    @ManyToOne
    private Quizz quizz;

    protected DetalleRespuesta() {}

    public DetalleRespuesta(String pregunta, String respuesta, Quizz quizz) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.quizz = quizz;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Quizz getQuizz() {
		return quizz;
	}

	public void setQuizz(Quizz quizz) {
		this.quizz = quizz;
	}

	@Override
	public String toString() {
		return "DetalleRespuesta [id=" + id + ", pregunta=" + pregunta + ", respuesta=" + respuesta + ", quizz=" + quizz
				+ "]";
	}
    
}
