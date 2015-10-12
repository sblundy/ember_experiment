package net.sblundy.experiments.ember.functional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStreamReader;

/**
 */
public class AuthorEndpointTest {
    private CloseableHttpClient client;
    private HttpHost host;

    @BeforeMethod
    public void setUp() throws Exception {
        this.client = HttpClients.createDefault();
        this.host = new HttpHost("localhost", 8080);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        this.client.close();
    }


    @Test
    public void testAddAndQuery() throws Exception {
        HttpPost request = new HttpPost("/data/author");
        JSONObject authorDef = new JSONObject();
        authorDef.put("fullName", RandomStringUtils.randomAlphabetic(16));
        final StringEntity entity = new StringEntity(authorDef.toJSONString());
        entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setEntity(entity);
        String authorLocation;
        try ( CloseableHttpResponse res = this.client.execute(host, request) ) {
            Assert.assertEquals(res.getStatusLine().getStatusCode(), HttpStatus.CREATED.value(), res.getStatusLine().getReasonPhrase());
            Header locationHeader = res.getFirstHeader(HttpHeaders.LOCATION);
            Assert.assertNotNull(locationHeader);
            authorLocation = locationHeader.getValue();
        }

        HttpGet queryRequest = new HttpGet(authorLocation);
        try ( CloseableHttpResponse res = this.client.execute(queryRequest)) {
            Assert.assertEquals(res.getStatusLine().getStatusCode(), HttpStatus.OK.value(), res.getStatusLine().getReasonPhrase());
            JSONObject actualAuthor = (JSONObject) new JSONParser().parse(new InputStreamReader(res.getEntity().getContent()));
            Assert.assertNotNull(actualAuthor);
            Assert.assertEquals(actualAuthor.get("fullName"), authorDef.get("fullName"));
        }
    }
}
