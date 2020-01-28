package com.alomonshi.server;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Time;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.alomonshi.bussinesslayer.tableutils.*;
import com.alomonshi.datalayer.dataaccess.*;
import com.alomonshi.server.message.MessageDecoder;
import com.alomonshi.server.message.MessageEncoder;
import com.alomonshi.server.message.ServerMessages;
import com.alomonshi.server.config.HandShakeConfiguration;
import com.alomonshi.server.session.SessionHandler;
import com.alomonshi.server.session.SessionUtils;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ServerEndpoint(value = "/alomonshi", encoders = MessageEncoder.class ,
decoders = MessageDecoder.class, configurator = HandShakeConfiguration.class)
public class AlomonshiServer {

	private Session session;
	private String pageName;
	private ServerMessages serverMessage = new ServerMessages();
	private ClientUtils clientUtil = new ClientUtils();
	private AdminUnitUtils adminUnit = new AdminUnitUtils();
	private TableAds adUtil = new TableAds();
	private TableReserveTimeServices resTimeServices = new TableReserveTimeServices();

	@OnOpen
    public void open(Session session, EndpointConfig config)
    {		
    	SessionHandler.addSession(session);
    	this.session = session; 
    }
    @OnClose
    public void close(Session session) 
    {
    	SessionHandler.removeSession(session);
    	Logger.getLogger("Closed").log(Level.SEVERE, "Session " + session + " is closed");
    }

    @OnError
    public void onError(Throwable error) 
    {
/*
        SendMessage sendmessage = new SendMessage();
        Logger.getLogger("Error").log(Level.SEVERE, "Error on websocket connection", error);
    	try {
    		SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(this.pageName)
    				.setError(serverMessage.main_error).messageBuild());
    		} catch (Exception e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
*/
    }

    @OnMessage
    public void handleMessage(String message, Session session) {}
    /*{
    	if(message.equals("ping"))
    	{
    		try {
				session.getBasicRemote().sendText("pong");
			} catch (IllegalArgumentException | IOException e) {
				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			}
		}
    	else
    	{
    		try (JsonReader reader = Json.createReader(new StringReader(message)))
            {
                SendMessage sendmessage = new SendMessage();
                JsonObject jsonMessage = reader.readObject();                        
                this.pageName = jsonMessage.get("PageName") != null ? jsonMessage.getString("PageName") : "";
                int userID = jsonMessage.get("userID") == null  ? 0 : (jsonMessage.getString("userID").equals("")
						? 0 : Integer.parseInt(jsonMessage.getString("userID"))) ;
                Logger.getLogger("Websocket received message").log(Level.INFO, "Message received from websocket client: " + message);
                if(pageName.equals("Pg_login"))
                {
                	String phone_no = UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("phoneNo"));
                	String password = jsonMessage.getString("password");
                	if(TableAdmin.getUserIDByPhone(phone_no) == 0 && TableClient.getUser(phone_no).getUserID() == 0)
                	{
                    	try {
            				SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
            						.setError(serverMessage.error_gen_01).messageBuild());
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}else if (TableAdmin.getUserIDByPhone(phone_no) != 0)
                	{
                		userID = TableAdmin.getUserIDByPhone(phone_no);
                		if(TableAdmin.isRegistered(userID) && TableAdmin.getPassword(userID).equals(password))
                		{
                			Users user  = TableAdmin.getUser(userID);
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setSingleData(sendmessage.createJsonObjectFromUser(user)).setAdmin().messageBuild());
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                		}else
                		{
                        	try {
                        		SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        				.setError(serverMessage.error_gen_01).messageBuild());
                        		} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                		}
                	}else if (TableClient.getUser(phone_no).getUserID() != 0)
                	{
                		userID = TableClient.getUser(phone_no).getUserID();
                		if(clientUtil.isRegistered() && clientUtil.getPassword().equals(password))
                		{
                			Users user  = clientUtil.getUser();
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setSingleData(sendmessage.createJsonObjectFromUser(user)).setClient().messageBuild());

                        	} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                		}else
                		{
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                						.setError(serverMessage.error_gen_01).messageBuild());
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                		}
                	}
                }
                else if(pageName.equals("Pg_register_1"))
                {
                	String phoneNo = UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("phoneNo"));
                	String password = jsonMessage.getString("password");
                	if(!clientUtil.setUserID(clientUtil.getUserIDByPhone(phoneNo)).isRegistered() &&
                			!TableAdmin.isRegistered(TableAdmin.getUserIDByPhone(phoneNo)))
                	{
            			if(clientUtil.getUserIDByPhone(phoneNo) == 0)
            			{
                			Users newuser = new Users();
                			int newuserID = UtilityFunctions.phoneEncryption(phoneNo);
                			if(!clientUtil.setUserID(newuserID).isRegistered())
                				newuser.setPassword(password).setPhoneNo(phoneNo).setUserID(newuserID);
                			clientUtil.insert(newuser);
            			}

            			int newuserID = clientUtil.getUserIDByPhone(phoneNo);
            			String formatted = UtilityFunctions.generateFiveDigitRandomNum();
            			String[] toNumbers = {phoneNo};
            			String messageContent = "به وب سایت الومنشی خوش آمدید. \n" + "کد امنیتی شما: " + formatted;

        				if (SMSUtils.sendSMS(toNumbers, messageContent))
        				{
                			clientUtil.setUserID(newuserID).setMessageID(Integer.parseInt(formatted));
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setUserID(newuserID).setClient().messageBuild());//send password
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}else
                    	{
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                						.setError(serverMessage.error_gen_07).messageBuild());//passwrod not match
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                	}else
                	{
                    	try {
            				SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
            						.setError(serverMessage.error_gen_05).messageBuild());//passwrod not match
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}
                }
                else if(pageName.equals("Pg_register_2"))
                {
                	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                	int enteredCode = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("smscode")));
                	if(clientUtil.setUserID(userID).getMessageID() == enteredCode)
                	{
                		clientUtil.setUserID(userID).setStatus(1);
                		try {
            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
            						.setSingleData(sendmessage.createJsonObjectFromUser(clientUtil.getUser())).setClient().messageBuild());

                		} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}else
                	{
                		try {
            				SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
            						.setError(serverMessage.error_gen_06).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}
                }else if(pageName.equals("Pg_CompanyRegister"))
                {
                	String phoneNo = UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("mngPhone"));
                	if(TableAdmin.getUserIDByPhone(phoneNo) == 0)
                	{
                		if (clientUtil.getUserIDByPhone(phoneNo) != 0 )
                		{
                			clientUtil.setUserID(clientUtil.getUserIDByPhone(phoneNo)).delete();
                		}
                    	String mngName = jsonMessage.getString("mngName");
                    	String mngPassword = jsonMessage.getString("mngPassword");
                    	int newUserID = UtilityFunctions.phoneEncryption(phoneNo);
                    	String mngEmail = jsonMessage.getString("mngEmail");
                    	String logoURL = jsonMessage.getString("logoURL");
                    	String compName = jsonMessage.getString("compName");
                    	String compAddress = jsonMessage.getString("compAddress");
                    	String compPhone = jsonMessage.getString("compPhone").equals("") ? null : jsonMessage.getString("compPhone");
                    	String compWebsite = jsonMessage.getString("compWebsite");
                    	int catID = Integer.parseInt(jsonMessage.getString("categoryID"));
                    	float latitude = Float.parseFloat(jsonMessage.getString("latitude"));
                    	float longitude  = Float.parseFloat(jsonMessage.getString("longitude"));
                    	JsonObject mapResult = jsonMessage.getJsonObject("result");
                    	String locality = mapResult.getJsonObject("result").getString("locality");
                    	String city = mapResult.getJsonObject("result").getString("city");
                    	String district = mapResult.getJsonObject("result").getString("district");
                    	int status = 0;
                    	Company newCompany = new Company();
                    	newCompany.setCompanyName(compName).setCompanyAddress(compAddress).setCompanyPhoneNo(compPhone)
								.setCompanyCatID(catID).setWebsite(compWebsite).setPicURL(logoURL).setStatus(status)
								.setLocationLat(latitude).setLocationLon(longitude).setLocality(locality)
								.setCityID(UtilityFunctions.cityNameToNumber(city)).setDistrictID(UtilityFunctions
								.districtNameToNumber(district)).setActive(false);
                    	if(TableCompanies.insert(newCompany)) {
                        	int companyID = TableCompanies.getID(compName ,catID);
                        	Users newAdmin = new Users();
                        	newAdmin.setName(mngName).setPassword(mngPassword).setPhoneNo(phoneNo).setEmail(mngEmail)
                        	.setCompanyID(companyID).setUserLevel(1).setUserID(newUserID);
                        	TableAdmin.insertUser(newAdmin);
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_main_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}else
                    	{
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                						.setError(serverMessage.main_error).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                	}else
                	{
                    	try {
            				SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
            						.setError(serverMessage.error_gen_05).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}
                }
                else if(pageName.equals("Pg_index"))
            	{
                	int typeID = jsonMessage.getInt("typeID");
                	List<CompanyCategories> companyCategoryList = TableCompanyCategory.getCompanyCategoryList(typeID);
    				try {
        				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
        						.setSingleData(sendmessage.createJsonArrayFromCategoriesAndNewest(companyCategoryList)).messageBuild()); //
        			} catch (Exception e) {
        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        			}
            	}
                                
                else if(pageName.equals("Pg_Categories"))
            	{
                	int typeID = jsonMessage.getInt("typeID");
                	List<CompanyCategories> categoryList = TableCompanyCategory.getCompanyCategoryList(typeID);
    				try {
        				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
        						.setData(sendmessage.createJsonArrayFromCompanyCats(categoryList)).messageBuild()); //
        			} catch (Exception e) {
        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        			}
            	}
                
                else if(pageName.equals("Pg_cltcompanies"))
            	{
            		int catID = Integer.parseInt(jsonMessage.getString("categoryID"));
            		List<Company> companies = TableCompanies.getCompanies(catID);
            		if(!companies.isEmpty())
            		{
    					try {
            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
            						.setData(sendmessage.createJsonArrayFromCompanyList(companies)).messageBuild()); // 
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
            		}else
                	{
                		try {
                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                					.setError(serverMessage.error_company_01).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}
            	}
				else if(pageName.equals("Pg_cltAllCompanies"))
				{
					List<Company> companies = TableCompanies.getAllCompanies();
					if(!companies.isEmpty())
					{
						try {
							SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
									.setData(sendmessage.createJsonArrayFromCompanyList(companies)).messageBuild()); //
						} catch (Exception e) {
							Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
						}
					}else
					{
						try {
							SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
									.setError(serverMessage.error_company_01).messageBuild()); //
						} catch (Exception e) {
							Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
						}
					}
				}
                
                else if(pageName.equals("Pg_search"))
            	{
                	int catID = Integer.parseInt(jsonMessage.getString("categoryID"));
                	String companyName = jsonMessage.getString("SRcompanyName");
                	String serviceName = jsonMessage.getString("SRserviceName");
                	JsonArray latlan = jsonMessage.getJsonArray("latlan");
                	List<Company> companies = new ArrayList<>();
					companies = CompanyUtils.getSearchedCompanies(catID, companyName, serviceName);
                	if(!latlan.getString(0).equals("undefined"))
                		companies = CompanyUtils.getNearestCompanies(companies, Float.parseFloat(latlan.getString(0)),
                				Float.parseFloat(latlan.getString(1)));
            		if(!companies.isEmpty())
            		{
    					try {
            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
            						.setData(sendmessage.createJsonArrayFromCompanyList(companies)).messageBuild()); // 
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
            		}else
                	{
                		try {
                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                					.setError(serverMessage.error_company_02).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}            		            		
            	}
                
            	else if(pageName.equals("Pg_cltcompany"))
            	{
            		int companyID = Integer.parseInt(jsonMessage.getString("companyID"));
            		Company company = TableCompanies.getCompany(companyID);
            		if(company.getID() != 0)
            		{
            			try {
            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
            						.setSingleData(sendmessage.createJsonObjectFromCompany(company)).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
            		}else
                	{
                		try {
                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                					.setError(serverMessage.error_company_01).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}
            	}
                
            	else if(pageName.equals("Pg_cltdate"))
            	{
                	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
    				int dateID = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("dateID")));
                	if(!CalendarUtils.isExpiredDate(dateID)) {
						Map<Enum, List<ReserveTime>> resTimes = TableReserveTime.getClientUnitReserveTimeInADay(dateID, unitID);
        				if(!resTimes.isEmpty())
        				{
                    		try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setSingleData(sendmessage.createJsonObjFromDayReservetimesList(resTimes)).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
                	}else
    				{
                		try {
                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                					.setError(serverMessage.error_retime_12).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
    				}
            	}       
                
            	else if(pageName.equals("Pg_clthourcheck"))
            	{
                	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                	//int dateID = (int)UtilityFunctions.convertToEnglishDigits(Integer.parseInt(jsonMessage.getString("dateID")));	
                	int restimeID = Integer.parseInt(jsonMessage.getString("restimeID"));
                	JsonArray IDs = jsonMessage.getJsonArray("serviceIDs");                	
                	List<Integer> serviceIDs = new ArrayList<>();
                	for(int i=0; i<IDs.size(); i++)
                		serviceIDs.add(Integer.parseInt(IDs.get(i).toString()));                
    				long Dur = (long) 0;
    				for(int i = 0; i<serviceIDs.size(); i++)
    				{
    					Dur += CalendarUtils.stringToTime(TableService.getTime(serviceIDs.get(i)));
    				}
    				Time serviceDur = new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(Dur)));
    				if(ReserveTimeUtils.isNotReserved(restimeID))
    				{
    					if(ServiceUtils.isExistedClientServices(serviceIDs, unitID))
    					{
    						if(ReserveTimeUtils.isPossibleToGetForService(restimeID, serviceDur))
    						{
    							try {
    	            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).messageBuild()); //
    	            			} catch (Exception e) {
    	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
    	            			}								
    						}else
    	    				{
    	                		try {
    	                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
    	                					.setError(serverMessage.error_retime_03).messageBuild()); //
    	            			} catch (Exception e) {
    	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
    	            			}
    	    				}
    					}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_04).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
    				}else
    				{
                		try {
                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                					.setError(serverMessage.error_retime_06).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
    				}
            	}
                            
                else if(TableAdmin.isRegistered(userID))
                {
                    if(pageName.equals("Pg_adnpro"))
                	{
            			Users user = TableAdmin.getUser(userID);
    					try {
            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).setSingleData(sendmessage.createJsonObjectFromUser(user))
            						.setMessage(serverMessage.message_profile_02).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}

                    else if(pageName.equals("Pg_adnproedt"))
                	{
                    	String Name = jsonMessage.getString("Name");
                    	String Email = jsonMessage.getString("Email");
                		if(TableAdmin.setName(Name, userID) && TableAdmin.setEmail(Email, userID))
                		{
                			Users user = TableAdmin.getUser(userID);
        					try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).setSingleData(sendmessage.createJsonObjectFromUser(user))
                						.setMessage(serverMessage.message_profile_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                		}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                	}
                    else if(pageName.equals("Pg_adnPassedt"))
                	{
                    	String oldpass = jsonMessage.getString("oldpass");
                    	String newpass = jsonMessage.getString("newpass");
                    	String renewpass = jsonMessage.getString("renewpass");
                		if(TableAdmin.getPassword(userID).equals(oldpass))
                		{
                			if(newpass.equals(renewpass))
                			{
                    			if(TableAdmin.setPassword(newpass, userID))
                    			{
                					try {
                        				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                        						.setMessage(serverMessage.message_profile_02).messageBuild()); //
                        			} catch (Exception e) {
                        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                        			}
                    			}else
                            	{
                            		try {
                            			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                            					.setError(serverMessage.main_error).messageBuild()); //
                        			} catch (Exception e) {
                        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                        			}
                            	}                    			
                			}else
                        	{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_gen_01).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                        	}
                		}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_gen_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                	}                    
                	else if(pageName.equals("Pg_unitadd"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	String unitName = jsonMessage.getString("Name");
                    	String stepTime = jsonMessage.getString("StepTime");
                    	int companyID = TableAdmin.getCompanyID(userID);
                    	int unitID = TableUnit.getID(companyID, unitName);
                    	if(AdminUtils.isManager(userID))
                    	{
                    		if(companyID != 0 && !UnitUtils.isExisted(companyID, unitID))
                        	{
                        		
                            	Units added_unit = new Units();
                            	added_unit.setUnitName(unitName).setUnitStepTime(stepTime).setCompanyID(companyID).setIsActive(true);
                            	TableUnit.insertUnit(added_unit);
                            	unitID = TableUnit.getID(companyID, unitName);
                            	adminUnit.setMngID(userID).insertAdmin(unitID);
                        		try {
                    				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                    						.setMessage(serverMessage.message_unit_01).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);            				
                    			}
                        	}else
                        	{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_unit_01).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                        	}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    	            	            	
                    }else if(pageName.equals("Pg_unit"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	adminUnit.setMngID(userID);
                    	if(!adminUnit.getAdminUnitForJson().isEmpty())
                    	{
                    		try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setData(sendmessage.createJsonArrayFromUnitList(adminUnit.getAdminUnitForJson())).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_unit_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }else if(pageName.equals("Pg_unitdel"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	adminUnit.setMngID(userID);
                    	if(AdminUtils.isManager(userID))
                    	{
                    		adminUnit.deleteAdminUnit(unitID);
                    		TableService.deleteUnitServices(unitID);
                    		TableUnit.delete(Objects.requireNonNull(TableUnit.getUnit(unitID)));
                    		TableReserveTime.deleteUnit(unitID);
                    		try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_unit_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}            				            	
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    	
                    }else if(pageName.equals("Pg_unitnameedit"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	String newName = jsonMessage.getString("unitnewname");
                    	int companyID = TableAdmin.getCompanyID(userID);
                    	if(AdminUtils.isManager(userID))
                    	{
                    		if(!UnitUtils.isRepeatedName(newName, companyID))
                    		{
                    			TableUnit.setName(newName, unitID);
                    			try {
                    				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                    						.setMessage(serverMessage.message_unit_03).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                    		}else
                        	{
                            	try {
                    				SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    						.setError(serverMessage.error_unit_01).messageBuild());//server action is not ok
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                        	}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}            	
                    }
                    
                    else if(pageName.equals("Pg_unitstedit"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	String newsteptime = jsonMessage.getString("newsteptime");
                    	if(AdminUtils.isManager(userID))
                    	{
                    		TableUnit.setSteptime(newsteptime, unitID);
                    		try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_unit_04).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}            	
                    }else if(pageName.equals("Pg_servadd"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int companyID = TableAdmin.getCompanyID(userID);
                    	if(UnitUtils.isExisted(companyID, unitID))
                    	{
                    		if(TableService.getID(jsonMessage.getString("servname"), unitID) == 0)
                    		{
                    			Services newServe = new Services().setUnitID(unitID)
                        				.setServiceName(jsonMessage.getString("servname"))
                        				.setServiceTime(jsonMessage.getString("servtime"))
                        				.setServicePrice(Integer.parseInt(jsonMessage.getString("servprice"))).setIsActive(true);
                    			TableService.insertService(newServe);
                        		try {
                    				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                    						.setMessage(serverMessage.message_serv_01).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                    		}else
                    		{
                    			try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_serv_02).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                    		}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_serv_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }else if(pageName.equals("Pg_service"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int companyID = TableAdmin.getCompanyID(userID);
                    	if(UnitUtils.isExisted(companyID,unitID))
                    	{
                    		if(!ServiceUtils.isEmpty(unitID))
                    		{
                        		try {
                    				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                    						.setData(sendmessage.createJsonArrayFromServiceList
													(TableService.getUnitServices(unitID))).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                    		}else
                        	{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_serv_04).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                        	}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_unit_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }else if(pageName.equals("Pg_servdel"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int serviceID = Integer.parseInt(jsonMessage.getString("serviceID"));
                    	if(ServiceUtils.isServiceExisted(serviceID, unitID))
                    	{
                    		TableService.delete(TableService.getService(serviceID));
                    		TableReserveTime.deleteService(unitID, serviceID);
                			try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_serv_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_serv_03).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    	
                    }else if(pageName.equals("Pg_servtimeedt"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int serviceID = Integer.parseInt(jsonMessage.getString("serviceID"));
                    	String newtime = jsonMessage.getString("newtime");
                    	adminUnit.setMngID(userID);
                    	if(!adminUnit.isEmpty())
                    	{
                    		TableService.setTime(newtime, serviceID);
                			try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_serv_03).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}

                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_unit_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    	
                    }else if(pageName.equals("Pg_servprcedt"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int serviceID = Integer.parseInt(jsonMessage.getString("serviceID"));
                    	int newprice = Integer.parseInt(jsonMessage.getString("newprice"));
                    	adminUnit.setMngID(userID);
                    	if(!adminUnit.isEmpty())
                    	{
                    		TableService.setPrice(newprice, serviceID);
                			try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_serv_04).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}

                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_unit_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}                	
                    }
                    
                    else if(pageName.equals("Pg_hourset"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
        				int stDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("stDate")));
        				int endDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("endDate")));
        				String mor_stTime = jsonMessage.getString("mor_sttime");
        				String mor_endTime = jsonMessage.getString("mor_endtime");
        				String aft_stTime = jsonMessage.getString("aft_sttime");
        				String aft_endTime = jsonMessage.getString("aft_endtime");
                    	List<ReserveTime> mor_restimes = ReserveTimeUtils.getReserveTimesForMiddayActions(unitID, stDate, endDate, 1);
    					List<ReserveTime> clientrestims = ReserveTimeUtils.getClientResTimeForChangeActions(mor_restimes);
    					
    					List<ReserveTime> aft_restimes = ReserveTimeUtils.getReserveTimesForMiddayActions(unitID, stDate, endDate, 2);
    					List<ReserveTime> aft_clientrestims = ReserveTimeUtils.getClientResTimeForChangeActions(aft_restimes);
    					
    					clientrestims.addAll(aft_clientrestims);
    					
    					if(clientrestims.isEmpty())
    					{
    						long mor_work_time = CalendarUtils.stringToTime(mor_endTime) - CalendarUtils.stringToTime(mor_stTime);
    						long aft_work_time = CalendarUtils.stringToTime(aft_endTime) - CalendarUtils.stringToTime(aft_stTime);
    						long unit_steptime = CalendarUtils.stringToTime(TableUnit.getStepTime(unitID));
    						if(mor_work_time !=0 && unit_steptime < mor_work_time )
    						{
    							if(aft_work_time !=0 && unit_steptime < aft_work_time)
    							{
            	    				List<ReserveTime> restimes = ReserveTimeUtils.getReserveTimesForMiddayActions(unitID, stDate, endDate, 1);
            	    				List<Integer> IDs = ReserveTimeUtils.getRestimeIDsForChangeActions(restimes);
									ReserveTimeUtils.deleteReserveTimes(IDs);
            						if(ReserveTimeUtils.insertReserveTimes(ReserveTimeUtils.generateReserveTime(unitID, stDate, endDate, 1, mor_stTime, mor_endTime))) {
                						restimes = ReserveTimeUtils.getReserveTimesForMiddayActions(unitID, stDate, endDate, 2);
                	    				IDs = ReserveTimeUtils.getRestimeIDsForChangeActions(restimes);
										ReserveTimeUtils.deleteReserveTimes(IDs);
                	    				if(ReserveTimeUtils.insertReserveTimes(ReserveTimeUtils.generateReserveTime(unitID, stDate, endDate, 2, aft_stTime, aft_endTime))) {
                    	        			try {
                    	        				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                    	        						.setMessage(serverMessage.message_restime_01).messageBuild()); //
                    	        			} catch (Exception e) {
                    	        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    	        			}
                	    				}else
                						{
                                    		try {
                                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                                    					.setError(serverMessage.main_error).messageBuild()); //
                                			} catch (Exception e) {
                                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                                			}
                						}
            						}else
            						{
                                		try {
                                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                                					.setError(serverMessage.main_error).messageBuild()); //
                            			} catch (Exception e) {
                            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                            			}
            						}
    							}else
        						{
                            		try {
                            			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                            					.setError(serverMessage.error_retime_11).messageBuild()); //
                        			} catch (Exception e) {
                        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                        			}
        						}
    						}else
    						{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_retime_10).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
    						}
    					}else
        				{
    						StringBuilder date = new StringBuilder();
    						for(int i = 0; i<clientrestims.size(); i++ )
    						{
    							date.append(new CalendarUtils().getDate(clientrestims.get(i).getDateID())).append("\n");
    						}
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_09 + "\n" + date).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
                    	
        				//List<Integer> interferedTimes = restime.getInterferedDatesBetweenMiddays(unitID, stDate, endDate, stTime, endTime, middayID);            	
                    }
                    else if(pageName.equals("Pg_mordel"))
                    {
                    	//int userID = Integer.parseInt(jsonMessage.getString("userID"));
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
        				int stDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("stDate")));
        				int endDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("endDate")));
        				int middayID = 1;
        				List<ReserveTime> resTimes = ReserveTimeUtils.getReserveTimesForMiddayActions(unitID, stDate, endDate, middayID);
        				List<Integer> IDs = ReserveTimeUtils.getRestimeIDsForChangeActions(resTimes);
        				if(!IDs.isEmpty())
        				{
							ReserveTimeUtils.deleteReserveTimes(IDs);
                			try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_restime_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        					
        				}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
                    }
                    
                    else if(pageName.equals("Pg_aftdel"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
        				int stDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("stDate")));
        				int endDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("endDate")));
        				int middayID = 2;
        				List<ReserveTime> restimes = ReserveTimeUtils.getReserveTimesForMiddayActions(unitID, stDate, endDate, middayID);
        				List<Integer> IDs = ReserveTimeUtils.getRestimeIDsForChangeActions(restimes);
        				if(!IDs.isEmpty())
        				{
							ReserveTimeUtils.deleteReserveTimes(IDs);
                			try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_restime_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        					
        				}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
        			
                    }
                    
                    else if(pageName.equals("Pg_daydel"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
        				int stDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("stDate")));
        				int endDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("endDate")));
        				List<ReserveTime> reserveTimes = ReserveTimeUtils.getReserveTimesForDayActions(unitID, stDate, endDate);
        				List<Integer> IDs = ReserveTimeUtils.getRestimeIDsForChangeActions(reserveTimes);
        				if(!IDs.isEmpty())
        				{
							ReserveTimeUtils.deleteReserveTimes(IDs);
                			try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_restime_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        					
        				}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}    		
                    }
                    else if(pageName.equals("Pg_houredt"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
        				int dateID = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("dateID")));
        				List<ReserveTime> reserveTimes = ReserveTimeUtils.getAdminUnitReserveTimeInADay(dateID, unitID);
        				if(reserveTimes != null)
        				{
                    		try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setSingleData(sendmessage.createJsonObjFromDayReservetimesList(reserveTimes)).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}                	
                    }
                    else if(pageName.equals("Pg_hourdel"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int dateID = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("dateID")));
                    	int restimeID = Integer.parseInt(jsonMessage.getString("restimeID"));
                    	if(ReserveTimeUtils.isExisted(restimeID))
                    	{
							ReserveTimeUtils.cancelClientReserveTime(restimeID);
                    		List<ReserveTime> restimes = ReserveTimeUtils.getAdminUnitReserveTimeInADay(dateID, unitID);
    						try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).setMessage(serverMessage.message_restime_04)
                						.setSingleData(sendmessage.createJsonObjFromDayReservetimesList(restimes)).messageBuild()); // 
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_05).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
                    }
                    else if(pageName.equals("Pg_hourdev"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int dateID = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("dateID")));
                    	int restimeID = Integer.parseInt(jsonMessage.getString("restimeID"));
                    	JsonArray IDs = jsonMessage.getJsonArray("serviceIDs");
                    	List<Integer> serviceIDs = new ArrayList<>();
                    	for(int i=0; i<IDs.size(); i++)
                    		serviceIDs.add(Integer.parseInt(IDs.get(i).toString()));                
    					long Dur = (long) 0;
    					for(int i = 1; i<serviceIDs.size(); i++)
    					{
    						Dur += CalendarUtils.stringToTime(TableService.getTime(serviceIDs.get(i)));
    					}
    					Time serviceDur = new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(Dur)));
    					if(ReserveTimeUtils.isNotReserved(restimeID))
    					{
    						if(ServiceUtils.isExistedClientServices(serviceIDs, unitID))
    						{
    							if(ReserveTimeUtils.isPossibleToGetForService(restimeID, serviceDur))
    							{
    								ReserveTime devotedResTime = ReserveTimeUtils.getReserveTimeFromID(restimeID);
    								devotedResTime.setServiceIDs(serviceIDs).setClientID(1);
    								if(ReserveTimeUtils.setClientNewReserveTime(devotedResTime)) {
										List<ReserveTime> restimes = ReserveTimeUtils.getAdminUnitReserveTimeInADay(dateID, unitID);
										try {
											SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).setMessage(serverMessage.message_restime_03)
													.setSingleData(sendmessage.createJsonObjFromDayReservetimesList(restimes)).messageBuild()); //
										} catch (Exception e) {
											Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
										}
									}else {
										try {
											SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
													.setError(serverMessage.error_retime_13).messageBuild()); //
										} catch (Exception e) {
											Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
										}
									}
    							}else
    		    				{
    		                		try {
    		                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
    		                					.setError(serverMessage.error_retime_03).messageBuild()); //
    		            			} catch (Exception e) {
    		            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
    		            			}
    		    				}
    						}else
    	    				{
    	                		try {
    	                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
    	                					.setError(serverMessage.error_retime_05).messageBuild()); //
    	            			} catch (Exception e) {
    	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
    	            			}
    	    				}
    					}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}		
                    }
                    else if(pageName.equals("Pg_hourcancel"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int dateID = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("dateID")));
                    	int restimeID = Integer.parseInt(jsonMessage.getString("restimeID"));                	
                    	if(ReserveTimeUtils.isExisted(restimeID))
                    	{
                    		if(ReserveTimeUtils.isNotReserved(restimeID))
                    		{
								ReserveTimeUtils.cancelReserveTime(restimeID);
                    			List<ReserveTime> restimes = ReserveTimeUtils.getAdminUnitReserveTimeInADay(dateID, unitID);
    							try {
    	            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).setMessage(serverMessage.message_restime_05)
    	            						.setSingleData(sendmessage.createJsonObjFromDayReservetimesList(restimes)).messageBuild()); // 
    	            			} catch (Exception e) {
    	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
    	            			}
                    		}else
            				{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_retime_06).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
            				}
                    	}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_06).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
                    }
                    else if(pageName.equals("Pg_hourret"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int dateID = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("dateID")));
                    	int restimeID = Integer.parseInt(jsonMessage.getString("restimeID"));
                    	if(ReserveTimeUtils.isExisted(restimeID))
                    	{
                    		if(ReserveTimeUtils.isCanceled(restimeID))
                    		{
								ReserveTimeUtils.setFreeReserveTime(restimeID);
                    			List<ReserveTime> restimes = ReserveTimeUtils.getAdminUnitReserveTimeInADay(dateID, unitID);
    							try {
    	            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).setMessage(serverMessage.message_restime_06)
    	            						.setSingleData(sendmessage.createJsonObjFromDayReservetimesList(restimes)).messageBuild()); // 
    	            			} catch (Exception e) {
    	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
    	            			}
                    		}else
            				{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_retime_06).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
            				}
                    	}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_06).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
                    }
                    else if(pageName.equals("Pg_adminadd"))
                    {
                    	String newadminPN = UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("newadminPN"));
                    	JsonArray unitIDs = jsonMessage.getJsonArray("unitIDs");
                    	if(AdminUtils.isManager(userID))
                    	{
                        	int companyID = TableAdmin.getCompanyID(userID);
                        	int companycatID = TableAdmin.getCompanyCatID(userID);
                        	int newadmincltID = clientUtil.getUserIDByPhone(newadminPN);
                        	int newadminadnID = TableAdmin.getUserIDByPhone(newadminPN);
                        	clientUtil.setUserID(newadmincltID);
                        	if(!AdminUtils.isAdmin(newadminadnID))
                        	{
                            	if(clientUtil.isRegistered())
                            	{
                                	Users newAdmin = clientUtil.setUserID(newadmincltID).getUser();
                                	clientUtil.setStatus(0);
                                	TableAdmin.insertUser(newAdmin);
                                	int newID = TableAdmin.getUserIDByPhone(newadminPN);
                                	AdminUtils.setAdmin(newID);
                                	TableAdmin.setCompanyID(companyID, newID);
                                	TableAdmin.setCompanyCatID(companycatID, newID);
                                	adminUnit.setMngID(newID);
                                	for(int i = 0; i < unitIDs.size(); i++)
                                		adminUnit.insertAdmin(Integer.parseInt(unitIDs.get(i).toString()));
                        			try {
                        				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                        						.setMessage(serverMessage.message_admin_01).messageBuild()); //
                        			} catch (Exception e) {
                        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                        			}
                            	}else
                            	{
                            		try {
                            			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                            					.setError(serverMessage.error_admin_01).messageBuild()); //
                        			} catch (Exception e) {
                        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                        			}
                            	}
                        	}else
                        	{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_admin_02).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                        	}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }
                    else if(pageName.equals("Pg_admindel"))
                    {
                    	String adminPN = UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("adminPN"));
                    	if(AdminUtils.isManager(userID))
                    	{
                        	int companyID = TableAdmin.getCompanyID(userID);
                        	int deladminID = TableAdmin.getUserIDByPhone(adminPN);
                        	if(TableAdmin.getCompanyID(deladminID) == companyID)
                        	{
                        		adminUnit.setMngID(deladminID).deleteAdmin();
                            	TableAdmin.deleteUser(deladminID);
                            	int clientid = clientUtil.getUserIDByPhone(adminPN);
                            	clientUtil.setUserID(clientid).setStatus(1);
                    			try {
                    				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                    						.setMessage(serverMessage.message_admin_02).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                        	}else
                        	{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_admin_03).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                        	}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }
                    else if(pageName.equals("Pg_sendpic"))
                    {
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	JsonArray serviceIDs = jsonMessage.getJsonArray("serviceIDs");
                    	JsonArray picURLs = jsonMessage.getJsonArray("picURL");
                    	JsonArray picCaptions = jsonMessage.getJsonArray("picCaption");
                    	if(!serviceIDs.isEmpty()) {
                        	for (int i = 0; i< picURLs.size(); i++)
                        	{
                        		UnitPicture unitPicture = new UnitPicture();
                        		TableUnitPics.insertUnitPic(unitPicture.setUnitID(unitID)
                        				.setServiceID(serviceIDs.getInt(i))
                        				.setCaption(picCaptions.getString(i))
                        				.setPicURL(picURLs.getString(i))
                        				.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()))
                        				.setIsActive(true));
                        	}
                        	
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_sendpic_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	} else {
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_pic_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }
                    else if(pageName.equals("Pg_sendad"))
                    {
                    	int companyID = TableAdmin.getCompanyID(userID);
                    	adUtil.setCompanyID(companyID);
                    	
                    	String picURL = jsonMessage.getString("picURL");
                    	String caption = jsonMessage.getString("caption");
                    	
                    	if(adUtil.insertAds(picURL, caption, new java.sql.Date(Calendar.getInstance().getTime().getTime())))
                    	{
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_sendad_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    	else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}               	
                    }else if(pageName.equals("Pg_CompanyProfile"))
                    {
                    	String compName = !jsonMessage.getString("compName").equals("")
								? jsonMessage.getString("compName") : TableCompanies.getName(TableAdmin.getCompanyID(userID));
                    	String compAddress = !jsonMessage.getString("compAddress").equals("")
								? jsonMessage.getString("compAddress") : TableCompanies.getName(TableAdmin.getCompanyID(userID));
                    	String compPhone = !jsonMessage.getString("compPhone").equals("")
								? jsonMessage.getString("compPhone") : TableCompanies.getName(TableAdmin.getCompanyID(userID));
                    	String compWebsite = !jsonMessage.getString("compWebsite").equals("")
								? jsonMessage.getString("compWebsite") : TableCompanies.getName(TableAdmin.getCompanyID(userID));
                    	
                    	if(TableCompanies.setName(compName, TableAdmin.getCompanyID(userID)) &&
								TableCompanies.setAddress(compAddress, TableAdmin.getCompanyID(userID))
                    			&& TableCompanies.setPhone(compPhone, TableAdmin.getCompanyID(userID))
                    			&& TableCompanies.setWebSite(compWebsite, TableAdmin.getCompanyID(userID)))
                    	{
                        	try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_sendad_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    	else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }else if(pageName.equals("Pg_adnrptcltlist"))
                    {
                    	adminUnit.setMngID(userID);
                    	                    	
                    	JsonArray uIDs = jsonMessage.getJsonArray("unitIDs");
                    	JsonArray sIDs = jsonMessage.getJsonArray("serviceIDs");
                    	List<Integer> unitIDs = new ArrayList<>();
                    	List<Integer> serviceIDs = new ArrayList<>();
                    	Map<Users,Integer> clientList;
                    	                    	
                    	if(uIDs.getString(0).equals("0"))
                    	{
                    		unitIDs = adminUnit.getAdminUnitIDs();
                    	}
                    	else
                    	{                        	
                        	for(int i=0; i<uIDs.size(); i++)
                        		unitIDs.add(Integer.parseInt(uIDs.get(i).toString()));
                    	}	
                    	
                    	if(sIDs.getString(0).equals("0"))
                    		clientList = new RestimeServiceUtils().getUnitClientListInUnits(unitIDs);
                    	else
                    	{
                        	for(int i=0; i<sIDs.size(); i++)
                        		serviceIDs.add(Integer.parseInt(sIDs.getString(i)));
                    		clientList = new RestimeServiceUtils().getUnitClientListInServices(serviceIDs);
                    	}
                    	
                    	if (!clientList.isEmpty())
                    	{
                    		try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).
                						setData(sendmessage.createJsonArrayFromClientList(clientList)).messageBuild()); 
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.message_report_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }
                    else if(pageName.equals("Pg_adnrptrestime"))
                    {
                    	adminUnit.setMngID(userID);
        				int stDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("stDate")));
        				int endDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("endDate")));
                    	JsonArray uIDs = jsonMessage.getJsonArray("unitIDs");
                    	List<Integer> unitIDs = new ArrayList<>();
                    	List<ReserveTime> reserveTimes = new ArrayList<>();
                    	                    	
                    	if(uIDs.getString(0).equals("0"))
                    	{
                    		unitIDs = adminUnit.getAdminUnitIDs();
                    	}
                    	else
                    	{                        	
                        	for(int i=0; i<uIDs.size(); i++)
                        		unitIDs.add(Integer.parseInt(uIDs.get(i).toString()));
                    	}
                    	
                    	for(int i = 0; i < unitIDs.size(); i++)
                    		reserveTimes.addAll(TableReserveTime.getClientReservedListInAUnit(unitIDs.get(i), stDate, endDate));

                    	if (!reserveTimes.isEmpty())
                    	{
                    		try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).
                						setData(sendmessage.createJsonArrayFromUnitReservedtimesList(reserveTimes)).messageBuild());
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.message_report_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }
                    else if(pageName.equals("Pg_adnrptfinlst"))
                    {
                    	adminUnit.setMngID(userID);
        				int stDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("stDate")));
        				int endDate = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("endDate")));
                    	JsonArray uIDs = jsonMessage.getJsonArray("unitIDs");
                    	//JsonArray sIDs = jsonMessage.getJsonArray("serviceIDs");
                    	List<Integer> unitIDs = new ArrayList<>();
                    	//List<Integer> serviceIDs = new ArrayList<>();
                    	List<ReserveTime> restimes = new ArrayList<ReserveTime>();
                    	                    	
                    	if(uIDs.getString(0).equals("0"))
                    	{
                    		unitIDs = adminUnit.getAdminUnitIDs();
                    	}
                    	else
                    	{                        	
                        	for(int i=0; i<uIDs.size(); i++)
                        		unitIDs.add(Integer.parseInt(uIDs.get(i).toString()));
                    	}
                    	
                    	for(int i = 0; i < unitIDs.size(); i++)
                    		restimes.addAll(ReserveTimeUtils.getClientReservedListInAUnit(unitIDs.get(i), stDate, endDate));
                    	
                    	if (!restimes.isEmpty())
                    	{
                    		try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).
                						setData(sendmessage.createJsonArrayFromFinancialRpt(restimes, unitIDs, stDate, endDate)).messageBuild()); 
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.message_report_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                    }
                    
                    else
                	{
                		try {
                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                					.setError(serverMessage.main_error_01).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}
                }
                else if(clientUtil.isRegistered())
                {
                    if(pageName.equals("Pg_CltProfile"))
                	{
                    	String Name = jsonMessage.getString("Name");
                    	String Email = jsonMessage.getString("Email");
                    	clientUtil.setUserID(userID);
                		if(clientUtil.setName(Name) && clientUtil.setEmail(Email))
                		{
                			Users user = clientUtil.getUser();
        					try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).setSingleData(sendmessage.createJsonObjectFromUser(user))
                						.setMessage(serverMessage.message_profile_02).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                		}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.main_error).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                	}
                    else if(pageName.equals("Pg_CltChangePass"))
                	{
                    	String oldpass = jsonMessage.getString("oldpass");
                    	String newpass = jsonMessage.getString("newpass");
                    	String renewpass = jsonMessage.getString("renewpass");
                    	clientUtil.setUserID(userID);
                		if(clientUtil.getPassword().equals(oldpass))
                		{
                			if(newpass.equals(renewpass))
                			{
                    			if(clientUtil.setPassword(newpass))
                    			{
                					try {
                        				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                        						.setMessage(serverMessage.message_profile_02).messageBuild()); //
                        			} catch (Exception e) {
                        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                        			}
                    			}else
                            	{
                            		try {
                            			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                            					.setError(serverMessage.main_error).messageBuild()); //
                        			} catch (Exception e) {
                        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                        			}
                            	}                    			
                			}else
                        	{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_gen_01).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                        	}
                		}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_gen_01).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}
                	}                    
                    else if(pageName.equals("Pg_clthour"))
                	{
                    	int unitID = Integer.parseInt(jsonMessage.getString("unitID"));
                    	int dateID = Integer.parseInt(UtilityFunctions.convertToEnglishDigits(jsonMessage.getString("dateID")));
                    	int restimeID = Integer.parseInt(jsonMessage.getString("restimeID"));
                    	JsonArray IDs = jsonMessage.getJsonArray("serviceIDs");
                    	List<Integer> serviceIDs = new ArrayList<>();
                    	for(int i=0; i<IDs.size(); i++)
                    		serviceIDs.add(Integer.parseInt(IDs.get(i).toString()));                
        				long Dur = (long) 0;
        				for(int i = 0; i<serviceIDs.size(); i++)
        				{
        					Dur += CalendarUtils.stringToTime(TableService.getTime(serviceIDs.get(i)));
        				}
        				Time serviceDur = new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(Dur)));
        				if(ReserveTimeUtils.isNotReserved(restimeID))
        				{
        					if(ServiceUtils.isExistedClientServices(serviceIDs, unitID))
        					{
        						if(ReserveTimeUtils.isPossibleToGetForService(restimeID, serviceDur))
        						{
        							ReserveTime devotedResTime = ReserveTimeUtils.getReserveTimeFromID(restimeID);
        							if(SessionUtils.checkRestimeIsfreeAndAdd(session, devotedResTime)) {
            							devotedResTime.setServiceIDs(serviceIDs).setClientID(userID);
            							if(ReserveTimeUtils.setClientNewReserveTime(devotedResTime)) {
            								List<Integer> mngIDs = adminUnit.getAdminIDs(unitID);
            								String[] toNumbers = new String[mngIDs.size()];
            								for(int i = 0; i<mngIDs.size(); i++)
            									toNumbers[i] = "0" + Long.toString(TableAdmin.getPhoneNo(mngIDs.get(i)));
            								String SMSmessage  = "وقت بخیر\n\n" + " یک مشتری با مشخصات زیر در واحد شما وقت رزرو نمود." + "\n\n"
            									+ "شماره تماس: "	+ clientUtil.getPhoneNo() +  "\n"
            									+ "روز:  " + new CalendarUtils().getDayName(devotedResTime.getDateID()) + "\n"
            									+ "تاریخ:  " + new CalendarUtils().getDate(devotedResTime.getDateID()) + "\n"
            									+ "ساعت:  " + CalendarUtils.timeToString(CalendarUtils.sqlTimeToLong(devotedResTime.getStartTime())) + "\n"
            									+ "کد رزرو:  " + devotedResTime.getResCodeID() +"\n\n"
            									+ "وب سایت الومنشی: \n" + "https://alomonshi.com";
            								//UtilityFunctions.sendSMS(toNumbers, SMSmessage);

            								//List<ReserveTime> restimes = restimeutil.getClientUnitReserveTimeInADay(dateID, unitID);
                							try {
                								SessionHandler.removeReservingTimes(session, devotedResTime);
                	            				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName).setMessage(serverMessage.message_restime_07).messageBuild()); //
                	            			} catch (Exception e) {
                	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                	            			}        								
            							}else
                	    				{
                	                		try {
                	                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                	                					.setError(serverMessage.error_retime_13).messageBuild()); //
                	            			} catch (Exception e) {
                	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                	            			}
                	    				}
        							}else {
            	                		try {
            	                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
            	                					.setError(serverMessage.error_retime_06).messageBuild()); //
            	            			} catch (Exception e) {
            	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            	            			}
        							}
        						}else
        	    				{
        	                		try {
        	                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
        	                					.setError(serverMessage.error_retime_03).messageBuild()); //
        	            			} catch (Exception e) {
        	            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        	            			}
        	    				}
        					}else
            				{
                        		try {
                        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                        					.setError(serverMessage.error_retime_05).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
            				}
        				}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_06).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}
                	}
                	else if(pageName.equals("Pg_cltreservedtime"))
                	{
                		Map<String,ReserveTime> restimes = ReserveTimeUtils.getClientAllReserveTimes(userID);
                		if(!restimes.isEmpty())
                		{

    						try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setData(sendmessage.createJsonObjFromClientReservedtimesList(restimes)).messageBuild()); // 
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                		}else
        				{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_07).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
        				}            		
                	}                    
                	else if(pageName.equals("Pg_SubmitComm"))
                	{
                		String cltcomment = jsonMessage.getString("comment");
                		int rate = Integer.parseInt(jsonMessage.getString("rate"));
                		int restimeID = Integer.parseInt(jsonMessage.getString("restimeID"));
                		Comments comment = new Comments();
                		if(CommentUtils.getCommentByResTimeID(restimeID) == null) {
                    		comment.setClientID(userID).setComment(cltcomment).setServiceRate(rate).setCommentDate(CalendarUtils.getCurrDate())
                    		.setResTimeID(restimeID).setIsACtive(false).setUnitID(resTimeServices.setRestimeID(restimeID).getCompany());
                    		if(CommentUtils.insertComment(comment)) {
                    			Map<String,ReserveTime> restimes = ReserveTimeUtils.getClientAllReserveTimes(userID);
                            	try {
                    				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                    						.setData(sendmessage.createJsonObjFromClientReservedtimesList(restimes))
                    								.setMessage(serverMessage.message_comment_01).messageBuild()); //
                    			} catch (Exception e) {
                    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                    			}
                    		}else {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_comment_01).messageBuild()); //
                    		}
                		}else {
                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                					.setError(serverMessage.error_comment_02).messageBuild()); //
                		}
                	}
                    else if(pageName.equals("Pg_cltcanceltime"))
                    {
                    	int restimeID = jsonMessage.getInt("restimeID");
                    	if(restimeID != 0)
                    	{
							ReserveTime restime = ReserveTimeUtils.getReserveTimeFromID(restimeID);
							int unitID = restime.getUnitID();
							List<Integer> mngIDs = adminUnit.getAdminIDs(unitID);
							String[] toNumbers = new String[mngIDs.size()];
							if(ReserveTimeUtils.isReserved(restimeID)) {
								ReserveTimeUtils.cancelClientReserveTime(restimeID);
								for(int j = 0; j<mngIDs.size(); j++)
									toNumbers[j] = "0" + Long.toString(TableAdmin.getPhoneNo(mngIDs.get(j)));
								String SMSmessage  = "وقت بخیر\n\n" + "  مشتری شما با مشخصات زیر نوبت خود را در واحد شما کنسل نمود:" + "\n\n"
										+ "شماره تماس:  " + clientUtil.getPhoneNo() +  "\n"
										+ "روز:  " + new CalendarUtils().getDayName(restime.getDateID()) + "\n"
										+ "تاریخ:  " + new CalendarUtils().getDate(restime.getDateID()) + "\n"
										+ "ساعت:  " + CalendarUtils.timeToString(CalendarUtils.sqlTimeToLong(restime.getStartTime())) +"\n\n"
										+ "وب سایت الومنشی: \n" + "https://alomonshi.com";
								//UtilityFunctions.sendSMS(toNumbers, SMSmessage);
							} try {
                				SessionUtils.sendToSession(session,sendmessage.setOK().setPageName(pageName)
                						.setMessage(serverMessage.message_restime_05).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}	
                    	}else
                    	{
                    		try {
                    			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                    					.setError(serverMessage.error_retime_08).messageBuild()); //
                			} catch (Exception e) {
                				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                			}
                    	}

                    }
                	else
                	{
                		try {
                			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
                					.setError(serverMessage.main_error_01).messageBuild()); //
            			} catch (Exception e) {
            				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            			}
                	}
                	
                }else
            	{
            		try {
            			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
            					.setError(serverMessage.main_error_01).messageBuild()); //
        			} catch (Exception e) {
        				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        			}
            	}
            }catch (Exception e1) {
            	e1.printStackTrace();
        		try {
                    SendMessage sendmessage = new SendMessage();
        			SessionUtils.sendToSession(session,sendmessage.setNotOK().setPageName(pageName)
        					.setError(serverMessage.main_error).messageBuild()); //
    			} catch (Exception e) {
    				Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
    			}
            }    		
    	}
    }*/
}