package org.jparsec;

import org.jparsec.error.Location;

public class Extension {

  public static final Parser<Location> LOCATION = new Parser<Location>() {
    @Override boolean apply(final ParseContext ctxt) {
      ctxt.result = ctxt.locator.locate(ctxt.getIndex());
      return true;
    }
    @Override public String toString() {
      return "getLocation";
    }
  };
  
  public static final Parser<Location> LOCATION_EOF = new Parser<Location>() {
  @Override boolean apply(final ParseContext ctxt) {
    ctxt.result = ctxt.locator.locate(ctxt.source.length());
    return true;
  }
  @Override public String toString() {
    return "getLocation";
  }
};

}
