package library;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This object connects to a Library Database. Contains all commands to stored
 * procedures
 *
 * @author aribdhuka
 */
public class LibraryConnection {

    //holds sql connection and reference to database location
    private Connection connection;
    SQLServerDataSource dataSource;

    //Creates a Library connection object
    public LibraryConnection() {
        dataSource = new SQLServerDataSource();
        try {
            createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the list of all books that match a given input
     *
     * @param isbn isbn to search for
     * @param title title of book to search for
     * @param author author to search for
     * @return ResultSet that contains all rows from database
     * @throws SQLException if connection to server fails
     */
    public ResultSet getBooks() throws SQLException {
        //Statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call getbooks stored procedure with given input parameters
        cstmt = connection.prepareCall("{call dbo.getBooks()}");

        //execute and capture result
        cstmt.execute();
        rs = cstmt.getResultSet();

        //return result
        return rs;
    }

    /**
     * Gets all books that are already checked out
     *
     * @param isbn isbn to search for
     * @param title title of book to search for
     * @param author author to search for
     * @return ResultSet that contains all rows from database
     * @throws SQLException if connection to server fails
     */
    public ResultSet getCheckedOutBooks() throws SQLException {
        //statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call getCheckedOutBooks procedure and set inputs
        cstmt = connection.prepareCall("{call dbo.getCheckedOutBooks()}");

        //execute and capture result
        cstmt.execute();
        rs = cstmt.getResultSet();

        //return result
        return rs;
    }

    /**
     * Checks in a book
     *
     * @param isbn isbn to check in
     * @throws SQLException if connection fails
     */
    public void checkInBook(String isbn) throws SQLException {
        //statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call checkinbook procedure with inputs
        cstmt = connection.prepareCall("{call dbo.CheckinBook(?)}");
        cstmt.setString(1, isbn);

        //execute and capture results
        cstmt.execute();

    }

    /**
     * Checks in a book
     *
     * @param isbn isbn to check in
     * @return returns messages in form of warnings
     * @throws SQLException if connection fails
     */
    public void updateFines() throws SQLException {
        //statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call checkinbook procedure with inputs
        cstmt = connection.prepareCall("{call dbo.UpdateFineAmounts}");

        //execute and capture results
        cstmt.execute();
    }

    public void payFine(String loanID) throws SQLException {
        //statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call checkinbook procedure with inputs
        cstmt = connection.prepareCall("{call dbo.payFine(?)}");
        cstmt.setString(1, loanID);

        //execute and capture results
        cstmt.execute();
    }

    public void addBorrower(String ssn, String first, String last, String address, String city, String state, String email, String phone) throws SQLException {
        //statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call checkinbook procedure with inputs
        cstmt = connection.prepareCall("{call dbo.addBorrower(?,?,?,?,?,?,?,?)}");
        cstmt.setString(1, ssn);
        cstmt.setString(2, first);
        cstmt.setString(3, last);
        cstmt.setString(4, address);
        cstmt.setString(5, city);
        cstmt.setString(6, state);
        cstmt.setString(7, email);
        cstmt.setString(8, phone);

        //execute and capture results
        cstmt.execute();
    }

    /**
     * Checks out a book
     *
     * @param isbn which book to check out
     * @param borrowerNumber who to check out to
     * @return messages in the form of warnings
     * @throws SQLException if connection fails
     */
    public void checkoutBook(String isbn, String borrowerNumber) throws SQLException {
        //Statement to send, result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call checkout book stored proecedure and set inputs
        cstmt = connection.prepareCall("{call dbo.CheckoutBook(?,?)}");
        cstmt.setString(1, borrowerNumber);
        cstmt.setString(2, isbn);

        //execute and capture results
        cstmt.execute();

    }

    /**
     * Gets Borrowers
     *
     * @return resultset
     * @throws SQLException if connection fails
     */
    public ResultSet getBorrowers() throws SQLException {
        //Statement to send, result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call checkout book stored proecedure and set inputs
        cstmt = connection.prepareCall("{call dbo.getBorrowers}");

        //execute and capture results
        cstmt.execute();
        rs = cstmt.getResultSet();

        return rs;
    }

    public ResultSet getFines(String onlyPaid) throws SQLException {
        //Statement to send, result to receive
        CallableStatement cstmt;
        ResultSet rs;

        //call checkout book stored proecedure and set inputs
        cstmt = connection.prepareCall("{call dbo.getFines (?)}");
        cstmt.setString(1, onlyPaid);
        //execute and capture results
        cstmt.execute();
        rs = cstmt.getResultSet();

        return rs;
    }

    /**
     * Creates a connection to a database given hardcoded inputs
     *
     * @throws Exception if connection to server fails.
     */
    private void createConnection() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlserver://localhost\\sqlexpress:1433;databaseName=Library;integratedSecurity=true;");
//        
//        dataSource.setUser("sa");
//        dataSource.setPassword("databasepassword1!");
//        dataSource.setServerName("localhost");
//        dataSource.setDatabaseName("Library");
//
//        connection = dataSource.getConnection();
    }

}
