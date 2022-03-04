package org.h0110w.somclient.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.h0110w.somclient.exception.ServiceException;
import org.h0110w.somclient.service.dto.AuthToken;
import org.h0110w.somclient.service.dto.ErrorResponse;
import org.h0110w.somclient.utils.mapper.CustomObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

@Slf4j
public class Client {
    private static final String CONTENT_JSON = "application/json";
    private static Client customClient;
    private AuthToken token = null;
    private final HttpClient httpClient;


    private Client() {
        this.httpClient = HttpClientBuilder.create().build();
    }

    public static Client getCustomClient() {
        if (customClient == null) {
            customClient = new Client();
        }
        return customClient;
    }

    public <T> T get(String url, Class<T> responseObjectClass) throws IOException {
        HttpGet httpGet = new HttpGet(WebConfig.URL + url);
        return execute(httpGet, responseObjectClass);
    }

    public <T> T post(String url, Object payload, Class<T> responseObjectClass) throws IOException {
        HttpPost httpPost = new HttpPost(WebConfig.URL + url);
        httpPost.setEntity(convertObjectToHttpEntity(payload));
        httpPost.setHeader("Accept", CONTENT_JSON);
        httpPost.setHeader("Content-type", CONTENT_JSON);

        return execute(httpPost, responseObjectClass);
    }

    private <T> T execute(HttpRequestBase request, Class<T> responseObjectClass) throws IOException {
        if (token != null) {
            request.setHeader("Authorization", "Bearer " + token.getAccessToken());
        }
        return httpClient.execute(request, getResponseHandler(responseObjectClass));
    }


    private HttpEntity convertObjectToHttpEntity(Object payload) throws JsonProcessingException, UnsupportedEncodingException {
        String json = CustomObjectMapper.getMapper().writeValueAsString(payload);
        return new StringEntity(json);
    }

    private static <T> ResponseHandler<T> getResponseHandler(Class<T> c) {
        return response -> {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300) {
                ErrorResponse errorResponse=null;
                if (entity != null) {
                    Reader reader = new InputStreamReader(entity.getContent());
                    errorResponse = CustomObjectMapper.getMapper().readValue(reader,
                            ErrorResponse.class);
                }
                log.error(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                throw new ServiceException(errorResponse.toString());
            }
            if (entity == null) {
                throw new ServiceException("Response contains no content");
            }
            Reader reader = new InputStreamReader(entity.getContent());

            return CustomObjectMapper.getMapper().readValue(reader, c);
        };
    }

    public void setToken(AuthToken token) {
        this.token = token;
    }
}
