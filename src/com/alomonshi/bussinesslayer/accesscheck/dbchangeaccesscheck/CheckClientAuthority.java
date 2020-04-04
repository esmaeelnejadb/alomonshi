package com.alomonshi.bussinesslayer.accesscheck.dbchangeaccesscheck;

import com.alomonshi.datalayer.dataaccess.TableReserveTime;

public class CheckClientAuthority {

    /**
     * Check if client has access to change the comment
     * @return true if yes
     */
    public static boolean isAuthorizedToChangeComment(int clientID, int reserveTimeID){
        return TableReserveTime.getReserveTime(reserveTimeID).getClientID() == clientID;
    }

}
