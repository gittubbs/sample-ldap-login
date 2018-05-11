package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LDAPConnectionProperties {
	
	
	/* useful for outside */
	public static final String BASE 		= "dc=myorg,dc=test";
	public static final String OU_PEOPLE    = "ou=People";
	
	/* used for prop parsing*/
	private static final String PREFIX		 = "ldap://";
	private static final String HOSTNAME 	 = "hostname";
	private static final String PORT 		 = "port";
	
	public static String getConnectionURL ( String path ){
		
		Properties prop = new Properties();
		InputStream input = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			input = new FileInputStream(path);

			// load a properties file
			prop.load(input);

			sb.append(PREFIX)
				.append(prop.get(HOSTNAME))
				.append(':')
				.append(prop.getProperty(PORT));
			
			return sb.toString();

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return sb.toString();

	}
}
