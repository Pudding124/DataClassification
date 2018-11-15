package jsonreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

import RestauranTO.Database.Datamodel.Jsonthing;

public class CJsonReader extends SelectorComposer<Component> {

	private static final long serialVersionUID = 3078745997595341184L;



	public static List<Jsonthing> getEmployees( ){
			// making url request
			try {
				List<Jsonthing> result = new ArrayList<Jsonthing>();
				
				URL url = new URL("https://connect.squareup.com/v1/me/employees");
				// making connection
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Authorization", "Bearer sq0atp-3Dr5hBRkfws1_Nv5PV7zxQ");
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}

				// Reading data's from url
			   BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

				String output;
				String out="";
				//System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					//System.out.println(output +"\n");
					out=out+output;
				}
				
				// Converting Json formatted string into JSON object
				//JSONObject json = (JSONObject) JSONSerializer.toJSON(out);
				JSONArray results=(JSONArray) JSONSerializer.toJSON(out);
				//JSONObject rec = results.getJSONObject(0);
				for(int i=0;i<results.size();i++){
				JSONObject rec1 = results.getJSONObject(i);
				
				Jsonthing jsonthing = new Jsonthing();
				
				jsonthing.setFirst_name(rec1.getString("first_name") );
				
				jsonthing.setLast_name(rec1.getString("last_name") );
				
				jsonthing.setId(rec1.getString("id") );
				
				jsonthing.setAuthorized_location_ids(rec1.getString("authorized_location_ids"));
				
				jsonthing.setCreated_at( rec1.getString("created_at" ));
				
				jsonthing.setRole_ids(rec1.getString("role_ids" ));
				
				jsonthing.setExternal_id(rec1.getString("external_id" ));
				
				jsonthing.setStatus(rec1.getString("status" ));
				
				jsonthing.setUpdated_at(rec1.getString("updated_at") );
				
				jsonthing.setEmail(rec1.getString("email" ));
				
				result.add(jsonthing);
				
				}
				conn.disconnect();
			    return result;		
		      	
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
		
				e.printStackTrace();
			}
			return null;
   }

}