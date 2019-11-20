package library; 
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This object connects to a Library Database.
 * Contains all commands to stored procedures
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
     * @param isbn isbn to search for
     * @param title title of book to search for
     * @param author author to search for
     * @return ResultSet that contains all rows from database
     * @throws SQLException if connection to server fails
     */
    public ResultSet getBooks(String isbn, String title, String author) throws SQLException {
        //Statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;
        
        //call getbooks stored procedure with given input parameters
        cstmt = connection.prepareCall("{call dbo.getBooks(?, ?, ?)}");
        cstmt.setString(1, isbn);
        cstmt.setString(2, title);
        cstmt.setString(3, author);
        
        //execute and capture result
        cstmt.execute();
        rs = cstmt.getResultSet();
        
        //return result
        return rs;
    }
    
    /**
     * Gets all books that are already checked out
     * @param isbn isbn to search for
     * @param title title of book to search for
     * @param author author to search for
     * @return ResultSet that contains all rows from database
     * @throws SQLException if connection to server fails
     */
    public ResultSet getCheckedOutBooks(String isbn, String title, String author) throws SQLException {
        //statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;
        
        //call getCheckedOutBooks procedure and set inputs
        cstmt = connection.prepareCall("{call dbo.getCheckedOutBooks(?, ?, ?)}");
        cstmt.setString(1, isbn);
        cstmt.setString(2, title);
        cstmt.setString(3, author);
        
        //execute and capture result
        cstmt.execute();
        rs = cstmt.getResultSet();
        
        //return result
        return rs;
    }
    
    /**
     * Checks in a book
     * @param isbn isbn to check in 
     * @return returns messages in form of warnings
     * @throws SQLException if connection fails
     */
    public SQLWarning checkInBook(String isbn) throws SQLException {
        //statement to send, and result to receive
        CallableStatement cstmt;
        ResultSet rs;
        
        //call checkinbook procedure with inputs
        cstmt = connection.prepareCall("{call dbo.CheckinBook(?)}");
        cstmt.setString(1, isbn);
        
        //execute and capture results
        cstmt.execute();
        rs = cstmt.getResultSet();
        //try to capture messages
        SQLWarning warnings = cstmt.getWarnings();
        if(warnings != null) {
            return warnings;
        }
        
        //iterate through all results and try to find message
        while(!cstmt.getMoreResults() && cstmt.getUpdateCount() == -1) {
            warnings = cstmt.getWarnings();
            if(warnings != null) {
                return warnings;
            }
        }
        
        return null;
    }
    
    
    /**
     * Checks out a book
     * @param isbn which book to check out
     * @param borrowerNumber who to check out to
     * @return messages in the form of warnings
     * @throws SQLException if connection fails
     */
    public SQLWarning checkoutBook(String isbn, String borrowerNumber) throws SQLException {
        //Statement to send, result to receive
        CallableStatement cstmt;
        ResultSet rs;
        
        //call checkout book stored proecedure and set inputs
        cstmt = connection.prepareCall("{call dbo.CheckoutBook(?, ?)}");
        cstmt.setString(1, borrowerNumber);
        cstmt.setString(2, isbn);
        
        //execute and capture results
        cstmt.execute();
        rs = cstmt.getResultSet();
        //try to capture messages
        SQLWarning warnings = cstmt.getWarnings();
        if(warnings != null) {
            return warnings;
        }
        
        //iterate through all results and try to find message
        while(!cstmt.getMoreResults() && cstmt.getUpdateCount() == -1) {
            warnings = cstmt.getWarnings();
            if(warnings != null) {
                return warnings;
            }
        }
        
        return null;
    }
    
    /**
     * Creates a connection to a database given hardcoded inputs
     * @throws Exception if connection to server fails.
     */
    private void createConnection() throws Exception {
        dataSource.setUser("sa");
        dataSource.setPassword("databasepassword1!");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("Library");

        connection = dataSource.getConnection();
    }
    
}
