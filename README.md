# OIDC Authentication With JavaFX and Okta

**Requires** Oracle's Java 8

This example shows how to use OIDC authentication in a JavaFX application. Please read [How to Build a JavaFX Desktop App with OIDC Authentication](https://developer.okta.com/blog/2019/08/14/javafx-tutorial-oauth2-oidc) to see how this example was created.

**Prerequisites:** [Java 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html/).

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example, run the following commands:

```bash
git clone https://github.com/oktadeveloper/javafx-oauth2-oidc-example.git
cd javafx-oauth2-oidc-example
```

### Create an Application in Okta

Log in to your Okta Developer account (or [sign up](https://developer.okta.com/signup/) if you don’t have an account).

1. From the **Applications** page, choose **Add Application**.
2. On the Create New Application page, select **Web**.
3. Give your app a memorable name (e.g., `JavaFX`), then click **Done**.

Copy your issuer (found under **API** > **Authorization Servers**), client ID, and client secret into `src/main/resources/app.properties` as follows:

```properties
oktaDomain={yourOktaDomain}
oktaClientId={yourClientId}
oktaClientSecret={yourClientSecret}
```

**NOTE:** The value of `{yourOktaDomain}` should be something like `dev-123456.okta.com`. Make sure you don't include `-admin` in the value!

After modifying this file, start the JavaFX app and you should be able to authenticate with Okta.

```
./gradlew build run
```

## Links

This example uses the following open source libraries:

* [Microsoft OAuth 2.0 User Agent library for Java](https://github.com/microsoft/oauth2-useragent) 

## Help

Please post any questions as comments on the [blog post](https://developer.okta.com/blog/2019/08/14/javafx-tutorial-oauth2-oidc), or on the [Okta Developer Forums](https://devforum.okta.com/).

## License

Apache 2.0, see [LICENSE](LICENSE).
