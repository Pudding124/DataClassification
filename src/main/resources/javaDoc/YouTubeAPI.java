package queries.showglobalinfochannel;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import queries.mediaresonance.videos.Video;

public class YouTubeAPI {
    public static final String API_KEY = "AIzaSyCrVQPr-LRlFYQkpipjrY0x1HGYAwoP7E8";

    public static String channel(String id) throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://www.googleapis.com/youtube/v3/channels")
                .queryString("key", API_KEY)
                .queryString("id", id)
                .queryString("part", "snippet, statistics")
                .asString();
        return response.getBody();
    }

    public static String search(String channelId, String query, int maxResults, String pageToken) throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://www.googleapis.com/youtube/v3/search")
                .queryString("part", "snippet")
                .queryString("channelId", channelId)
                .queryString("maxResults", maxResults)
                .queryString("pageToken", pageToken)
                .queryString("key", API_KEY)
                .queryString("q", query)
                .asString();
        return response.getBody();
    }

    public static String videos(String id) throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://www.googleapis.com/youtube/v3/videos")
                .queryString("part", "statistics")
                .queryString("id", id)
                .queryString("key", API_KEY)
                .asString();
        return response.getBody();
    }
}