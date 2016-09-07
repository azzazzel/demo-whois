package demo.whois.adaptable.provider;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import demo.whois.api.SiteInfoDTO;


public class AdaptableWhoisTest {
	
	@Test
	public void simpleComTest () throws URISyntaxException {
		AdaptableWhoisService whois = new AdaptableWhoisService();
		SiteInfoDTO siteInfo = whois.getSiteInfo(new URI(null, "google.com", null, null));
		Assert.assertEquals("Dns Admin", siteInfo.admin.person);
	}
	
	@Test
	public void simplePlTest () throws URISyntaxException {
		AdaptableWhoisService whois = new AdaptableWhoisService();
		SiteInfoDTO siteInfo = whois.getSiteInfo(new URI(null, "google.pl", null, null));
		Assert.assertEquals("Dns Admin", siteInfo.admin.person);
	}

}
