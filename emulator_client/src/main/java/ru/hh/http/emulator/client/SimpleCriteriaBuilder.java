package ru.hh.http.emulator.client;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.jetty.client.api.ContentResponse;
import ru.hh.http.emulator.client.entity.AttributeType;
import ru.hh.http.emulator.client.entity.HttpEntry;

public class SimpleCriteriaBuilder extends CriteriaBuilder {
  private final Collection<HttpEntry> result = new ArrayList<HttpEntry>();

  private HttpEntry rule;

  private final EmulatorClient client;

  public SimpleCriteriaBuilder(final EmulatorClient client) {
    this.client = client;
  }

  public SimpleCriteriaBuilder setRule(final AttributeType type, final String key, final String value) {
    rule = new HttpEntry(type, key, value);
    return this;
  }

  public SimpleCriteriaBuilder addResponseEntry(final AttributeType type, final String key, final String value) {
    result.add(new HttpEntry(type, key, value));
    return this;
  }

  public long save() throws Exception {
    final ContentResponse response = client.putSimple(rule, result);
    if (response.getStatus() == 200) {
      return Long.parseLong(response.getContentAsString());
    }

    throw new IllegalStateException("HTTP status code = " + response.getStatus());
  }
}
