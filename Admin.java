/*
*Author:Pravalika Gunti
*/
/*This program runs when admin login into system*/
/*In this admin can check number of products that are delivered  to customer on given dates*/
/*managing director can view future orders */
/*managing director can delete employee working details*/
import java.io.*;
import java.sql.*;
import java.text.*;

public class Admin {	
    static BufferedReader keyboard;//used for keyboard i/o
    static Connection conn;  //used for database connection
    static Statement stmt;  //requests are sent via statements 
	
/*main method*/
	public static void main (String args [])throws IOException{
		String username="****", password = "******";
	    String ename;
	    int original_empno=0;
	    int empno;
	    keyboard = new BufferedReader(new InputStreamReader (System.in));
	    try { //errors will throw a "SQLException"(caught below)
	    	DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); //load the oracle JDBC driver
	    	conn = DriverManager.getConnection ("jdbc:oracle:thin:@oracle1",username, password);  /*add your database connectivity*/
	        conn.setAutoCommit(false);
	        stmt = conn.createStatement ();
	        System.out.println("Welcome To The Managing Director");
	        System.out.println("*-------------------------------*");
	        String input;
			char ch;
			keyboard= new BufferedReader(new InputStreamReader(System.in));
			try{
                        /*It continues until admin enter 0*/
			do{
			System.out.println("Enter 1 to view the number of products that are deliverd to customer from given dates");
			System.out.println("2 to view products ordered by customer");
                        System.out.println("3 to delete employee working details");
			System.out.println("4 to exit");
			input=keyboard.readLine();
				ch=input.charAt(0);
				switch(ch){
				case '1': Customer_Products_view();
				break;
				case '2': Customer_Orders_view();
				break;
                case '3': DeletingRecords();
                break;
				case '4': System.exit(0);
				break;
				default: System.out.println("Enter correct input");
				}		
			}while(ch!='4');
		        }
			catch(Exception e){
				System.out.println("caught SQL EXCEPTION:\n "+e);
			}
	                }
                         catch(Exception e){
                         System.out.println(""+e);
                         }
                  }
	    
                 /*program to check whether is given or not*/
                 public static boolean isEmpty(String str){
                return str.isEmpty();
                }
         
          /*program to view the number of products sold to customer*/
	
		public static void Customer_Products_view(){
		try{
			System.out.println("Enter Starting Date Format as '01-APR-14'");
			String input1=keyboard.readLine();
                        while(isEmpty(input1)){                    //validation code if input is not given
                        input1=keyboard.readLine();
                        }
			input1=input1.toUpperCase();
			System.out.println("Enter Ending Date Format as '01-APR-14'");
			String input2=keyboard.readLine();
                        while(isEmpty(input2)){                    //validation code if input is not given
                        input2=keyboard.readLine();
                        }
			input2=input2.toUpperCase();
                        /*query to number products sold on these dates*/
			String output1="select product_id,sum(products_purchased) as TotalProducts"+ 
                                      " from customer_purchase_details"+
                                      "  where purchased_date between '"+input1+"' and '"+input2+"' group by product_id";
                ResultSet qrytxt=stmt.executeQuery(output1);
	        if(!qrytxt.next()){
	        	System.out.println("No products sold at given dates");
	        } 
	        else{ //it displays sum of products sold on given dates 
	        	System.out.println("product_id"+"\t"+"TotalProducts");
	        	System.out.println("-----------------------------------------");
	        	while(qrytxt.next()){
	        		System.out.println(qrytxt.getString("product_id")+""+"\t\t\t"+qrytxt.getString("TotalProducts"));
	        	}
	        	System.out.println();
	        }			
		}
		catch(Exception e){
		System.out.println("Given wrong input");	
		}
		}
                /*This is method is used to view the products to deliver*/
		public static void Customer_Orders_view(){
			try{
				System.out.println("Future Orders From Customer");
                /*query selects the products to deliver*/
				String output2="select * from customer_purchase_details"
                                               +" where purchased_date>=sysdate";
				ResultSet qrytxt=stmt.executeQuery(output2);
		        if(!qrytxt.next()){
		        	System.out.println("No products sold at these dates");
		        }
		        else{//it displays the retrieved values 
		        	System.out.println("Contact Number"+"\t\t\t"+"Delivery Date"+"\t\t"+"Product Id"+"\t"+"Number of products");
		        	System.out.println("------------------------------------------------------------------------------------------------------------------");
		        	while(qrytxt.next()){
		        		System.out.println(qrytxt.getString("Contact_Num")+"\t\t\t"+qrytxt.getDate("purchased_Date")+"\t\t"+qrytxt.getInt("Product_Id")+"\t\t\t"+qrytxt.getInt("products_purchased"));
		        	}
		        	System.out.println();
		        }	
			}
			catch(Exception e){
				System.out.println("Exception:"+e);
			}
		}
               /*this method used to delete records of employ manaufactured the product*/
               public static void DeletingRecords(){
               try{
               System.out.println("Do you want to delete details of employ involved in manufacturing of products until previous month by considering todays date .....type(yes/no)");
               String input=keyboard.readLine();
               while(isEmpty(input)){                    //validation code if input is not given
                        input=keyboard.readLine();
                }
               input=input.toLowerCase();
               if(input.equals("yes"))
               {
               Statement InsStmt=conn.createStatement();
               /*it deletes data from database.........if todays date is 04-APR-2017. This command delete data until 04-MAR-2017 */
               /*this is used for data obsolescence*/
               String select="DELETE FROM EMPLOYEE_PRODUCT"+
                              " WHERE MANUFACTURE_DATE < ADD_MONTHS(SYSDATE,-1)";
               InsStmt.executeUpdate(select);
               conn.commit();
               System.out.println("Deleted Successfully");
               }
               }
               catch(Exception e)
               {
               System.out.println(e);
               }
               }
	       }

