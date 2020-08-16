package com.alomonshi.bussinesslayer.company;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.configuration.ConfigurationParameter;
import com.alomonshi.datalayer.dataaccess.TableAdmin;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.datalayer.dataaccess.TableFavoriteCompany;
import com.alomonshi.object.enums.FilterItem;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.*;
import com.alomonshi.object.uiobjects.AddingCompany;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.util.*;

/**
 * Company Service
 */
public class ClientCompanyService {

    /**
     * Adding company by a registered client
     * @param addingCompany to be inserted
     * @return service response
     */
    public static ServiceResponse registerCompany(AddingCompany addingCompany, ServiceResponse serviceResponse) {
        //Getting company admin to be checked if registered
        Users user = TableClient.getUser(addingCompany.getUser().getPhoneNo());
        //Admin is not registered to the website
        if (user.getClientID() == 0) {
            insertNotRegisteredAdminCompany(addingCompany, serviceResponse);
        }
        //Admin is registered before in website as company admin
        else if (user.getUserLevel().getValue() >= UserLevels.COMPANY_ADMIN.getValue()) {
            addingCompany.setUser(user);
            insertExistedAdminCompany(addingCompany, serviceResponse);
        }
        //Admin is registered as client in the website
        else {
            addingCompany.setUser(user);
            insertRegisteredAdminCompany(addingCompany, serviceResponse);
        }
        return serviceResponse;
    }

    /**
     * Inserting existed admin's company
     * @param addingCompany to be inserted
     * @param serviceResponse returned to client
     */
    private static void insertExistedAdminCompany (AddingCompany addingCompany, ServiceResponse serviceResponse) {
        int companyID = TableCompanies.insert(addingCompany.getCompany());
        if (companyID > 0) {
            Admin admin = new Admin();
            admin.setActive(true);
            admin.setManagerLevel(UserLevels.COMPANY_ADMIN);
            admin.setManagerID(addingCompany.getUser().getClientID());
            admin.setCompanyID(companyID);
            if (TableAdmin.insertManager(admin)) {
                serviceResponse.setResponse(true);
                serviceResponse.setMessage(ServerMessage.SUCCESSMESSAGE);
            }else {
                serviceResponse.setResponse(false);
                serviceResponse.setMessage(ServerMessage.FAULTMESSAGE);
            }
        }else {
            serviceResponse.setResponse(false);
            serviceResponse.setMessage(ServerMessage.FAULTMESSAGE);
        }
    }

    /**
     * Inserting not register client as admin company
     * @param addingCompany company to be added
     * @param serviceResponse returned response to client
     */
    private static void insertNotRegisteredAdminCompany (AddingCompany addingCompany, ServiceResponse serviceResponse) {
        addingCompany.getUser().setUserLevel(UserLevels.COMPANY_ADMIN);
        addingCompany.getUser().setActive(true);
        int userID = TableClient.insert(addingCompany.getUser());
        if (userID > 0) {
            addingCompany.getUser().setClientID(userID);
            insertExistedAdminCompany(addingCompany, serviceResponse);
        }else {
            serviceResponse.setResponse(false);
            serviceResponse.setMessage(ServerMessage.FAULTMESSAGE);
        }
    }

    /**
     * inserting registered company of a client but not admin company
     * @param addingCompany to be inserted
     * @param serviceResponse returned response
     */
    private static void insertRegisteredAdminCompany (AddingCompany addingCompany, ServiceResponse serviceResponse) {
        addingCompany.getUser().setUserLevel(UserLevels.COMPANY_ADMIN);
        int result = TableClient.update(addingCompany.getUser());
        if (result > 0) {
            insertExistedAdminCompany(addingCompany, serviceResponse);
        }else {
            serviceResponse.setResponse(false);
            serviceResponse.setMessage(ServerMessage.FAULTMESSAGE);
        }
    }

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
