package daw2.desarollo.quizz.MySQL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

//En application.properties hay que poner server.servlet.session.tracking-modes=cookie para que no haga cosas raras

@Controller
public class FormsController {

	static final String[] COMIDAS = { "", "Papas Fritas", "Pizza", "Hamburguesa", "Ensalada", "Sopa", "Vegetales" };
	static final String[] AFICCIONES = { "Deportes", "Lectura", "Fiesta", "VideosJuegos" };
	
	private RepoQuizz repo;
	
	public FormsController(RepoQuizz repo) {
		this.repo=repo;
	}
	
	@PostMapping("/")
	String procesaEtapaX(@RequestParam(name = "numEtapa") Integer numEtapa,
			@RequestParam(name = "aficciones", required = false) String aficciones,
			@RequestParam(name = "nombre", required = false) String nombre,
			@RequestParam(name = "comida", required = false) String comida,
			@RequestParam(name= "volver", required = false) String volver,
			@RequestParam(name= "enviar", required = false) String enviar,
			Model modelo,  HttpSession session) {
		
		LocalDateTime hora = LocalDateTime.now();
		
		if (numEtapa == null) {
			return "etapa1";
		}
		
		if(volver != null && !volver.isBlank()) {
			numEtapa--;
			modelo.addAttribute("nombre", session.getAttribute("nombre"));
		}
		
		modelo.addAttribute("comidas", COMIDAS);
		modelo.addAttribute("aficciones", AFICCIONES);
		
		if (session.getAttribute("nombre") != null && (nombre == null || nombre.isBlank())) {
			nombre = (String) session.getAttribute("nombre");
		}
		if (session.getAttribute("comida") != null && (comida == null || comida.isBlank() || comida.equals("0"))) {
			comida = (String) session.getAttribute("comida");
		}
		if (session.getAttribute("aficciones") != null && (aficciones == null || aficciones.isBlank())) {
			aficciones = (String) session.getAttribute("aficciones");
		}


		String errores = "";

		if (numEtapa == 1 && (nombre == null || nombre.isBlank())) {
			errores = "Debes poner un nombre no vacío";
		} else if (numEtapa == 1 && (nombre.length() < 3 || nombre.length() > 10)) {
			errores = "La longitud del nombre debe estar entre 3 y 10";
		} else if (numEtapa == 1) {
			session.setAttribute("nombre", nombre);
		}
		
		
		if (numEtapa == 2 && (comida == null || comida.equals("0"))) {
			errores = "Debes seleccionar un comida";
		} else if (numEtapa == 2) {
			if (!comida.equals("0")) {
				session.setAttribute("comida", comida);
			}
		}

		if (numEtapa == 3 && (aficciones == null || aficciones.isBlank())) {
			errores = "Debes elegir al menos una aficción, no seas soso/a";
		} else if (numEtapa == 3) {
			session.setAttribute("aficciones", aficciones);
		}
		
		if (!errores.isBlank()) {
			modelo.addAttribute("errores", errores);
			modelo.addAttribute("numEtapa", numEtapa);
			return "etapa" + numEtapa;
		}
		
		if(enviar != null && !enviar.isBlank()) {
			numEtapa++;
		}
		
		modelo.addAttribute("numEtapa", numEtapa);
		
		double puntuacion= 0;
		
		if (numEtapa == 4) {
			ArrayList<String> respuestas = new ArrayList<String>();
			respuestas.add("Nombre: "+nombre);
			respuestas.add("Comida Elegida: "+COMIDAS[Integer.parseInt(comida)]);
			respuestas.add("Aficiones: "+aficciones);
			
			
			if(Integer.parseInt(comida)>=4 && Integer.parseInt(comida)<=6) {
				puntuacion+=5;	
			}
			
			
			String[] af=aficciones.split(",");
			for(int i= 0;i<af.length;i++) {
				if(af[i].equals("Deportes") || af[i].equals("Lectura")) {
					puntuacion+=2.5;
				}
			}
			
			System.out.println(puntuacion);
			String mensaje = "";
			if(puntuacion==10) {
				mensaje="Tu puntuacion ha sido de: "+puntuacion+". Felicidades eres muy saludable";
			}
			
			if(puntuacion==5) {
				mensaje="Tu puntuacion ha sido de: "+puntuacion+". Tienes una vida equilibrada";
			}
			
			if(puntuacion==2.5) {
				mensaje="Tu puntuacion ha sido de: "+puntuacion+". Deberias Cuidarte mas";
			}
			
			if(puntuacion==0){
				mensaje="Tu puntuacion ha sido de: "+puntuacion+". Tienes que empezar a cuidarte mas";
			}
			
			Categoria cat;
			if(puntuacion >= 8) {
			    cat = Categoria.ALTA;
			} else if(puntuacion >= 4) {
			    cat = Categoria.MEDIA;
			} else {
			    cat = Categoria.BAJA;
			}
			
			respuestas.add(mensaje);
			modelo.addAttribute("respuestas", respuestas);
			
			Quizz quizz = new Quizz(nombre, puntuacion, hora.format(DateTimeFormatter.ofPattern("dd-MM-uu HH:mm")), cat); 
			
		    List<DetalleRespuesta> detalles = new ArrayList<>();
		    detalles.add(new DetalleRespuesta("Comida", COMIDAS[Integer.parseInt(comida)], quizz));
		    detalles.add(new DetalleRespuesta("Aficciones", aficciones, quizz));

		    quizz.setDetalles(detalles);

		    repo.save(quizz);
		}
		
		
		return "etapa" + numEtapa;
	}

	@GetMapping("/")
	String getEtapa0() {
		return "etapa1";
	}

}
