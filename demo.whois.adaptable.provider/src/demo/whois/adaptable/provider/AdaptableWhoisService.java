package demo.whois.adaptable.provider;

import java.net.URI;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import demo.whois.api.SiteInfoDTO;
import demo.whois.api.WhoisService;

/**
 * 
 */
@Component (
		property = {
				Constants.SERVICE_RANKING + ":Integer = 100"
		},
		immediate = true
	)
public class AdaptableWhoisService implements WhoisService {

	@Override
	public SiteInfoDTO getSiteInfo(URI site) {
		
		SiteInfoDTO siteInfoDTO = new SiteInfoDTO();
		siteInfoDTO.dataProvider = "AdaptableWhoisService";
		siteInfoDTO.admin.person = "Fake data for now";
		
		return siteInfoDTO;
	}

}
