package ru.hh.http.emulator;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.hh.http.emulator.client.entity.HttpEntry;
import ru.hh.http.emulator.engine.CriteriaHttpEngine;
import ru.hh.http.emulator.engine.ScenarioEngine;
import ru.hh.http.emulator.exception.AmbiguousRulesException;
import ru.hh.http.emulator.exception.RuleNotFoundException;
import ru.hh.http.emulator.exception.ScenarioNotFoundException;
import ru.hh.http.emulator.utils.CharsetUtils;
import ru.hh.http.emulator.utils.HttpUtils;

@Controller
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequestMapping("/**")
public class RequestController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestController.class);

  @Autowired
  private ScenarioEngine scenarioEngine;

  @Autowired
  private CriteriaHttpEngine criteriaEngine;

  @RequestMapping(
    method =
    {
      RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.PATCH,
      RequestMethod.TRACE
    }
  )
  @ResponseBody
  public void process(final HttpServletRequest request, final HttpServletResponse response) throws AmbiguousRulesException, RuleNotFoundException,
    IOException, ScenarioNotFoundException {
    convertToHttpResponse(request, response, criteriaEngine.process(HttpUtils.convertToHttpEntries(request)));
  }

  public void convertToHttpResponse(final HttpServletRequest request, final HttpServletResponse response, final Collection<HttpEntry> entries)
    throws IOException, ScenarioNotFoundException {
    for (HttpEntry httpEntry : entries) {
      switch (httpEntry.getType()) {
        case BODY:
          response.getOutputStream().write(httpEntry.getValue().getBytes(CharsetUtils.UTF_8));
          break;
        case HEADER:
          response.addHeader(httpEntry.getKey(), httpEntry.getValue());
          break;
        case COOKIE:
          response.addCookie(new Cookie(httpEntry.getKey(), httpEntry.getValue()));
          break;
        case STATUS:
          response.setStatus(Integer.parseInt(httpEntry.getValue()));
          break;
        case SCENARIO:
          scenarioEngine.executeScenario(httpEntry.getValue(), request, response, entries);
        default:
          break;
      }
    }
  }

  @ExceptionHandler({ AmbiguousRulesException.class })
  @ResponseBody
  @ResponseStatus(HttpStatus.CONFLICT)
  public String badRequest(AmbiguousRulesException e) {
    LOGGER.warn(HttpStatus.CONFLICT.toString(), e);
    return e.getMessage();
  }

  @ExceptionHandler({ RuleNotFoundException.class, ScenarioNotFoundException.class })
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void notFound(RuleNotFoundException e) {
    LOGGER.warn(HttpStatus.NOT_FOUND.toString(), e);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String internalFail(Exception e) {
    LOGGER.warn(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e);
    return e.getMessage();
  }
}
