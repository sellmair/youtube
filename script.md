# Intro: Reasoning about performance is hard

"Nobody cares about performance", ... "
Those were the words from the great Volodia Dolzhenko, the Kotlin IntelliJ Plugin team lead when talking
about the Kotlin plugin's performance at Kotlin Conf 2023. Similar to me, right now, he said this very well expecting
plenty
of disagreements from the audience and yep: Nobody was happy to hear this from the Kotlin IntelliJ plugins
team leads mouth!!
However and quite unsurprisingly, he made a good point from there and followed up with "... until it bothers you".

## forEach vs for loop

Ever wondered what is going on when using 'forEach' to loop over all elements of given list instead of
just using a loop over all indices, getting the element? It allocates an Iterator, which, yes sure, provides us
with some safety, but has to check for 'Concurrent Modification' on each iteration.

Let's measure how much this additional safety costs us and when to use an optimized 'forEach' implementation.

    Code: 
    Let there be a list containing 100_000 elements; We can measure the time it takes to iterate over all of them
    using .forEach. Alternatively, we could measure how long it takes to iterate over those elements by just
    using a for loop with an index i. 

Let's run this and: Quite obviously! Our for loop is significantly faster! Damn, we should really use for loops more
often or consider implementing a 'fastForEach' for situations where performance matters more than the 
safety guarding against Concurrent modifications.

There is just one catch here: 
All of it is nonsense! And most if this is wrong or misleading at best. I picked exactly this example because
I have done this mistake, proposing potential performance benefits by creating a 'fastForEach' method.
Don't believe me?

    
    - Show the intellij suppress
    - Apply the fix
    - Same result

We have done one of many possible mistakes when measuring performance! 
There is a lot going on when launching a fresh JVM. Let's give the JIT some time to 'warm up' and optimize this
code before we start measuring this.

But still: The warm 'forEach' results in 1.6ms, but we have seen the for-loop performing significantly faster
at 1.2ms. I might have one or two more ideas why our initial measurements
showed these results: Maybe the JIT compiler optimized the empty loops? Maybe the .forEach call did some unboxing
of Int where the empty for loop did not? Getting performance measurements right is a science, which gets me 
to the main point of this video: 

Use a benchmarking framework!! We can use kotlinx.benchmark to do the heavy lifting