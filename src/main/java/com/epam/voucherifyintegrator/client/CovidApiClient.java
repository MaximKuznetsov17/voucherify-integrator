package com.epam.voucherifyintegrator.client;

import com.epam.voucherifyintegrator.model.CovidData;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.Optional;

public class CovidApiClient {
    Logger logger = LogManager.getLogger(CovidApiClient.class);

    private final String baseUrl;
    private final Integer connectTimeout;

    public CovidApiClient(String baseUrl, Integer connectTimeout) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "Can't get baseUrl");
        this.connectTimeout = Objects.requireNonNull(connectTimeout, "Can't get connectTimeout");
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            CloseableHttpClient customHttpClient = getClient(connectTimeout);
            Unirest.setHttpClient(customHttpClient);
        } catch (Exception e) {
            logger.error("Can't create COVID rest client", e);
        }
    }

    private CloseableHttpClient getClient(int timeout)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) {
                return true;
            }
        }).build();

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setDefaultRequestConfig(config)
                .build();
    }

    public Optional<CovidData> getCountyCovidInfo(String county, Boolean yesterday) {
        String url = baseUrl + county;
        try {
            CovidData covidData = Unirest.get(url)
                    .header("Content-Type", "application/json")
                    .queryString("yesterday", yesterday != null && yesterday)
                    .asObject(CovidData.class)
                    .getBody();
            return Optional.of(covidData);
        } catch (UnirestException e) {
            logger.error("Error in call COVID service url = {}", url, e);
            return Optional.empty();
        }
    }
}