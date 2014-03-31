package ru.hh.standalone.utils.oauth;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class OAuthUtils {
  public static String buildOAuthRedirrectURL(
      final String site, final String currentPage, final String system, final boolean mergeOAuth, final String code, final String error,
      final String error_description, String hhid_public_url) throws UnsupportedEncodingException {
    final String callbackPage = "http://" + site + "/oauth/callback";
    //TODO get from a file as a param http://hhid.cobalt.pyn.ru/
    final StringBuilder oAuthURL = new StringBuilder(hhid_public_url).append("oauth2/code?")
      .append("reg=")
      .append(URLEncoder.encode(callbackPage + "?reg=oauth2", "UTF-8"))
      .append('&')
      .append("fail=")
      .append(URLEncoder.encode(callbackPage, "UTF-8"))
      .append('&')
      .append("login=")
      .append(URLEncoder.encode(callbackPage + "?url=" + URLEncoder.encode(currentPage, "UTF-8"), "UTF-8"))
      .append('&')
      .append("system=")
      .append(system)
      .append('&')
      .append("mergeOAuth=")
      .append(mergeOAuth)
      .append('&')
      .append("code=")
      .append(code)
      .append('&')
      .append("error=")
      .append(error)
      .append('&')
      .append("error_description=")
      .append(error_description);

    return oAuthURL.toString();
  }

  public static String buildAccessTokenResponse(final String token, final String uid) {
    final StringBuilder sb = new StringBuilder("{\"access_token\": \"").append(token);

    if (uid != null) {
      sb.append("\", \"user_id\": ").append(uid);
    }

    sb.append("}");

    return sb.toString();
  }


}
