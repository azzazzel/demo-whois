package demo.whois.simple.provider;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import demo.whois.api.SiteInfoDTO;
import demo.whois.api.WhoisService;


public class DefaultWhoisTest {
	
	@Test
	public void simpleComTest () throws URISyntaxException {
		WhoisService whois = new DefaultWhoisService();
		SiteInfoDTO siteInfo = whois.getSiteInfo(new URI(null, "google.com", null, null));
		Assert.assertEquals("Dns Admin", siteInfo.admin.person);
	}
	
}
