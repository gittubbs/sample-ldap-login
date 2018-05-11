package main;

import java.util.Scanner;

import javax.naming.NamingException;

import model.LDAPConnection;
import model.OTPValidator;
import model.QRCodeGenerator;
import model.bean.Response;
import model.bean.User;

public class Loader {

	private static String ldapPropPath;
	private static Scanner sc = new Scanner(System.in);
	private static String username;
	private static String password;
	private static LDAPConnection conn;
	
	public static void main(String[] args) throws Exception {
		ldapPropForm();
		conn = new LDAPConnection(ldapPropPath);
		inner();
	}
	
	private static void inner(){
		while (true) {
			int choice = menu();
			switch (choice) {
			case 1:
				generateQR();
				break;
			case 2:
				loginWithOtp();
				break;
			case 3:
				System.exit(0);
			default:

			}
		}
	}
	
	private static void loginWithOtp() {
		loginForm();
		User user = new User(username,password);
		conn.submit(user);
		Response resp = null;
		try {
			resp = conn.getResponse();
		} catch (NamingException e) {
			System.out.println("unsuccessful login.");
			return;
		}
		
		System.out.print("Please, insert the OTP > ");
		String otp = sc.nextLine();
		System.out.println("Authenticating with ["+otp+"] ...");
		
		boolean valid = OTPValidator.validate(user, resp, otp);
		if(valid){
			welcomePage(resp);
		} else {
			System.out.println("Invalid OTP!");
		}
	}


	private static void welcomePage(Response resp) {
		System.out.println();
		System.out.println(" --   HELLO!   --");
		System.out.println("Name: "+resp.getName());
		System.out.println("Surname: "+resp.getSurname());
		System.out.println("------------------");
		System.out.println("Press ENTER to continue...");
		sc.nextLine();
	}


	private static void loginForm() {
		sc = new Scanner(System.in);
		System.out.print("Enter your username > ");
		username = sc.nextLine();
		System.out.print("Enter your password > ");
		password = sc.nextLine();
	}

	private static void ldapPropForm() {
		System.out.print("Please, specify the ldap.prop location > ");
		ldapPropPath = sc.nextLine();
		System.out.println();
		System.out.println();
	}

	private static int menu() {
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("           Welcome to the login portal!       ");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println(" .1     ----> Generate QRCode        ");
		System.out.println(" .2     ----> Login with OTP         ");
		System.out.println(" .3     ----> Exit");
		System.out.println("");
		System.out.print("Please, insert your choice and press ENTER > ");
		int c = sc.nextInt();
		return c;
	}

	
	
	private static void generateQR (){
		sc.reset();
		loginForm();
		System.out.println("");
		System.out.print("Insert the path where to save the PNG image > ");
		String path = sc.nextLine();
		User user = new User(username,password);
		Response resp = authenticateUserPassword(user);
		if(resp == null) return;
		QRCodeGenerator.generate(user, resp, path);
		System.out.println("done.");
	}


	private static Response authenticateUserPassword(User user) {
		conn.submit(user);
		try {
			return conn.getResponse();
		} catch (NamingException e) {
			System.out.println("Error while authenticating to LDAP - credentials wrong...?");
		}
		return null;
	}
	
	

}
