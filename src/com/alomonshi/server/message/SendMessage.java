package com.alomonshi.server.message;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

public class SendMessage extends SendMessageUtils{
	
	private int userID;
	private String user_type;
	private String message_stat;
	private String error;
	private String pagename;
	private String message;
	private JsonArray ArrayData;
	private JsonObject singledata;
	private JsonObjectBuilder provider = JsonProvider.provider().createObjectBuilder();
	
	// Constructor
	public SendMessage()
	{ }
	
	public SendMessage setOK()
	{
		this.message_stat = "ok";
		return this;
	}
	
	public SendMessage setNotOK()
	{
		this.message_stat = "not_ok";
		return this;
	}
	
	public SendMessage setUserID(int userID)
	{
		this.userID = userID;
		return this;
	}
	
	public SendMessage setAdmin()
	{
		this.user_type = "admin";
		return this;
	}
	
	public SendMessage setClient()
	{
		this.user_type = "client";
		return this;
	}
	
	public SendMessage setError(String error)
	{
		this.error = error;
		return this;
	}
	
	public SendMessage setPageName(String pagename)
	{
		this.pagename = pagename;
		return this;
	}
	
	public SendMessage setData(JsonArray Data)
	{
		this.ArrayData = Data;
		return this;
	}
	
	public SendMessage setSingleData(JsonObject singledata)
	{
		this.singledata = singledata;
		return this;
	}
	
	public SendMessage setMessage(String message)
	{
		this.message = message;
		return this;
	}
			
	public String getUserType()
	{		
		return this.user_type;
	}
	
	public String getMessageStatus()
	{		
		return this.message_stat;
	}
	
	public JsonObject messageBuild()
	{
		
		if(this.message_stat.equals("ok"))
		{
			provider.add("message_stat", this.message_stat);
			if(this.userID != 0)
				provider.add("user_id", this.userID);
			if(this.user_type != null)
				provider.add("user_type", this.user_type);
			if(this.pagename != null)
				provider.add("PageName", this.pagename);
			if(this.ArrayData != null)
				provider.add("Data", this.ArrayData);
			if(this.singledata != null)
			{
				provider.add("singledata", this.singledata);
			}
			if(this.message != null)
				provider.add("message", this.message);
			
		}else if(this.message_stat.equals("not_ok"))
		{
			provider.add("message_stat", this.message_stat);
			if(this.error !=null)
				provider.add("error", this.error);
			if(this.pagename != null)
				provider.add("PageName", this.pagename);
			
		}
			return provider.build();
	}	
}
