package altimetrik.trip.amadeusclient;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AmadeusApiClient {
	public String loadFastestFlightsApiHandler(String origin, String destination, String departureDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/flights/extensive-search?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&origin=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				basicURL + origin + "&destination=" + destination + "&departure_date=" + departureDate + "--"
						+ departureDate + "&one-way=true&direct=true&max_price=50000",
				HttpMethod.GET, entity, String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}

	public String loadCheapestFlightsApiHandler(String origin, String destination, String departureDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&origin=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				basicURL + origin + "&destination=" + destination + "&departure_date=" + departureDate, HttpMethod.GET,
				entity, String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}

	public String loadFlightsApiHandler(String origin, String destination, String departureDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/flights/extensive-search?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&origin=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				basicURL + origin + "&destination=" + destination + "&departure_date=" + departureDate, HttpMethod.GET,
				entity, String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}

	public String loadNearestHotelsApiHandler(String location, String checkInDate, String checkOutDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/hotels/search-airport?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&location=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(basicURL + location + "&check_in=" + checkInDate
				+ "&check_out=" + checkOutDate + "&radius=10&all_rooms=true", HttpMethod.GET, entity, String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}

	public String loadCheapestHotelsApiHandler(String location, String checkInDate, String checkOutDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/hotels/search-airport?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&location=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				basicURL + location + "&check_in=" + checkInDate + "&check_out=" + checkOutDate, HttpMethod.GET, entity,
				String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}

	public String loadHotelsApiHandler(String location, String checkInDate, String checkOutDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/hotels/search-airport?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&location=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				basicURL + location + "&check_in=" + checkInDate + "&check_out=" + checkOutDate
						+ "&amenity=PARKING&amenity=RESTAURANT&amenity=PETS_ALLOWED",
				HttpMethod.GET, entity, String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}

	public String loadFastestCarsApiHandler(String location, String pickUpDate, String dropOffDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/cars/search-airport?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&location=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				basicURL + location + "&pick_up=" + pickUpDate + "&drop_off=" + dropOffDate + "&vehicle=ELITE",
				HttpMethod.GET, entity, String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}

	public String loadCheapestCarsApiHandler(String location, String pickUpDate, String dropOffDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/cars/search-airport?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&location=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				basicURL + location + "&pick_up=" + pickUpDate + "&drop_off=" + dropOffDate + "&vehicle=STANDARD",
				HttpMethod.GET, entity, String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}

	public String loadCarsApiHandler(String location, String pickUpDate, String dropOffDate) {
		String basicURL = "https://api.sandbox.amadeus.com/v1.2/cars/search-airport?apikey=3hDHMBXGnhg9AV51NXgF9wShE7u91wKV&location=";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				basicURL + location + "&pick_up=" + pickUpDate + "&drop_off=" + dropOffDate, HttpMethod.GET, entity,
				String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			System.out.println(responseEntity.getBody());
			return responseEntity.getBody();
		} else
			return null;
	}
}