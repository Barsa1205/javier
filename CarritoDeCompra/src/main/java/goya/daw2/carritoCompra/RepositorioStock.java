package goya.daw2.carritoCompra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/*
 Ejemplo de clase para mantener un mapa de productos, cargarlo de disco en el constructor
 y guardar en disco tras cada cambio.

 De esta manera vuestro controlador puede definir una variable global repositorioStock

 Al ser un mapa no permite repetidos
*/

public class RepositorioStock {
	protected Map<String, Integer> stock;
	protected final static String RUTA_FICHERO = "stock.data";

	public RepositorioStock() {
		super();
		stock = load();
	}

	public Map<String,Integer> load() {
		// TRAZA PARA SABER DÓNDE RESUELVE LA RUTA RELATIVA:
        // System.out.println((new File(RUTA_FICHERO)).getAbsolutePath());
		// intentamos leer el mapa del archivo, si no podemos quedará vacío
		try (
			ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(RUTA_FICHERO))
		) {
			return (HashMap<String, Integer>) entrada.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("Problema leyendo archivo " + RUTA_FICHERO);
             return new HashMap<String,Integer>();
		}
       
	}

	public boolean save() {
		try (
				ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(RUTA_FICHERO))
		) {
			salida.writeObject(stock);
		} catch (IOException e1) {
			System.err.println("ERROR GUARDANDO STOCK");
			return false;
		}
		return true;
	}

	public Map<String, Integer> getAll() {
		return stock;
	}

	public Integer getOne(String producto) {
		return stock.get(producto);
	}

	public void add(String producto, Integer cantidad) {
		stock.put(producto, cantidad);
		save();
	}

	public void del(String producto) {
	        stock.remove(producto);
	    	save();
		}

	public void modify(String producto, Integer cantidad) {
			stock.put(producto, cantidad);
	    	save();
		}

    // Prueba de concepto para ejecutar desde Eclipe o Visual
    
	/*
	 * public static void main(String[] args) { RepositorioStock repo = new
	 * RepositorioStock(); System.out.println(repo.getAll()); repo.add("Peras",10);
	 * repo.add("Manzanas", 14); repo.save();
	 * System.out.println(repo.getOne("Peras"));
	 * 
	 * }
	 */
    

}
