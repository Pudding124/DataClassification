
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tom
 */
public class SentimentAPI {
    
    private String api_key = "0b719126adf7aeaa36e76749999f4a7b";

    public SentimentAPI(){
        
    }
    
    private String GetParameters(String text)
    {
        String urlParameters = null;
        try {
            urlParameters = "api_key=" + URLEncoder.encode(api_key, "UTF-8") +
                "&text=" + URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SentimentAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return urlParameters;
    }

    public String SendPostRequest(String targetURL, String text)
    {
        URL url;
        HttpURLConnection connection = null;  
        String urlParameters = GetParameters(text);
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", 
               "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" + 
                   Integer.toString(urlParameters.getBytes().length));
            
            connection.setRequestProperty("Content-Language", "en-US");  

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                     connection.getOutputStream());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response	
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer(); 
            
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            
            rd.close();
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if(connection != null) {
            connection.disconnect(); 
            }
        }
    }
    
    public String SentimentAnalysis(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/SentimentAnalysis.json", text);
    }

    public String TwitterSentimentAnalysis(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/TwitterSentimentAnalysis.json", text);
    }

    public String SubjectivityAnalysis(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/SubjectivityAnalysis.json", text);
    }

    public String TopicClassification(String text)
    {
       return SendPostRequest("http://api.datumbox.com/1.0/TopicClassification.json", text);
    }

    public String SpamDetection(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/SpamDetection.json", text);
    }

    public String AdultContentDetection(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/AdultContentDetection.json", text);
    }

    public String ReadabilityAssessment(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/ReadabilityAssessment.json",text);
    }

    public String LanguageDetection(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/LanguageDetection.json", text);
    }

    public String CommercialDetection(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/CommercialDetection.json", text);
    }

    public String EducationalDetection(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/EducationalDetection.json", text);
    }

    public String GenderDetection(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/GenderDetection.json", text);
    }

    public String TextExtraction(String text)
    {
        return SendPostRequest("http://api.datumbox.com/1.0/TextExtraction.json", text);
    }

//    public String KeywordExtraction(String text, int n)
//    {
//        Dictionary<String,String> arguments = new Dictionary<String,String>();
//
//        AddArgument(arguments, "api_key", api_key);
//        AddArgument(arguments, "text", text);
//        AddArgument(arguments, "n", n.ToString());
//
//        return SendPostRequest("http://api.datumbox.com/1.0/KeywordExtraction.json", arguments);
//    }
//
//    public String DocumentSimilarity(String original, String copy)
//    {
//        Dictionary<String,String> arguments = new Dictionary<String, String>();
//
//        AddArgument(arguments, "api_key", api_key);
//        AddArgument(arguments, "original", original);
//        AddArgument(arguments, "copy", copy);
//
//        return SendPostRequest("http://api.datumbox.com/1.0/DocumentSimilarity.json", arguments);
//    }
    
}