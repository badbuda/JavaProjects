package il.co.ilrd.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.google.gson.JsonObject;

public class CRUDsql implements CRUD<JsonObject, Integer> {
	private Connection mySQL = null;
	@SuppressWarnings("unused")
	private JsonObject json;
	private PreparedStatement stmt;
	
	public CRUDsql(Connection conn, JsonObject json) {
		mySQL = conn;
		this.json = json;
	}
	
	@Override
	public Integer create(JsonObject json) {
		String SerialNumber = "";
		String Data = "";
		
		if (json.get("SerialNumber") != null) { 
			SerialNumber = json.get("SerialNumber").getAsString();
		}
		
		String Product = json.get("Product").getAsString();
		
		if ( json.get("Data") != null) { 
			Data = json.get("Data").getAsString();
		}
		
		System.out.println("serial Number: " + SerialNumber + " and Product: " + Product + " and data: " + Data);
		if (SerialNumber == null) {return null;}
		
		enterDataToTable(mySQL,Product, SerialNumber, Data);
	
		return null;
	}
	
	@Override
	public JsonObject read(Integer id) {
		return json;
		
	}
	
	@Override
	public void update(Integer id, JsonObject entity) {
		
	}
	

	private void enterDataToTable(Connection mySQL, String product, String SerialNumber, String Data) {
		
		try {
			String statement = "INSERT INTO " + product + " VALUES(?,?,?,?)";
			stmt = mySQL.prepareStatement(statement);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			stmt.setString(1,null);
			stmt.setString(2, SerialNumber);
			stmt.setString(3, Data);
			stmt.setTimestamp(4, timestamp);
			
			stmt.executeUpdate();
			
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub	
	}
}