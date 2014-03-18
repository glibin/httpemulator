package ru.hh.standalone.utils.oauth;

import java.util.UUID;

public class UniqueIdentifierUtils {
  public static final String OAUTH_REQUEST_ID_COOKIE = "OAUTH-REQUEST-ID";

  public static String generateRequestId(final Class<?> testClass, final String testMethod) {
    return testClass.getName() + '_' + testMethod + "_reqId" + UUID.randomUUID();
  }

  public static String generateOAuthCode(final Class<?> testClass, final String testMethod) {
    return testClass.getName() + '_' + testMethod + "_code" + UUID.randomUUID();
  }

  public static String generateOAuthToken(final Class<?> testClass, final String testMethod) {
    return testClass.getName() + '_' + testMethod + "_token" + UUID.randomUUID();
  }

  public static String generateSocialUserId(final Class<?> testClass) {
    return String.valueOf(testClass.hashCode());
  }
}
