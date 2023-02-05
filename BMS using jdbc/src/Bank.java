import java.util.*;
import java.io.*;
import java.sql.*;
class Account
{
	public int acc_no;
	public String name;
	public String type;
	public float balance;
	static int c=0;
	public Account(String name,String type,float bal) throws Exception
	{
		Class.forName("oracle.jdbc.driver.OracleDriver"); 
		Connection con=DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe","hr","hr");     
		String q="select * from bank";
		Statement stmt=con.createStatement();
		ResultSet r=stmt.executeQuery(q);
		Account.c=count(r);
		con.close();
		this.acc_no=Account.c+1;
		this.name=name;
		this.type=type;
		this.balance=bal;
	}
	public static void print(ResultSet r) throws Exception
	{
		while(r.next())
		{
			System.out.println("Account Number :: "+r.getInt(1)+"    Name :: "+r.getString(2));
			System.out.println("Account Type :: "+r.getString(3)+"    Balance :: "+r.getFloat(4));
		}
	}
	public static int count(ResultSet r) throws Exception
	{
		int count=0;
		while(r.next())
		{
			count+=1;
		}
		return count;
	}
	
}
class Bank{
	public Bank()
	{
		
	}
	public void createAccount() throws Exception
	{ 
		Scanner s=new Scanner(System.in);
		System.out.print("1.SAVINGS\n2.CURRENT\nEnter the type of account : ");
		int c=s.nextInt();
		System.out.print("Enter your name : ");
		String name=s.next();
		System.out.print("Enter the amount that you want to deposit initially : ");
		float amt=s.nextFloat();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		//step2 create  the connection object  
		Connection con=DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe","hr","hr");
		if(c==1) {
			Account a=new Account(name,"SAVINGS",amt);    
			//step3 create the statement object  
			String q="insert into bank values('"+a.acc_no+"','"+a.name+"','SAVINGS',"+a.balance+")";
			Statement stmt=con.createStatement();
			//System.out.println(a.acc_no+a.name+a.type+a.balance);
			stmt.executeUpdate(q);
			System.out.println("Your account has been created successfully .Thank you ");
			System.out.println("Your account number is : "+a.acc_no);
		}
		else
		{
			Account a=new Account(name,"SAVINGS",amt);   
			//step3 create the statement object  
			String q="insert into bank values('"+a.acc_no+"','"+a.name+"','CURRENT',"+a.balance+")";
			Statement stmt=con.createStatement();
			stmt.executeUpdate(q);
			System.out.println("Your account has been created successfully .Thank you ");
			System.out.println("Your account number is : "+a.acc_no);
		}
		con.close();
	}
	public void search(int acc) throws Exception
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con=DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe","hr","hr");    
		String q="select * from bank where acc_no="+acc+"";
		Statement stmt=con.createStatement();
		ResultSet r=stmt.executeQuery(q);
		Account.print(r);
		con.close();
	}
	public void update() throws Exception{
		System.out.print("Enter your account number : ");
		Scanner s=new Scanner(System.in);
		int a=s.nextInt();
		System.out.println("Do you want to");
		System.out.println("1. DEPOSIT");
		System.out.println("2. WITHDRAW");
		int choice=s.nextInt();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con=DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe","hr","hr");    
		String q="select * from bank where acc_no="+a+"";
		Statement stmt=con.createStatement();
		ResultSet r=stmt.executeQuery(q);
		if(choice==1)
		{
			System.out.print("Enter the amount that you wanted to deposit : ");
			float amt=s.nextFloat();
			if(Account.count(r)==1)
			{
				r=stmt.executeQuery(q);
				r.next();
				amt+=r.getFloat(4);
				q="update bank set balance="+amt+" where acc_no="+a+"";
				stmt.executeUpdate(q);
				System.out.println("Amount Credited to your Account");
			}
			else {
				System.out.println("Account is not found please create a new account to add amount");
			}
		}
		if(choice==2)
		{
			System.out.print("Enter the amount that you wanted to withdraw : ");
			float amt=s.nextFloat();
			if(Account.count(r)==1)
			{
				r=stmt.executeQuery(q);
				r.next();
				float k=r.getFloat(4);
				if(amt<=k)
				{
					amt=k-amt;
					q="update bank set balance="+amt+" where acc_no="+a+"";
					stmt.executeUpdate(q);
					System.out.println("Amount Withdrawn from your Account");
				}
				else {
					System.out.println("Unable to withdraw as your your account balance is less than the amount you wanted to withdraw.");
				}
			}
			else {
				System.out.println("Account is not found.");
			}
		}
		con.close();
	}
    public static void main(String [] args) throws Exception{
        Bank b=new Bank();
    	int c;
    	Scanner s=new Scanner(System.in);
    	while(true)
    	{
    		System.out.print("Enter your choice : \n\t1.CREATE NEW ACCOUNT \n\t2.PRINT ACCOUNT DETAILS\n\t3.DEPOSIT OR WITHDRAW\n\t4.EXIT\nEnter your choice :");
    		c=s.nextInt();
    		if(c==1) {
    			b.createAccount();
    		}
    		else if(c==2)
    		{
    			System.out.println("Enter the account number : ");
    			int a=s.nextInt();
    			b.search(a);
    		}
    		else if(c==3)
    		{
    			b.update();
    		}
    		else if(c==4)
    		{
    			break;
    		}
    		else {
    			System.out.println("You have entered a wrong choice .Please enter again...");
    		}
    	}
    }
}