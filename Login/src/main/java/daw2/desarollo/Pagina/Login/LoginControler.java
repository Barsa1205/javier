package daw2.desarollo.Pagina.Login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

//Hace falta añadir esta linea al build.gradle: implementation 'org.springframework.security:spring-security-crypto'   para que funcione lo de hashear

@Controller
public class LoginControler {
	
	//Map donde estaran los usuarios y contraseñas
	Map<String, String> datos = new HashMap<>();
	
	//Constructor donde guardo los usuarios y sus contraseñas
	public LoginControler() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		datos.put("usuario1", encoder.encode("Cesar"));
		datos.put("password1", encoder.encode("12345"));
		datos.put("usuario2", encoder.encode("Manuel"));
		datos.put("password2", encoder.encode("54321"));
	}
	
	//Mapping donde muestro el login siempre que no se haya iniciado sesion
	@GetMapping("login")
	public String mostrarLogin(HttpSession session, Model modelo ) {
		if (session.getAttribute("usuario") != null && session.getAttribute("password") != null) {
			modelo.addAttribute("usuario", session.getAttribute("usuario"));
			return "inicio";
		}
		return "login";
	}
	
	//Mapping donde muestro el inicio siempre que se haya iniciado sesion
	@GetMapping("/")
	public String mostrarInicio(HttpSession session, Model modelo) {
		if (session.getAttribute("usuario") != null && session.getAttribute("password") != null) {
			modelo.addAttribute("usuario", session.getAttribute("usuario"));
			return "inicio";
		}
		return "redirect:/login";
	}
	
	//Mapping donde muestro la pagina 1 siempre que se haya iniciado sesion
	@GetMapping("pag1")
	public String mostrarPag1(HttpSession session) {
		if (session.getAttribute("usuario") != null && session.getAttribute("password") != null) {
			return "pag1";
		}
		return "redirect:/login";
	}
	
	//Mapping donde muestro la pagina 2 siempre que se haya iniciado sesion
	@GetMapping("pag2")
	public String mostrarPag2(HttpSession session) {
		if (session.getAttribute("usuario") != null && session.getAttribute("password") != null) {
			return "pag2";
		}
		return "redirect:/login";
	}
	
	//Mapping donde cierro sesion y lo redirijo al login
	@GetMapping("cerrar")
	public String cerrarSesion(HttpSession session) {
		session.setAttribute("usuario", null);
		session.setAttribute("password", null);
		return "redirect:/login";
	}
	
	//Mapping donde compruebo si estan bien el usuario y contraseña
	@PostMapping("login")
	public String procesarInfo(@RequestParam(name="usuario", required=false) String usuario, 
							   @RequestParam(name="password", required=false) String password,
							   HttpSession session, Model modelo) {
		
		//Variables, y endocer para hashear
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String error= "";
		boolean usuarioCorrecto = false;
		boolean passwordCorrecto = false;
		int numUsuario = 0;
		
		
		//Comprobaciones
		
		//Que no este ni null ni blanco el usuario y contraseña
		if ((usuario == null || usuario.isBlank()) || (password == null || password.isBlank())) {
			error="No pueden estar en blanco ni la contraseña ni el usuario";
		}
		
		//Si no hay ningun error compruebo si el usuario y contraseña que se introdujo es valido
		if (error.isBlank()) {
			for(int i=1;i<=datos.size();i++) {
				if(encoder.matches(usuario, datos.get("usuario"+i))) {
					usuarioCorrecto=true;
					numUsuario=i;
				}
				if(encoder.matches(password, datos.get("password"+i))) {
					if(i==numUsuario) {
						passwordCorrecto=true;
					}
				}
			}
		}
		
		
		if ((!usuarioCorrecto || !passwordCorrecto) && error.isBlank()) {
			error="El usuario y contraseña no son correctos";
		}
		
		//Si no hay errores guardo el usuario y contraseña en session
		if (error.isBlank()){
			session.setAttribute("usuario", usuario);
			session.setAttribute("password", password);
		}
		
		//si hay algo mal lo devuelvo al login con el error
		if(!error.isBlank()) {
			modelo.addAttribute("error", error);
			return "login";
		}
		
		
		modelo.addAttribute("usuario", session.getAttribute("usuario"));
		return "inicio";
	}
}
