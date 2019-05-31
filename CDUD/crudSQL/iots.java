package il.co.ilrd.sql;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class iots
 */
@WebServlet("/")
public class iots extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	static Crudfile fileName;
	static final Map<String, Connection> mymap = new HashMap<String, Connection>();
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		
		System.out.println("init...");
		
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		
		for (Entry<String, Connection> entry : mymap.entrySet()) {
		    Connection toClose = entry.getValue();
		    try {
				toClose.close();
			} catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
      		    System.out.println("SQLState: " + ex.getSQLState());
      		    System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("inside doGet ");
		response.getWriter().append("Served at this point: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream inputStream = request.getInputStream();
        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject)parser.parse(new InputStreamReader(inputStream));

      		Connection crudConn = null;
      		try {
      			try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
      			crudConn = DriverManager.getConnection("jdbc:mysql://localhost/CRUD", "root" ,"12345678");
      		} catch (SQLException ex) {
      			System.out.println("SQLException: " + ex.getMessage());
      		    System.out.println("SQLState: " + ex.getSQLState());
      		    System.out.println("VendorError: " + ex.getErrorCode());
      		}
      		if (crudConn != null) {
      			mymap.put("CRUD", crudConn);
      		}
        
        Connection conn = null;
        String product =null;
        
        if (null != json.get("Company").getAsString()) {
        	if (mymap.containsKey(json.get("Company").getAsString())) {
        		conn = mymap.get(json.get("Company").getAsString());
        		if (json.get("Product") != null ) {
        			product = json.get("Product").getAsString();
        		} else {
        			return;
        		}
        		if (validateTable(conn, product)) {
        			CRUDsql myCRUDsql = new CRUDsql(conn, json);
        			myCRUDsql.create(json);
        		}
        	}
        	
        }
	}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * Check if the table "product" is in the database "conn"
	 * @param conn - the name of the company and connection
	 * @param product - the name of the product
	 * @return if it is or isn't there.
	 */
//	??? - problem in this function!
	private boolean validateTable(Connection conn, String product) {
		Objects.requireNonNull(product);
		
		DatabaseMetaData meta;
		try {
			meta = conn.getMetaData();
			ResultSet res = meta.getTables(null, null, product, null);
			while (res.next()) {
				String tableName = res.getString(3);
				if (tableName.equals(product)) {
					return true;
				}
			}
			
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}

		return false;
		
	}
}