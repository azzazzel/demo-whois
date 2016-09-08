package demo.whois.adaptable.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.WhoisClient;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import demo.whois.adaptable.spi.WhoisAdapter;
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

	private WhoisClient whois = new WhoisClient();

	private Map<String, WhoisAdapter> adapters = new HashMap<String, WhoisAdapter>();
	
	private static final Pattern WHOIS_SERVER_PATTERN = Pattern.compile("Whois Server:\\s(.*)");
	
	@Override
	public SiteInfoDTO getSiteInfo(URI site) {
		for (Entry<String, WhoisAdapter> entry : adapters.entrySet()) {
			if (site.getHost().endsWith(entry.getKey())) {
				WhoisAdapter adapter = entry.getValue();
				String data = getWhoisData(adapter.getWhoisServer(), site.getHost());
				try {
					return adapter.extractData(data);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	
	private String getWhoisData(String whoisServer, String domain) {
		whois.setConnectTimeout(3000);
		whois.setDefaultTimeout(3000);
		String whoisData = null;
		try {

			String prefix = "";
			if (WhoisClient.DEFAULT_HOST.equals(whoisServer)) {
				prefix = "=";
			}
			
			whois.connect(whoisServer);
			whoisData = whois.query(prefix + domain);
			whois.disconnect();

			
			Matcher matcher = WHOIS_SERVER_PATTERN.matcher(whoisData);
			String foundWhoisHost = null;
			if (matcher.find()) {
				foundWhoisHost = matcher.group(1);
			}

			if (foundWhoisHost == null) {
				return whoisData;
			} else {
				return getWhoisData(foundWhoisHost, domain);
			}
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return whoisData;
	}

	
	@Reference(
			cardinality = ReferenceCardinality.MULTIPLE, 
			policy = ReferencePolicy.DYNAMIC,
			policyOption = ReferencePolicyOption.GREEDY
		)
	void addWhoisAdapter(WhoisAdapter adapter, Map<String, Object> config) {
		String[] domains = ((String)config.get("domains")).split(",");
		if (domains != null) {
			for (String domain : domains) {
				String trimmedDomain = domain.trim();
				if (trimmedDomain.length() > 0) {
					adapters.put(trimmedDomain, adapter);
				}
			}
		}
	} 
	
	void updatedWhoisAdapter(WhoisAdapter adapter, Map<String, Object> config) {
		removeWhoisAdapter(adapter, config);
		addWhoisAdapter(adapter, config);
	}
	
	void removeWhoisAdapter(WhoisAdapter adapter, Map<String, Object> config) {
		for (Iterator<Entry<String, WhoisAdapter>> iterator = adapters.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, WhoisAdapter> entry = iterator.next();
			if (entry.getValue().equals(adapter)) {
				iterator.remove();
			}
		}
		
	} 	
	
}
