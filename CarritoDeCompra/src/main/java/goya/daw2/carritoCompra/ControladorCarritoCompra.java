package goya.daw2.carritoCompra;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;





@Controller
public class ControladorCarritoCompra {

	RepositorioStock stock= new RepositorioStock();
	
	public ControladorCarritoCompra() {
		stock.add("platanos", 10);
		stock.add("fresas", 10);
		stock.add("kiwis", 10);
	}
	
	@GetMapping("/")
	public String quienEres() {
		return "quienEres";
	}
	
	@PostMapping("quienEres")
	public String entradaTienda(@RequestParam(name="nombre", required=false) String nombre, Model modelo, HttpSession session) {
		if (nombre==null || nombre.isBlank()) {
			modelo.addAttribute("error", "El nombre no puede estar en blanco");
			return "quienEres";
		}
		session.setAttribute("nombre", nombre);
		modelo.addAttribute("nombre", nombre);
		return "entradaTienda" ;
	}
	
	@GetMapping("tienda")
	public String mostrarCarritoCompra(Model modelo, HttpSession session) {
		modelo.addAttribute("kiwis", stock.getOne("kiwis"));
		modelo.addAttribute("platanos", stock.getOne("platanos"));
		modelo.addAttribute("fresas", stock.getOne("fresas"));
		modelo.addAttribute("saludo", "¡Bienvenido " + session.getAttribute("nombre") + "!" );
		return "carritoCompra";
	}
	
	@GetMapping("reponer")
	public String mostrarReponerAdmin(Model modelo) {
		modelo.addAttribute("kiwis", stock.getOne("kiwis"));
		modelo.addAttribute("platanos", stock.getOne("platanos"));
		modelo.addAttribute("fresas", stock.getOne("fresas"));
		return "reponer";
	}
	
	@PostMapping("reponer")
	public String procesarReponerAdmin(@RequestParam(name="kiwis", required=false) String kiwis,
							   @RequestParam(name="platanos", required=false) String platanos,
							   @RequestParam(name="fresas", required=false) String fresas,
							   Model modelo) {
		
		int kiwisNum = 0;
		int platanosNum = 0;
		int fresasNum = 0;
		
		if (kiwis != null && !kiwis.isBlank()) {
			kiwisNum = Integer.parseInt(kiwis);
		}
		if (platanos != null && !platanos.isBlank()) {
			platanosNum = Integer.parseInt(platanos);
		}
		if (fresas != null && !fresas.isBlank()) {
			fresasNum = Integer.parseInt(fresas);
		}
		
		if(kiwisNum>0) { 
			stock.add("kiwis", kiwisNum);
		}
		if(platanosNum>0) {  
			stock.add("platanos", platanosNum);
		}
		if(fresasNum>0) {  
			stock.add("fresas", fresasNum);
		}
		
		modelo.addAttribute("kiwis", stock.getOne("kiwis"));
		modelo.addAttribute("platanos", stock.getOne("platanos"));
		modelo.addAttribute("fresas", stock.getOne("fresas"));
		return "reponer";
	}
	
	
	@PostMapping("carritoCompra")
	public String procesarCarritoCompra(@RequestParam(name="act", required=false) String actualizar,
								@RequestParam(name="fin", required=false) String finalizar,
								@RequestParam(name="kiwis", required=false) String kiwis,
								@RequestParam(name="platanos", required=false) String platanos,
								@RequestParam(name="fresas", required=false) String fresas,
								Model modelo, HttpSession session) {
		
		
		int rsdo=0;
		int kiwisNum = 0;
		int platanosNum = 0;
		int fresasNum = 0;
		boolean exito = false;
		
		if (kiwis != null && !kiwis.isBlank()) {
			kiwisNum = Integer.parseInt(kiwis);
		}
		if (platanos != null && !platanos.isBlank()) {
			platanosNum = Integer.parseInt(platanos);
		}
		if (fresas != null && !fresas.isBlank()) {
			fresasNum = Integer.parseInt(fresas);
		}
		
		
		if (actualizar!=null && actualizar.equals("act")) {
			if(platanos.equals("00") && fresas.equals("000") && kiwis.equals("0")) {
				modelo.addAttribute("kiwis", stock.getOne("kiwis"));
				modelo.addAttribute("platanos", stock.getOne("platanos"));
				modelo.addAttribute("fresas", stock.getOne("fresas"));
				return "reponer";
			}
			
			if(kiwisNum<0) { 
				kiwisNum = kiwisNum * -1;
				rsdo = stock.getOne("kiwis") + kiwisNum; 
				stock.modify("kiwis", rsdo);
				
			}
			if(platanosNum<0) { 
				platanosNum = platanosNum * -1;
				rsdo = stock.getOne("platanos") + platanosNum; 
				stock.modify("platanos", rsdo);
				
			}
			if(fresasNum<0) { 
				fresasNum = fresasNum * -1;
				rsdo = stock.getOne("fresas") + fresasNum; 
				stock.modify("fresas", rsdo);
				
			}
			if (rsdo==0 && (fresasNum>=0 || kiwisNum>=0 || fresasNum>=0)) {
				modelo.addAttribute("error", "Para actualizar los valores deben ser negativos");
			}
			
		}
		
		if(finalizar!=null && finalizar.equals("fin")) {
			if(kiwisNum>0) {
				if(kiwisNum<=stock.getOne("kiwis")) {
					rsdo = stock.getOne("kiwis") - kiwisNum;
					stock.modify("kiwis", rsdo);
					exito=true;
				}
			}
			if(platanosNum>0) {
				if(platanosNum<=stock.getOne("platanos")) {
					rsdo = stock.getOne("platanos") - platanosNum;
					stock.modify("platanos", rsdo);
					exito=true;
				}
			}
			if(fresasNum>0) {
				if(fresasNum<=stock.getOne("fresas")) {
					rsdo = stock.getOne("fresas") - fresasNum;
					stock.modify("fresas", rsdo);
					exito=true;
				}
			}
			if (!exito) {
				modelo.addAttribute("mensaje", "Lo siento " + session.getAttribute("nombre") + ", ha ocurrido un error y la compra no ha sido posible");
			}else {
				modelo.addAttribute("mensaje", "¡Felicidades " + session.getAttribute("nombre") + ", la compra ha sido realizada con exito!");
			}
			modelo.addAttribute("kiwis", stock.getOne("kiwis"));
			modelo.addAttribute("platanos", stock.getOne("platanos"));
			modelo.addAttribute("fresas", stock.getOne("fresas"));
			return "rsdoCompra";
		}
		
		modelo.addAttribute("saludo", "¡Bienvenido " + session.getAttribute("nombre") + "!" );
		modelo.addAttribute("kiwis", stock.getOne("kiwis"));
		modelo.addAttribute("platanos", stock.getOne("platanos"));
		modelo.addAttribute("fresas", stock.getOne("fresas"));
		return "carritoCompra";
	}
	
	
}
