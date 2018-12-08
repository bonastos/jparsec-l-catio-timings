package org.bonastos;

import org.jparsec.error.Location;

public class WithLocation<T> {
  public final Location location;
  public final T value;

  public WithLocation(Location location, T value) {
    super();
    this.location = location;
    this.value = value;
  }

}
