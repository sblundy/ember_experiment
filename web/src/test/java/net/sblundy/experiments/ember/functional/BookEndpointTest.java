package net.sblundy.experiments.ember.functional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStreamReader;

/**
 */
public class BookEndpointTest {
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
        HttpPost addAuthor = new HttpPost("/data/author");
        JSONObject authorDef = new JSONObject();
        authorDef.put("fullName", RandomStringUtils.randomAlphabetic(16));
        JSONObject bookDef = new JSONObject();
        bookDef.put("name", RandomStringUtils.randomAlphabetic(12));
        bookDef.put("authorName", authorDef.get("fullName"));
        addAuthor.setEntity(new StringEntity(authorDef.toJSONString(), ContentType.APPLICATION_JSON));
        try ( CloseableHttpResponse res = this.client.execute(host, addAuthor) ) {
            Assert.assertEquals(res.getStatusLine().getStatusCode(), HttpStatus.CREATED.value(), res.getStatusLine().getReasonPhrase());
        }
        HttpPost request = new HttpPost("/data/book");
        request.setEntity(new StringEntity(bookDef.toJSONString(), ContentType.APPLICATION_JSON));

        String bookLocation;
        try ( CloseableHttpResponse res = this.client.execute(host, request)) {
            Assert.assertEquals(res.getStatusLine().getStatusCode(), HttpStatus.CREATED.value(), res.getStatusLine().getReasonPhrase());
            Header location = res.getFirstHeader(HttpHeaders.LOCATION);
            Assert.assertNotNull(location);
            bookLocation = location.getValue();
        }

        try (CloseableHttpResponse res = this.client.execute(new HttpGet(bookLocation))) {
            Assert.assertEquals(res.getStatusLine().getStatusCode(), HttpStatus.OK.value(), res.getStatusLine().getReasonPhrase());
            JSONObject actualBook = (JSONObject) new JSONParser().parse(new InputStreamReader(res.getEntity().getContent()));
            Assert.assertNotNull(actualBook);
            Assert.assertEquals(actualBook.get("name"), bookDef.get("name"));
            Assert.assertEquals(actualBook.get("authorName"), bookDef.get("authorName"));
        }
    }
}
