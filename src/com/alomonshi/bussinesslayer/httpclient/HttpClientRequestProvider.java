package com.alomonshi.bussinesslayer.httpclient;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;

public class HttpClientRequestProvider<Object> {

    private WebTarget webTarget;
    private Invocation.Builder invocationBuilder;

    public HttpClientRequestProvider(String targetURL) {
        Client client = ClientBuilder.newClient();
        webTarget = client.target(targetURL);
    }

    /**
     * Get mothod impeletation
     * @param localPath local path
     * @param mediaType media type
     * @return Response object
     */
    public Response get (String localPath, String mediaType) {
        this.getInvocationBuilder(localPath, mediaType);
        this.getInvocationBuilder(localPath, mediaType);
        return invocationBuilder.get();
    }

    /**
     * Post method
     * @param localPath local path
     * @param mediaType media type
     * @return response
     */
    public Response post (String localPath, String mediaType, Object object) {
        this.getInvocationBuilder(localPath, mediaType);
        this.getInvocationBuilder(localPath, mediaType);
        return invocationBuilder.post(Entity.entity(object, mediaType));
    }

    /**
     * Getting invocation from web target object
     * @param localPath intended local path
     * @param mediaType intended media type
     */
    private void getInvocationBuilder (String localPath, String mediaType) {
        invocationBuilder = webTarget.path(localPath).request(mediaType);
    }
}
