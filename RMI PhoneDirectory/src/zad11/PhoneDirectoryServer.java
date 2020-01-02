package zad11;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JOptionPane;

public class PhoneDirectoryServer 
{
	private static String[] infoMessages = {"Server has started.", "Error has occured while starting...", "Error has occured while working..."};	
	public static void main(String[] args)
	{
		try 
		{
			PhoneDirectory phoneDirectory = new PhoneDirectory("phoneContact.txt");			
			Properties configurationProperties = new Properties();
			configurationProperties.setProperty(Context.PROVIDER_URL, "iiop://localhost:3334");	
			configurationProperties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");				
			Context context = new InitialContext(configurationProperties);
			context.rebind("PhoneDirectoryService", phoneDirectory);
			JOptionPane.showMessageDialog(null, infoMessages[0]);
		} 
		catch (Exception exception) 
		{			
			JOptionPane.showMessageDialog(null, infoMessages[1]);
			exception.printStackTrace();
		}
	}
}