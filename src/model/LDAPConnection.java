package model;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import model.bean.Response;
import model.bean.User;
import utils.LDAPConnectionProperties;

public class LDAPConnection {
	
	private String url;
	private Response response;
	
	private Hashtable<String, String> environment = new Hashtable<>();
	private String dnString;
	
	
	public LDAPConnection(String path) {
		this.url = LDAPConnectionProperties.getConnectionURL(path);
		
	}

	public void submit(User user) {
		
		StringBuilder dn = new StringBuilder();
		dn.append("uid=")
			.append(user.getUsername())
			.append(',')
			.append(LDAPConnectionProperties.OU_PEOPLE)
			.append(',')
			.append(LDAPConnectionProperties.BASE);
		
			this.dnString = dn.toString();
			
			this.environment.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");
			this.environment.put(Context.PROVIDER_URL, this.url);
			this.environment.put(Context.SECURITY_AUTHENTICATION, "simple");
			this.environment.put(Context.SECURITY_PRINCIPAL, dnString);
			this.environment.put(Context.SECURITY_CREDENTIALS, user.getPassword());
		
	}
	
	public Response getResponse() throws NamingException {
			if(response == null){
				DirContext authContext = new InitialDirContext(environment);
				
				this.response = new Response(
						authContext.getAttributes(dnString).get("cn").get().toString(),
						authContext.getAttributes(dnString).get("sn").get().toString(),
						authContext.getAttributes(dnString).get("l").get().toString()
						);
			}	
			
			return this.response;
	}
	
	public void clear(){
		this.response = null;
		this.dnString = null;
		this.environment = new Hashtable<>();
	}
}
