package com.revature.application.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.revature.application.model.Resident;
import com.revature.application.service.ResidentService;
import com.revature.mockmodels.User;

@Controller
public class LoginController {
	@Autowired
	ResidentService residentService;
	
	
	@RequestMapping(value = "api/login/{data}", method = RequestMethod.GET)
	public ResponseEntity<Object> login(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		//slack code to get token
		String code = req.getParameter("code");
		String clientId = "229600595489.230193848804";
		String clientSecret = "c779a43e2f51027a9865f3631db02696";
		String legacyToken = "xoxp-229600595489-230131963906-233947627280-e2ab7d071d9f9bd8bb946f806c7aa774";
		// get parameters client_id, client_secret,code to retrieve token
		String redirectUrl = "https://slack.com/api/oauth.access?client_id=" + clientId + "&client_secret="
				+ clientSecret + "&code=" + code;

		// https://slack.com/api/oauth.access
		URL url = new URL(redirectUrl);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("POST");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String s = br.readLine();
		System.out.println(s);

		br.close();
		JsonObject jobj = new Gson().fromJson(s, JsonObject.class);
		JsonObject jobj2 = new Gson().fromJson(s, JsonObject.class);
        String token = jobj2.get("access_token").getAsString();
		JsonObject user = jobj.get("user").getAsJsonObject();//.get("id").getAsString();
		String id = user.get("id").getAsString();
		user.addProperty("isManager", false);
		if(s.contains("channels:write"))
			user.addProperty("isManager", true);
		
		Resident resident = residentService.findByEmail(user.get("email").getAsString());
		if (resident != null) {
			resident.setSlackId(id);
			residentService.updateResident(resident);
		}

		

/*		redirectUrl = "https://slack.com/api/users.info?token=" + token +
		"&user="+ id;
		
		url = new URL(redirectUrl);
		httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("POST");
		br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		*/
		
/*		jobj = new Gson().fromJson(br.readLine(), JsonObject.class);
		System.out.println(jobj);
		Boolean isAdmin = jobj.get("user").getAsJsonObject().get("is_admin").getAsBoolean();*/

				
		/*br.close();*/
		//line = sb.toString();
		 HttpSession session = req.getSession(true);
		 //System.out.println(line.user);
        session.setAttribute("user", user.toString());
        session.setAttribute("token",token);
        //User user = new User("Person", "One", "1@gmail.com");
/*		if(isAdmin) {
			
			redirectUrl =  "https://slack.com/oauth/authorize?scope=channels:write&client_id=229600595489.230193848804";
			url = new URL(redirectUrl);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("POST");
			br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
			System.out.println(br.readLine());
		}*/
		return ResponseEntity.ok(user.toString());
        
	}
	
}	