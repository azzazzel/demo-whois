package demo.whois.application;

import java.net.URI;
import java.net.URISyntaxException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import demo.whois.api.SiteInfoDTO;
import demo.whois.api.WhoisService;
import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.google.angular.capabilities.RequireAngularWebResource;
import osgi.enroute.rest.api.REST;
import osgi.enroute.twitter.bootstrap.capabilities.RequireBootstrapWebResource;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@RequireAngularWebResource(resource={"angular.js","angular-resource.js", "angular-route.js"}, priority=1000)
@RequireBootstrapWebResource(resource="css/bootstrap.css")
@RequireWebServerExtender
@RequireConfigurerExtender
@Component(name="demo.whois")
public class WhoisApplication implements REST {
	
	@Reference
	WhoisService service;

	public SiteInfoDTO getWhois(String string) throws URISyntaxException {
		
		URI uri = new URI(null, string, null, null);
		return service.getSiteInfo(uri);
	}

}
