package com.alomonshi.bussinesslayer.accesscheck.changeaccesscheck;

import com.alomonshi.datalayer.dataaccess.TableReserveTime;

public class CheckClientAuthority {

    private int clientID;
    private int IDtoBeChecked;

    public CheckClientAuthority(int clientID, int IDtoBeChecked) {
        this.clientID = clientID;
        this.IDtoBeChecked = IDtoBeChecked;
    }

    /**
     * Check if client has access to change the comment
     * @return true if yes
     */

    public boolean isAuthorizedToChangeComment(){
        return TableReserveTime.getReserveTime(IDtoBeChecked).getClientID() == clientID;
    }

}
