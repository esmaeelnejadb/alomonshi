package com.alomonshi.object;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class CategoryTypes {
	private int ID;
	private String typeName;
	private List<CompanyCategories> categories;
	
	public int getID() {
		return ID;
	}
	public String getTypeName() {
		return typeName;
	}
	public List<CompanyCategories> getCategories() {
		return categories;
	}
	public CategoryTypes setID(int iD) {
		ID = iD;
		return this;
	}
	public CategoryTypes setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}
	public CategoryTypes setCompanyCats(List<CompanyCategories> categories) {
		this.categories = categories;
		return this;
	}
}
