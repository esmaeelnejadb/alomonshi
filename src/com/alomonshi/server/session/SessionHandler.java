package com.alomonshi.server.session;

import java.util.LinkedHashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.websocket.Session;

import com.alomonshi.object.ReserveTime;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessionHandler {
	protected static final List<Session> connectedSessions = new LinkedList<Session>();
	protected static final Map<Session,ReserveTime> reservingTimes = new LinkedHashMap<Session,ReserveTime>();
	
	public static Map<Session,ReserveTime> getReservingTimes() {
		return SessionHandler.reservingTimes;
	} 
	
	public static List<Session> getConnectedSessions() {
		return SessionHandler.connectedSessions;
	}
	
	public static void addSession(Session session) {
		connectedSessions.add(session);
	}
	
	public static void removeSession(Session session) {
		connectedSessions.remove(session);
	}
	
	public static void addReservingTimes(Session session, ReserveTime restime) {
		reservingTimes.put(session, restime);
	}
	
	public static void removeReservingTimes(Session session, ReserveTime restime) {
		reservingTimes.remove(session);
	}		
}