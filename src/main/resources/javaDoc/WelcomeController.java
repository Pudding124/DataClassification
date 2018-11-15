package com.box;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.box.sdk.BoxAPIConnection;

@Controller
public class WelcomeController {
	
	private final static String CLIENT_ID = "76ram8ecfaj2cm1dc7ypklpi15sgjn9c";
	private final static String CLIENT_SECRET = "0vACSzY3R8KWjKLKd0vmAYj5iSOWbduN";

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hi";
	
	@RequestMapping("/")
	public @ResponseBody void welcome(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Welcome controller");
		String box_auth_endpoint = "https://account.box.com/api/oauth2/authorize?response_type=code&";
		String box_redirect = box_auth_endpoint
				  + "&client_id=" + "76ram8ecfaj2cm1dc7ypklpi15sgjn9c" 
				  + "&redirect_uri=" + "http://localhost:8080/auth";
		try {
			response.sendRedirect(box_redirect);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/auth")
	public String authorize(HttpServletRequest request, Map<String, Object> model) {
		String authCode = request.getParameter("code");
		System.out.println("Welcome controller -- Auth Code "+authCode);
		BoxAPIConnection client = new BoxAPIConnection(CLIENT_ID, CLIENT_SECRET, authCode);
		model.put("message", this.message);
		return "welcome";
	}
}