package com.alomonshi.server.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class HandShakeConfiguration extends Configurator {
	
	@Override
    public boolean checkOrigin(String originHeaderValue) {
//        return originHeaderValue.equals("https://alomonshi.com") ? true : false;
        return true;
    }
	
    @Override
    public void modifyHandshake(ServerEndpointConfig config, 
                                HandshakeRequest request, 
                                HandshakeResponse response){
    }
}
