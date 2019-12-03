package com.alomonshi.server.session;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Session;

import com.alomonshi.object.ReserveTime;

public class SessionUtils extends SessionHandler {
	
	public static synchronized boolean checkRestimeIsfreeAndAdd(Session session, ReserveTime restime) {
		int currentDateID = restime.getDateID();
		int currentUnitID = restime.getUnitID();
		int currentmiddayID = restime.getMiddayID();
		for(Entry<Session,ReserveTime> entry : reservingTimes.entrySet()) {
			if(entry.getValue().getDateID() == currentDateID) {
				if(entry.getValue().getUnitID() == currentUnitID) {
					if(entry.getValue().getMiddayID() == currentmiddayID) {											
						return false;
					}
				}
			}
		}
		reservingTimes.put(session, restime);
		return true;
	}
	
    public static void sendToSession(Session session, JsonObject message)
	{
		Logger.getLogger("WebSocketSentMessage").log(Level.INFO,"Message Send to Websocket Client : " + message);
		try {
            session.getBasicRemote().sendObject(message);
        } catch (EncodeException | IOException ex) {
            Logger.getLogger("Error On Send Message").log(Level.SEVERE, "Exception" + ex);
        }
	}    

}
