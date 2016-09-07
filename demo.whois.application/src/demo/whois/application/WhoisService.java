package demo.whois.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.WhoisClient;
import org.osgi.service.component.annotations.Component;


@Component(service=WhoisService.class)
public class WhoisService {

	private static final Pattern WHOIS_SERVER_PATTERN = Pattern.compile("Whois Server:\\s(.*)");
	private WhoisClient whois = new WhoisClient();

	public SiteInfoDTO check(String domain) {
		return check(WhoisClient.DEFAULT_HOST, domain);
	}

	public SiteInfoDTO check(String whoisServer, String domain) {

		whois.setConnectTimeout(3000);
		whois.setDefaultTimeout(3000);

		try {

			String prefix = "";
			if (WhoisClient.DEFAULT_HOST.equals(whoisServer)) {
				prefix = "=";
			}

			whois.connect(whoisServer);
			String whoisData = whois.query(prefix + domain);
			whois.disconnect();

			Matcher matcher = WHOIS_SERVER_PATTERN.matcher(whoisData);
			String foundWhoisHost = null;
			if (matcher.find()) {
				foundWhoisHost = matcher.group(1);
			}

			if (foundWhoisHost == null) {
				SiteInfoDTO result = new SiteInfoDTO();

				BufferedReader reader = new BufferedReader(new StringReader(whoisData));
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(": ");
					parseLine("Registrant", result.owner, parts);
					parseLine("Admin", result.admin, parts);
				}
				return result;
			} else {
				return check(foundWhoisHost, domain);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
