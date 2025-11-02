package goya.daw2.pruebas_plantillas;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FormsController {

	static final String[] SIGNOS = { "", "Aries", "Tauro", "Géminis", "Cáncer", "Leo", "Virgo", "Libra", "Escorpio",
			"Sagitario", "Capricornio", "Acuario", "Piscis" };
	static final String[] AFICCIONES = { "Deportes", "Juerga", "Lectura", "Relaciones sociales" };

	@PostMapping("/")
	String procesaEtapaX(@RequestParam(name = "numEtapa") Integer numEtapa,
			@RequestParam(name = "aficciones", required = false) String aficciones,
			@RequestParam(name = "nombre", required = false) String nombre,
			@RequestParam(name = "signo", required = false) String signo,
			@CookieValue(name = "nombre", required = false) String cookieNombre,
			@CookieValue(name = "signo", required = false) String cookieSigno,
			@CookieValue(name = "aficciones", required = false) String cookieAficciones, Model modelo,  HttpServletResponse response) {
		
		if (numEtapa == null)
			return "etapa1";
		
		modelo.addAttribute("signos", SIGNOS);
		modelo.addAttribute("aficciones", AFICCIONES);
		
		if (cookieNombre != null && (nombre == null || nombre.isBlank())) {
			nombre = URLDecoder.decode(cookieNombre, StandardCharsets.UTF_8);
		}
		if (cookieSigno != null && (signo == null || signo.isBlank() || signo.equals("0"))) {
			signo = URLDecoder.decode(cookieSigno, StandardCharsets.UTF_8);
		}
		if (cookieAficciones != null && (aficciones == null || aficciones.isBlank())) {
			aficciones = URLDecoder.decode(cookieAficciones, StandardCharsets.UTF_8);
		}


		String errores = "";

		if (numEtapa == 1 && (nombre == null || nombre.isBlank())) {
			errores = "Debes poner un nombre no vacío";
		} else if (numEtapa == 1 && (nombre.length() < 3 || nombre.length() > 10)) {
			errores = "La longitud del nombre debe estar entre 3 y 10";
		} else if (numEtapa == 1) {
			response.addCookie(new Cookie("nombre", URLEncoder.encode(nombre, StandardCharsets.UTF_8)));
		}
		
		
		if (numEtapa == 2 && (signo == null || signo.equals("0"))) {
			errores = "Debes seleccionar un signo";
		} else if (numEtapa == 2) {
			if (!signo.equals("0")) {
				response.addCookie(new Cookie("signo", URLEncoder.encode(signo, StandardCharsets.UTF_8)));
			}
		}

		if (numEtapa == 3 && (aficciones == null || aficciones.isBlank())) {
			errores = "Debes elegir al menos una aficción, no seas soso/a";
		} else if (numEtapa == 3) {
			response.addCookie(new Cookie("aficciones", URLEncoder.encode(aficciones, StandardCharsets.UTF_8)));
		}

		if (!errores.isBlank()) {
			modelo.addAttribute("errores", errores);
			modelo.addAttribute("numEtapa", numEtapa);
			return "etapa" + numEtapa;
		}

		numEtapa++;
		modelo.addAttribute("numEtapa", numEtapa);

		if (numEtapa == 4) {
			ArrayList<String> respuestas = new ArrayList<String>();
			respuestas.add(nombre);
			respuestas.add(SIGNOS[Integer.parseInt(signo)]);
			respuestas.add(aficciones);
			modelo.addAttribute("respuestas", respuestas);
		}

		return "etapa" + numEtapa;
	}

	@GetMapping("/")
	String getEtapa0() {
		return "etapa1";
	}

}
