package demo.whois.adaptable.spi;

import java.io.IOException;

import org.osgi.annotation.versioning.ProviderType;

import demo.whois.api.SiteInfoDTO;

/**
 * 
 */
@ProviderType
public interface WhoisAdapter {

	public static final String PROPERTY_WHOIS_SERVER = "whoisServer";
	public static final String PROPERTY_WHOIS_DOMAINS = "domains";

	String getWhoisServer();

	SiteInfoDTO extractData(String data) throws IOException;

}
