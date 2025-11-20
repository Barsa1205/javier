package goya.daw2.carritoCompra;

import java.io.FileWriter;
import java.io.IOException;
import jakarta.servlet.http.HttpSession;

public class FileLog {

    private final String LOG_PATH = "compras.log";

    public void registrarCompra(String usuario, String[] productos, HttpSession session) {

        StringBuilder sb = new StringBuilder();

        sb.append(usuario).append(" - ");

        for (String p : productos) {
            int cantidad = (int) session.getAttribute("carrito_" + p);
            sb.append(p).append("=").append(cantidad).append(" ");
        }

        sb.append("\n");

        try (FileWriter fw = new FileWriter(LOG_PATH, true)) {
            fw.write(sb.toString());
        } catch (IOException e) {}
    }
    
    public void borrarLog() {
        try (FileWriter fw = new FileWriter(LOG_PATH, false)) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
