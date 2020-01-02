package zad11;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class PhoneDirectoryClient 
{
	private static JButton button;
	private static JTextField jTextField;
	private static Pattern requestNumberPattern = Pattern.compile(" +", 3);
	
	final static String WELCOME_TEXT = "WELCOME!";
	static String command = null;
	static String result = null;
	
	private static Context context;
	private static Object object;
	
	private PhoneDirectoryInterface phoneDirectoryInterface;
	
	private static String requestString[] = { 
			"OK", "Command not found", 
			"Object not found", "Object exists",
			"This object cannot be replace", "Fatal Error"
		};
		
	public static void main(String[] args)
	{
		try 
		{
			PhoneDirectoryClient phoneDirectoryClient = new PhoneDirectoryClient();
			Properties propertiesClient = new Properties();
			
			propertiesClient.setProperty(Context.PROVIDER_URL, "iiop://localhost:3334");
			propertiesClient.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
			
			context = new InitialContext(propertiesClient);
			object = context.lookup("PhoneDirectoryService");

			phoneDirectoryClient.phoneDirectoryInterface = (PhoneDirectoryInterface)PortableRemoteObject.narrow(object,
					PhoneDirectoryInterface.class);
		
		    JFrame jframeDirectoryClient = new JFrame("PhoneBook");
	       
	        jframeDirectoryClient.setLayout(null); 
	        jframeDirectoryClient.setMinimumSize(new Dimension(600, 150));
	        jframeDirectoryClient.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        
	        button = new JButton("Send");
	        jTextField = new JTextField();
	        
	        jTextField.setBounds(25, 10, 550, 30);
	        button.setBounds(250, 50, 100, 30); 
	        button.addActionListener(new ActionListener() 
	        {
	            @Override
	            public void actionPerformed(ActionEvent e) 
	            {
	            	command =  jTextField.getText();
	            	jTextField.setText(null);
	            	try
	            	{
						phoneDirectoryClient.request(command);
					} 
	            	catch (IOException exc) 
	            	{
						exc.printStackTrace();
					}
	            }
	        });	        
			jframeDirectoryClient.add(button);
			jframeDirectoryClient.add(jTextField);
	        jframeDirectoryClient.pack();
	        jframeDirectoryClient.setVisible(true);
		} 
		catch (Exception exc) 
		{
			exc.printStackTrace();
		}
	}

	private void request(String request) throws IOException 
	{		
		int requestDataLengthGet = 2;
		int requestDataLengthChange = 3;
		String description = "";
		String[] requestData = requestNumberPattern.split(request, 3); 
		String commandName = requestData[0];
			
		if (commandName.equals("get"))
		{ 	
			StringBuilder sb = new StringBuilder();
			if (requestData.length != requestDataLengthGet)
			{
				showResponse(1, null);
			}			
			else 
			{
				String phoneNumber = (String)phoneDirectoryInterface.getPhoneNumber(requestData[1]);
				if (phoneNumber == null)
				{
					showResponse(2, null);
				} 
				else 
				{
					sb.append(requestData[1]);
					sb.append(" contact number: ");
					sb.append(phoneNumber);
					description = sb.toString();				
					showResponse(0, description); 
				}
			}
		}
		else if (commandName.equals("replace")) 
		{ 
			StringBuilder sb = new StringBuilder();
			if (requestData.length != requestDataLengthChange)
			{
				showResponse(1, null);
			}			
			else 
			{
				boolean isOperationReplaceSuccessful = phoneDirectoryInterface.replacePhoneNumber(requestData[1], requestData[2]);
				if(isOperationReplaceSuccessful == true) 
				{
					sb.append(requestData[1]);
					sb.append(" contact number: ");
					sb.append(requestData[2]);
					sb.append(" replaced.");
					description = sb.toString();				
					showResponse(0, description); 
				} 
				else
				{
					showResponse(4, null);
				}			
			}
		} 
		else if (commandName.equals("add"))
		{
			StringBuilder stringBuilder = new StringBuilder();
			if (requestData.length != requestDataLengthChange)
			{
				showResponse(1, null);
			}			
			else 
			{
				boolean isAddedOperationSuccessful = phoneDirectoryInterface.addPhoneNumber(requestData[1], requestData[2]); 
				if (isAddedOperationSuccessful == true) 
				{
					stringBuilder.append(requestData[1]);
					stringBuilder.append(" contact number: ");
					stringBuilder.append(requestData[2]);
					stringBuilder.append(" has been added.");
					description = stringBuilder.toString();
					showResponse(0, description); 
				} 
				else
				{
					showResponse(3, null); 
				}
			}
		}		
		else
		{
			showResponse(1, null);
		}
	}

	private void showResponse(int codeOfResponse, String descriptionMessage) throws IOException 
	{
		if(descriptionMessage != null) 
		{
			JOptionPane.showMessageDialog(null, descriptionMessage);
			System.out.println(descriptionMessage);
		} 
		else
		{
			JOptionPane.showMessageDialog(null, requestString[codeOfResponse]);
		}
	}
}