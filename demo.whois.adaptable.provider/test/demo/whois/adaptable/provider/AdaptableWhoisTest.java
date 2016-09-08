package demo.whois.adaptable.provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.whois.adaptable.spi.WhoisAdapter;
import demo.whois.api.SiteInfoDTO;


public class AdaptableWhoisTest {
	
	AdaptableWhoisService whois = new AdaptableWhoisService();

	MockAdapter comAdapter = new MockAdapter("com");
	MockAdapter plAdapter = new MockAdapter("pl");
	
	
	@Before
	public void setUp () {
		Map<String, Object> config = new HashMap<String, Object>();
		config.put(WhoisAdapter.PROPERTY_WHOIS_DOMAINS, "com");
		whois.addWhoisAdapter(comAdapter, config);
		
		config = new HashMap<String, Object>();
		config.put(WhoisAdapter.PROPERTY_WHOIS_DOMAINS, "pl");
		whois.addWhoisAdapter(plAdapter, config);
	}
	
	@Test
	public void simpleComTest () throws URISyntaxException {
		SiteInfoDTO siteInfo = whois.getSiteInfo(new URI(null, "google.com", null, null));
		Assert.assertEquals("SOMEONE", siteInfo.admin.person);
		Assert.assertEquals("com", siteInfo.dataProvider);
		
		siteInfo = whois.getSiteInfo(new URI(null, "google.pl", null, null));
		Assert.assertEquals("SOMEONE", siteInfo.admin.person);
		Assert.assertEquals("pl", siteInfo.dataProvider);

		siteInfo = whois.getSiteInfo(new URI(null, "google.another", null, null));
		Assert.assertNull(siteInfo);
		
		whois.removeWhoisAdapter(plAdapter, null);
		siteInfo = whois.getSiteInfo(new URI(null, "google.com", null, null));
		Assert.assertEquals("SOMEONE", siteInfo.admin.person);
		Assert.assertEquals("com", siteInfo.dataProvider);
		siteInfo = whois.getSiteInfo(new URI(null, "google.pl", null, null));
		Assert.assertNull(siteInfo);
		
	
	}
	
}
