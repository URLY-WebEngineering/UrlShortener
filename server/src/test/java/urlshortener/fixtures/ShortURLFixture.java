package urlshortener.fixtures;

import urlshortener.domain.ShortURL;

public class ShortURLFixture {

  public static ShortURL url1() {
    return new ShortURL(
        "1",
        "http://www.unizar.es/",
        null,
        null,
        null,
        null,
        null,
        false,
        null,
        null,
        null,
        false,
        false);
  }

  public static ShortURL url1modified() {
    return new ShortURL(
        "1",
        "http://www.unizar.org/",
        null,
        null,
        null,
        null,
        null,
        false,
        null,
        null,
        null,
        false,
        false);
  }

  public static ShortURL url1Custom(String custombackhalf) {
    return new ShortURL(
        custombackhalf,
        "http://www.unizar.es/",
        null,
        null,
        null,
        null,
        null,
        false,
        null,
        null,
        null,
        false,
        false);
  }

  public static ShortURL url2() {
    return new ShortURL(
        "2",
        "http://www.unizar.es/",
        null,
        null,
        null,
        null,
        null,
        false,
        null,
        null,
        null,
        false,
        false);
  }

  public static ShortURL url3() {
    return new ShortURL(
        "3",
        "http://www.google.es/",
        null,
        null,
        null,
        null,
        null,
        false,
        null,
        null,
        null,
        false,
        false);
  }

  public static ShortURL badUrl() {
    return new ShortURL(
        null, null, null, null, null, null, null, false, null, null, null, false, false);
  }

  public static ShortURL urlSponsor() {
    return new ShortURL(
        "3", null, null, "sponsor", null, null, null, false, null, null, null, false, false);
  }

  public static ShortURL urlSafe() {
    return new ShortURL(
        "4", null, null, "sponsor", null, null, null, true, null, null, null, false, false);
  }

  public static ShortURL someUrl() {
    return new ShortURL(
        "someKey",
        "http://example.com/",
        null,
        null,
        null,
        null,
        307,
        true,
        null,
        null,
        null,
        false,
        false);
  }

  public static ShortURL someOKUrl() {
    return new ShortURL(
        "someKey",
        "http://example.com/",
        null,
        null,
        null,
        null,
        307,
        true,
        null,
        null,
        null,
        true,
        true);
  }

  public static ShortURL urlIp() {
    return new ShortURL(
        "someKey",
        "http://example.com/",
        null,
        null,
        null,
        null,
        307,
        true,
        "192.168.125.123",
        "Sweden",
        null,
        true,
        true);
  }
}
