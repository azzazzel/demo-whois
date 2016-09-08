package demo.whois.toplevel.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import demo.whois.adaptable.spi.WhoisAdapter;
import demo.whois.adaptable.spi.WhoisAdapterConfig;
import demo.whois.api.ContactDTO;
import demo.whois.api.SiteInfoDTO;

/**
 * 
 */
@Designate(ocd = WhoisAdapterConfig.class)
@Component(
		name = "demo.whois.toplevel",
		property = {
				WhoisAdapter.PROPERTY_WHOIS_SERVER + "=whois.internic.net",
				WhoisAdapter.PROPERTY_WHOIS_DOMAINS + "=com,net,org"
		},
		configurationPid = "demo.whois.toplevel.adapter"
	)
public class ToplevelAdapter implements WhoisAdapter {

	
	private String whoisServer;
	
	@Activate
	@Modified
	public void configure(WhoisAdapterConfig config) {
		whoisServer = config.whoisServer();
	}
	
	@Override
	public String getWhoisServer() {
		return whoisServer;
	}

	@Override
	public SiteInfoDTO extractData(String data) throws IOException {
		SiteInfoDTO result = new SiteInfoDTO();
		result.dataProvider = "AdaptableWhoisService using ToplevelAdapter";

		BufferedReader reader = new BufferedReader(new StringReader(data));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(": ");
			parseLine("Registrant", result.owner, parts);
			parseLine("Admin", result.admin, parts);
		}
		return result;
	}
	
	private void parseLine(String role, ContactDTO dto, String[] parts) {
		if ((role + " Name").equals(parts[0])) {
			dto.person = parts.length > 1 ? parts[1] : null;
		}
		if ((role + " Organization").equals(parts[0])) {
			dto.organization = parts.length > 1 ? parts[1] : null;
		}
		if ((role + " Phone").equals(parts[0])) {
			dto.phoneNumber = parts.length > 1 ? parts[1] : null;
		}
	}
}
