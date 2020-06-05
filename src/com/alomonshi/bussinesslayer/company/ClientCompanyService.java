package com.alomonshi.bussinesslayer.company;

import com.alomonshi.configuration.ConfigurationParameter;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.datalayer.dataaccess.TableFavoriteCompany;
import com.alomonshi.object.enums.FilterItem;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.tableobjects.CompanyCategories;
import com.alomonshi.object.tableobjects.FavoriteCompany;

import java.util.*;

/**
 * Company Service
 */
public class ClientCompanyService {


    /**
     * Getting best list in each category
     * @return list of best companies in each categories
     */
    public static List<CompanyCategories> getBestCompaniesInCategories() {
        return TableCompanies.getBestList(ConfigurationParameter.homePageCompaniesLimitationNumber);
    }

    /**
     * Getting discount list in each category
     * @return list of discount companies in each categories
     */
    public static List<CompanyCategories> getDiscountCompaniesInCategories() {
        return TableCompanies.getDiscountList(ConfigurationParameter.homePageCompaniesLimitationNumber);
    }

    /**
     * Getting newest list in each category
     * @return list of newest companies in each categories
     */
    public static List<CompanyCategories> getNewestCompaniesInCategories() {
        return TableCompanies.getNewestList(ConfigurationParameter.homePageCompaniesLimitationNumber);
    }

    /**
     * Getting all categories discounted companies
     * @return list of companies
     */
    public static List<Company> getAllDiscountCompanies(int limitNumber) {
        limitNumber = limitNumber == 0 ? ConfigurationParameter.homePageCompaniesLimitationNumber : limitNumber;
        return TableCompanies.getAllDiscountCompanies(limitNumber);
    }

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
                                                     int categoryID,
                                                     int clientID) {
        return TableCompanies.getSearchedCompanies(compName, serveName, lat, lon, categoryID, clientID);
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
                                                     FilterItem filterItem,
                                                     int clientID) {
        switch (filterItem) {
            case BEST:
                return TableCompanies.getFilteredBestCompanies(categoryID, clientID);
            case NEAREST:
                return TableCompanies.getFilteredNearestCompanies(lat, lon, categoryID, clientID);
            case CHEAPEST:
                return TableCompanies.getFilteredCheapestCompanies(categoryID, clientID);
            case EXPENSIVE:
                return TableCompanies.getFilteredMostExpensiveCompanies(categoryID, clientID);
            case DISCOUNT:
                return TableCompanies.getFilteredDiscountCompanies(categoryID, clientID);
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
                                                             FilterItem filterItem,
                                                             int clientID) {
        return TableCompanies.getFilteredSearchedCompanies(compName,
                serveName,
                lat,
                lon,
                categoryID,
                filterItem,
                clientID);
    }

    /**
     * Getting client favorite companies
     * @param clientID intended client
     * @return list of companies
     */
    public static List<Company> getFavoriteClientCompanies (int clientID) {
        return TableCompanies.getFavoriteClientCompanies(clientID);
    }

    /**
     * Inserting favorite company
     * @param favoriteCompany to be inserted
     */
    public static boolean handleFavoriteCompany (FavoriteCompany favoriteCompany) {
        TableFavoriteCompany.getFavoriteCompany(favoriteCompany);
        if (favoriteCompany.getID() != 0)
            return TableFavoriteCompany.deleteFavoriteCompany(favoriteCompany);
        else {
            favoriteCompany.setActive(true);
            return TableFavoriteCompany.insertFavoriteCompany(favoriteCompany);
        }
    }
}