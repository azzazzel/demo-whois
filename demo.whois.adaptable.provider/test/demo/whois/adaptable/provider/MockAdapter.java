package demo.whois.adaptable.provider;

import java.io.IOException;

import demo.whois.adaptable.spi.WhoisAdapter;
import demo.whois.api.SiteInfoDTO;

public class MockAdapter implements WhoisAdapter {

	String domain;
	
	public MockAdapter(String domain) {
		this.domain = domain;
	}
	
	@Override
	public String getWhoisServer() {
		return null;
	}

	@Override
	public SiteInfoDTO extractData(String data) throws IOException {
		SiteInfoDTO siteInfoDTO = new SiteInfoDTO();
		siteInfoDTO.admin.person = "SOMEONE";
		siteInfoDTO.dataProvider = domain;
		return siteInfoDTO;
	}

}
