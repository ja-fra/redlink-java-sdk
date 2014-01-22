package io.redlink.sdk.impl;


import io.redlink.sdk.Credentials;
import io.redlink.sdk.util.ApiHelper;
import io.redlink.sdk.util.RedLinkClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * Public API {@link Credentials} implementation. This implementation should be used as the most simple way to access to the RedLink public platform
 * An user valid API key is necessary for building the credential object
 * 
 * @author sergio.fernandez@redlink.co
 * @author jakob.frank@redlink.co
 */
public final class DefaultCredentials extends AbstractCredentials {

    private static Logger log = LoggerFactory.getLogger(DefaultCredentials.class);

    private static final String ENDPOINT = "https://api.redlink.io";

    private static final String KEY_PARAM = "key";

    private final ResteasyClientBuilder clientBuilder;

    public DefaultCredentials(String apiKey) {
        this(apiKey, ApiHelper.getApiVersion());
    }

    public DefaultCredentials(String apiKey, String version) {
        super(ENDPOINT, version, apiKey);
        clientBuilder = new RedLinkClientBuilder();
        log.debug("created credentials over {}/{}", ENDPOINT, version);
    }

    /*
     * (non-Javadoc)
     * @see io.redlink.sdk.Credentials#buildUrl(javax.ws.rs.core.UriBuilder)
     */
    @Override
    public WebTarget buildUrl(UriBuilder builder) throws MalformedURLException, IllegalArgumentException, UriBuilderException {
        URI uri = builder.queryParam(KEY_PARAM, apiKey).build();
        return clientBuilder.build().target(uri.toString());
    }

}
