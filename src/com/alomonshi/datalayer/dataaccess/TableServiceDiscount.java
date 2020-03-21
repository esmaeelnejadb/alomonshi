package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.ServiceDiscount;
import com.alomonshi.object.tableobjects.Services;

import javax.xml.ws.Service;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableServiceDiscount {
    private static String insertCommand = "INSERT INTO servicediscount (" +
            "DISCOUNT," +
            "SERVICE_ID," +
            "CREATE_DATE," +
            "EXPIRE_DATE," +
            "REMOVE_DATE," +
            "IS_ACTIVE) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static String updateCommand = "UPDATE servicediscount SET " +
            "DISCOUNT = ?," +
            "SERVICE_ID = ?," +
            "CREATE_DATE = ?," +
            "EXPIRE_DATE = ?," +
            "REMOVE_DATE = ?," +
            "IS_ACTIVE = ? WHERE ID =";

    /**
     * Inserting a discount for a service
     * @param serviceDiscount to be inserted
     * @return result
     */
    public static boolean insert (ServiceDiscount serviceDiscount) {
        Connection connection = DBConnection.getConnection();
        boolean result;
        if (deleteDiscountFromService(serviceDiscount.getServiceID(), connection)) {
            serviceDiscount.setActive(true);
            result = executeInsert(serviceDiscount, insertCommand, connection);
        }
        else {
            DBConnection.closeConnection(connection);
            return false;
        }
        DBConnection.closeConnection(connection);
        return result;
    }

    /**
     * Inserting discount list for a unit
     * @param serviceDiscounts to be inserted
     * @return result
     */
    public static boolean insertList (List<ServiceDiscount> serviceDiscounts) {
        Connection connection = DBConnection.getConnection();
        for (ServiceDiscount serviceDiscount : serviceDiscounts) {
            //First delete existed discount on current service id
            if (deleteDiscountFromService(serviceDiscount.getServiceID(), connection)) {
                serviceDiscount.setActive(true);
                if (!executeInsert(serviceDiscount, insertCommand, connection)) {
                    DBConnection.closeConnection(connection);
                    return false;
                }
            }else {
                DBConnection.closeConnection(connection);
                return false;
            }
        }
        DBConnection.closeConnection(connection);
        return true;
    }

    /**
     * Updating a discount for a service
     * @param serviceDiscount to be updated
     * @return result
     */
    public static boolean update (ServiceDiscount serviceDiscount) {
        Connection connection = DBConnection.getConnection();
        boolean result = executeInsert(serviceDiscount,
                updateCommand + serviceDiscount.getID(),
                connection);
        DBConnection.closeConnection(connection);
        return result;
    }

    /**
     * Deleting a discount for a service
     * @param serviceDiscount to be deleted
     * @return result
     */
    public static boolean delete (ServiceDiscount serviceDiscount) {
        serviceDiscount.setActive(false);
       return update(serviceDiscount);
    }

    /**
     * Deleting service discount list
     * @param serviceDiscounts to be deleted
     * @return result
     */
    public static boolean deleteList (List<ServiceDiscount> serviceDiscounts) {
        Connection connection = DBConnection.getConnection();
        for (ServiceDiscount serviceDiscount : serviceDiscounts) {
            if (!deleteDiscountFromService(serviceDiscount.getServiceID(), connection)) {
                DBConnection.closeConnection(connection);
                return false;
            }
        }
        DBConnection.closeConnection(connection);
        return true;
    }

    /**
     * Executing insert update
     * @param serviceDiscount to be inserted or updated
     * @param command to be executed
     * @param connection object from JDBC
     * @return result
     */
    private static boolean executeInsert(ServiceDiscount serviceDiscount, String command, Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
            prepare(ps, serviceDiscount);
            int i = ps.executeUpdate();
            return i == 1;

        } catch (SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
        return false;
    }

    /**
     * Deleting discount from a service
     * @param serviceID to be deleted
     * @param connection got from JDBC
     * @return result
     */
    private static boolean deleteDiscountFromService (int serviceID, Connection connection) {
        try {
            String command = "UPDATE servicediscount" +
                    " SET" +
                    " REMOVE_DATE = now()," +
                    " IS_ACTIVE = FALSE" +
                    " WHERE" +
                    " IS_ACTIVE IS TRUE AND" +
                    " SERVICE_ID = " + serviceID;
            PreparedStatement ps = connection.prepareStatement(command);
            int i = ps.executeUpdate();
            return i >= 0;

        }catch(SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            return false;
        }
    }

    /**
     * Getting discount object
     * @param discountID to be got
     * @return discount object
     */
    public static ServiceDiscount getDiscount(int discountID) {
        ServiceDiscount serviceDiscount = new ServiceDiscount();
        Connection conn = DBConnection.getConnection();
        try {
            Statement stmt =conn.createStatement();
            String command = "SELECT" +
                    " *" +
                    " FROM" +
                    " servicediscount" +
                    " WHERE" +
                    " IS_ACTIVE IS TRUE AND ID = " + discountID;
            ResultSet rs = stmt.executeQuery(command);
            fillSingleDiscount(rs, serviceDiscount);
        }catch(SQLException e)
        {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }finally
        {
            if(conn != null)
            {
                try
                {
                    conn.close();
                } catch (SQLException e)
                {
                    Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                }
            }
        }
        return serviceDiscount;
    }

    /**
     * Prepare statement ro executing query
     * @param preparedStatement to be prepared
     * @param serviceDiscount to be prepared with
     */
    private static void prepare (PreparedStatement preparedStatement,
                                 ServiceDiscount serviceDiscount) {
        try{
            preparedStatement.setInt(1, serviceDiscount.getDiscount());
            preparedStatement.setInt(2,serviceDiscount.getServiceID());
            preparedStatement.setObject(3,serviceDiscount.getCreateDate());
            preparedStatement.setObject(4,serviceDiscount.getExpireDate());
            preparedStatement.setObject(5,serviceDiscount.getRemoveDate());
            preparedStatement.setBoolean(6,serviceDiscount.isActive());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
        }
    }

    /**
     * Filling a discount object with result set got from database
     * @param resultSet JDBC object
     * @param serviceDiscount object returned from database
     */
    private static void fillDiscount (ResultSet resultSet, ServiceDiscount serviceDiscount) {
        try {
            serviceDiscount.setID(resultSet.getInt(1));
            serviceDiscount.setDiscount(resultSet.getInt(2));
            serviceDiscount.setServiceID(resultSet.getInt(3));
            serviceDiscount.setCreateDate(resultSet.getObject(4, LocalDate.class));
            serviceDiscount.setExpireDate(resultSet.getObject(5, LocalDate.class));
            serviceDiscount.setRemoveDate(resultSet.getObject(6, LocalDateTime.class));
            serviceDiscount.setActive(resultSet.getBoolean(7));
        } catch (SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
        }
    }

    /**
     * Filling a discount returned from database
     * @param resultSet object returned from jDBC
     * @param serviceDiscount to be filled
     */
    private static void fillSingleDiscount (ResultSet resultSet, ServiceDiscount serviceDiscount) {
        try {
            while (resultSet.next()) {
                fillDiscount(resultSet, serviceDiscount);
            }
        } catch (SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
        }
    }
}
