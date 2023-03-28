package ma.enset.tp2sma.Presentation;

import ma.enset.tp2sma.controller.ClientController;
import ma.enset.tp2sma.controller.VendeurController;

public class SingletonControllers {
   private static SingletonControllers instance = new SingletonControllers();

    private VendeurController vendeurCtr;
    private ClientController clientCtr;

    public SingletonControllers() {}

    public static SingletonControllers getInstance() {
        return instance;
    }


    public VendeurController getVendeurCtr() {
        return vendeurCtr;
    }

    public void setVendeurCtr(VendeurController vendeurCtr) {
        this.vendeurCtr = vendeurCtr;
    }

    public ClientController getClientCtr() {
        return clientCtr;
    }

    public void setClientCtr(ClientController clientCtr) {
        this.clientCtr = clientCtr;
    }
}
