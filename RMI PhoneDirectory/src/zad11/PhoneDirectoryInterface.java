package zad11;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PhoneDirectoryInterface extends Remote
{
	public boolean addPhoneNumber(String name, String num) throws RemoteException;
	public boolean replacePhoneNumber(String name, String num) throws RemoteException;	
	public String getPhoneNumber(String name) throws RemoteException;
}