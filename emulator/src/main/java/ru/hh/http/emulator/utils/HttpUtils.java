package ru.hh.http.emulator.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import ru.hh.http.emulator.entity.AttributeType;
import ru.hh.http.emulator.entity.HttpEntry;

public class HttpUtils {
  public static Collection<HttpEntry> convertToHttpEntries(final HttpServletRequest request) {
    final Collection<HttpEntry> entries = new ArrayList<HttpEntry>();

    entries.add(new HttpEntry(AttributeType.PATH, null, request.getPathInfo()));

    for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements();) {
      final String headerName = headerNames.nextElement();

      for (Enumeration<String> headerValues = request.getHeaders(headerName); headerValues.hasMoreElements();) {
        entries.add(new HttpEntry(AttributeType.HEADER, headerName, headerValues.nextElement()));
      }
    }

    for (Cookie cookie : request.getCookies()) {
      entries.add(new HttpEntry(AttributeType.COOKIE, cookie.getName(), cookie.getValue()));
    }

    entries.add(new HttpEntry(AttributeType.METHOD, null, request.getMethod()));

    for (Map.Entry<String, String[]> parameters : request.getParameterMap().entrySet()) {
      for (String paramValue : parameters.getValue()) {
        entries.add(new HttpEntry(AttributeType.PARAMETER, parameters.getKey(), paramValue));
      }
    }

    entries.add(new HttpEntry(AttributeType.PROTOCOL, null, request.getProtocol()));

    return entries;
  }
}
