package demo.whois.pl.adapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import demo.whois.adaptable.spi.WhoisAdapter;
import demo.whois.adaptable.spi.WhoisAdapterConfig;
import demo.whois.api.SiteInfoDTO;

/**
 * 
 */
@Designate(ocd = WhoisAdapterConfig.class)
@Component(
		name = "demo.whois.pl",
		property = {
				WhoisAdapter.PROPERTY_WHOIS_SERVER + "=whois.dns.pl",
				WhoisAdapter.PROPERTY_WHOIS_DOMAINS + "=pl"
		},
		configurationPid = "demo.whois.pl.adapter"
	)
public class PlAdapter implements WhoisAdapter {
	
	private static final Pattern PATTERN_COMPANY = Pattern.compile("company:\\s+(.*)");
	private static final Pattern PATTERN_PHONE = Pattern.compile("phone:\\s+(.*)");
	
	private String whoisServer;

	@Override
	public String getWhoisServer() {
		return whoisServer;
	}

	@Override
	public SiteInfoDTO extractData(String data) {
		SiteInfoDTO result = new SiteInfoDTO();
		result.dataProvider = "AdaptableWhoisService using PlAdapter";

		Matcher matcher = PATTERN_COMPANY.matcher(data);
		if (matcher.find()) result.admin.organization = matcher.group(1);

		matcher = PATTERN_PHONE.matcher(data);
		if (matcher.find()) result.admin.phoneNumber = matcher.group(1);
		
		return result;
	}

	
	
	@Activate
	@Modified
	public void configure(WhoisAdapterConfig whoisAdapterConfig) {
		whoisServer = whoisAdapterConfig.whoisServer();
	}
	

}
