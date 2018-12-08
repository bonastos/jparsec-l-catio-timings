package org.bonastos;

import static  org.jparsec.Extension.LOCATION;
import static  org.jparsec.Extension.LOCATION_EOF;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;


public class LocationTiming {
  
  public static Parser<String> ABCD = Scanners.string("abcd").source().followedBy(Scanners.WHITESPACES.skipMany());
  
  public static <T> Parser<WithIndex<T>> withIndex(Parser<T> p)
  {
    return Parsers.sequence(Parsers.INDEX, p, (i, v) -> new WithIndex<T>(i, v) );
  }
  
  public static <T> Parser<WithLocation<T>> withLocation(Parser<T> p)
  {
    return Parsers.sequence(LOCATION, p, (l, v) -> new WithLocation<T>(l, v) );
  }
 
  public static void main(String[] args)
  {  
    String source = createSource(50_000, 10); 

    measureBase(ABCD, source);
    measureWithIndex(ABCD, source);
    measureWithLocation(ABCD, source);
    measureWithLocationP(ABCD, source);
   
//    measureBase(ABCD, source);
//    measureWithIndex(ABCD, source);
//    measureWithLocation(ABCD, source);
//    
//    measureBase(ABCD, source);
//    measureWithIndex(ABCD, source);
//    measureWithLocation(ABCD, source);   
  }
  
  static <T> double measureBase(Parser<T> p, CharSequence source)
  {
    return measure_parser("base      ", p.many(), source);
  }
  
  static <T> double measureWithIndex(Parser<T> p, CharSequence source)
  {
    return measure_parser("+index    ", withIndex(p).many(), source);
  }
  
  static <T> double measureWithLocation(Parser<T> p, CharSequence source)
  {
    return measure_parser("+location ", withLocation(p).many(), source);
  }
  
  static <T> double measureWithLocationP(Parser<T> p, CharSequence source)
  {
    return measure_parser("+locationP", LOCATION_EOF.next(withLocation(p).many()), source);
  }
  
 static <T> double measure_parser(CharSequence context, Parser<List<T>> p, CharSequence source)
  {
    long startTime = System.nanoTime();
    List<T> result = p.parse( source );
    long endTime = System.nanoTime();
    long elapsedTime = endTime - startTime;
    double millis = elapsedTime / 1_000_000.0;
    System.out.format( "%s: %.2f ms (%s)\n", context, millis, result.size());
    return millis;
  }

 static String createSource(int n, int m)
  {
    String line = StringUtils.repeat("abcd ", m);
    return StringUtils.repeat(line + "\n", n / m);
  }
}
