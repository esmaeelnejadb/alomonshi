package com.alomonshi.bussinesslayer.adminmanagement;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.ClientInformationCheck;
import com.alomonshi.datalayer.dataaccess.TableAdmin;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.datalayer.dataaccess.TableUnitAdmin;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.Admin;
import com.alomonshi.object.tableobjects.UnitAdmin;
import com.alomonshi.object.tableobjects.Units;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.object.uiobjects.CompanyAdmin;
import com.alomonshi.restwebservices.message.ServerMessage;
import java.util.ArrayList;
import java.util.List;


public class AdminManagementService {

    private CompanyAdmin companyAdmin;
    private ServiceResponse serviceResponse;
    private ClientInformationCheck clientInformationCheck;

    /**
     * Constructor
     * @param serviceResponse to be injected
     */
    public AdminManagementService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Constructor
     * @param serviceResponse object to be injected
     * @param companyAdmin object to be injected
     */
    public AdminManagementService(CompanyAdmin companyAdmin, ServiceResponse serviceResponse) {
        this.companyAdmin = companyAdmin;
        this.serviceResponse = serviceResponse;
    }

    /**
     * Getting unit admin list
     * @return service response
     */
    public ServiceResponse getCompanyAdminList(int companyID) {
        List<CompanyAdmin> companyAdmins = TableAdmin.getCompanyAdmin(companyID);
        if (!companyAdmins.isEmpty()) {
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(companyAdmins);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.ADMINERROR_01);
    }

    /**
     * Add admin to a company
     * @return service response
     */
    public ServiceResponse addAdmin() {
        //Getting client information to be inserted as admin
        Users user = TableClient.getUser(companyAdmin.getAdminPhone());
        Admin admin = TableAdmin.getAdminOfCompany(user.getClientID(), companyAdmin.getCompanyID());
        if (admin.getID() == 0) {
            prepareManagerForInsert(admin, user);
            //Check if client is registered in web site
            clientInformationCheck = new ClientInformationCheck(user);
            if (clientInformationCheck.isClientRegistered()) {
                //Setting admin id in company admin object for editing admin unit table
                companyAdmin.setAdminID(user.getClientID());
                //Updating user level in table client
                if (user.getUserLevel() == UserLevels.CLIENT) {
                    user.setUserLevel(UserLevels.COMPANY_SUB_ADMIN);
                    TableClient.update(user);
                    //Inserting admin in manager table
                    if (TableAdmin.insertManager(admin)) {
                        //Inserting admin units
                        return editAdminUnits();
                    }else
                        return serviceResponse
                                .setResponse(false)
                                .setMessage(ServerMessage.FAULTMESSAGE);
                }else
                    return serviceResponse
                            .setResponse(false)
                            .setMessage(ServerMessage.FAULTMESSAGE);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ADMINERROR_02);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.ADMINERROR_05);

    }

    /**
     * Editing admin units into admin unit table
     * @return service response
     */
    public ServiceResponse editAdminUnits() {
        Admin admin = TableAdmin.getAdminOfCompany(companyAdmin.getAdminID()
                , companyAdmin.getCompanyID());
        //Check if this admin is registered before in this company
        if (admin.getID() != 0) {
            if (admin.getManagerLevel().getValue() < UserLevels.COMPANY_ADMIN.getValue()) {
                if (companyAdmin.getAdminUnit() != null) {
                    List<UnitAdmin> unitAdmins = new ArrayList<>();
                    // Preparing unit admin list to be inserted in database
                    prepareAdminUnitForUpdate(unitAdmins, companyAdmin.getAdminID());
                    //Deleting old admin ids
                    if (TableUnitAdmin.deleteAdmin(companyAdmin.getAdminID(), companyAdmin.getCompanyID())) {
                        //Inserting new admin units
                        if (TableUnitAdmin.insertUnitAdmins(unitAdmins))
                            return serviceResponse
                                    .setResponse(true)
                                    .setMessage(ServerMessage.SUCCESSMESSAGE);
                        else
                            return serviceResponse
                                    .setResponse(false)
                                    .setMessage(ServerMessage.FAULTMESSAGE);
                    }else
                        return serviceResponse
                                .setResponse(true)
                                .setMessage(ServerMessage.FAULTMESSAGE);

                }else
                    return serviceResponse
                            .setResponse(false)
                            .setMessage(ServerMessage.ADMINERROR_04);
            }else
                return serviceResponse
                        .setResponse(false)
                        .setMessage(ServerMessage.ACCESSFAULT);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.ADMINERROR_03);
    }

    /**
     * Delete company Admin
     * @return service response;
     */
    public ServiceResponse deleteAdmin() {
        Admin admin = TableAdmin.getAdminOfCompany(companyAdmin.getAdminID()
                , companyAdmin.getCompanyID());
        if (admin.getID() != 0) {
            if (admin.getManagerLevel().getValue() < UserLevels.COMPANY_ADMIN.getValue()) {
                boolean response = TableAdmin.deleteAdmin(admin)
                        && TableUnitAdmin.deleteAdmin(companyAdmin.getAdminID(), companyAdmin.getCompanyID());

                //Check if this user is admin of any other companies to change his level in client info table
                List<Integer> adminCompanies = TableAdmin.getAdminCompanyIDs(admin.getID());
                if (adminCompanies.isEmpty()) {
                    Users user = TableClient.getUser(admin.getManagerID());
                    user.setUserLevel(UserLevels.CLIENT);
                    TableClient.update(user);
                }
                if (response)
                    return serviceResponse
                            .setResponse(true)
                            .setMessage(ServerMessage.SUCCESSMESSAGE);
                else
                    return serviceResponse
                            .setResponse(false)
                            .setMessage(ServerMessage.FAULTMESSAGE);
            }else
                return serviceResponse
                        .setResponse(false)
                        .setMessage(ServerMessage.ACCESSFAULT);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.ADMINERROR_03);
    }

    /**
     * Prepare admin for insert process
     * @param admin to be inserted
     * @param user is going to be admin
     */
    private void prepareManagerForInsert (Admin admin, Users user) {
        admin.setCompanyID(companyAdmin.getCompanyID());
        admin.setManagerID(user.getClientID());
        admin.setManagerLevel(UserLevels.COMPANY_SUB_ADMIN);
        admin.setActive(true);
    }

    /**
     * Preparing admin unit list to be inserted into database
     * @param unitAdminList to be prepared for insertion
     * @param adminID intended admin
     */
    private void prepareAdminUnitForInsert (List<UnitAdmin> unitAdminList, int adminID) {
        //Getting admin unit IDs from database to be checked with new units got from database
        List<Integer> existedAdminUnitIDs = TableUnitAdmin.getAdminUnitIDs(companyAdmin.getAdminID());
        //Preparing unit admin to be inserted in AdminUnit Table
        for (Units unit : companyAdmin.getAdminUnit()) {
            if (!existedAdminUnitIDs.contains(unit.getID())) {
                UnitAdmin unitAdmin = new UnitAdmin();
                unitAdmin.setActive(true);
                unitAdmin.setManagerID(adminID);
                unitAdmin.setUnitID(unit.getID());
                unitAdminList.add(unitAdmin);
            }
        }
    }

    /**
     * Preparing admin unit list to be updated into database
     * @param unitAdminList to be prepared for update
     * @param adminID intended admin
     */
    private void prepareAdminUnitForUpdate (List<UnitAdmin> unitAdminList, int adminID) {
        for (Units unit : companyAdmin.getAdminUnit()) {
            UnitAdmin unitAdmin = new UnitAdmin();
            unitAdmin.setActive(true);
            unitAdmin.setManagerID(adminID);
            unitAdmin.setUnitID(unit.getID());
            unitAdminList.add(unitAdmin);
        }
    }
}
