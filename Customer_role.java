/*
*
*Author: Pravalika Gunti
*
*/
/*
---Program for Customer purchasing products
---In this program if customer is new customer to factory, customer should enter details
---If old customer no need to enter details
--directly customer can view all  products and available products
*/
/*
---I have written validations to program
---checking input is given or not
---for number values if input is wrong then prompt is generated 
---validation for mobile number and characters
*/
import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Customer_role {
    static BufferedReader keyboard;//used for keyboard i/o
    static Connection conn;  //used for database connection
    static Statement stmt;  //requests are sent via statements 

/*method for purchasing products by customer*/

public static void Customer()throws SQLException ,IOException{
                try{
                String contact;
                System.out.println("Enter Contact Number");
                contact=keyboard.readLine();  //reads contact number from keyboard
                while(isEmpty(contact)){
                contact=keyboard.readLine();
                }
                while(!isMobile(contact)){  //if given input is not in digit and not 10 digits
                    System.out.println("You mobile number should be 10 digit number");
                    System.out.println("Enter Contact Number");//generate message to enter contact number
                    contact=keyboard.readLine();
                }//loop continues until contact number is 10 digits
                   /*Query to check contact number exist*/
                	String output = "SELECT *"+
                                        " FROM customer_details"
                                       +" where contact_num = "+contact;  
                    //query to know contact number exist in database
                        ResultSet qryext=stmt.executeQuery(output);      	
                        /*if contact number does not exist in database asking customer to enter details*/
                        if(!qryext.next()){  
                        System.out.println("New Customer");
                        System.out.println("First Name: ");
                        String Fname=keyboard.readLine();  //Fname:customer first name
                        /*enters when input is not given*/
                        while(isEmpty(Fname)){
                        Fname=keyboard.readLine();
                        }
                        System.out.println("Last Name");
                        String Lname=keyboard.readLine();  //Lname:customer last name
                        while(isEmpty(Lname)){
                        Lname=keyboard.readLine();
                        }
                        System.out.println("Address");
                        String address=keyboard.readLine();  //address:Address of customer
                        while(isEmpty(address)){
                        address=keyboard.readLine();
                        }
                        Statement InsStmt=conn.createStatement();
                        /*Inserting details of customer in Customer_Details table*/
                        String Inscmd="insert into Customer_Details values("
                                                           +contact+",'"
                        		                   +Lname+"','"
                                                           +Fname+"','"
                                                           +address+"')";
                         InsStmt.executeUpdate(Inscmd); 
                         System .out.println();
                         System.out.println("Thank You for your patience");
                         conn.commit();
                        }    //close if
                	/*Retrieving Available products from Customer_Purchase_Details and Daily_Production_Details*/
                        String product1="select * from product_details";
                        ResultSet text=stmt.executeQuery(product1);
                        System.out.println("Product Details...... ");
                        System.out.println("---------------------");
                        System.out.println("Product ID     Product Name                 Product Size       Product Class          Price");
                        System.out.println("--------------------------------------------------------------------------------------------");
                        while(text.next()){
                        System.out.println(text.getString("product_id")+""+"\t\t"+text.getString("product_name")+""+"\t\t"+text.getString("product_size")+"\t\t"+text.getString("product_class")+"\t\t\t"+text.getString("price"));
                        }
                        /*Query for getting available products*/
                	String available="select f.product_id, p.sum_p-nvl(f.sum_products_purchased,0) as Available_Products"+
                                         " FROM (select product_id,  Sum(number_of_products) as sum_p"+
                                                     " from daily_production_details group by product_id)"+
                                        " p right outer join (select customer_purchase_details.product_id, Sum(products_purchased) as sum_products_purchased"+
                                                     " from customer_purchase_details"+
                                                        " group by(customer_purchase_details.product_id)) f on p.product_id=f.product_id";
                        ResultSet qrytext=stmt.executeQuery(available);
                	if(!qrytext.next()){
                		System.out.println("Sorry for inconvience no products are available");
                		System.out.println("Thank You For Visiting");
                	} //if
                	/*Products are available*/
                	else{	
                 	ResultSetMetaData rsmd=qrytext.getMetaData();
                	int colcount=rsmd.getColumnCount();   //retrieving available product count
                	String[] array1=new String[100];       
                	int[] array2=new int[100];  
                        int b=0;
                        array1[b]=qrytext.getString("product_id");    //reading product id in array1
                                        System.out.println("\n\n");
                                        array2[b]=qrytext.getInt("Available_Products");  //reading available products in array2
                                        System.out.println("product Id : "+array1[b]+" and available products: "+array2[b]);   //it prints available products
                                        System.out.println("*----------------------------------------------------*");
                	while(qrytext.next()){
                	b=b+1;	
                			array1[b]=qrytext.getString("product_id");    //reading product id in array1
                			array2[b]=qrytext.getInt("Available_Products");  //reading available products in array2
                			System.out.println("product Id : "+array1[b]+" and available products: "+array2[b]);   //it prints available products
                			System.out.println("*----------------------------------------------------*");
                	
                	}//while
                        System.out.println("\n\n");
                	System.out.println("Enter product Id for purchasing");
                	String in=keyboard.readLine();  //product_Id in product
                        while(isEmpty(in)){
                        in=keyboard.readLine();
                        }
                        while(!isDigit(in)){  //if given input is not in digit
                            System.out.println("you should select number");
                            System.out.println("enter correct product id"); //generate message to re-enter
                                in=keyboard.readLine(); //reads number of products from keyboard
                            }
                	System.out.println("enter number of products you wish to buy");
                	String in_num=keyboard.readLine();
                        while(isEmpty(in_num)){
                        in_num=keyboard.readLine();
                        }
                    	while(!isDigit(in_num)){  //if given input is not in digit
                            System.out.println("you should select number");
                            System.out.println("enter number of products you wish to buy"); //generate message to re-enter
                                in_num=keyboard.readLine(); //reads number of products from keyboard
                            } //close while//number of products in input1
                        int a2=Integer.parseInt(in_num);
                    //	for(int i=0;i<=b;i++){
                    	//	if(array1[i].equals(in)){  //checking user entered product id is present in available product id list
                    	//		if(a2<=array2[i]){                       //checking user entered number of products present in available product list
                                    DateFormat df = new SimpleDateFormat("dd-MMM-yy"); 
                                    Calendar calobj = Calendar.getInstance();
                                    String st=df.format(calobj.getTime()).toUpperCase().trim();  //retrieves today's date
                                    Statement InsStmt=conn.createStatement();
                                    //inserting customer purchased details in Customer_Purchase_details table 
                            	String Instruction="insert into Customer_Purchase_details values("+contact+","+in+",'"+st+"',"+in_num+")"; 
                           	InsStmt.executeUpdate(Instruction);
                            	conn.commit();
                            	/*Query used in generating bill*/
                            	String bill="select purchased_date, customer_purchase_details.product_id, products_purchased,price, products_purchased * price AS TotalPrice"+
                                                 " from customer_purchase_details, product_details"+
                                                        " where customer_purchase_details.product_id = product_details.product_id and"+
                                                             " contact_num="+contact+" and purchased_date='"+st+"'"+
                                                                  " and customer_purchase_details.product_id="+in;        	
                               ResultSet qrytext1=stmt.executeQuery(bill);
                            	/*generating bill*/
                               System.out.println("------------------------------------------------------------");
                               while(qrytext1.next()){
                            	System.out.println("----------------------*Bill Generated----------------------*");
                                System.out.println("Date          "+"\tProduct Id"+"\t"+"Number Of Product"+"\t"+"Price    "+"\t"+"Total Amount");
                            		System.out.println();
                            		System.out.println(qrytext1.getDate("purchased_date")+"\t\t"+qrytext1.getString("product_id")+"\t\t"+qrytext1.getString("products_purchased")+"\t\t"+qrytext1.getString("price")+"\t\t"+qrytext1.getString("TotalPrice"));
                                        }
                            	System.out.println("Thank You");
                    		//	}// if
                                //        else{
                               //  System.out.println("You have entered un identified product number");
                               //         }
                    		//	}//if
                    	//	}
                    		}//else
                }//try
                catch(Exception e){
                	System.out.println("You have entered un identified product details");
                        System.out.println();
                }
                }//close method
/*method to check input is given or not*/
public static boolean isEmpty(String str){
	        return str.isEmpty();
	    }
/*code to check giving input is digit*/
public static boolean isDigit(String str){  //method to valiadate string is digit
	char ch[]=str.toCharArray();
	for(char c:ch)
		if(!Character.isDigit(c))
			return false;
           return true;
        }//close method	
/*method to check mobile number is 10 digits*/
public static boolean isMobile(String str){
	char ch[]=str.toCharArray();
	for(char c:ch)
		if(str.length()!=10||!Character.isDigit(c))
		return false;
	return true;
}
/*Main Method*/
public static void main (String args [])throws IOException{
    String username="******", password = "******";
    String ename;
    int original_empno=0;
    int empno;
    keyboard = new BufferedReader(new InputStreamReader (System.in));
    try { //errors will throw a "SQLException"(caught below)
    	DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); //load the oracle JDBC driver
    	conn = DriverManager.getConnection ("jdbc:oracle:thin:@oracle1",username, password);  /*add your database connectivity*/
        conn.setAutoCommit(false);
        System.out.println("\n\n");
        System.out.println("Welcome To RAMAMURTHY CEMENT SPUN PIPES & BRICKS FACTORY");
        System.out.println("*.......................................................*");
        stmt = conn.createStatement ();
        Customer();
    }
    catch(SQLException e)
    {
        System.out.println("Caught SQL Exception: \n     " + e);
    }//close catch
}
}
