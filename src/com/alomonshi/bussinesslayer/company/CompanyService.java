package com.alomonshi.bussinesslayer.company;

import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.datalayer.dataaccess.TableService;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.object.tableobjects.Company;

import java.util.*;

public class CompanyService {
    public static List<Company> getNearestCompanies(List<Company> companies, float lat, float lon)
    {
        int nearNum = companies.size();
        List<Company> nearestCompanies = new ArrayList<>();
        List<Float> sortedDistances = new ArrayList<>();
        Map<Company, Float> distancesMap = new LinkedHashMap<>();
        for(Company company : companies)
        {
            float lat_distance = Math.abs(company.getLocationLat() - lat);
            float lon_distance = Math.abs(company.getLocationLon() - lon);
            float distance = (float) Math.sqrt(Math.pow(lat_distance, 2) + Math.pow(lon_distance, 2));
            sortedDistances.add(distance);
            distancesMap.put(company, distance);
        }
        Collections.sort(sortedDistances);
        for(int i = 0; i<Math.min(sortedDistances.size(), nearNum); i++)
        {
            for (Map.Entry<Company, Float> entry : distancesMap.entrySet()) {
                if(sortedDistances.get(i) == (float)entry.getValue())
                    nearestCompanies.add(entry.getKey());
            }
        }
        return nearestCompanies;
    }

    public static List<Company> getSearchedCompanies(int categoryID, String compName, String serveName)
    {
        List<Company> companies = new ArrayList<>();
        List<Integer> companyIDs = new ArrayList<>();
        Iterator<Integer> it;
        if (serveName == null){
            if (compName == null){
                companies = TableCompanies.getCompanies(categoryID);
            }else {
                companies = TableCompanies.getSearchedCompanies(compName, categoryID);
            }
        }else {
            it = Objects.requireNonNull(TableService.getSearchedServices(serveName)).iterator();
            if (compName == null){
                while (it.hasNext()) {
                    int unitID = it.next();
                    int companyID = TableUnit.getCompanyID(unitID);
                    if(!companyIDs.contains(companyID))
                    {
                        if(TableCompanies.getCompany(companyID).getCompanyCatID() == categoryID)
                        {
                            Company company =  TableCompanies.getCompany(companyID);
                            companies.add(company);
                            companyIDs.add(companyID);
                        }
                    }
                }
            }else {
                List<Company> companies1 = TableCompanies.getSearchedCompanies(compName, categoryID);
                List<Company> companies2 = new ArrayList<>();
                it = Objects.requireNonNull(TableService.getSearchedServices(serveName)).iterator();
                while (it.hasNext()) {
                    int unitID = it.next();
                    int companyID = TableUnit.getCompanyID(unitID);
                    if(!companyIDs.contains(companyID))
                    {
                        if(TableCompanies.getCompany(companyID).getCompanyCatID() == categoryID)
                        {
                            Company company =  TableCompanies.getCompany(companyID);
                            companies2.add(company);
                            companyIDs.add(companyID);
                        }
                    }
                }
                Set<Company> hs = new LinkedHashSet<>();
                hs.addAll(companies1);
                hs.addAll(companies2);
                companies.addAll(hs);
            }
        }
        return companies;
    }
}
