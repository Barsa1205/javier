package goya.daw2.carritoCompra;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorCarritoCompra {
	
	//variables Globales
	RepositorioStock stock= new RepositorioStock();
	FileLog fileLog =new FileLog();
	String[] productos;
	double[] preciosProductos= {3.2, 1.5, 6.8};
	
	public ControladorCarritoCompra(HttpSession session) {
		//Añado los productos al stock
		stock.add("platanos", 10);
		stock.add("fresas", 10);
		stock.add("kiwis", 10);
		
		//Añado a la lista los nombre de los productos para hacer mas facil las comprobaciones
		productos = stock.getAll().keySet().toArray(new String[0]);
		
		//borro el log para que solo existan las compras que se hagan desde que se ejecuta y no tenga viejas de otras ejecuciones
		File log = new File("compras.log");

	    if (log.exists() && log.length() > 0) {
	    	fileLog.borrarLog();
	    }
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
		
		//Variables
		double total=0;
		double aux=0;
		
		Integer[] productosEnCarrito = new Integer[productos.length];
		
		//Comprobacion para que no sea null la session
		for (String p : productos) {
	        if (session.getAttribute("carrito_" + p) == null) {
	            session.setAttribute("carrito_" + p, 0);
	        }
	    }
		
		//Pasar los datos del carrito a un array para mostrarlo y calcular el precio total
		int i= 0;
		for (String p : productos) {
			//Datos del carrito en el array
			productosEnCarrito[i] = (Integer)session.getAttribute("carrito_" + p);
			
			//Calcular precio total
			aux = preciosProductos[i] * (Integer)session.getAttribute("carrito_" + p);
	    	total += aux;
			i++;
		}
		//Evito que el total tenga mas de 2 decimales
		String totalFormateado = String.format("%.2f", total);
		
		modelo.addAttribute("precioTotal", totalFormateado);
		modelo.addAttribute("stock", stock.getAll());
		modelo.addAttribute("precios", preciosProductos);
		modelo.addAttribute("enCarrito", productosEnCarrito);
		modelo.addAttribute("saludo", "¡Bienvenido " + session.getAttribute("nombre") + "!" );
		return "carritoCompra";
	}
	
	@GetMapping("reponer")
	public String mostrarReponerAdmin(Model modelo, HttpSession session) {		
		modelo.addAttribute("stock", stock.getAll());
		modelo.addAttribute("saludo", "¡Bienvenido Admin " + session.getAttribute("nombre") + "!");
		return "reponer";
	}
	
	@PostMapping("reponer")
	public String procesarReponerAdmin(@RequestParam Map<String,String> cantidadesIntro, Model modelo, HttpSession session) {
		
		//Variables
		int num = 0;
		Map<String,Integer> cantidadesIntroNums = new HashMap<>();
		
		//Pasar de string a int los valores introducidos
		for (String p : productos) {
			num=0;
	        if (cantidadesIntro.get(p) != null && !cantidadesIntro.get(p).isBlank()) {
	            num = Integer.parseInt(cantidadesIntro.get(p));
	        }
	        cantidadesIntroNums.put(p, num);
	    }
		
		//Añadir los datos nuevos 
		for (String p : productos) {
			if (cantidadesIntroNums.get(p) > 0) {
				stock.add(p, cantidadesIntroNums.get(p));
            }
		}
		
		modelo.addAttribute("stock", stock.getAll());
		modelo.addAttribute("saludo", "¡Bienvenido Admin " + session.getAttribute("nombre") + "!");
		return "reponer";
	}
	
	
	@PostMapping("carritoCompra")
	public String procesarCarritoCompra(
	        @RequestParam(name="act", required=false) String actualizar,
	        @RequestParam(name="fin", required=false) String finalizar,
	        @RequestParam Map<String,String> cantidadesIntro,
	        Model modelo, HttpSession session) {
		
		//Variables
		int num = 0;
		double total = 0;
		int enCarrito = 0;
		int cantidadCar = 0;
		boolean exito = true;
		boolean carritoVacio = true;
		
		Integer[] productosEnCarrito = new Integer[productos.length];
	    Map<String,Integer> cantidadesIntroNums = new HashMap<>();
	    
	    //Comprobacion para que no sea null la session
	    for (String p : productos) {
	        if (session.getAttribute("carrito_" + p) == null) {
	            session.setAttribute("carrito_" + p, 0);
	        }
	    }
	    
	    //Pasar de string a int los valores introducidos
	    for (String p : productos) {
	    	num=0;
	        if (cantidadesIntro.get(p) != null && !cantidadesIntro.get(p).isBlank()) {
	            num = Integer.parseInt(cantidadesIntro.get(p));
	        }
	        cantidadesIntroNums.put(p, num);
	    }

	    //Actualizar
	    if (actualizar != null && actualizar.equals("act")) {
	       
	    	//Caso para entrar a reponer sin tocar la url
	    	if (cantidadesIntro.get("platanos").equals("00") && cantidadesIntro.get("fresas").equals("000") && cantidadesIntro.get("kiwis").equals("0")) {
	        	modelo.addAttribute("stock", stock.getAll());
	        	modelo.addAttribute("saludo", "¡Bienvenido Admin " + session.getAttribute("nombre") + "!");
	        	return "reponer";
	        }

	    	//Actualizar carrito
	        for (String p : productos) {
	            enCarrito = (Integer) session.getAttribute("carrito_" + p);

	            if (cantidadesIntroNums.get(p) > 0) {
	                enCarrito += cantidadesIntroNums.get(p);
	                
	                if (enCarrito > stock.getOne(p)) {
	                	enCarrito = stock.getOne(p);
	                }
	            }
	            
	            if (cantidadesIntroNums.get(p) < 0) {
	                enCarrito += cantidadesIntroNums.get(p);
	               
	                if (enCarrito < 0) {
	                	enCarrito = 0; 
	                }
	            }

	            session.setAttribute("carrito_" + p, enCarrito);
	        }
	        
	        //Pasar los datos del carrito a un array para mostrarlo y calcular el precio total
	        int i= 0;
	        double aux = 0; 
		    for (String p : productos) {
		    	
		    	//Datos del carrito en el array
		    	productosEnCarrito[i] = (Integer)session.getAttribute("carrito_" + p);
		    	
		    	//Calcular precio total
		    	aux = preciosProductos[i] * (Integer)session.getAttribute("carrito_" + p);
		    	total += aux;
		    	i++;
			}
			//Evito que el total tenga mas de 2 decimales
		    String totalFormateado = String.format("%.2f", total);
		    
		    modelo.addAttribute("precioTotal", totalFormateado);
		    modelo.addAttribute("enCarrito", productosEnCarrito);
	    }

	    //Finalizar compra
	    if (finalizar != null && finalizar.equals("fin")) {
	        
	    	//Comprueba si se puede hacer la compra
	    	for (String p : productos) {
	            cantidadCar = (Integer) session.getAttribute("carrito_" + p);

	            if (cantidadCar > 0) {
	            	carritoVacio = false;
	            }
	        }
	    	if (carritoVacio) {
                exito = false;
            }
	    	
	    	//Resultado de la compra
	        if (!exito) {
	            modelo.addAttribute("mensaje", "Lo siento " + session.getAttribute("nombre") + ", ha ocurrido un error y la compra no ha sido posible");
	        } else {
	        	
	        	//Pasar los datos del carrito a un array para mostrarlo y calcular el precio total
	        	int i= 0;
		        double aux = 0; 
			    for (String p : productos) {
			    	
			    	//Datos del carrito en el array
			    	productosEnCarrito[i] = (Integer)session.getAttribute("carrito_" + p);
			    	
			    	//Calcular precio total
			    	aux = preciosProductos[i] * (Integer)session.getAttribute("carrito_" + p);
			    	total += aux;
			    	i++;
				}
				//Evito que el total tenga mas de 2 decimales
			    String totalFormateado = String.format("%.2f", total);
			    
	            modelo.addAttribute("mensaje", "¡Felicidades " + session.getAttribute("nombre") + ", la compra ha sido realizada con éxito!");
	            modelo.addAttribute("enCarrito", productosEnCarrito);
	            modelo.addAttribute("precioTotal", totalFormateado);
	            
	            //Agrego la persona que compro algo y el que compro
	            fileLog.registrarCompra((String)session.getAttribute("nombre"), productos, session);
	            
	            //pongo a 0 los datos del carrito y modifico el stock porque la compra ha salido exitosa
	            for (String p : productos) {
	            	
	            	//modifico el stock
	            	cantidadCar = (Integer) session.getAttribute("carrito_" + p);
	            	stock.modify(p, stock.getOne(p) - cantidadCar);
		            
	            	//Pongo a 0 el carrito
	            	session.setAttribute("carrito_" + p, 0);
		        }
	        }
	        modelo.addAttribute("stock", stock.getAll());
	        return "rsdoCompra";
	    }
	    
	    modelo.addAttribute("precios", preciosProductos);
	    modelo.addAttribute("stock", stock.getAll());
	    modelo.addAttribute("saludo", "¡Bienvenido " + session.getAttribute("nombre") + "!");
	    return "carritoCompra";
	}
	
}
