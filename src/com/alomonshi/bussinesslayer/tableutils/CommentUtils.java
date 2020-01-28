package com.alomonshi.bussinesslayer.tableutils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alomonshi.datalayer.dataaccess.TableComment;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.object.tableobjects.Units;

public class CommentUtils extends TableComment {
	public Map<Integer,LinkedList<Comments>> getCompanyComments(int companyID) {
		Map<Integer,LinkedList<Comments>> unitComments = new LinkedHashMap<Integer,LinkedList<Comments>>();
		List<Comments> allComments = getCommentsOfCompany(companyID);
		List<Units> units = TableCompanies.getCompany(companyID).getUnits();
		ReserveTimeUtils restimeutil = new ReserveTimeUtils();
		for(int i = 0; i<units.size(); i++) {
			LinkedList<Comments> list = new LinkedList<Comments>();
			for(int j = 0; j<allComments.size(); j++) {
				if(restimeutil.getReserveTimeFromID(allComments.get(j).getResTimeID()).getUnitID() == units.get(i).getID()) {
					if(!(allComments.get(j).getComment() == null || allComments.get(j).getComment().equals("")))
						list.add(allComments.get(j));
				}
					
			}
			if(!list.isEmpty())
				unitComments.put(units.get(i).getID(), list);
		}
		return unitComments;
	}
}
