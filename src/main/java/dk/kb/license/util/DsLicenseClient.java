/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package dk.kb.license.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dk.kb.license.client.v1.DsLicenseApi;
import dk.kb.license.invoker.v1.ApiClient;
import dk.kb.license.invoker.v1.ApiException;
import dk.kb.license.invoker.v1.Configuration;
import dk.kb.license.model.v1.CheckAccessForIdsInputDto;
import dk.kb.license.model.v1.CheckAccessForIdsOutputDto;
import dk.kb.util.yaml.YAML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Client for the service. Intended for use by other projects that calls this service.
 * See the {@code README.md} for details on usage.
 * </p>
 * This class is not used internally.
 * </p>
 * The client is Thread safe and handles parallel requests independently.
 * It is recommended to persist the client and to re-use it between calls.
 * <p>
 * The client supports caching for {@link #checkAccessForIds} and {@link #checkAccessForResourceIds}.
 * See the {@link DsLicenseApi(YAML)} constructor for details.
 */
public class DsLicenseClient extends DsLicenseApi {
    private static final Logger log = LoggerFactory.getLogger(DsLicenseClient.class);

    public static final String URL_KEY = "licensemodule.url";
    public static final String CACHE_ID_COUNT_KEY = "licensemodule.cache.id.count";
    public static final int CACHE_ID_COUNT_DEFAULT = 100;
    public static final String CACHE_ID_MS_KEY = "licensemodule.cache.id.ms";
    public static final long CACHE_ID_MS_DEFAULT = 60000;

    Cache<CheckAccessForIdsInputDto, CheckAccessForIdsOutputDto> idcache =
            Caffeine.newBuilder()
                    .maximumSize(CACHE_ID_COUNT_DEFAULT)
                    .expireAfterWrite(CACHE_ID_MS_DEFAULT, TimeUnit.MILLISECONDS)
                    .build();

    /**
     * Creates a client for the service based on YAML config with the following structure:
     * <pre>
     * licensemodule:
     *   url: 'http://localhost:9076/ds-license/v1' # Mandatory
     *   cache:
     *     id:
     *       count: 100 # Default
     *       ms: 60000  # Default
     * </pre>
     * @param yaml setup for the license client, as outlined above.
     */
    @SuppressWarnings("JavadocLinkAsPlainText")
    public DsLicenseClient(YAML yaml) {
        this(yaml.getString(URL_KEY),
                yaml.getInteger(CACHE_ID_COUNT_KEY, CACHE_ID_COUNT_DEFAULT),
                yaml.getLong(CACHE_ID_MS_KEY, CACHE_ID_MS_DEFAULT));
    }

    /**
     * Creates a client for the service.
     * @param serviceURI the URI for the service, e.g. {@code https://example.com/ds-license/v1}.
     */
    public DsLicenseClient(String serviceURI, int cacheIDCount, long cacheIDms) {
        super(createClient(serviceURI));
        cache(cacheIDCount, cacheIDms);
        log.info("Created OpenAPI client for '{}' with ID-cache(count={}, ms={})",
                serviceURI, cacheIDCount, cacheIDms);
    }

    /**
     * Adjust caching if {@link #checkAccessForIds} and {@link #checkAccessForResourceIds}.
     * <p>
     * Calling this method resets the cache.
     * @param cacheIDCount the maximum number of {@link CheckAccessForIdsInputDto} to cache.
     * @param cacheIDms the maximum amount of milliseconds for any object in the cache.
     * @return this object with caching adjusted
     */
    @SuppressWarnings("UnusedReturnValue")
    public DsLicenseApi cache(int cacheIDCount, long cacheIDms) {
        log.debug("Setting ID cache to count={}, ms={}", cacheIDCount, cacheIDms);
        idcache = Caffeine.newBuilder()
                .maximumSize(cacheIDCount)
                .expireAfterWrite(cacheIDms, TimeUnit.MILLISECONDS)
                .build();
        return this;
    }

    /**
     * Creates a client for the service.
     * @param serviceURI the URI for the service, e.g. {@code https://example.com/ds-license/v1}.
     * @deprecated use {@link DsLicenseApi(YAML)} or {@link DsLicenseApi(YAML, int, long)} instead.
     */
    @Deprecated
    public DsLicenseClient(String serviceURI) {
        this(serviceURI, CACHE_ID_COUNT_DEFAULT, CACHE_ID_MS_DEFAULT);
    }

    /**
     * @param serviceURIString a URI to a service.
     * @return an ApiClient constructed from the serviceURIString.
     */
    private static ApiClient createClient(String serviceURIString) {
        log.debug("Creating OpenAPI client with URI '{}'", serviceURIString);

        URI serviceURI = URI.create(serviceURIString);
        // No mechanism for just providing the full URI. We have to deconstruct it
        return Configuration.getDefaultApiClient().
                setScheme(serviceURI.getScheme()).
                setHost(serviceURI.getHost()).
                setPort(serviceURI.getPort()).
                setBasePath(serviceURI.getRawPath());
    }

    @Override
    public CheckAccessForIdsOutputDto checkAccessForIds(CheckAccessForIdsInputDto idInputDto) throws ApiException {
        return checkAccessForIdsGeneral(idInputDto, true);
    }

    @Override
    public CheckAccessForIdsOutputDto checkAccessForResourceIds(CheckAccessForIdsInputDto idInputDto)
            throws ApiException {
        return checkAccessForIdsGeneral(idInputDto, false);
    }

    /**
     * Bypass the cache and always perform a remote check for access.  
     * @param idInputDto request for access information.
     * @return direct result from {@link DsLicenseApi#checkAccessForIds} 
     * @throws ApiException if the remote call failed.
     */
    public CheckAccessForIdsOutputDto directCheckAccessForIds(CheckAccessForIdsInputDto idInputDto) throws ApiException {
        return super.checkAccessForIds(idInputDto);
    }

    /**
     * Bypass the cache and always perform a remote check for access.  
     * @param idInputDto request for access information.
     * @return direct result from {@link DsLicenseApi#checkAccessForResourceIds} 
     * @throws ApiException if the remote call failed.
     */
    public CheckAccessForIdsOutputDto directCheckAccessForResourceIds(CheckAccessForIdsInputDto idInputDto)
            throws ApiException {
        return super.checkAccessForResourceIds(idInputDto);
    }

    /**
     * Helper for {@link #checkAccessForIds} and {@link #checkAccessForResourceIds} that takes care of wrapping and
     * unwrapping the use of {@link #idcache}.
     * @param idInputDto an input for either ID or resourceID lookup.
     * @param directID if true, the given IDs are treated as primary IDs. If false, they are treated as resourceIDs.
     *                 This switches between forwarding to {@link #checkAccessForIds} and
     *                 {@link #checkAccessForResourceIds}.
     * @return the response from the forward call, potentially fetched from cache.
     * @throws ApiException if the ID access check failed.
     */
    public CheckAccessForIdsOutputDto checkAccessForIdsGeneral(CheckAccessForIdsInputDto idInputDto, boolean directID)
            throws ApiException {
        // This is a mess of try-catches, but it is hard to avoid as we really want to use the Function based
        // idcache.get(idInputDto, inputDTO -> ...)
        // That method guards against multiple concurrent checks for the same IDs,
        // which is an extremely common occurrence when handling Tile based image viewing.
        try {
            return idcache.get(idInputDto, inputDTO -> {
                try {
                    return directID ?
                            directCheckAccessForIds(inputDTO) :
                            directCheckAccessForResourceIds(inputDTO);
                } catch (ApiException e) { // ApiException is checked; we cannot throw those directly in lambdas
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            // If possible, locate the inner ApiException and throw that. Quite a hack, but what to do?
            if (e.getCause() != null && e.getCause() instanceof ApiException) {
                throw (ApiException) e.getCause();
            } else {
                throw e;
            }
        } catch (Exception e) {
            ApiException wrapped = new ApiException(
                    "Unknown Exception calling checkAccessForIds with directID=" + directID);
            wrapped.initCause(e);
            throw wrapped;
        }
    }

}
