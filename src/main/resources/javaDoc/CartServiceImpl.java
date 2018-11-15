/**
 * 
 */
package com.food.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.dao.DaoFactory;
import com.food.dao.ObjectDao;
import com.food.model.Cart;
import com.food.model.MenuItem;
import com.food.model.RequestData;
import com.food.model.amount_money;
import com.food.model.billing_address;
import com.food.model.shipping_address;
import com.food.response.Response;
import com.food.response.SimpleResponse;
import com.food.service.CartService;
import com.food.util.ServiceUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;


/**
 * @author bkagidal
 *
 */

@Controller
@RequestMapping("/ffc/services/cart")
public class CartServiceImpl implements CartService {

	private  UUID idKey = null;
	private String test = "hello";
	private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
	
	@Override
	@RequestMapping(value="/addToCart" ,method = RequestMethod.POST)
	public ResponseEntity<Response> addToCart(@RequestBody  RequestData request) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		ObjectDao dao = factory.getObjectDao();
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", request.getCart_data().getUserId());
		
		String entityName = "GET_CART_ID_N";
		List<Object> readObject =  dao.readObjects(entityName, queryParams);
		String exitCartId = ((Map)readObject.get(0)).get("cartId").toString();
		
		entityName = "GET_CART_ID_Y";
		List<Object> readObject1 =  dao.readObjects(entityName, queryParams);
		String ordCartId = ((Map)readObject1.get(0)).get("cartId").toString();
		
		String cartId = ServiceUtil.getCartId(exitCartId, ordCartId);
		System.out.println(cartId);
		
		entityName="INSERT_CART";
		
		queryParams.clear();
		queryParams.put("cartId", cartId);
		queryParams.put("itemId", request.getCart_data().getItemId());
		queryParams.put("userId", request.getCart_data().getUserId());
		queryParams.put("qty", request.getCart_data().getQty());
		queryParams.put("orderType", request.getCart_data().getOrderType());
		
		boolean insertFlag ;
		try{
			insertFlag = dao.insertUpdateObject(entityName, queryParams);
		} catch(Exception e) {
			insertFlag = false;
		}
		
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", insertFlag);
		result.put("cartId", cartId);
		
		SimpleResponse reponse = new SimpleResponse("" + true,
				request.getRequest_data_type(),
				request.getRequest_data_method(), result);
		
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		
		return entity;
	}
	
	
	
	@RequestMapping(value="/checkOut" ,method = RequestMethod.POST)
	public ResponseEntity<Response> checkOut(@RequestBody  RequestData request) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		ObjectDao dao = factory.getObjectDao();
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", request.getCart_data().getUserId());
		queryParams.put("cartId", request.getCart_data().getCartId());	
		Map<String,Object> result = new HashMap<String,Object>();
		/*String entityName = "GET_TOTAL_AMT";
		List<Object> readObject = null;		
		readObject = dao.readObjects(entityName, queryParams);		
		String totalAmt = ((Map)readObject.get(0)).get("totalAmt").toString();
		System.out.println(totalAmt);
		
		entityName = "GET_NO_TAX_AMT";		
		List readObject1 = dao.readObjects(entityName, queryParams);	
		String noTaxAmt = " ";
		if( ((Map)readObject1.get(0)).get("noTaxAmt")!=null)
		 	noTaxAmt = ((Map)readObject1.get(0)).get("noTaxAmt").toString();
		else
			noTaxAmt="0";
			 
		System.out.println(noTaxAmt);		
		double taxableAmt = Double.parseDouble(totalAmt)-Double.parseDouble(noTaxAmt);		
		double tax =(taxableAmt*8.75)/100;		
		double netAmount= Double.parseDouble(totalAmt) + tax;
		System.out.println(netAmount);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("totalAmt", Double.parseDouble(totalAmt));
		result.put("tax", tax);
		result.put("netAmount", netAmount);*/
		
		Map<String, String> queryItemParams = new HashMap<String, String>();
		queryItemParams.put("cartId", request.getCart_data().getCartId());
		queryItemParams.put("userId", request.getCart_data().getUserId());
		String entityItemName = "GET_ITEM_DETAILS";
		List<Object> readItemObject = null;
		readItemObject = dao.readObjects(entityItemName, queryItemParams);
		Map<String, List<Object>> items = new HashMap<String, List<Object>>();
		List<Object> itemsLst = new ArrayList<Object>();
		if(null != readItemObject) {	
			for(int i=0; i<readItemObject.size(); i++) {
				Cart mnu = new Cart();
				mnu.setItemId(((Map)readItemObject.get(i)).get("itemId").toString());
				mnu.setItemName(((Map)readItemObject.get(i)).get("itemName").toString());
				mnu.setDescription(((Map)readItemObject.get(i)).get("description").toString());
				//mnu.setQty(((Map)readItemObject.get(i)).get("quantity").toString());
				mnu.setImageUrl(((Map)readItemObject.get(i)).get("imageUrl").toString());			
				//mnu.setAmount(((Map)readItemObject.get(i)).get("price").toString());
				itemsLst.add(mnu);
			}			
		}
		//items.put("items", itemsLst);
		result.put("itemsObj", itemsLst);
				
		SimpleResponse reponse = new SimpleResponse("" + true,
				request.getRequest_data_type(),
				request.getRequest_data_method(), result);
		
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		
		return entity;
		
	}
	
	
	@RequestMapping(value="/deleteItem" ,method = RequestMethod.POST)
	public ResponseEntity<Response> deleteItem(@RequestBody  RequestData request) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		ObjectDao dao = factory.getObjectDao();
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", request.getCart_data().getUserId());
		queryParams.put("cartId", request.getCart_data().getCartId());		
		queryParams.put("itemId", request.getCart_data().getItemId());
		String entityName = "DELETE_CART_ITEM";
		boolean itemDeleted = false;		
		itemDeleted = dao.insertUpdateObject(entityName, queryParams);	
		System.out.println("item deleted : " + itemDeleted);
		SimpleResponse reponse = new SimpleResponse("" + itemDeleted,
				request.getRequest_data_type(),
				request.getRequest_data_method(), itemDeleted);
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		
		return entity;
	}
		
		
	@RequestMapping(value="/previewMenu" ,method = RequestMethod.POST)
	public ResponseEntity<Response> previewMenu(@RequestBody  RequestData request) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		ObjectDao dao = factory.getObjectDao();
		System.out.println("No of ppl :" + request.getCart_data().getNumPpl());
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", request.getCart_data().getUserId());
		queryParams.put("cartId", request.getCart_data().getCartId());	
		queryParams.put("numPpl", request.getCart_data().getNumPpl());
		queryParams.put("noPpl", request.getCart_data().getNumPpl());
		Map<String,Object> result = new HashMap<String,Object>();
		
		String entityShipName = "GET_SHIP_ADDRESS";
		List<Object> readShipObject = null;		
		readShipObject = dao.readObjects(entityShipName, queryParams);		
		Map<String, String> shipMap = new HashMap();
		shipMap.put("add1", ((Map)readShipObject.get(0)).get("Address1").toString());
		shipMap.put("add2", ((Map)readShipObject.get(0)).get("Address2").toString());
		shipMap.put("city", ((Map)readShipObject.get(0)).get("City").toString());
		shipMap.put("state", ((Map)readShipObject.get(0)).get("State").toString());
		shipMap.put("zip", ((Map)readShipObject.get(0)).get("Zip").toString());
		result.put("shipAddress", shipMap);
		
		String entityPayName = "GET_PAY_METHOD";
		List<Object> readPayObject = null;		
		readPayObject = dao.readObjects(entityPayName, queryParams);		
		String cardNo = ((Map)readPayObject.get(0)).get("cardNo").toString();
		String cardType = ((Map)readPayObject.get(0)).get("cardType").toString();
		String cardExp = ((Map)readPayObject.get(0)).get("cardExp").toString();
		result.put("cardNo", cardNo);
		result.put("cardType", cardType);
		result.put("cardExp", cardExp);
		
		String entityName = "GET_EVENT_AMT";
		List<Object> readObject = null;		
		readObject = dao.readObjects(entityName, queryParams);		
		String totalAmt = ((Map)readObject.get(0)).get("totalAmt").toString();
		System.out.println(totalAmt);
		
				
		//double taxableAmt = Double.parseDouble(totalAmt)-Double.parseDouble(noTaxAmt);		
		double tax =(Double.parseDouble(totalAmt)*8.75)/100;		
		double netAmount= Double.parseDouble(totalAmt) + tax;
		System.out.println(netAmount);
		
		result.put("totalAmt", Double.parseDouble(totalAmt));
		result.put("tax", tax);
		result.put("netAmount", netAmount);
		
		Map<String, String> queryItemParams = new HashMap<String, String>();
		queryItemParams.put("cartId", request.getCart_data().getCartId());
		queryItemParams.put("userId", request.getCart_data().getUserId());
		String entityItemName = "GET_ITEM_DETAILS";
		List<Object> readItemObject = null;
		readItemObject = dao.readObjects(entityItemName, queryItemParams);
		Map<String, List<Object>> items = new HashMap<String, List<Object>>();
		List<Object> itemsLst = new ArrayList<Object>();
		if(null != readItemObject) {	
			for(int i=0; i<readItemObject.size(); i++) {
				Cart mnu = new Cart();
				mnu.setItemId(((Map)readItemObject.get(i)).get("itemId").toString());
				mnu.setItemName(((Map)readItemObject.get(i)).get("itemName").toString());
				mnu.setDescription(((Map)readItemObject.get(i)).get("description").toString());
				//mnu.setQty(((Map)readItemObject.get(i)).get("quantity").toString());
				mnu.setImageUrl(((Map)readItemObject.get(i)).get("imageUrl").toString());			
				//mnu.setAmount(((Map)readItemObject.get(i)).get("price").toString());
				itemsLst.add(mnu);
			}			
		}
		//items.put("items", itemsLst);
		result.put("itemsObj", itemsLst);
				
		SimpleResponse reponse = new SimpleResponse("" + true,
				request.getRequest_data_type(),
				request.getRequest_data_method(), result);
		
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		
		return entity;
		
	}
	
	@Override
	@RequestMapping(value="/submitOrder" ,method = RequestMethod.POST)
	public ResponseEntity<Response> submitOrder(@RequestBody  RequestData request) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		ObjectDao dao = factory.getObjectDao();
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", request.getCart_data().getUserId());
		
		String entityName = "SUBMIT_ORDER";
		
		queryParams.clear();
		queryParams.put("cartId", request.getCart_data().getCartId());
		queryParams.put("userId", request.getCart_data().getUserId());
		queryParams.put("orderDate", request.getCart_data().getOrderDate());
		queryParams.put("userPaymentId", request.getCart_data().getUserPaymentId());
		queryParams.put("itemId", request.getCart_data().getItemId());
		queryParams.put("quantity", request.getCart_data().getQty());
		queryParams.put("amount", request.getCart_data().getAmount());
		queryParams.put("netAmount", request.getCart_data().getNetAmount());
		queryParams.put("shiptoFirstName", request.getCart_data().getShiptoFirstName());
		queryParams.put("shiptoMiddlName", request.getCart_data().getShiptoMiddlName());
		queryParams.put("shiptoLastName", request.getCart_data().getShiptoLastName());
		queryParams.put("shiptoAddress1", request.getCart_data().getShiptoAddress1());
		queryParams.put("shiptoAddress2", request.getCart_data().getShiptoAddress2());
		queryParams.put("shiptoCity", request.getCart_data().getShiptoCity());
		queryParams.put("shiptoState", request.getCart_data().getShiptoState());
		queryParams.put("shiptoZipCode", request.getCart_data().getShiptoZipCode());
		queryParams.put("discountPercentage", request.getCart_data().getDiscountPercentage());
		
		
		boolean insertFlag ;
		insertFlag = dao.insertUpdateObject(entityName, queryParams);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", insertFlag);
		
		
		SimpleResponse reponse = new SimpleResponse("" + true,
				request.getRequest_data_type(),
				request.getRequest_data_method(), result);
		
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		
		return entity;
	}

	
	@Override
	@RequestMapping(value = "/listSquareLocation", method = RequestMethod.POST)
	public ResponseEntity<Response> listSquareLocation(@RequestBody RequestData request) {

		HttpResponse<JsonNode> jsonResponse = null;
		String access_token = "sq0atp-V15-WBE-TEyO-a_c0Ha7CQ";
		try {
			jsonResponse = Unirest.get("https://connect.squareup.com/v2/locations")
					.header("Authorization", "Bearer " + access_token)
					.asJson();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("location respones " + completed(jsonResponse));
		SimpleResponse reponse = new SimpleResponse("" + true,
				request.getRequest_data_type(),
				request.getRequest_data_method(), completed(jsonResponse));
		
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);	
		return entity;
	}
	
	public String completed(HttpResponse<JsonNode> response) {
        int code = response.getStatus();
        Map<String, List<String>> headers = response.getHeaders();
        JsonNode body = response.getBody();
        InputStream rawBody = response.getRawBody();
        return getStringFromInputStream(rawBody);
   }
	
	// convert InputStream to String
		private static String getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return sb.toString();

		}
		
		
	  
	@Override
	@RequestMapping(value = "/createCustomer", method = RequestMethod.POST)
	public ResponseEntity<Response> createCustomer(@RequestBody RequestData request) {
		HttpResponse<String> jsonResponse = null;
		String access_token = "sq0atp-V15-WBE-TEyO-a_c0Ha7CQ";
		Map<String, Object> reqMap = new HashMap();
		shipping_address ship = new shipping_address();
		shipping_address shipData = (shipping_address)request.getCart_data().getShipping_address();
		ship.setAddress_line_1(shipData.getAddress_line_1());
		ship.setAddress_line_2(shipData.getAddress_line_2());
		ship.setLocality(shipData.getLocality());
		ship.setAdministrative_district_level_1(shipData.getAdministrative_district_level_1());
		ship.setPostal_code(shipData.getPostal_code());
		ship.setCountry(shipData.getCountry());
		
		reqMap.put("given_name", request.getCart_data().getShiptoFirstName());
		reqMap.put("family_name", request.getCart_data().getShiptoLastName());
		reqMap.put("email_address", request.getCart_data().getEmailAddress());
		reqMap.put("phone_number", request.getCart_data().getPhoneNo());
		reqMap.put("reference_id", request.getCart_data().getDescription());
		reqMap.put("note", request.getCart_data().getNote());
		reqMap.put("address", ship);
		
		System.out.println("request string " + reqMap.toString());
		
		try {
			
			jsonResponse = Unirest.post("https://connect.squareup.com/v2/customers")
					.header("Authorization", "Bearer " + access_token)
					.body(jacksonObjectMapper.writeValueAsString(reqMap))
					.asString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SimpleResponse reponse = new SimpleResponse("" + true,
				request.getRequest_data_type(),
				request.getRequest_data_method(), jsonResponse.getBody());
		
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);	
		return entity;
		
	}
	
	// square personal access token : sq0atp-V15-WBE-TEyO-a_c0Ha7CQ
	// square sandbox application id : sandbox-sq0idp-JAj21eCv6q4s9kLqY9dWxA
	// square sandbox access token : sq0atb-xHgAtmv5Ij4fDZHDaaK1yA
	@Override
	@RequestMapping(value="/chargeCreditCardWithSquare" ,method = RequestMethod.POST)
	public ResponseEntity<Response> chargeCreditCardWithSquare(@RequestBody  RequestData request){
		idKey =  UUID.randomUUID();
		Map<String, Object> reqMap = new HashMap();
		HttpResponse<String> jsonResponse = null;
		String access_token = "sq0atp-V15-WBE-TEyO-a_c0Ha7CQ";
		
		System.out.println("ID " + request.getCart_data().getUserPaymentId());
		shipping_address ship = new shipping_address();
		
		shipping_address shipData = (shipping_address)request.getCart_data().getShipping_address();
		ship.setAddress_line_1(shipData.getAddress_line_1());
		ship.setAddress_line_2(shipData.getAddress_line_2());
		ship.setLocality(shipData.getLocality());
		ship.setAdministrative_district_level_1(shipData.getAdministrative_district_level_1());
		ship.setPostal_code(shipData.getPostal_code());
		ship.setCountry(shipData.getCountry());
				
		reqMap.put("idempotency_key", idKey);
		reqMap.put("shipping_address", ship);
		
		
		billing_address billTo = new billing_address();
		billing_address billToData = (billing_address)request.getCart_data().getBilling_address();
		
		billTo.setAddress_line_1(billToData.getAddress_line_1());
		billTo.setAddress_line_2(billToData.getAddress_line_2());
		billTo.setAdministrative_district_level_1(billToData.getAdministrative_district_level_1());
		billTo.setLocality(billToData.getLocality());
		billTo.setPostal_code(billToData.getPostal_code());
		billTo.setCountry(billToData.getCountry());
		
		reqMap.put("billing_address", billTo);
		
		amount_money amt = new amount_money();		
		amount_money amtData = (amount_money)request.getCart_data().getAmount_money();
		
		amt.setAmount(amtData.getAmount());
		amt.setCurrency(amtData.getCurrency());
		
		reqMap.put("amount_money", amt);
		//reqMap.put("card_nonce", request.getCart_data().getUserPaymentId());
		reqMap.put("customer_card_id",request.getCart_data().getCustomerCardId());
		reqMap.put("customer_id",request.getCart_data().getCustomerId());
		reqMap.put("reference_id", request.getCart_data().getDescription());
		reqMap.put("note", request.getCart_data().getNote());
		reqMap.put("delay_capture", false);
		System.out.println("request string " + reqMap.toString());
		
		
		
		/*public <T> T readValue(String value, Class<T> valueType) {
	        try {
	            return jacksonObjectMapper.readValue(value, valueType);
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
	    public String writeValue(Object value) {
	        try {
	            return jacksonObjectMapper.writeValueAsString(value);
	        } catch (JsonProcessingException e) {
	            throw new RuntimeException(e);
	        }
	    }*/
	    
		try{
			jsonResponse = Unirest.post("https://connect.squareup.com/v2/locations/EBAT9F9VFA3M9/transactions")
					  .header("Authorization", "Bearer " + access_token)
					  .body(jacksonObjectMapper.writeValueAsString(reqMap))
					  .asString();
					  
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        Map<String,Object> returnResult = new HashMap<String,Object>();
        returnResult.put("id", jsonResponse.getBody());
        System.out.println("response " + jsonResponse.getBody());
        SimpleResponse reponse = new SimpleResponse("" + true,
				request.getRequest_data_type(),
				request.getRequest_data_method(), returnResult);
		
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		
		return entity;
		
    }
	
	
	private boolean insertOrderInDB() {
		boolean insertStatus = false;
		
		return insertStatus;
	
	}
	
	/*@Override
	@RequestMapping(value="/chargeCreditCard" ,method = RequestMethod.POST)
	public ResponseEntity<Response> chargeCreditCard(@RequestBody  RequestData request) {
		//Common code to set for all requests
        ApiOperationBase.setEnvironment(Environment.SANDBOX);
        System.out.println("Credit card number : " + request.getCart_data().getUserPaymentId());
        System.out.println("Credit card Exp : " + request.getCart_data().getExpDate());
        System.out.println("Net Amt : " + request.getCart_data().getNetAmount());
        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName("576xNPzL");
        merchantAuthenticationType.setTransactionKey("3244bEEYctn6K545");
        ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
        // Populate the payment data
        PaymentType paymentType = new PaymentType();
        CreditCardType creditCard = new CreditCardType();
        creditCard.setCardNumber(request.getCart_data().getUserPaymentId());
        creditCard.setExpirationDate(request.getCart_data().getExpDate());
        paymentType.setCreditCard(creditCard);
        // Create the payment transaction request
        TransactionRequestType txnRequest = new TransactionRequestType();
        txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
        txnRequest.setPayment(paymentType);
        txnRequest.setAmount(new BigDecimal(request.getCart_data().getNetAmount()));
        // Make the API Request
        CreateTransactionRequest apiRequest = new CreateTransactionRequest();
        apiRequest.setTransactionRequest(txnRequest);
        CreateTransactionController controller = new CreateTransactionController(apiRequest);
        controller.execute();
        CreateTransactionResponse response = controller.getApiResponse();
        Map<String,Object> returnResult = new HashMap<String,Object>();
        if (response!=null) {
            // If API Response is ok, go ahead and check the transaction response
            if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
                TransactionResponse result = response.getTransactionResponse();
                if (result.getResponseCode().equals("1")) {
                    System.out.println(result.getResponseCode());
                    System.out.println("Successful Credit Card Transaction");
                    System.out.println(result.getAuthCode());
                    System.out.println(result.getTransId());
                    try{
                    	String orderNum = insertOrder(request);
                    	returnResult.put("orderNum", orderNum);
                    } catch(Exception e){
                    	e.printStackTrace();
                    }
                    
                }
                else
                {
                    System.out.println("Failed Transaction"+result.getResponseCode());
                }
                returnResult.put("status", result.getResponseCode());
            }
            else
            {
                System.out.println("Failed Transaction:  "+response.getMessages().getResultCode());
            }
        }
        SimpleResponse reponse = new SimpleResponse("" + true,
				request.getRequest_data_type(),
				request.getRequest_data_method(), returnResult);
		
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		
		return entity;
		
    }*/
	
	public String insertOrder(@RequestBody  RequestData request) throws Exception{
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		ObjectDao dao = factory.getObjectDao();
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", request.getCart_data().getUserId());
		
		String entityName = "SUBMIT_ORDER";
		String orderNum = createOrderNumber(new Date());
		queryParams.clear();
		queryParams.put("orderId", orderNum);
		queryParams.put("cartId", request.getCart_data().getCartId());
		queryParams.put("userId", request.getCart_data().getUserId());
		queryParams.put("orderDate", request.getCart_data().getOrderDate());
		queryParams.put("userPaymentId", request.getCart_data().getUserPaymentId());
		queryParams.put("itemId", request.getCart_data().getItemId());
		queryParams.put("quantity", request.getCart_data().getQty());
		queryParams.put("amount", request.getCart_data().getAmount());
		queryParams.put("netAmount", request.getCart_data().getNetAmount());
		queryParams.put("shiptoFirstName", request.getCart_data().getShiptoFirstName());
		queryParams.put("shiptoMiddlName", request.getCart_data().getShiptoMiddlName());
		queryParams.put("shiptoLastName", request.getCart_data().getShiptoLastName());
		queryParams.put("shiptoAddress1", request.getCart_data().getShiptoAddress1());
		queryParams.put("shiptoAddress2", request.getCart_data().getShiptoAddress2());
		queryParams.put("shiptoCity", request.getCart_data().getShiptoCity());
		queryParams.put("shiptoState", request.getCart_data().getShiptoState());
		queryParams.put("shiptoZipCode", request.getCart_data().getShiptoZipCode());
		queryParams.put("discountPercentage", request.getCart_data().getDiscountPercentage());
		
		
		boolean insertFlag ;
		insertFlag = dao.insertUpdateObject(entityName, queryParams);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", insertFlag);		
		if (insertFlag == true) {
			return orderNum;
		} else 
			return "error in inserting order details";
		
	}

	public static String createOrderNumber(Date orderDate) throws NoSuchAlgorithmException {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(orderDate).toString();
    }
	
}