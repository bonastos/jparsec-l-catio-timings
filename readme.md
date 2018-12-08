# Measuring the Overhead of a potential Jparsec LOCATION parser

*Caveat*: I've discovered  that using a walltimer timer (aka Sytem.nanotime) for performance 
measurements gives strange/unreliable results. (Uncomment the additional measurements in 
LocationTimer.main to see how bad it is ...)

So I decided to use a profiler for measuring. Jprofiler gave consistent, reproducible result.

I measured 4 scenarios. Each has a named function, so I can extract the times from the calltree.

 .1 Base Case : Simple Parser<Strin>,many()
 .2 sequence(INDEX, Parser<Strin>),many()
 .3 sequence(LOCATION, Parser<Strin>),many()
 .4 LOCATION_EOF.next(sequence(LOCATION, Parser<Strin>),many())
 
 LOCATION_EOF is a parser that returns the location of EOF. Thus all following locations will
 be looked up in the cache.

scenario | t[ms] | t-t_base[ms] | t/t_base{%]

base |1,080	|| 
base + index | 1.230 | 0.150 | 113,9
base + location | 1.251	0,171 | 115.8
base + location_p | 1.635 | 0.555 | 151.4

My conclusions:

For the forward parsing case without backtracking the overhead is negligible, For a complete program, 
there will likely be no overhead, as the work of recovering line/offset from an index has to be roughly 
equivalent the work now done in the parser.

The backtracking case is more complicated . Clearly there is a potential for a noticeable overhead. 
But how much depends on the specific grammar and how much backtracking is invoked by the input.

Anecdotal Evidence: My application (no backtracking) shows no performace degradation when replacing 
INDEX with LOCATION. The resulting code is just much cleaner.
   