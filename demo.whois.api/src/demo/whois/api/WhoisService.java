package demo.whois.api;

import java.net.URI;

/**
 * 
 */
public interface WhoisService {
	
	SiteInfoDTO getSiteInfo (URI site); 

	
}
