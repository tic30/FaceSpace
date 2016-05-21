/*
  Term Project Java file 
  For class cs1555
  Created by 
  Tianxin Chu, tic30
  Yincheng He, yih39
  ***Please 1. Run fs-db.sql to create database.
  			2. Run createdata.java to insert data.
  			3. Run FaceSpace.java to test individual function.
*/

import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
import oracle.jdbc.*;//needed by java for database connection and manipulation
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;

public class createdata {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one exists)
    private String query;  //this will hold the query we are using
	
    public createdata() {
		Random random = new Random();
		
		//100 users
		for(int j=0;j<100;j++){
			String name = "";
			String email = "";
			String dob = "";
			//random length <=9
			int length1 = random.nextInt(10) + 1;
			for (int i=0;i<=length1;i++){
				//generate random character
				int n = random.nextInt(27) + 97;
		   	 	name += (char) n;
				email += (char) n;
			}
			email += "@pitt.edu";
			//generate random dob
			int yy1 = random.nextInt(30) + 1980;
			int mm1 = random.nextInt(3) + 10;
			int dd1 = random.nextInt(19) + 10;
			dob = yy1 + "-" + mm1 + "-" + dd1;
	    
			createUser(name,email,dob);
		}
		System.out.println("Insert Complete: 100 users successfully added." );
		
		//200 friends
		ArrayList<Integer> pairinglist = new ArrayList<Integer>();
		for(int j=0;j<200;j++){
			int senderID = random.nextInt(100);
			// receiver must not be sender
			int receiverID = senderID;
			// Cantor pairing function to ensure unique PK
			int pairing = (receiverID + senderID)*(receiverID +senderID + 1)/2 + senderID;
			if (!pairinglist.contains(pairing)){
				pairinglist.add(pairing);
			}
			while (pairinglist.contains(pairing)||receiverID == senderID){
				receiverID = random.nextInt(100);			
				pairing =(receiverID + senderID)*(receiverID +senderID + 1)/2 + senderID;
			}
			pairinglist.add(pairing);
			
			int established = random.nextInt(2);
	    	initiateFriendship(senderID, receiverID, established);
		}
		System.out.println("Insert Complete: 200 friends successfully added." );

		
		//10 groups
		for(int j=0;j<10;j++){
			String gname = "";
			String description = "";
			long memlimit = 0;
			//random length group name
			int length1 = random.nextInt(10) + 5;
			for (int i=0;i<=length1;i++){
				//generate random character
				int n = random.nextInt(27) + 97;
		   	 	gname += (char) n;
			}
			//random length description <100
			int length2 = random.nextInt(99)+1;
			for (int i=0;i<=length2;i++){
				//generate random character
				int n = random.nextInt(27) + 97;
				description += (char) n;
			}
			//generate random memlimit
			memlimit = random.nextInt(99) + 1;
	    
			createGroup(gname, description, memlimit);
		}
		System.out.println("Insert Complete: 10 groups successfully added." );
		
		//300 messages
		for(int j=0;j<300;j++){
			String name = "";
			String subject = "";
			String bodytext = "";
			int senderID = random.nextInt(100);
			// receiver must not be sender
			int receiverID = senderID;
			while (receiverID == senderID)
			{
				receiverID = random.nextInt(100);			
			}
				
			//random length subject
			int length1 = random.nextInt(10) + 5;
			for (int i=0;i<=length1;i++){
				//generate random character
				int n = random.nextInt(27) + 97;
		   	 	subject += (char) n;
			}
			//random length body <100
			int length2 = random.nextInt(99)+1;
			for (int i=0;i<=length2;i++){
				//generate random character
				int n = random.nextInt(27) + 97;
				bodytext += (char) n;
			}
	    	int x = random.nextInt(20)+1;
			if(x>0) x = -1*x;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, x);
			Date utilDate = cal.getTime();
			java.sql.Date date_sent = new java.sql.Date(utilDate.getTime());
			sendMessageToUser(senderID, receiverID, subject, bodytext, date_sent);
		}
		System.out.println("Insert Complete: 300 messages successfully added." );
		
	    try {
		connection.close();
	    }
	    catch(Exception Ex)  {
		System.out.println("Error connecting to database.  Machine Error: " +
				   Ex.toString());
	    }
	//}
			
    }

    /////////////////Function 0 //////////////////////////
    public void createUser(String name, String email, String dob) {
	
	int counter = 1;
	long usernumber=0;
	
	try{
	    statement = connection.createStatement(); //create an instance
	    String selectQuery = "SELECT count(*) FROM profile"; //sample query
	    
	    resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

	    while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
	    {
		   usernumber = resultSet.getLong(1);
		   counter++;
	    }
		/*Now, an insert to table profile*/
	    query = "insert into profile(user_ID,username,email,dob,lastlogin) values (?,?,?,?,?)";
	    prepStatement = connection.prepareStatement(query);
	    long classid = 1;
	    // This is how you can specify the format for the dates you will use
	    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
	    // This is how you format a date so that you can use the setDate format below
		Date utilDate = new Date();
	    java.sql.Date date_dob = new java.sql.Date (df.parse(dob).getTime());
	   //get current date time with Date()
	   	java.sql.Date date_login = new java.sql.Date(utilDate.getTime());
	    // replace ? with a value.
		prepStatement.setLong(1, usernumber);
	    prepStatement.setString(2, name);
		prepStatement.setString(3, email); 
	    prepStatement.setDate(4, date_dob);
	    prepStatement.setDate(5, date_login);
	    // execute
	    prepStatement.executeUpdate();
	    
	}
	catch(SQLException Ex) {
	    System.out.println("Error running the queries.  Machine Error: " +
			       Ex.toString());
	}catch (ParseException e) {
		System.out.println("Error parsing the date. Machine Error: " +
		e.toString());
	}
	finally{
		//System.out.println("Insert Complete: Success" );
		try {
			if (statement != null) statement.close();
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
 	}
	 
	/////////////////Function 1 //////////////////////////
    public void initiateFriendship(long senderID, long receiverID, int established) {
	try{
	    statement = connection.createStatement(); //create an instance
		
		/*Now, an insert to table friends*/
	    query = "insert into friends(sender_ID,receiver_ID,fstatus) values (?,?,?)";
	    prepStatement = connection.prepareStatement(query);
	    // replace ? with a value.
		prepStatement.setLong(1, senderID);
	    prepStatement.setLong(2, receiverID);
		prepStatement.setInt(3, established);
	    // execute
	    prepStatement.executeUpdate();
	}
	catch(SQLException Ex) {
	    System.out.println("Error running the queries.  Machine Error: " +
			       Ex.toString());
	}
	finally{
		//System.out.println("Insert Complete: Success" );
		try {
			if (statement != null) statement.close();
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
 	}
	 
	 /////////////////Function 5 //////////////////////////
	public void createGroup(String gname, String description, long memlimit){
	long groupnumber=0;
	try{
		statement = connection.createStatement(); //create an instance
	    String selectQuery = "SELECT count(*) FROM groups"; //query
	    resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

	    while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
	    {
		   groupnumber = resultSet.getLong(1);
	    }
		
	    query = "insert into groups(groupID,gname,description,memlimit) values (?,?,?,?)";
	    prepStatement = connection.prepareStatement(query);
		prepStatement.setLong(1, groupnumber);
	    prepStatement.setString(2, gname);
		prepStatement.setString(3, description); 
	    prepStatement.setLong(4, memlimit);
	    prepStatement.executeUpdate();
		resultSet.close();
	}
	catch(SQLException Ex) {
	    System.out.println("Error running the queries.  Machine Error: " +
			       Ex.toString());
	}
	finally{
		//System.out.println("Insert Complete: Success" );
		try {
			if (statement != null) statement.close();
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	}
    /////////////////Function 7 //////////////////////////
	public void sendMessageToUser(long senderID, long receiverID, String subject, String bodytext, java.sql.Date date_sent){
	long msgnumber = 0;
	try{
	    statement = connection.createStatement(); //create an instance
		String selectQuery = "SELECT count(*) FROM messages"; //query
	    resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

	    while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
	    {
		   msgnumber = resultSet.getLong(1);
	    }
		
	    query = "insert into messages(msgID,sender_ID,receiver_ID,subject,bodytext,date_sent) values (?,?,?,?,?,?)";
	    prepStatement = connection.prepareStatement(query);
		prepStatement.setLong(1, msgnumber);
		prepStatement.setLong(2, senderID);
	    prepStatement.setLong(3, receiverID);
		prepStatement.setString(4, subject);
		prepStatement.setString(5, bodytext);
		prepStatement.setDate(6, date_sent);
	    prepStatement.executeUpdate();
	}
	catch(SQLException Ex) {
	    System.out.println("Error running the queries.  Machine Error: " +
			       Ex.toString());
	}
	finally{
		//System.out.println("Insert Complete: Success" );
		try {
			if (statement != null) statement.close();
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	}
  public static void main(String args[]) throws SQLException
  {
    String username, password;
	username = "tic30";
	password = "3853184";
	
	try{
		System.out.print("Registering DB..");
	    // Register the oracle driver.  
	    DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
	    System.out.println("Success");

	    System.out.print("Set url..");
	    //This is the location of the database.  This is the database in oracle
	    //provided to the class
	    String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 
	    System.out.println("Success");
			    
	    System.out.print("Connect to DB..");
	    //create a connection to DB on class3.cs.pitt.edu
	    connection = DriverManager.getConnection(url, username, password); 
	    System.out.println("Success");
	    createdata demo = new createdata();
	    
	}
	catch(Exception Ex)  {
	    System.out.println("Error connecting to database.  Machine Error: " +
			       Ex.toString());
	}
	finally
	{
		/*
		 * NOTE: the connection should be created once and used through out the whole project;
		 * Is very expensive to open a connection therefore you should not close it after every operation on database
		 */
		connection.close();
	}
  }
}
