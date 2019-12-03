package com.alomonshi.server.message;

import java.util.ArrayList;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


import java.util.Map;
import java.util.Map.Entry;

import javax.json.*;
import javax.json.spi.JsonProvider;

import com.alomonshi.datalayer.dataaccess.*;
import com.alomonshi.bussinesslayer.tableutils.CalendarUtils;
import com.alomonshi.bussinesslayer.tableutils.ClientUtils;
import com.alomonshi.bussinesslayer.tableutils.CommentUtils;
import com.alomonshi.bussinesslayer.tableutils.ServiceUtils;
import com.alomonshi.bussinesslayer.tableutils.UnitUtils;
import com.alomonshi.object.CategoryTypes;
import com.alomonshi.object.Comments;
import com.alomonshi.object.Company;
import com.alomonshi.object.CompanyCategories;
import com.alomonshi.object.ReserveTime;
import com.alomonshi.object.Services;
import com.alomonshi.object.UnitPicture;
import com.alomonshi.object.Units;
import com.alomonshi.object.Users;

public class SendMessageUtils {
	
	public JsonObject mapToJson(Map<String,String> data){
		JsonObjectBuilder builder = Json.createObjectBuilder();
		for (Entry<String, String> entry : data
		        .entrySet()) {
			builder.add(entry.getKey(), entry.getValue());
		}
		JsonObject doc = builder.build();
		return doc;
	}

	public JsonObject createJsonObjectFromUnit(Units unit) {
		JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder();
		data.add("Name", unit.getUnitName())
				.add("ID", unit.getID())
				.add("steptime", unit.getUnitStepTime());
		if(!unit.getServices().isEmpty())
			data.add("Services", createJsonArrayFromServiceList(unit.getServices()));
		if(!unit.getUnitPics().isEmpty())
			data.add("Pictures", createJsonArrayFromPictureList(unit.getUnitPics()));
		return data.build();
	}

    public JsonArray createJsonArrayFromUnitList(List<Units> list) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for(Units unit : list) {
            jsonArray.add(createJsonObjectFromUnit(unit));
        }
        return jsonArray.build();
    }
    
    public JsonArray createJsonArrayFromServiceList(List<Services> list) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for(Services service : list) {
            jsonArray.add(Json.createObjectBuilder()
                .add("Name", service.getServiceName())
                .add("ID", service.getID())
                .add("price", Integer.toString(service.getServicePrice()))
                .add("steptime", service.getServiceTime()));
        }
        return jsonArray.build();
    }
    
    public JsonArray createJsonArrayFromPictureList(List<UnitPicture> unitpics) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for(UnitPicture unitpic : unitpics) {
            jsonArray.add(Json.createObjectBuilder()
                .add("picurl", unitpic.getPicURL())
                .add("Caption", unitpic.getCaption()));
        }
        return jsonArray.build();
    }
        
    public JsonArray createJsonArrayFromCompanyList(List<Company> list) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for(Company company : list) {
        	JsonObject data = createJsonObjectFromCompany(company);
            jsonArray.add(data);
        }
        return jsonArray.build();
    }
    
    public JsonObject createJsonObjectFromCompany(Company company)
    {
    	JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder();
    	if(company.getCompanyName()!=null)
    		data.add("Name", company.getCompanyName());
    	if(company.getCompanyAddress()!=null)
    		data.add("Address", company.getCompanyAddress());
    	data.add("ID", company.getID());
    	if(company.getRate()!= 0)
    		data.add("Rate", Float.toString(company.getRate()));
    	else
    		data.add("Rate", "New");
    	if(company.getCompanyPhoneNo() != null)
    		data.add("Phone", company.getCompanyPhoneNo());
    	if(company.getPicURL() != null)
    		data.add("url", company.getPicURL());
    	if(company.getWebsite()!= null)
    		data.add("website", company.getWebsite());
    	if(company.getLocationLat()!=0)
    		data.add("latitude", company.getLocationLat());
    	if(company.getLocationLon()!=0)
    		data.add("longitude", company.getLocationLon());
    	data.add("catID", company.getCompanyCatID());
    	data.add("catName", TableCompanyCategory.getCompanyCategory(company.getCompanyCatID()).getCategoryName());
    	if(company.getUnits() != null)
    		data.add("Units", createJsonArrayFromUnitList(company.getUnits()));
    	Map<Integer, LinkedList<Comments>> comments = new CommentUtils().getCompanyComments(company.getID());
    	if (comments != null)
    		data.add("comments", createJsonObjectFromCompanyCommentList(comments));
    	return data.build();
    }
    
    public JsonObject createJsonObjectFromUser(Users user)
    {
    	JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder().add("PhoneNo", user.getPhoneNo());
    	if(user.getName() != null)
    		data.add("Name", user.getName());
    	if(user.getEmail() != null)
    		data.add("Email", user.getEmail());
    	if(user.getUserID() != 0)
    		data.add("userID", user.getUserID());
    	data.add("Phone",user.getPhoneNo());
    	return data.build();
    }
        
    public JsonObject createJsonObjectFromReserveTime(ReserveTime restime)
    {
    	List<Services> services = new ArrayList<Services>();
    	int servsPrice = 0;
    	for(Integer serviceID : restime.getServIDs()) {
    		services.add(TableService.getService(serviceID));
    		servsPrice += TableService.getPrice(serviceID);
    	}

    	JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder().add("ID", restime.getID())
    			.add("unitName", TableUnit.getName(restime.getUnitID()))
				.add("midID", restime.getMiddayID())
				.add("date", new CalendarUtils().getDate(restime.getDateID()))
				.add("day", new CalendarUtils().getDayName(restime.getDateID()))
				.add("startTime", restime.getStarttime().toString().substring(0, 5))
				.add("duration", restime.getDuration().toString().substring(0, 5))
				.add("status", restime.getStatus());
		if(restime.getRescodeID() != null) {
			data.add("reserveCode", restime.getRescodeID());
			data.add("client", createJsonObjectFromUser(new ClientUtils().setUserID(restime.getClientID()).getUser()));
			data.add("services", createJsonArrayFromServiceList(services));
			data.add("servsPrice", servsPrice);
		}
    	return data.build();
    }

	public JsonObject createJsonObjectFromComment(Comments comment) {
		JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder();
		String name = new ClientUtils().setUserID(comment.getClientID()).getName();
		name = name == null ? "کاربر " + comment.getClientID() : name;
		data.add("ID", comment.getID()).add("comment", comment.getComment()).add("name", name)
				.add("date", comment.getCommentDate());
		return data.build();
	}
    
    public JsonArray createJsonArrayFromCompanyCats(List<CompanyCategories> list)
    {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for(CompanyCategories companycat : list) {
        	JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder();
        	data.add("Name", companycat.getCategoryName());
        	data.add("ID", companycat.getID());
        	data.add("companyNo", companycat.getCompanySize());
        	jsonArray.add(data);
        }
        return jsonArray.build();
    }
    
    public JsonObject createJsonArrayFromCategoriesAndNewest(List<CompanyCategories> list)
    {
    	int newNo = 6;
    	JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder();
        JsonArray categoies = createJsonArrayFromCompanyCats(list);
        JsonArray companies = createJsonArrayFromCompanyList(TableCompanies.getNewestCompanies(newNo));
        data.add("Categories", categoies).add("NewestComp", companies);
        return data.build();
    }
    
    public JsonArray createJsonArrayFromCompanyCatTypes(List<CategoryTypes> list)
    {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for(CategoryTypes types : list) {
        	JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder();
        	data.add("Name", types.getTypeName());
        	data.add("ID", types.getID());
        	data.add("categoryNo", types.getCategories().size());
        	jsonArray.add(data);
        }
        return jsonArray.build();
    }

    public JsonArray createJsonArrayFromClientList(Map<Users,Integer> clientlist)
    {
    	JsonArrayBuilder jsonArray = Json.createArrayBuilder();
		for (Entry<Users,Integer> entry : clientlist
		        .entrySet()) {
			JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder().add("user", createJsonObjectFromUser(entry.getKey()))
					.add("resno", entry.getValue());
			
			jsonArray.add(data);
		}		
		return jsonArray.build();
    }
    
    public JsonObject createJsonObjFromDayReservetimesList(List<ReserveTime> list) {
    	JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder();
    	JsonArrayBuilder jsonArray_mor = Json.createArrayBuilder();
        JsonArrayBuilder jsonArray_aft = Json.createArrayBuilder();
        for(ReserveTime restime : list) {
        	if(restime.getMiddayID() == 1)
        		jsonArray_mor.add(Json.createObjectBuilder()
                .add("stTime", restime.getStarttime().toString().substring(0, 5))
                .add("ID", restime.getID())
                .add("status", restime.getStatus()));
        	else if (restime.getMiddayID() == 2)
        	{
        		jsonArray_aft.add(Json.createObjectBuilder()
                        .add("stTime", restime.getStarttime().toString().substring(0, 5))
                        .add("ID", restime.getID())
                        .add("status", restime.getStatus()));
        	}
        }
        data.add("morning", jsonArray_mor.build()).add("afternoon", jsonArray_aft.build());
        return data.build();         
    }    
    
    public JsonArray createJsonObjFromClientReservedtimesList(Map<String,ReserveTime> timeList)
    {
		JsonArrayBuilder reservedTimes = Json.createArrayBuilder();
    	for(Entry<String,ReserveTime> entry : timeList.entrySet()) {
        	int compID = TableUnit.getCompanyID(entry.getValue().getUnitID());
        	int catID = TableCompanies.getCategoryID(compID);
        	int unitID = entry.getValue().getUnitID();
        	int dateID = entry.getValue().getDateID();
        	List<Integer> serviceIDs = entry.getValue().getServIDs();        	
        	String catname = TableCompanyCategory.getCompanyCategory(catID).getCategoryName();
        	String companyname = TableCompanies.getName(compID);
        	String adress ="";
        	String phone = "";
        	phone = phone != null ? phone : "";
        	String unitname = TableUnit.getName(unitID);
        	String[] services = ServiceUtils.getServicesNameFromIDs(serviceIDs);
        	JsonArrayBuilder servicejsonArray = Json.createArrayBuilder();
        	long totaltime = 0;
        	int totalprice = 0;
        	for(int i = 0 ; i< services.length; i++) {
        		servicejsonArray.add(services[i]);
        		totaltime=+ CalendarUtils.stringToTime(TableService.getService(serviceIDs.get(i)).getServiceTime());
        		totalprice =+ TableService.getPrice(serviceIDs.get(i));
        	}
        	String date = new CalendarUtils().getDayName(dateID) + " " + new CalendarUtils().getDate(dateID);
        	String hour = entry.getValue().getStarttime().toString().substring(0, 5);
    		switch(entry.getKey().substring(0,1)) {
    		case "0" :    			
    			reservedTimes.add(Json.createObjectBuilder()
					.add("type", "noEx")
					.add("catname", catname)
					.add("compname", companyname)
					.add("adress", adress)
					.add("phone", phone)
					.add("unitname", unitname)
					.add("services", servicejsonArray)
					.add("date", date)
					.add("hour", hour)
					.add("totaltime", CalendarUtils.timeToString(totaltime).substring(0, 5))
					.add("totalprice", totalprice)
					.add("reservecode", entry.getValue().getRescodeID())
					.add("restimeID", entry.getValue().getID()));
	            break;
    		case "1" :
            	reservedTimes.add(Json.createObjectBuilder()
        			.add("type", "exComm")
        			.add("catname", catname)
        			.add("compname", companyname)
        			.add("adress", adress)
        			.add("phone", phone)
        			.add("unitname", unitname)
        			.add("services", servicejsonArray)
        			.add("date", date)
        			.add("hour", hour)
					.add("totaltime", CalendarUtils.timeToString(totaltime).substring(0, 5))
					.add("totalprice", totalprice)
        			.add("reservecode", entry.getValue().getRescodeID())
        			.add("restimeID", entry.getValue().getID())
        			.add("comment", createJsonObjectFromComment(CommentUtils.getCommentByResTimeID(entry.getValue().getID()))));
	            break;
    		case "2" :
            	reservedTimes.add(Json.createObjectBuilder()
        			.add("type", "exNoComm")
        			.add("catname", catname)
        			.add("compname", companyname)
        			.add("adress", adress)
        			.add("phone", phone)
        			.add("unitname", unitname)
        			.add("services", servicejsonArray)
        			.add("date", date)
        			.add("hour", hour)
					.add("totaltime", CalendarUtils.timeToString(totaltime).substring(0, 5))
					.add("totalprice", totalprice)
        			.add("reservecode", entry.getValue().getRescodeID())
        			.add("restimeID", entry.getValue().getID()));
	            break;
	            default:
	            	break;
    		}
    	}
        return reservedTimes.build();
    }
    
    public JsonObject createJsonObjectFromCompanyCommentList(Map<Integer,LinkedList<Comments>> comments) {
    	JsonObjectBuilder companyComments = Json.createObjectBuilder();
    	UnitUtils unitutil = new UnitUtils();
    	ServiceUtils srvutil = new ServiceUtils();
    	TableReserveTimeServices restimesrv = new TableReserveTimeServices();
    	int count = 0;
    	for(Entry<Integer,LinkedList<Comments>> entry : comments.entrySet()) {
			JsonObjectBuilder unitComments = Json.createObjectBuilder();
    		JsonArrayBuilder commentArr = Json.createArrayBuilder();
    		for(int i = 0; i < entry.getValue().size(); i++) {
    			List<Integer> serviceIDs = restimesrv.setRestimeID(entry.getValue().get(i).getResTimeID()).getService();
            	String[] services = srvutil.getServicesNameFromIDs(serviceIDs);
            	JsonArrayBuilder servicejsonArray = Json.createArrayBuilder();
            	for(int j = 0 ; j< services.length; j++)
            		servicejsonArray.add(services[j]);  
        			commentArr.add(Json.createObjectBuilder().add("comment", createJsonObjectFromComment(entry.getValue().get(i)))
        				.add("services", servicejsonArray.build()));
    		}
//    		companyComments.add(unitutil.setUnitID(entry.getKey()).getName(), commentArr.build());
			unitComments.add("comments", commentArr.build()).add("unitName", TableUnit.getName(entry.getKey()));
			companyComments.add(Integer.toString(count), unitComments);
    		count++;
    	}
    	return companyComments.build();
    }
    
    public JsonArray createJsonArrayFromUnitReservedtimesList(List<ReserveTime> list)
    {
    	JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for(ReserveTime resTimes : list) {
        	jsonArray.add(createJsonObjectFromReserveTime(resTimes));
        }
        return jsonArray.build();
    }
    
    public JsonArray createJsonArrayFromFinancialRpt(List<ReserveTime> list, List<Integer> unitIDs, int stDate, int endDate)
    {
    	JsonArrayBuilder jsonArray = Json.createArrayBuilder();
    	LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> unit_serv_res_no = new LinkedHashMap<Integer,LinkedHashMap<Integer,Integer>>();
    	for(int i=0;i<unitIDs.size();i++) {
        	LinkedHashMap<Integer,Integer> serv_res_no = new LinkedHashMap<Integer,Integer>();
    		for(int j=0;j<TableUnit.getUnit(unitIDs.get(i)).getServices().size();j++)
    			serv_res_no.put(TableUnit.getUnit(unitIDs.get(i)).getServices().get(j).getID(), 0);
    		unit_serv_res_no.put(unitIDs.get(i), serv_res_no);
    	}
    		
        for(ReserveTime restime : list) {
    		int unitID  = TableService.getUnitID(restime.getServIDs().get(0));
        	for(int i = 0; i < restime.getServIDs().size(); i++) {
        		LinkedHashMap<Integer,Integer> serv_res_no = unit_serv_res_no.get(unitID);        		
        		int new_res_no = serv_res_no.get(restime.getServIDs().get(i))+1;
        		serv_res_no.put(restime.getServIDs().get(i), new_res_no);        		
        		unit_serv_res_no.put(unitID, serv_res_no);
        	}
        }
        
        for (int i = 0; i<unitIDs.size(); i++) {
        	JsonObjectBuilder unitfinrptjsonobject = JsonProvider.provider().createObjectBuilder();
        	JsonArrayBuilder servfinrptjsonArray = Json.createArrayBuilder();
        	LinkedHashMap<Integer,Integer> serv_res_no = unit_serv_res_no.get(unitIDs.get(i));
			long totalunittime = 0;
			int totalunitincome = 0;
			int totalresno = 0;
    		for (Entry<Integer, Integer> entry : serv_res_no
    		        .entrySet()) {
    			totalunittime += CalendarUtils.stringToTime(TableService.getTime(entry.getKey()))*entry.getValue();
    			totalunitincome += TableService.getPrice(entry.getKey())*entry.getValue();
    			totalresno += entry.getValue();
    			JsonObjectBuilder data = JsonProvider.provider().createObjectBuilder();
    			data.add("Name", TableService.getName(entry.getKey()))
    			.add("resno", entry.getValue())
    			.add("dayincome", TableService.getPrice(entry.getKey())*entry.getValue()/(endDate-stDate+1))
    			.add("daytime", CalendarUtils.timeToString(CalendarUtils.stringToTime(TableService.getTime(entry.getKey()))
    					*entry.getValue()/(endDate-stDate+1)))
    			.add("totalincome", TableService.getPrice(entry.getKey())*entry.getValue())
    			.add("totaltime", CalendarUtils.timeToString(CalendarUtils.stringToTime(TableService.getTime(entry.getKey()))
    					*entry.getValue()));
    			servfinrptjsonArray.add(data);    		
    		}
    		unitfinrptjsonobject.add("Name", TableUnit.getName(unitIDs.get(i)))
    		.add("resno", totalresno)
    		.add("daytime", CalendarUtils.timeToString(totalunittime/(endDate-stDate+1)))
    		.add("dayincome", totalunitincome/(endDate-stDate+1))
    		.add("totaltime", CalendarUtils.timeToString(totalunittime))
    		.add("totalincome", totalunitincome)
    		.add("servfinrpt", servfinrptjsonArray);
    		jsonArray.add(unitfinrptjsonobject);
        }
        return jsonArray.build();
    }
}
