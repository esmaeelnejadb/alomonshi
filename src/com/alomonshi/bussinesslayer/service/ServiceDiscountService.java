package com.alomonshi.bussinesslayer.service;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableServiceDiscount;
import com.alomonshi.object.tableobjects.ServiceDiscount;
import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.utility.CopyNotNullProperties;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceDiscountService {

    private ServiceResponse serviceResponse;
    private ServiceDiscount serviceDiscount;

    public ServiceDiscountService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    public ServiceDiscountService(ServiceResponse serviceResponse, ServiceDiscount serviceDiscount) {
        this.serviceResponse = serviceResponse;
        this.serviceDiscount = serviceDiscount;
    }

    /**
     * Inserting a service discount
     * @return service response
     */
    public ServiceResponse insertDiscount () {
        if (TableServiceDiscount.insert(serviceDiscount))
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Inserting a list of discount (First delete existed discount
     * on selected service and insert new discount)
     * @param serviceDiscounts to be inserted
     * @return service response
     */
    public ServiceResponse insertServiceDiscountList (List<ServiceDiscount> serviceDiscounts) {
        if (TableServiceDiscount.insertList(serviceDiscounts))
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Updating a discount for a service
     * @return service response
     */
    public ServiceResponse updateDiscount() {
        serviceDiscount = getCopiedServiceProperties();
        if (TableServiceDiscount.update(serviceDiscount))
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Deleting a list of discount
     * @param serviceDiscounts to be inserted
     * @return service response
     */
    public ServiceResponse deleteServiceDiscountList (List<ServiceDiscount> serviceDiscounts) {
        if (TableServiceDiscount.deleteList(serviceDiscounts))
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Copy new updated fields into old object got from table
     * @return new updated service
     */
    private ServiceDiscount getCopiedServiceProperties() {
        BeanUtilsBean utilsBean = new CopyNotNullProperties();
        ServiceDiscount newDiscount = TableServiceDiscount.getDiscount(serviceDiscount.getID());
        if (newDiscount.getID() != serviceDiscount.getID())
            return null;
        try {
            utilsBean.copyProperties(newDiscount, serviceDiscount);
        }catch (IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not copy properties " + e);
        }
        return newDiscount;
    }
}