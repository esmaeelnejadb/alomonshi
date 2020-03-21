package com.alomonshi.bussinesslayer.company;

import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.enums.FilterItem;
import com.alomonshi.object.tableobjects.Company;

import java.util.*;

/**
 * Company Service
 */
public class CompanyService {

    /**
     * Getting searched companies
     * @param compName company name
     * @param serveName service name
     * @param lat client location latitude
     * @param lon client location longiture
     * @param categoryID searched category id
     * @return list of company
     */
    public static List<Company> getSearchedCompanies(String compName,
                                                     String serveName,
                                                     float lat,
                                                     float lon,
                                                     int categoryID) {
        return TableCompanies.getSearchedCompanies(compName, serveName, lat, lon, categoryID);
    }

    /**
     * Getting filtered nearest companies
     * @param categoryID filtered category id
     * @param filterItem filtered item
     * @return list of filtered companies
     */
    public static List<Company> getFilteredCompanies(float lat,
                                                     float lon,
                                                     int categoryID,
                                                     FilterItem filterItem) {
        switch (filterItem) {
            case BEST:
                return TableCompanies.getFilteredBestCompanies(categoryID);
            case NEAREST:
                return TableCompanies.getFilteredNearestCompanies(lat, lon, categoryID);
            case CHEAPEST:
                return TableCompanies.getFilteredCheapestCompanies(categoryID);
            case EXPENSIVE:
                return TableCompanies.getFilteredMostExpensiveCompanies(categoryID);
            case DISCOUNT:
                return TableCompanies.getFilteredDiscountCompanies(categoryID);
        }
        return null;
    }

    /**
     * Getting filtered searched companies
     * @param compName company name
     * @param serveName service name
     * @param lat client location latitude
     * @param lon client location longitude
     * @param categoryID searched category id
     * @param filterItem to be filtered by
     * @return list of company
     */
    public static List<Company> getFilteredSearchedCompanies(String compName,
                                                     String serveName,
                                                     float lat,
                                                     float lon,
                                                     int categoryID,
                                                     FilterItem filterItem) {
        return TableCompanies.getFilteredSearchedCompanies(compName,
                serveName,
                lat,
                lon,
                categoryID,
                filterItem);
    }
}