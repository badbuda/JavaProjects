package il.co.ilrd.gatewayserver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class GatewayServer{
    private HttpServer server;
    private static ExecutorService executor;

    public enum States {
        OK("Ok youre good to go",  200, ""),
        NO_STATUS("STATUS IS NULL",  400, ""),
        NO_TOKEN("NO SUCH TOKEN",  400, " "),
        NO_SUCH_ACTION("NO SUCH ACTION",  400, ""),
        UNKNOWN(" ",  000, " ");

        private final String description;
        private int errorNumber;
        private String extraData;
        
        private States(String description, int errorNumber, String extraData) {
            this.description = description;
            this.errorNumber = errorNumber;
        }

        public String getDescription() {
            return description;
        }

        public int getErrorNumber() {
            return errorNumber;
        }
        
        public void setErrorNumber(int errorNumber) {
            this.errorNumber = errorNumber;
        }
        
        public String extraData() {
            return extraData;
        }
        
        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }
    }
    
    private static JsonObject jsonCreator(final States status) {
    	String json = "{\"results\":[{\"states\":\"value\",\"errorNumber\":\"value\" }, { \"states\":\"value\", \"errorNumber\":\"value\"}]}";
        Gson gson = new Gson();
		JsonObject inputObj  = gson.fromJson(json, JsonObject.class);
        JsonObject newObject = new JsonObject() ;
        newObject.addProperty("states", status.getDescription());
        newObject.addProperty("errorNumber", status.getErrorNumber());
        newObject.addProperty("extraData", status.extraData());
        inputObj.get("results").getAsJsonArray().add(newObject);

        return newObject;
    }
    
    public GatewayServer(int numOfThreads, int port, String Ip) throws IOException {    	
    	executor = Executors.newFixedThreadPool(numOfThreads);
        server = HttpServer.create(new InetSocketAddress(Ip, port), 0);
        server.createContext("/test", new MyHandler());
        server.start();
    }
  
	public static States threadpool (HttpExchange exchange) throws InterruptedException, ExecutionException {

        Future<States> f = executor.submit (()-> {
        	JsonObject jo = (JsonObject) new JsonParser().parse(new InputStreamReader(exchange.getRequestBody()));
    		return singletonCF.getInstance().runRequest(jo.get("Command Type").getAsString(), jo.getAsJsonObject("Data"), exchange);
    	});
    	
		return f.get();	
    }
    
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            JsonObject newObject;

            String response;
        	States status = null;
			try {
				status = threadpool(exchange);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			newObject = jsonCreator(status);
		
        	response = newObject.toString();
        	
    		exchange.sendResponseHeaders(status.getErrorNumber(), response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
        }
    }
    
	static class singletonCF{
		private static final singletonCF instance = new singletonCF();
		
		private final HashMap<String, Commandable> hashmap = new HashMap<String, Commandable>();
	
	    public void add(String key, Commandable function) {
		    hashmap.put(key, function);
	    }
	    
	    public States runRequest(String key, JsonObject jo, HttpExchange exchange) throws IOException {
	    	if (hashmap.get(key) == null) {
	    		States.NO_SUCH_ACTION.setExtraData("key isnt Valid");
	    		return States.NO_SUCH_ACTION;
	    	}
	    	else {
	    		return hashmap.get(key).action(jo, exchange);
	    	}
	    }
		
	    private singletonCF() {}
		
		public static singletonCF getInstance() {
			return instance;
		}
	}
}
