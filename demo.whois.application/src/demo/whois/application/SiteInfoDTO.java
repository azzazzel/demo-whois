package demo.whois.application;

import org.osgi.dto.DTO;

public class SiteInfoDTO extends DTO {

	public String url;
	
	public ContactDTO owner = new ContactDTO();

	public ContactDTO admin = new ContactDTO();
		
}
