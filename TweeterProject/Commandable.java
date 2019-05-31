package il.co.ilrd.gatewayserver;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import il.co.ilrd.gatewayserver.GatewayServer.States;

public interface Commandable {

	public States action(JsonObject jo, HttpExchange exchange);
}
