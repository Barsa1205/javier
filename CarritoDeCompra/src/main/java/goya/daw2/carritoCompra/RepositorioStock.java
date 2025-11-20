package goya.daw2.carritoCompra;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
			return (LinkedHashMap<String, Integer>) entrada.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("Problema leyendo archivo " + RUTA_FICHERO);
             return new LinkedHashMap<String,Integer>();
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
}
