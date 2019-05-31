package il.co.ilrd.sql;


import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonObject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientMonitor implements Observer{
	//private HttpPost post;
	private final String url;
	private final String CommandType;
	private final String Company;
	private final String Product;
	private final String Serial;
	
	public HttpClientMonitor(String url , String CommandType, String Company, String Product, String SerialNumber) {
		this.CommandType = (CommandType == null ? "" : CommandType);
		this.Company = (Company == null ? "" : Company);
		this.Product = (Product == null ? "" : Product);
		Serial = (SerialNumber == null ? "" : SerialNumber);
		this.url = url;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		try {
			String updateThisData = arg.toString();
			CloseableHttpClient client =  HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			JsonObject jsonObjectForRequest = createJsonRequest(CommandType, Company, Product, Serial, updateThisData);
			
			StringEntity entity = new StringEntity(jsonObjectForRequest.toString());
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			
			client.execute(httpPost);

			client.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JsonObject createJsonRequest(String commandType, String Company, String Product, String SerialNu, String DataToParse) {
		JsonObject jsonToUser =  new JsonObject();
		
		jsonToUser.addProperty("commandType", commandType);
		jsonToUser.addProperty("Company", Company);
		jsonToUser.addProperty("Product", Product);
		jsonToUser.addProperty("SerialNumber", SerialNu);
		jsonToUser.addProperty("Data", DataToParse);
		
		return jsonToUser;
	}
}

