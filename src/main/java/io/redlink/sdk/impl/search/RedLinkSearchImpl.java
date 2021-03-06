/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.redlink.sdk.impl.search;

import io.redlink.sdk.Credentials;
import io.redlink.sdk.RedLink;
import io.redlink.sdk.impl.RedLinkAbstractImpl;
import io.redlink.sdk.impl.search.model.SearchResults;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import java.net.MalformedURLException;

public class RedLinkSearchImpl extends RedLinkAbstractImpl implements RedLink.Search {

    public RedLinkSearchImpl(Credentials credentials) {
        super(credentials);
    }

    @Override
    public SearchResults search(String query, String core) {
        try {
            String service = credentials.buildUrl(getSearchUriBuilder(query, core)).toString();
            return execSearch(service);
        } catch (MalformedURLException | IllegalArgumentException | UriBuilderException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SearchResults search(String query, String core, int start, int results, boolean facet) {
        try {
            String service = credentials.buildUrl(getSearchUriBuilder(query, core, start, results, facet)).toString();
            return execSearch(service);
        } catch (MalformedURLException | IllegalArgumentException | UriBuilderException e) {
            throw new RuntimeException(e);
        }
    }

    private final UriBuilder getSearchUriBuilder(String query, String core) {
        return initiateUriBuilding().path(PATH).path(core).queryParam(QUERY, query);
    }

    private final UriBuilder getSearchUriBuilder(String query, String core, int start, int results, boolean facet) {
        return getSearchUriBuilder(query, core).
                queryParam(START, start).
                queryParam(RESULTS, results).
                queryParam(FACET, facet);
    }

    private final SearchResults execSearch(String uri) {
        ClientRequest request = new ClientRequest(uri);
        request.accept("application/json");
        try {
            //TODO> use try-with-resources statement of java7 for properly releasing the connection
            //		which requires a RestEasy ClientResponse implementing AutoCloseable
            ClientResponse<SearchResults> response = request.get(SearchResults.class);
            if (response.getStatus() != 200) {
                //TODO: improve this feedback from the sdk (400, 500, etc)
                throw new RuntimeException("Search failed: HTTP error code " + response.getStatus());
            } else {
                return response.getEntity();
            }
        } catch (Exception e) {
            throw new RuntimeException("Search failed: " + e.getMessage(), e);
        }
    }

}
