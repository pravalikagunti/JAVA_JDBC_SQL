/*
*Author:Pravalika Gunti
*/	
/*
*Program of Stock Manager Role
*In this program stock manager gets message if any of the stock quantity is low
*
*/
import java.text.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import java.sql.Date;

public class Stock {	       
            static BufferedReader keyboard;//used for keyboard i/o
	    static Connection conn;  //used for database connection
	    static Statement stmt;  //requests are sent via statements 

        	public static void Stock1(){
	        try{
	        String output="select p.stock_name, p.sum_p-nvl(f.quantity_used,0) as Available_Quantity"+
                              " FROM (select stock_name,  Sum(purchased_quantity) as sum_p from stock_purchased_vendor group by stock_name) p"+
                                  " left outer join (select stock_for_product.stock_name, Sum(quantity_used) as quantity_used"+
                                      " from stock_for_product group by(stock_for_product.stock_name)) f"+
                                         " on p.stock_name=f.stock_name order by p.stock_name asc";
	        ResultSet qrytext=stmt.executeQuery(output);
	        /*If stock not available*/
	        if(!qrytext.next()){
	                System.out.println("No stock available");
	        } //if
	        /*if Stock is available*/
	        else{
	        String[] array1=new String[100];
	        int[] array2=new int[100];
	        int a=0;
                /*reading available stock in array*/
	        while(qrytext.next()){
	                        array1[a]=qrytext.getString("stock_name");    //reading product id in array1
	                        array2[a]=qrytext.getInt("Available_Quantity");
	                        a=a+1;
	        }
                //it stock type prints if available stock is less than 5
	        for(int i=0;i<a;i++){
	                if(array2[i]<=5){
	                        System.out.println(array1[i]+" stock is low , available quantity is "+array2[i]);
	                }
	        }
                /*stock manager can select update/delete*/
	        System.out.println("Do you want to update price and quantity details of instock..... type yes/no");
	        String yes1=keyboard.readLine();
	        yes1=yes1.toLowerCase(); //if input is yes stock manager can update the details
	        if(yes1.equals("yes")){
	                Update_Instock();
	        }
	        System.out.println("Do you want to delete exist instock details..... type yes/no");
	        String yes2=keyboard.readLine(); // if input is yes stock manager can delete stock details
	        yes2=yes2.toLowerCase();
	           if(yes2.equals("yes")){
	                   Delete_Instock();
	                   }
	        System.out.println("Logging out Stock Manager");
	        }//else
	        }//try
	        catch(Exception e) 
	        {
	                System.out.println(e);
	        }
	}
       
        /*program to update in stock*/
	public static void Update_Instock(){
        try{
        /*query to retrieve data from stock_purchased_vendor*/
        String stock1="select * from stock_purchased_vendor";
        ResultSet stock2=stmt.executeQuery(stock1);
        String stk_arr[]= new String[1000];
        String st_ven[]=new String[1000];
        Date st_date[]=new Date[1000];
        System.out.println("\n\nAlready Exist In-Stock Details\n\n");
        System.out.println("Stock Name"+"\t\t"+"Vendor Id"+"\t\t"+"Instock Date"+"\t\t"+"Purchased Quantity"+"\t\t"+"purchased Price");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        int a1=0;
        /*prints instock details*/
        while(stock2.next()){
        stk_arr[a1] = stock2.getString("stock_name");
        st_ven[a1] = stock2.getString("vendor_id");
        st_date[a1] = stock2.getDate("instock_date");
        System.out.println(stock2.getString("stock_name")+""+"\t\t"+stock2.getString("vendor_id")+""+"\t\t\t"+stock2.getDate("Instock_date")+""+"\t\t"+stock2.getString("Purchased_Quantity")+""+"\t\t\t\t"+stock2.getString("purchased_Price")+""+"\t\t\t\t");
        a1=a1+1;
        }
        /*taking input from user*/
        System.out.println("Enter stock name");
        String stock=keyboard.readLine();
        while(isEmpty(stock)){ //if input is not given it cotinues
        stock=keyboard.readLine();
        }
        while(!isLetter(stock)){  // checking input is string
        System.out.println("stock name has characters");
        System.out.println("Enter stock name");
        stock=keyboard.readLine();
        }
        stock=stock.toUpperCase();
        System.out.println("Enter vendor id");
        String id=keyboard.readLine();
        while(isEmpty(id)){
        id=keyboard.readLine();
        }
         while(!isDigit(id)){  //if given input is not in digit
                    System.out.println("you should select number");
                    System.out.println("enter vendor id"); //generate message to re-enter
                        id=keyboard.readLine(); //reads number of products from keyboard
                    }
        System.out.println("Enter Date"); 
        String date=keyboard.readLine();
        while(isEmpty(date)){   
        date=keyboard.readLine();
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");  //converting date to date in database format
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
        java.util.Date date1 = format1.parse(date);
        String date3 = format2.format(date1).toUpperCase();
        System.out.println("Enter quantity to update");  //taking input to update stock details
        String quantity=keyboard.readLine();
        while(isEmpty(quantity)){
        quantity=keyboard.readLine();
        }
        while(!isDigit(quantity)){  //if given input is not in digit
            System.out.println("quantity should be number");
            System.out.println("enter quantity to update"); //generate message to re-enter
                quantity=keyboard.readLine(); //reads number of products from keyboard
            }
        System.out.println("Enter price to update price"); 
        String price=keyboard.readLine();//taking input to update price
        while(isEmpty(price)){
        price=keyboard.readLine();
        }
        while(!isDigit(price)){  //if given input is not in digit
            System.out.println("price should be number");
            System.out.println("enter price to update"); //generate message to re-enter
                price=keyboard.readLine(); //reads number of products from keyboard
            }
        
        Statement InsStmt=conn.createStatement();
        String input1="update stock_purchased_vendor"+
                      " set purchased_Quantity="+quantity
                     +", purchased_price="+price
                     +" where stock_name='"+stock
                     +"' and instock_date='"+date3
                     +"' and vendor_id='"+id+"'";
        InsStmt.executeUpdate(input1);
        conn.commit();
        System.out.println("In stock details updated successfully");
        System.out.println("-------------------------------------");
        }
        catch(Exception e){
                System.out.println("you have entered wrong details");
        }
        }
        
        /*program to delete in-stock */
	public static void Delete_Instock(){
        try{
        /*query to retrieve data from stock_purchased_vendor*/
        String stock1="select * from stock_purchased_vendor";
        ResultSet stock2=stmt.executeQuery(stock1);
        String stk_arr[]= new String[1000];
        String st_ven[]=new String[1000];
        Date st_date[]=new Date[1000];
        System.out.println("Already Exist In-Stock Details");
        System.out.println("Stock Name"+"\t\t"+"Vendor Id"+"\t\t"+"Instock Date"+"\t\t"+"Purchased Quantity"+"\t\t"+"purchased Price");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        int a1=0;
        /*prints instock details*/
        while(stock2.next()){
        stk_arr[a1] = stock2.getString("stock_name");
        st_ven[a1] = stock2.getString("vendor_id");
        st_date[a1] = stock2.getDate("instock_date");
        System.out.println(stock2.getString("stock_name")+""+"\t\t"+stock2.getString("vendor_id")+""+"\t\t\t"+stock2.getDate("Instock_date")+""+"\t\t"+stock2.getString("Purchased_Quantity")+""+"\t\t\t\t"+stock2.getString("purchased_Price")+""+"\t\t\t\t");
        a1=a1+1;
        }
        /*taking input from user*/
        System.out.println("Enter stock name");
        String stock=keyboard.readLine();
        while(isEmpty(stock)){//if input is not given it cotinues
        stock=keyboard.readLine();
        }
        while(!isLetter(stock)){ //if input is not character it cotinues
        System.out.println("stock name has characters");
        System.out.println("Enter stock name");
        stock=keyboard.readLine();
        }
        stock=stock.toUpperCase();
        System.out.println("Enter vendor id");
        String id=keyboard.readLine();
        while(isEmpty(id)){
        id=keyboard.readLine();
        }
        while(!isDigit(id)){  //if given input is not in digit
                    System.out.println("you should select number");
                    System.out.println("enter vendor id"); //generate message to re-enter
                        id=keyboard.readLine(); //reads number of products from keyboard
        }
        System.out.println("Enter Date");
        String date=keyboard.readLine();
        while(isEmpty(date)){
        date=keyboard.readLine();
        }
        /*formatting date as date in database */
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
        java.util.Date date1 = format1.parse(date);
        String date3 = format2.format(date1).toUpperCase();
        Statement InsStmt=conn.createStatement();
        String input1="delete from stock_purchased_vendor"+
                         " where stock_name='"+stock+"'"
                             +" and instock_date='"+date3+"'"
                                +" and vendor_id='"+id+"'";
        InsStmt.executeUpdate(input1);
        conn.commit();
        System.out.println("In stock details deleted successfully");
        System.out.println("*------------------------------------*");
        }
        	
        catch(Exception e){
                System.out.println("you have entered wrong details");
        }
        }
      /*program to check whether is given or not*/
      public static boolean isEmpty(String str){
	        return str.isEmpty();
	    }
      /*program to check given string consist of letter*/	
      public static boolean isLetter(String str){
        char ch[]=str.toCharArray();
          for(char c:ch)
            if(!Character.isLetter(c))
            return false;
    return true;
      }
/*program to check digits or not*/
public static boolean isDigit(String str){
    char ch[]=str.toCharArray();
    for(char c:ch)
        if(!Character.isDigit(c))
            return false;
    return true;
//check letter and digit
}
//main method
public static void main (String args [])throws IOException{
String username="********", password = "********";
String ename;
int original_empno=0;
int empno;
keyboard = new BufferedReader(new InputStreamReader (System.in));
try { //errors will throw a "SQLException"(caught below)
    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); //load the oracle JDBC driver
    conn = DriverManager.getConnection ("jdbc:oracle:thin:@oracle1",username, password);  /*add your database connectivity*/
    conn.setAutoCommit(false);
    System.out.println("\n\nWelcome Stock Manager\n\n");
    stmt = conn.createStatement ();
    Stock1();
}
catch(SQLException e)
{
    System.out.println("Caught SQL Exception: \n     " + e);
}//close catch

}	
}
