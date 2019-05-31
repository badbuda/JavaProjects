package il.co.ilrd.gatewayserver;

import java.io.IOException;

public class Main {
	  public static void main(String[] args) throws IOException {
	    	GatewayServer gs = new GatewayServer(4, 8002, "127.0.0.1");
	    	GatewayServer.singletonCF.getInstance().add("tweet", new Tweet());
	    }
}
