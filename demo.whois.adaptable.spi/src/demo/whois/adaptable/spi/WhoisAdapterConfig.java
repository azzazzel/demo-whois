package demo.whois.adaptable.spi;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface WhoisAdapterConfig {
	
	@AttributeDefinition(required=true, defaultValue = "whois.internic.net")
	String whoisServer() default "whois.internic.net";

	@AttributeDefinition(required=true)
	String[] domains() default {};

}
