package com.alomonshi.datalayer.dataaccess.payment;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.payment.PaymentRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TablePaymentRequest {

    /**
     * insert commant
     */
    private static final String insertCommand = "INSERT INTO PAYMENTREQUEST (" +
            " CLIENT_ID," +
            " AMOUNT," +
            " AUTHORITY," +
            " MESSAGE," +
            " CODE," +
            " REQUEST_DATETIME," +
            " UNIT_ID," +
            " REQUEST_TYPE," +
            " RES_TIME_ID" +
            ")" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";



    /**
     * insert a manager into manager table
     * @param paymentRequest manager to be inserted
     * @return true if inserted correctly
     */

    public static boolean insertRequest(PaymentRequest paymentRequest){
        Connection connection = DBConnection.getConnection();
        boolean response = executeInsertUpdate(paymentRequest, insertCommand, connection);
        DBConnection.closeConnection(connection);
        return response;
    }

    /**
     * Executing insert update command
     * @param paymentRequest to be inserted
     * @param command command to be executed
     * @param connection JDBC connection
     * @return Execution result
     */
    private static boolean executeInsertUpdate(
            PaymentRequest paymentRequest,
            String command,
            Connection connection)
    {
        try {
            PreparedStatement ps = connection.prepareStatement(command);
            prepare(ps, paymentRequest);
            return ps.executeUpdate() == 1;
        }catch(SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            return false;
        }
    }

    /**
     * Getting payment request by authprity code
     * @param authorityCode authority code
     * @return payment request
     */
    public static PaymentRequest getPaymentRequest(String authorityCode) {
        Connection connection  = DBConnection.getConnection();
        PaymentRequest paymentRequest = new PaymentRequest();
        try{
            String command = "SELECT * FROM" +
                    " paymentrequest " +
                    "WHERE " +
                    "AUTHORITY = '" + authorityCode + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            fillPaymentRequest(resultSet, paymentRequest);
        }catch (SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
        return paymentRequest;
    }

    /**
     * Prepare statement object to be filled by intended object
     * @param preparedStatement JDBC object to be prepared for execution
     * @param paymentRequest to be prepared with
     */
    private static void prepare(PreparedStatement preparedStatement, PaymentRequest paymentRequest){
        try {
            preparedStatement.setInt(1, paymentRequest.getClientID());
            preparedStatement.setString(2, paymentRequest.getAmount());
            preparedStatement.setString(3, paymentRequest.getAuthority());
            preparedStatement.setString(4, paymentRequest.getMessage());
            preparedStatement.setInt(5, paymentRequest.getCode());
            preparedStatement.setObject(6, paymentRequest.getDatetime());
            preparedStatement.setInt(7, paymentRequest.getUnitID());
            preparedStatement.setBoolean(8, paymentRequest.getRequestType());
            preparedStatement.setInt(9, paymentRequest.getReserveTimeID());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

    /**
     * Filling payment request object
     * @param resultSet returned from JDBC
     * @param paymentRequest to be filled
     */
    private static void fillPaymentRequest (ResultSet resultSet, PaymentRequest paymentRequest) {
        try {
            while (resultSet.next()) {
                paymentRequest.setId(resultSet.getInt(1));
                paymentRequest.setClientID(resultSet.getInt(2));
                paymentRequest.setAmount(resultSet.getString(3));
                paymentRequest.setAuthority(resultSet.getString(4));
                paymentRequest.setMessage(resultSet.getString(5));
                paymentRequest.setCode(resultSet.getInt(6));
                paymentRequest.setDatetime(resultSet.getObject(7, LocalDateTime.class));
                paymentRequest.setUnitID(resultSet.getInt(8));
                paymentRequest.setRequestType(resultSet.getBoolean(9));
                paymentRequest.setReserveTimeID(resultSet.getInt(10));
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }
}
