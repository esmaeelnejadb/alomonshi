package com.alomonshi.datalayer.dataaccess.payment;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.payment.PaymentRequest;
import com.alomonshi.object.tableobjects.payment.PaymentVerified;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TablePaymentVerified {

    /**
     * insert commant
     */
    private static final String insertCommand = "INSERT INTO PAYMENTVERIFIED (" +
            " AUTHORITY," +
            " CODE," +
            " MESSAGE," +
            " CARD_HASH," +
            " CARD_PAN," +
            " REF_ID," +
            " FEE_TYPE," +
            " FEE," +
            " VERIFICATION_DATETIME" +
            ")" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * insert a manager into manager table
     * @param paymentVerified manager to be inserted
     * @return true if inserted correctly
     */

    public static boolean insertVerifiedTransaction(PaymentVerified paymentVerified){
        Connection connection = DBConnection.getConnection();
        boolean response = executeInsertUpdate(paymentVerified, insertCommand, connection);
        DBConnection.closeConnection(connection);
        return response;
    }

    /**
     * Executing insert update command
     * @param paymentVerified to be inserted
     * @param command command to be executed
     * @param connection JDBC connection
     * @return Execution result
     */
    private static boolean executeInsertUpdate(
            PaymentVerified paymentVerified,
            String command,
            Connection connection)
    {
        try {
            PreparedStatement ps = connection.prepareStatement(command);
            prepare(ps, paymentVerified);
            return ps.executeUpdate() == 1;
        }catch(SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            return false;
        }
    }

    /**
     * Getting payment verified by authority code
     * @param authorityCode authority code
     * @return payment request
     */
    public static PaymentVerified getPaymentRequest(String authorityCode) {
        Connection connection  = DBConnection.getConnection();
        PaymentVerified paymentVerified = new PaymentVerified();
        try{
            String command = "SELECT * FROM" +
                    " paymentverified " +
                    "WHERE " +
                    "AUTHORITY = '" + authorityCode + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            fillPaymentVerified(resultSet, paymentVerified);
        }catch (SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
        return paymentVerified;
    }

    /**
     * Prepare statement object to be filled by intended object
     * @param preparedStatement JDBC object to be prepared for execution
     * @param paymentVerified to be prepared with
     */
    private static void prepare(PreparedStatement preparedStatement, PaymentVerified paymentVerified){
        try {
            preparedStatement.setString(1, paymentVerified.getAuthority());
            preparedStatement.setInt(2, paymentVerified.getCode());
            preparedStatement.setString(3, paymentVerified.getMessage());
            preparedStatement.setString(4, paymentVerified.getCardHash());
            preparedStatement.setString(5, paymentVerified.getCardPan());
            preparedStatement.setLong(6, paymentVerified.getRefID());
            preparedStatement.setString(7, paymentVerified.getFeeType());
            preparedStatement.setInt(8, paymentVerified.getFee());
            preparedStatement.setObject(9, paymentVerified.getDateTime());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

    /**
     * Filling payment verified object
     * @param resultSet returned from JDBC
     * @param paymentVerified to be filled
     */
    private static void fillPaymentVerified (ResultSet resultSet, PaymentVerified paymentVerified) {
        try {
            while (resultSet.next()) {
                paymentVerified.setID(resultSet.getInt(1));
                paymentVerified.setAuthority(resultSet.getString(2));
                paymentVerified.setCode(resultSet.getInt(3));
                paymentVerified.setMessage(resultSet.getString(4));
                paymentVerified.setCardHash(resultSet.getString(5));
                paymentVerified.setCardPan(resultSet.getString(6));
                paymentVerified.setRefID(resultSet.getLong(7));
                paymentVerified.setFeeType(resultSet.getString(8));
                paymentVerified.setFee(resultSet.getInt(9));
                paymentVerified.setDateTime(resultSet.getObject(10, LocalDateTime.class));
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }
}
