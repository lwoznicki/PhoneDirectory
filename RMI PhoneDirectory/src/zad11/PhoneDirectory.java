package zad11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.rmi.PortableRemoteObject;

public class PhoneDirectory extends PortableRemoteObject implements PhoneDirectoryInterface
{
	@SuppressWarnings("rawtypes")
	private Map phoneDirectoryMap = new HashMap();

	@SuppressWarnings({ "resource", "unchecked" })
	public PhoneDirectory(String filename) throws RemoteException 
	{
		// Inicjowanie zawartosci ksiazki, wczytywanie w formacie: imie numer_telefonu
		try 
		{
			String line;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			while ((line = bufferedReader.readLine()) != null)
			{
				String[] data = line.split(" +", 2);
				phoneDirectoryMap.put(data[0], data[1]);
			}
		} 
		catch (Exception exception) 
		{
			exception.printStackTrace();
			System.exit(1);
		}
	}
	// Zwraca numer telefonu dla podanej osoby
    public String getPhoneNumber(String name) throws RemoteException 
    {
        return (String) phoneDirectoryMap.get(name);
    }

  
    @SuppressWarnings("unchecked")
	public boolean addPhoneNumber(String name, String num) 
    {
      if (phoneDirectoryMap.containsKey(name)) return false;
      phoneDirectoryMap.put(name, num);
      return true;
    }

    // Zastępuje numer podanej osoby nowym [Result == True -> Numer zastapiony || Result == False -> numer jest juz przypisany do kogos

    @SuppressWarnings("unchecked")
	public boolean replacePhoneNumber(String name, String num) 
    {
      if (!phoneDirectoryMap.containsKey(name)) return false;
      phoneDirectoryMap.put(name, num);
      return true;
    }
    
}