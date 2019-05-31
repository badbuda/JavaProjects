package il.co.ilrd.gatewayserver;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import il.co.ilrd.gatewayserver.GatewayServer.States;

public class Tweet implements Commandable{
	private String consumerKeyStr;
	private String consumerSecretStr;
	private String accessTokenStr;
	private String accessTokenSecretStr;
	private String status;
	private boolean isOk;

    private States validationCheck(HttpExchange exchange) {
    	if (consumerKeyStr.equals("") || consumerSecretStr.equals("") || 
				accessTokenStr.equals("") || accessTokenSecretStr.equals("") ) {
				return States.NO_TOKEN;
		}
		else if (status.equals("")) {
			return States.NO_STATUS;
		}
		else {
			isOk = true;
			return States.OK;
		}
    }
    
	@Override
	public States action(JsonObject jo, HttpExchange exchange) {
		States result = null;
		
		consumerKeyStr = jo.get("consumerKey").getAsString();
		consumerSecretStr = jo.get("consumerSecret").getAsString();
		accessTokenStr = jo.get("oAuthAccessToken").getAsString();
		accessTokenSecretStr = jo.get("oAuthAccessTokenSecret").getAsString();
		status = jo.get("status").getAsString();
		
		result = validationCheck(exchange);
		
		if (isOk) {
			try {
				
				Twitter twitter = new TwitterFactory().getInstance();
				
				twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
				AccessToken accessToken = new AccessToken(accessTokenStr,
						accessTokenSecretStr);
				
				twitter.setOAuthAccessToken(accessToken);
				
				twitter.updateStatus(status);
				
				System.out.println("Successfully updated the status in Twitter.");
				
			} catch (TwitterException te) {
				States.UNKNOWN.setErrorNumber(te.getErrorCode());
				States.UNKNOWN.setExtraData(te.getErrorMessage());
				result = States.UNKNOWN;
			}
		}
		return result;
	}
}
