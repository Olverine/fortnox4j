package org.notima.api.fortnox.clients;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.notima.api.fortnox.FortnoxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to manage several different clients.
 * 
 * @author Daniel Tamm
 *
 */
public class FortnoxClientManager {

	private Logger log = LoggerFactory.getLogger(FortnoxClientManager.class);	
	
	private FortnoxClientList clientList;
	
	// The path to the clients file
	private String	clientsFile;

	// The default client id
	static private String	defaultClientId;
	// The default client secret
	static private String	defaultClientSecret;
	
	public FortnoxClientManager() {}
	
	/**
	 * Instantiates this client manager by reading clients from given file.
	 * 
	 * @param fortnoxClientsFile	An XML-file containing client data
	 * @throws FileNotFoundException	If the file is not found.
	 */
	public FortnoxClientManager(String fortnoxClientsFile) throws FileNotFoundException {

		readClientsFromFile(fortnoxClientsFile);
		
	}
	
	public FortnoxClientManager getThis() {
		return this;
	}
	
	/**
	 * Takes the FortnoxClientInfo parameter and updates / adds it to the
	 * client list and saves the list to the file specified by clientsFile.
	 * 
	 * @param ci		The client info
	 * @return			The client info.
	 * @throws Exception	If something goes wrong.
	 */
	public FortnoxClientInfo updateAndSaveClientInfo(FortnoxClientInfo ci) throws Exception {
		
		if (ci.getOrgNo()==null) {
			throw new Exception("OrgNo is mandatory");
		}
		
		FortnoxClientInfo dst = getClientInfoByOrgNo(ci.getOrgNo());
		if (dst==null) {
			dst = addClient(ci);
		} else {
			// Update existing
			dst.setApiKey(ci.getApiKey());
			dst.setClientId(ci.getClientId());
			dst.setClientSecret(ci.getClientSecret());
		}
		if (!dst.hasOrgName()) {
			dst.setOrgName(ci.getOrgName());
		}

		boolean saved = saveClientInfo();
		if (!saved) {
			log.warn("No FortnoxClientsFile specified. Update of orgNo {} not persisted.", ci.getOrgNo());
		}else{
			readClientsFromFile(getClientsFile());
		}
		
		return dst;
		
	}

	/**
	 * Saves current client info to file.
	 * 	
	 * @return	True if it was saved to file. False if there was no file to save to.
	 * 
	 * @throws Exception	If something goes wrong.
	 */
	public boolean saveClientInfo() throws Exception {

		// Save to file if a file is specified
		if (clientsFile!=null) {
			FortnoxUtil.writeFortnoxClientListToFile(clientList, clientsFile);
			log.info("{} file updated.", clientsFile);
			return true;
		} else  {	
			return false;
		}
		
	}
	
	/**
	 * Reads clients from xml-file and sets the clients file.
	 * 
	 * @param fortnoxClientsFile	Path to XML file. Can be in classpath.
	 * @return	True if clients where read.
	 * @throws FileNotFoundException	if the client's file is not found.
	 */
	public Boolean readClientsFromFile(String fortnoxClientsFile) throws FileNotFoundException {
		
		try {
			clientList = FortnoxUtil.readFortnoxClientListFromFile(fortnoxClientsFile);
			clientsFile = fortnoxClientsFile;
			return clientList!=null ? Boolean.TRUE : Boolean.FALSE; 
		} catch (JAXBException e) {
			log.warn("Can't read Fortnox Client file: {} ", fortnoxClientsFile);
			log.debug(e.getMessage());
			return Boolean.FALSE;
		}
		
	}

	/**
	 * Path to the clients xml-file.
	 * 	
	 * @return	A path if set.
	 */
	public String getClientsFile() {
		return clientsFile;
	}

	/**
	 * Sets the clients file property (doesn't read the file).
	 * 
	 * @param clientsFile		The client's file.
	 */
	public void setClientsFile(String clientsFile) {
		this.clientsFile = clientsFile;
	}

	/**
	 * A list of FortnoxClientInfo
	 * 
	 * @return	A list of clients and their credentials
	 */
	public List<FortnoxClientInfo> getFortnoxClients() {
		List<FortnoxClientInfo> list = new ArrayList<FortnoxClientInfo>();
		if (clientList==null)
			return list;
		ListOfClientInfo ll = clientList.getClients();
		if (ll==null)
			return list;
		List<FortnoxClientInfo> tmpList = ll.getFortnoxClient();
		FortnoxClientInfo fi;
		if(tmpList != null){
			for (FortnoxClientInfo ii : tmpList) {
				fi = getClientInfoByOrgNo(ii.getOrgNo());
				if (fi!=null)
					list.add(fi);
			}
		}
		return list;
	}

	/**
	 * Adds a new client info to the list.
	 * No checking for duplicates is made here.
	 * 
	 * @param ci		The client info.
	 * @return			The added client info.
	 */
	public FortnoxClientInfo addClient(FortnoxClientInfo ci) {
		
		List<FortnoxClientInfo> list = getFortnoxClients();
		list.add(ci);
		ListOfClientInfo ll = clientList.getClients();
		if (ll==null) {
			ll = new ListOfClientInfo();
			clientList.setClients(ll);
		}
		ll.setFortnoxClient(list);
		return ci;
	}
	
	/**
	 * Removes a client from the list.
	 * Remember to 
	 * 
	 * @param ci		The client to be removed
	 * @return			True if the client was removed. False if not found.
	 */
	public boolean removeClient(FortnoxClientInfo ci) {
		
		String orgNo = ci.getOrgNo();
		if (orgNo==null) return false;
		
		ListOfClientInfo ll = clientList.getClients();
		if (ll==null)
			return false;
		
		List<FortnoxClientInfo> tmpList = ll.getFortnoxClient();
		int removeIndex = -1;
		for (int i = 0; i<tmpList.size(); i++) {
			if (tmpList.get(i).getOrgNo().equalsIgnoreCase(orgNo)) {
				removeIndex = i;
				break;
			}
		}
		
		if (removeIndex>=0) {
			tmpList.remove(removeIndex);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the client list data
	 * 
	 * @return	FortnoxClientList
	 */
	public FortnoxClientList getClientList() {
		return clientList;
	}

	public void setClientList(FortnoxClientList clientList) {
		this.clientList = clientList;
	}
	
	/**
	 * A place to store a default client id for this manager.
	 * 
	 * @return		The default client id (if any).
	 */
	public String getDefaultClientId() {
		return defaultClientId;
	}

	public void setDefaultClientId(String defaultClientId) {
		FortnoxClientManager.defaultClientId = defaultClientId;
	}
	
	/**
	 * A place to store a default client secret for this manager.
	 * 
	 * @return		The default client secret (if any).
	 */
	public String getDefaultClientSecret() {
		return defaultClientSecret;
	}

	public void setDefaultClientSecret(String defaultClientSecret) {
		FortnoxClientManager.defaultClientSecret = defaultClientSecret;
	}

	/**
	 * Gets client info by using the org no as key.
	 * 
	 * If client secret is supplied or can be derived from the list of Api Clients, the secret is also
	 * returned.
	 * 
	 * @param orgNo	The org number.
	 * @return		A FortnoxClientInfo record.
	 */
	public FortnoxClientInfo getClientInfoByOrgNo(String orgNo) {
		if (clientList==null) {
			log.warn("No Fortnox Clients available.");
			return null;
		}
		
		FortnoxClientInfo result = clientList.getClientInfoByOrgNo(orgNo);
		
		if (result!=null) {
			// Check and see if client secret is available
			if (result.getClientSecret()==null || result.getClientSecret().trim().length()==0) {
				// Lookup API client
				FortnoxApiClient apic = clientList.getApiClientById(result.getClientId());
				if (apic!=null) {
					result.setClientSecret(apic.getClientSecret());
				}
			}
		}
		
		return result;
	}
	

	/**
	 * Validates the connection. Gets an accessToken if an API-code is supplied. 
	 * 
	 * @param 		ci		The FortnoxClientInfo for the client to be validated.
	 * @return		True if the connection is validated.
	 * @throws Exception	If something goes wrong.
	 */
	@Deprecated
	public boolean validateConnection(FortnoxClientInfo ci) throws Exception {
		
		return true;
		
	}
	
}
