package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class CategoryTypes {
	private int ID;
	private String typeName;
	private List<CompanyCategories> categories;

	@JsonView(JsonViews.NormalViews.class)
	public int getID() {
		return ID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getTypeName() {
		return typeName;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public List<CompanyCategories> getCategories() {
		return categories;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public void setID(int iD) {
		ID = iD;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public void setCompanyCats(List<CompanyCategories> categories) {
		this.categories = categories;
	}
}
