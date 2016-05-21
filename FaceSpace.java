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
import java.util.ArrayList;

public class FaceSpace {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one exists)
    private String query;  //this will hold the query we are using
	
    public FaceSpace() {
		int function_no=0;
		System.out.println("Choose function:\n0:runDemo\n1: createUser\n2: initiateFriendship\n3: establishFriendship\n4: displayFriends\n5: createGroup");
		System.out.println("6: addToGroup\n7: sendMessageToUser\n8: displayMessages\n9: searchForUser\n10: threeDegrees\n11: topMessagers\n12: dropUser");
		System.out.println("Please enter a function number, then hit ENTER: ");
		Scanner reader = new Scanner(System.in); 
		function_no = reader.nextInt();
		
		switch ( function_no) {
		case 0:
			runDemo();//done
			break;
		case 1:
			createUser();//done
			break;
		case 2:
			initiateFriendship();//done
			break;
		case 3: 
			establishFriendship();//done
			break;
		case 4: 
			displayFriends();//done, identify friendship status??
			break;
		case 5: 
			createGroup();	//done
			break;
		case 6: 
			addToGroup();//done
			break;
		case 7: 
			sendMessageToUser();	//done
			break;
		case 8: 
			displayMessages();	//done
			break;
		case 9: 
			searchForUser();	//done
			break;
		case 10: 
			threeDegrees();		//done
			break;
		case 11: 
			topMessagers();		//done
			break;
		case 12: 
			dropUser();		//done
			break;
		default:
			System.out.println("Function not found for your entry: " + function_no);
			try {
			connection.close();
			}
			catch(Exception Ex)  {
			System.out.println("Error connecting to database.  Machine Error: " +
					Ex.toString());
			}
			break;
		}
			
    }
	/////////////////////////////// demo ///////////////////////
	public void runDemo(){
		System.out.println("Testing function 1: createUser()..." );
		createUser();
		System.out.println("Testing function 2: initiateFriendship()..." );
		initiateFriendship();
		System.out.println("Testing function 3: establishFriendship()..." );
		establishFriendship();
		System.out.println("Testing function 4: displayFriends()..." );
		displayFriends();
		System.out.println("Testing function 5: createGroup()..." );
		createGroup();
		System.out.println("Testing function 6: addToGroup()..." );
		addToGroup();
		System.out.println("Testing function 7: sendMessageToUser()..." );
		sendMessageToUser();
		System.out.println("Testing function 8: displayMessages()..." );
		displayMessages();
		System.out.println("Testing function 9: searchForUser()..." );
		searchForUser();
		System.out.println("Testing function 10: threeDegrees()..." );
		threeDegrees();	
		System.out.println("Testing function 11: topMessagers()..." );
		topMessagers();
		System.out.println("Testing function 12: dropUser()..." );
		dropUser();
		System.out.println("----------------The END----------------" );
	}
    /////////////////Function 1 //////////////////////////
    public void createUser() {
	
		int counter = 1;
		long usernumber=0;
		/*We will now perform a simple query to the database, asking for all the
		records it has.  For your project, performing queries will be similar*/
		System.out.println("Enter name:" );
		Scanner reader = new Scanner(System.in); 
		String name = reader.nextLine();
		System.out.println("Enter email:" );
		String email = reader.nextLine();
		System.out.println("Enter date of birth (YYYY-MM-DD):" );
		String dob = reader.nextLine();
		
		try{
			statement = connection.createStatement(); //create an instance
			String selectQuery = "SELECT count(*) FROM profile"; //sample query
			
			resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
		
			/*the results in resultSet have an odd quality. The first row in result
			set is not relevant data, but rather a place holder.  This enables us to
			use a while loop to go through all the records.  We must move the pointer
			forward once using resultSet.next() or you will get errors*/
	
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
			usernumber = resultSet.getLong(1);
			counter++;
			}
			
			System.out.println("Please remember your user ID is "+usernumber);
			
			/*Now, an insert to table profile*/
			
			// a string that stores the query. Put question marks as placeholders for the 
			// values you need to enter. Each question mark will later be replaced with 
			// the value specified by the set* method
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
			//java.sql.Date date_login = new java.sql.Date (df.parse(date1).getTime());
			
			
			// You need to specify which question mark to replace with a value.
			// They are numbered 1 2 3 etc..
			prepStatement.setLong(1, usernumber);
			prepStatement.setString(2, name);
			prepStatement.setString(3, email); 
			prepStatement.setDate(4, date_dob);
			prepStatement.setDate(5, date_login);
			// Now that the statement is ready. Let's execute it. Note the use of 
			// executeUpdate for insertions and updates instead of executeQuery for 
			// selections.
			int result1 = prepStatement.executeUpdate();
			if (result1 == 1) 
				System.out.println("Insert Complete: Success" );
			else 
				System.out.println("No rows were updated");
			//I will show the insert worked by selecting the content of the table again
			//statement = connection.createStatement();
			/*String testquery = "SELECT * FROM profile";
			
			resultSet = statement.executeQuery(testquery);
			System.out.println("\nAfter the insert, data is...\n");
			counter=1;
			while(resultSet.next()) {
			System.out.println("Record " + counter + ": " +
				resultSet.getLong(1) + ", " +
				resultSet.getString(2) + ", " +
				resultSet.getString(3) + ", " +
				resultSet.getDate(4) + ", " +
				resultSet.getDate(5));
			counter ++;
			}*/
			resultSet.close();
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}catch (ParseException e) {
			System.out.println("Error parsing the date. Machine Error: " +
			e.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
 	}
	 
	/////////////////Function 2 //////////////////////////
    public void initiateFriendship() {
		System.out.println("Enter your user ID:" );
		Scanner reader = new Scanner(System.in); 
		long senderID = reader.nextLong();
		System.out.println("Enter your friend's user ID:" );
		long receiverID = reader.nextLong();
		try{
			statement = connection.createStatement(); //create an instance
			query = "insert into friends(sender_ID,receiver_ID,fstatus) values (?,?,0)";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setLong(1, senderID);
			prepStatement.setLong(2, receiverID);
			int result1 = prepStatement.executeUpdate();
			if (result1 == 1) 
				System.out.println("Insert Complete: Success" );
			else 
				System.out.println("No rows were updated");
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
 	}
	 
	/////////////////Function 3 //////////////////////////
    public void establishFriendship() {
		System.out.println("Enter your user ID:" );
		Scanner reader = new Scanner(System.in); 
		long senderID = reader.nextLong();
		System.out.println("Enter your friend's user ID:" );
		long receiverID = reader.nextLong();
		try{
			statement = connection.createStatement(); //create an instance
			query = "update friends set fstatus = 1 where sender_ID = ? and receiver_ID = ?";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setLong(1, senderID);
			prepStatement.setLong(2, receiverID);
			int result1 = prepStatement.executeUpdate();
			if (result1 == 1) 
				System.out.println("Insert Complete: Success" );
			else 
				System.out.println("No rows were updated");
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
 	}
	 
	/////////////////Function 4 //////////////////////////
	public void displayFriends(){
		System.out.println("Enter your user ID:" );
		Scanner reader = new Scanner(System.in); 
		long senderID = reader.nextLong();
		try{
			statement = connection.createStatement(); //create an instance
			String selectQuery = "SELECT username from profile where user_ID in (select receiver_ID from friends where sender_ID = "+ senderID +")"; //query
			resultSet = statement.executeQuery(selectQuery);
			System.out.println("Your friends are: ");
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				System.out.println(resultSet.getString(1));
			}
			resultSet.close();
			statement = connection.createStatement(); //create an instance
			selectQuery = "SELECT username from profile where user_ID in (select sender_ID from friends where receiver_ID = "+ senderID +")"; //query
			resultSet = statement.executeQuery(selectQuery);
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				System.out.println(resultSet.getString(1));
			}
			resultSet.close();
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
 	/////////////////Function 5 //////////////////////////
	public void createGroup(){
		long groupnumber=0;
		System.out.println("Enter group name:" );
		Scanner reader = new Scanner(System.in); 
		String gname = reader.nextLine();
		System.out.println("Enter a short description:" );
		String description = reader.nextLine();
		System.out.println("Enter maximum number of group members:" );
		long memlimit = reader.nextLong();
		try{
			statement = connection.createStatement(); //create an instance
			String selectQuery = "SELECT count(*) FROM groups"; //query
			resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
	
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				groupnumber = resultSet.getLong(1);
			}
			System.out.println("Please remember your group ID is "+groupnumber);
			
			query = "insert into groups(groupID,gname,description,memlimit) values (?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setLong(1, groupnumber);
			prepStatement.setString(2, gname);
			prepStatement.setString(3, description); 
			prepStatement.setLong(4, memlimit);
			int result1 = prepStatement.executeUpdate();
			if (result1 == 1) 
				System.out.println("Insert Complete: Success" );
			else 
				System.out.println("No rows were updated");
			resultSet.close();
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	/////////////////Function 6 //////////////////////////
	public void addToGroup(){
		System.out.println("Enter your user ID: " );
		Scanner reader = new Scanner(System.in); 
		long senderID = reader.nextLong();
		System.out.println("Enter group ID: " );
		long groupid = reader.nextLong();
		long max = 0, cur = 0;
		try{
			statement = connection.createStatement();
			//read the maximum number of members from table groups
			String selectQuery = "SELECT memlimit FROM groups where groupID = "+groupid; //query
			resultSet = statement.executeQuery(selectQuery);
			resultSet.next();
			max = resultSet.getLong(1);
			resultSet.close();
			
			statement = connection.createStatement();
			//read the current number of members from table groupMem
			selectQuery = "SELECT count(*) FROM groupMem where groupID = "+groupid; //query
			resultSet = statement.executeQuery(selectQuery);
			resultSet.next();
			cur = resultSet.getLong(1);
			System.out.println("Group "+groupid);
			System.out.println("max: " + max);
			System.out.println("cur: " + cur);

			if(cur<max) {
				query = "insert into groupMem(groupID,user_ID) values (?,?)";
				prepStatement = connection.prepareStatement(query);
				prepStatement.setLong(1, groupid);
				prepStatement.setLong(2, senderID);
				int result1 = prepStatement.executeUpdate();
				if (result1 == 1) 
					System.out.println("Insert Complete: Success" );
				else 
					System.out.println("No rows were updated");
			}
			else{ 
				System.out.println("Failed: Group "+ groupid +" is full!");
			}
			resultSet.close();
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			//System.out.println("Insert Complete: Success" );
			try {
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	/////////////////Function 7 //////////////////////////
	public void sendMessageToUser(){
		long msgnumber = 0;
		System.out.println("Enter your user ID:" );
		Scanner reader = new Scanner(System.in); 
		long senderID = reader.nextLong();
		System.out.println("Enter receiver's user ID:" );
		long receiverID = reader.nextLong();
		reader.nextLine();
		System.out.println("Enter subject:" );
		String subject = reader.nextLine();
		System.out.println("Enter message body:" );
		String bodytext = reader.nextLine();
		
		try{
			statement = connection.createStatement(); //create an instance
			String selectQuery = "SELECT count(*) FROM messages"; //query
			resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
	
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				msgnumber = resultSet.getLong(1);
			}
			Date utilDate = new Date();
			java.sql.Date date_sent = new java.sql.Date(utilDate.getTime());
			
			query = "insert into messages(msgID,sender_ID,receiver_ID,subject,bodytext,date_sent) values (?,?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setLong(1, msgnumber);
			prepStatement.setLong(2, senderID);
			prepStatement.setLong(3, receiverID);
			prepStatement.setString(4, subject);
			prepStatement.setString(5, bodytext);
			prepStatement.setDate(6, date_sent);
			int result1 = prepStatement.executeUpdate();
			if (result1 == 1) 
				System.out.println("Insert Complete: Success" );
			else 
				System.out.println("No rows were updated");
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	/////////////////Function 8 //////////////////////////	 
	public void displayMessages(){
		System.out.println("Enter your user ID:" );
		Scanner reader = new Scanner(System.in); 
		long receiverID = reader.nextLong();
		int i=1;
		try{
			statement = connection.createStatement(); //create an instance
			String selectQuery = "SELECT profile.username,messages.subject,messages.bodytext from messages inner join profile on messages.sender_ID = profile.user_ID where messages.receiver_ID = "+ receiverID; //query
			resultSet = statement.executeQuery(selectQuery);
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				System.out.println("Message "+i);
				i++;
				System.out.println("Sender : "+resultSet.getString(1)+"\n"+
				"Subject: "+resultSet.getString(2) + "\n" +
				"Body   : "+resultSet.getString(3));
			}
			resultSet.close();
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	/////////////////Function 9 //////////////////////////
	public void searchForUser(){
		System.out.println("Search: (Case sensative!)" );
		Scanner reader = new Scanner(System.in); 
		String searchInput = reader.nextLine();
		String[] searchParts = searchInput.split(" ");
		int length1 = searchParts.length;
		for(int i=0;i<length1;i++){
			try{
			statement = connection.createStatement(); //create an instance
			String selectQuery = "select profile.* from profile JOIN (select user_ID, username||email||TO_CHAR(dob, 'DD-MON-YYYY')||TO_CHAR(lastlogin, 'DD-MON-YYYY') concatenated "+ 
								"FROM profile) T ON T.user_ID = profile.user_ID where t.concatenated like '%"+searchParts[i]+"%'"; //query
			resultSet = statement.executeQuery(selectQuery);
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				System.out.println("user_ID: "+resultSet.getLong(1)+"\n"+
				"Name   : "+resultSet.getString(2) + "\n" +
				"Email  : "+resultSet.getString(3) + "\n" +
				"Date-Of-Birth  : "+resultSet.getTimestamp(4) + "\n" +
				"Last Login Time: "+resultSet.getTimestamp(5));
			}
			resultSet.close();
			}
			catch(SQLException Ex) {
				System.out.println("Error running the queries.  Machine Error: " +
						Ex.toString());
			}
			finally{
				try {
					if (statement != null) statement.close();
				} catch (SQLException e) {
					System.out.println("Cannot close Statement. Machine error: "+e.toString());
				}
			}
		}
	}
	/////////////////Function 10 //////////////////////////
	public void threeDegrees(){
		System.out.println("Enter first user ID:" );
		Scanner reader = new Scanner(System.in); 
		long user1 = reader.nextLong();
		System.out.println("Enter second user ID:" );
		long user2 = reader.nextLong();
		int length2=0,length1=0;
		//ArrayList<ArrayList<ArrayList<Long>>> arrlistlvl00 = new ArrayList<ArrayList<ArrayList<Long>>>(); //arraylist of friendship, outer level
		ArrayList<ArrayList<Long>> arrlistlvl01 = new ArrayList<ArrayList<Long>>();//middle level ArrayList
		ArrayList<Long> arrlistlvl02;// = new ArrayList<Long>(); //inner level ArrayList
		//arraylist of reverse order friendship??
		//int count0=0,count1=0;
			//while(count0<=3 && count1<=3)
		arrlistlvl02 = findFriendship(user1);
		if(arrlistlvl02.contains(user2)){
			System.out.println("Success: The two users are direct firends.");
			return;
		}
		else{
			length2=arrlistlvl02.size();					
			arrlistlvl01.add(arrlistlvl02);//add to position 0 of outer al
			//arrlistlvl02 = new ArrayList<Long>();//clear inner al
			for(int i=0;i<length2;i++){
				long temp = arrlistlvl01.get(0).get(i);//temp is user_id
				arrlistlvl02 = findFriendship(temp);
				if(arrlistlvl02.contains(user2)){
					System.out.println("Success: Friendship path is "+user1+" -- "+temp+" -- "+user2);
					return;
				}
				arrlistlvl01.add(arrlistlvl02);
			}
			//level one no match, go to level two
			for(int i=0;i<length2;i++){
				length1 = arrlistlvl01.get(i+1).size();//current al length
				for(int j=0;j<length1;j++){
					long temp = arrlistlvl01.get(i+1).get(j);//temp is user_id
					arrlistlvl02 = findFriendship(temp);
					if(arrlistlvl02.contains(user2)){
						System.out.println("Success: Friendship path is "+user1+" -- "+arrlistlvl01.get(0).get(i)+" -- "+temp+" -- "+user2);
						return;
					}
				}
			}
			System.out.println("Failed: No relationship found within 3 hops.");
			return;
		}
	}
	/////////////////Function 11 //////////////////////////
	public void topMessagers(){
		System.out.println("Enter k for top k users:" );
		Scanner reader = new Scanner(System.in); 
		int k = reader.nextInt();
		System.out.println("Enter x for the past x months:" );
		int x = reader.nextInt();
		try{
			if(x>0) x = -1*x;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, x);
			Date result = cal.getTime();
			java.sql.Date date_intended = new java.sql.Date(result.getTime());
			
			statement = connection.createStatement(); //create an instance			
			query = "select * from (select sender_ID,count(*) as MsgC from messages where date_sent > ? group by sender_ID order by MsgC desc) where rownum <= "+k+" order by rownum";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setDate(1, date_intended);
			resultSet = prepStatement.executeQuery();
			
			System.out.println("User_ID\tMsgCount");
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				System.out.println(resultSet.getLong(1) + "\t" + resultSet.getInt(2));
			}
			resultSet.close();
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	/////////////////Function 12 //////////////////////////
	public void dropUser(){
		System.out.println("Enter the user ID:" );
		Scanner reader = new Scanner(System.in); 
		long user_ID = reader.nextLong();
		try{
			statement = connection.createStatement(); //create an instance
			String selectQuery = "delete from profile where user_ID = "+ user_ID; //query
			statement.executeQuery(selectQuery);
			//delete msg that has both sender_ID and receiver_ID null
			//when such thing happens, a trigger in fs-db.sql set msgID to be -1
			statement = connection.createStatement(); //create an instance
		 	selectQuery = "delete from messages where msgID = -1"; //query
			statement.executeQuery(selectQuery);
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			System.out.println("Delete Complete: Success" );
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	////////////////////////////////////
	////// my own function /////////////
	////////////////////////////////////
	public ArrayList<Long> findFriendship(long user_ID){
		ArrayList<Long> arrlistlvl02 = new ArrayList<Long>(); //return ArrayList
		try{
			//query
			statement = connection.createStatement(); //create an instance
			String selectQuery = "select receiver_ID from friends where sender_ID = "+ user_ID; //query
			resultSet = statement.executeQuery(selectQuery);
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				arrlistlvl02.add(resultSet.getLong(1));
			}
			resultSet.close();
			//finish query
			//query
			statement = connection.createStatement(); //create an instance
			selectQuery = "select sender_ID from friends where receiver_ID = "+ user_ID; //query
			resultSet = statement.executeQuery(selectQuery);
			while (resultSet.next()) //this not only keeps track of if another record exists but moves us forward to the first record
			{
				arrlistlvl02.add(resultSet.getLong(1));
			}
			resultSet.close();
			//finish query
			return arrlistlvl02;
				
		}
		catch(SQLException Ex) {
			System.out.println("Error running the queries.  Machine Error: " +
					Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
		return null;
	}
    
  public static void main(String args[]) throws SQLException
  {
    /* Making a connection to a DB causes certain exceptions.  In order to handle
	   these, you either put the DB stuff in a try block or have your function
	   throw the Exceptions and handle them later.  For this demo I will use the
	   try blocks */

    String username, password;
	username = "tic30"; //This is your username in oracle
	password = "yourpassword"; //This is your password in oracle / propleSoft ID
	String again = "y";
	
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
		while (again.equals("y")){
			FaceSpace demo = new FaceSpace();
			System.out.println("Another function? (y/n)" );
			Scanner reader = new Scanner(System.in); 
			again = reader.nextLine();
			again = again.substring(0,1).toLowerCase();
		}
	    System.out.println("Good Bye!");
	    
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
