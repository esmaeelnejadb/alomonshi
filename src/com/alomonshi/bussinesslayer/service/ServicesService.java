package com.alomonshi.bussinesslayer.service;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.datalayer.dataaccess.TableService;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.utility.CopyNotNullProperties;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicesService {

    private ServiceResponse serviceResponse;
    private Services service;


    /**
     * Constructor
     * @param serviceResponse injected object
     */

    public ServicesService(Services service, ServiceResponse serviceResponse) {
        this.service = service;
        this.serviceResponse = serviceResponse;
    }

    /**
     * Constructor
     * @param serviceResponse injected object
     */

    public ServicesService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Getting unit services from database
     * @return service response
     */
    public ServiceResponse getUnitServices(int unitID) {
        List<Services> services = TableService.getUnitServices(unitID);
        if (!services.isEmpty()) {
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(services);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.SERVICEERROR_01);
    }

    /**
     * Inserting new service
     * @return service response
     */
    public ServiceResponse insertNewService() {
        prepareServiceForInsert();
        if (TableService.insertService(service))
            return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Updating a service
     * @return service response
     */

    public ServiceResponse updateService() {
        prepareServiceForUpdate();
        if (TableService.updateService(service))
            return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Delete a service
     * @return service response
     */

    public ServiceResponse deleteService() {
        prepareServiceForDelete();
        // Check if any reserved time existed in that service
        List<ReserveTime> reserveTimes = TableReserveTime.getServiceNotYetReserveTime(service);
        if (reserveTimes.isEmpty()) {
            if (TableService.delete(service))
                return serviceResponse.setResponse(true)
                        .setMessage(ServerMessage.SUCCESSMESSAGE);
            else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.SERVICEERROR_02)
                    .setResponseData(reserveTimes);
    }

    /**
     * Copy new updated fields into old object got from table
     * @return new updated service
     */

    private Services getCopiedServiceProperties() {
        BeanUtilsBean utilsBean = new CopyNotNullProperties();
        Services newService = TableService.getService(service.getID());
        if (newService.getID() != service.getID())
            return null;
        try {
            utilsBean.copyProperties(newService, service);
        }catch (IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not copy properties " + e);
        }
        return newService;
    }

    /**
     * Prepare service object for insert action
     */
    private void prepareServiceForInsert() {
        service.setCreateDate(LocalDateTime.now());
        service.setActive(true); // Activate service
    }

    /**
     * Prepare service object for update action
     */
    private void prepareServiceForUpdate() {
        // Copy not null objects into object to be updated into database
        service.setUpdateDate(LocalDateTime.now());
        // fill null properties which get from ui with properties get from database
        service = getCopiedServiceProperties();
    }

    /**
     * Prepare service for delete
     */

    private void prepareServiceForDelete() {
        service.setServiceName(null);
        service.setServicePrice(0);
        service.setPictureURLs(null);
        service.setRemark(null);
        service.setServiceDuration(0);
        service.setCreateDate(null);
        service.setUpdateDate(null);
        service.setRemoveDate(LocalDateTime.now());
        // fill null properties which get from ui with properties get from database
        service = getCopiedServiceProperties();
    }
}
