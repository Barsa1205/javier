package daw2.desarollo.practica.formulario;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ControladorFormularios {

	static ArrayList<String> datos = new ArrayList<>();
	static String error = "";
			
	@GetMapping("/")
	  public String Form() {
	    return "form";
	  }
	
	@PostMapping("/form")
	  public String Form1(@RequestParam("nombre") String nombre, Model model) {
		if (nombre.isBlank() || nombre.length() <=2 ) {
			error ="No puede estar en blanco ni ser de 2 caracteres";
			model.addAttribute("error", error);
			return "form";
		}
		datos.add("Nombre: "+nombre);
	    return "form1";
	  }
	
	@PostMapping("/form1")
	  public String Form2(@RequestParam(value ="opciones", required = false) ArrayList<String> opciones, Model model) {
		String aux = "";
		if (opciones == null) {
			error ="No puede estar sin seleccionar ninguna opcion";
			model.addAttribute("error", error);
			return "form1";
		}
		for (String elemento : opciones) {
		    aux += elemento+" ";
		}

		datos.add("Tiempo Libre: "+aux);
		
		return "form2";
	  }
	
	@PostMapping("/form2")
	  public String result(@RequestParam(value= "genero", required = false) String genero, Model modelo) {
		if (genero == null) {
			error ="No puede estar sin seleccionar ninguna opcion";
			modelo.addAttribute("error", error);
			return "form2";
		}
		datos.add("Genero: "+genero);
		modelo.addAttribute("datos", datos);
	    return "result";
	    
	    
	  }
	
}
