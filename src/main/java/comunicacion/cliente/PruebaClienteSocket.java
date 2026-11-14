package comunicacion.cliente;

public class PruebaClienteSocket {

    public static void main(String[] args) {

        ClienteSocket cliente =
                new ClienteSocket();

        cliente.conectar();
    }
}