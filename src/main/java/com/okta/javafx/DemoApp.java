package com.okta.javafx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.microsoft.alm.oauth2.useragent.AuthorizationException;
import com.microsoft.alm.oauth2.useragent.AuthorizationResponse;
import com.microsoft.alm.oauth2.useragent.UserAgent;
import com.microsoft.alm.oauth2.useragent.UserAgentImpl;
import javafx.scene.text.Text;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.http.util.EntityUtils;

public class DemoApp extends Application {

    static String OktaUrl = "dev-533919.oktapreview.com";
    static String ClientID = "0oam11dpwomy8VSmT0h7";
    static String ClientSecret = "RgzoAqNjWsGV_SshG9ne4u-hlpUgO07hY9DWBV1E";
    static String RedirectUri = "http://localhost:8080/authorization-code/callback";
    static String Scope = "profile email openid";
    static String GrantType = "authorization_code";

    public static URI getAuthorizationEndpointUri() throws URISyntaxException, MalformedURLException {
        
        URIBuilder builder = new URIBuilder();

        builder.setScheme("https");
        builder.setHost("dev-533919.oktapreview.com");
        builder.setPath("/oauth2/default/v1/authorize");
        builder.addParameter("client_id", "0oam11dpwomy8VSmT0h7");
        builder.addParameter("redirect_uri", "http://localhost:8080/authorization-code/callback");
        builder.addParameter("response_type", "code");
        builder.addParameter("state", "this is a state");
        builder.addParameter("scope", "profile email openid");

        URL url = builder.build().toURL();
        URI authorizationEndpoint = url.toURI();

        return authorizationEndpoint;
        
    }

    public String requestAuthCode() throws MalformedURLException, URISyntaxException, AuthorizationException {

        // Generate the auth endpoint URI to request the auth code

        URI authorizationEndpoint = DemoApp.getAuthorizationEndpointUri();

        System.out.print("Authorization Endpoint URI: ");
        System.out.println(authorizationEndpoint.toString());

        final URI redirectUri = new URI(RedirectUri);

        // Create the user agent and make the call to the auth endpoint

        final UserAgent userAgent = new UserAgentImpl();

        final AuthorizationResponse authorizationResponse = userAgent.requestAuthorizationCode(authorizationEndpoint, redirectUri);

        // We should have the code, which we can trade for the token

        final String code = authorizationResponse.getCode();

        System.out.print("Authorization Code: ");
        System.out.println(code);

        return code;
        
    }

    public String getTokenForCode(String code) throws URISyntaxException, IOException {
        
        final String tokenUrl = "https://"+OktaUrl+"/oauth2/default/v1/token";

        final URI redirectUri = new URI(RedirectUri);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(tokenUrl);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("grant_type", GrantType));
        urlParameters.add(new BasicNameValuePair("code", code));
        urlParameters.add(new BasicNameValuePair("redirect_uri", redirectUri.toString()));
        urlParameters.add(new BasicNameValuePair("client_id", ClientID));
        urlParameters.add(new BasicNameValuePair("client_secret", ClientSecret));
        urlParameters.add(new BasicNameValuePair("scope", Scope));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        String content = EntityUtils.toString(response.getEntity());

        System.out.println("Result : " + content.toString());

        return content.toString();
    }

    public static String prettyPrintJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    public static void main(String[] args) throws AuthorizationException, URISyntaxException  {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {

            String code = requestAuthCode();

            String token = getTokenForCode(code);
            
            primaryStage.setTitle("JavaFX Okta OAuth");

            Text tokenText = new Text();
            tokenText.setText("Your token: \n" + prettyPrintJson(token));
            tokenText.setWrappingWidth(600);
            tokenText.setX(100);
            tokenText.setY(100);
            StackPane root = new StackPane();
            root.getChildren().add(tokenText);
            primaryStage.setScene(new Scene(root, 800, 800));
            primaryStage.show();


        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (AuthorizationException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

}