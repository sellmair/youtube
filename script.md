# Outline
# Title: ViewModels feel wrong

##  Intro
ViewModels feel _wrong_. I am sorry! I liked the idea so very much, back in 2018. Finally, after years of doing 
'freestyle' MVC, there was some guidance bringing structure into my codebase 
    !Note: loop back to 'freestyle' MVC

And I was trying to make it work ever since... I tried RX style input/output mapping... I tried actor models, but
it never felt quite right and now I am done. I am breaking up with ViewModels. I am sure it's about them, its me, 
but let me explain...

### Screenshot
    Show screenshot

I have seen this tweet and realized how annoyed I was by it. Not because of the tweet, its a great question, but
with the fact that **after all those years** such a simple question is still highly debated. There is something wrong
with how we have been thought about ViewModels. 

### Screenshot 2

Here was the reply from the fantastic Ian Lake: 
And I agree! While you will never hear me using the word 'anti-pattern',
Both options really do not feel right and might have some kind of drawbacks.
But... 

### Screenshot 3
In official documentation and samples, this approach is used heavily.


## What's wrong with ViewModels
So what is wrong with ViewModels?

### How to structure your code?
ViewModels cannot provide easy & satisfying answers, even for the most basic of questions: 
How to structure your code (the one thing I thought they will do): 

What do I mean by that? Clearly, ViewModels provide a separation between your View and whatever we call
'business' logic. Sure, but how should this be splitted?

One ViewModel per screen?
One ViewModel per UI component (whatever an UI component then should be?)

Having one ViewModel per UI component sounds nice in theory because it would allow 
for smaller independent peaces that we could '_compose_'. But how to connect those View-Models together?


One ViewModel per screen gets rid of the complicated question of 'how to connect parent -> child' viewmodels
But, there are so many more questions to be answered
1) Should a ViewModel contain one 'State' also presented as single 'StateFlow'
2) Should a ViewModel contain multiple 'State' flows?

Browsing through Google's examples and guides seemingly answers this with
"One State class"/"One StateFlow" in a single ViewModel. 

Now combining the fact that we would like to have one ViewModel for a screen and one State per ViewModel
kind of requires me to cramp in everything a Screen needs in a single State object. No matter how unrelated certain
parts might be?

    !Show some images in the background


Let's look at some code from the android/compose-samples repository to see what this will look like: 
This is the 'HomeViewModel' which is associated with the 'HomeScreen' of the 'Jetcaster' app. 
As you can see: While it contains several small private 'states', it exposes its overall State as one 
single 'HomeScreenUiState'. Just combining those smaller states into the exposed one requires 55 lines 
of code and even a special 'combine' utility function. I don't know about you, but this does not feel nice to me?

    And please! I am not saying this is bad code, by any means! The engineers that wrote those examples 
    have done a great job imho. My point is that the idea of 'ViewModels' just does not fit and requires 
    such code to be written.



You can see the trouble in the official examples already: 


// HERE
## Passing down, passing up
For now, let's just take accept the facts of live: Coding is hard, exposing your state requires some
coroutines flow code to be written, who really cares nowadays? We can put the entire screens logic into a single
class, use a dependency injection framework to create it and carry on with our live. 

But again we have some more issues to resolve: 
If we have this one state, representing our entire screen: 
How gets the state passed down and how shall we wire events back up?

The pragmatic and quick approach is: 
Just pass the viewModel down into your child composables, bro. 
And yes, this will lead to launching a working app quickly. 
But despite it being quite a well working approach it feels entirely wrong!!!
Now, every small UI Component has to know about the entire screen? re-using UI components is practically 
impossible (Lets say you want to re-use components in another screen?)
No surprise: This is something the community highly diregards.

The _right_ way is: "State Hoisting"
We will pass relevant peaces of state down and wire events back up, passing lambdas.
This makes our components re-usable, 

    > Look at code and be mad at it.

    > Points: 
    - Again, lots and lots of boilerplate 'wire up' code
    - This makes everything so hard to refactor, brittle and context dependent.




## Example: 'architecture samples'

Here is how this would look like: 
We're looking at android's 'architecture-sampels'. This is the 'TasksScreen.'

As you can see, it gets the 'TasksViewModel' passed by Hilt, the dependency injection framework. 
Events, such as the user selecting some kind of 'filter' will be wired up using this lambdas which get passed 
down the composable hierarchy.

The state gets passed down 'raw', from the TasksScreen to the TasksContent into 'leaf' composables 
such as this 'Loading Content'



This approach is promising: 
It seems to make components very easily re-usable and maybe even testable (if we right tests).
I cannot possibly have any further complaints here, right?

Damn, I told you its not about ViewModels its about me, but I have even two issues. 
The first one might be an obvious skill issue on my end, but look at how huge and ugly those Composable 
function signatures can become? Lets not even talk about libraries and compatibility issues. 

But my most pressing point: 
Have you ever tried refactoring such composable functions? 
Refactoring a leaf composable can easily end up with you re-touching your entire codebase.
And who's fault is it? The ViewModel.



Now, what's the point of me renting about ViewModels?
Ever since I started building Android Applications, I've remembered the community struggling to find good consens about
architecture and I know that this video will be my most disliked and disagreed one, yet.
(I hope it was still fun, though). 

I remember the first time I have written some code for iOS. 
Wait? What? You can just create a ViewController yourself, attach it to the window and this works? 
This moment was pivotal for me as engineer. Android's complex lifecycle (and a handful of dudes on reddit) have
scared us so much, that we do not even dare to 'think' about finding approaches that _we like_ and that 
work for us and our app. 

If you like viewmodels and they work well for you, your app, your team, then go for it! 
There is so much content on how to approach this and so many success stories, please feel encouraged.

If you are like me and its just not for you, dare to dream of approaches that *you* like.



For me: 
I won't go back to 'freestyle MVC'. Remember the advise from Ian Lake? Using cold flows?
I am doing 'freestyle MVI' now, which basically just defines independent Events and States 
All the boiler-plate, wire-up logic was extracted to a Library I call 'Evas' (Events and States). 
Let me show an example and encourage you to build something you can call 'your own'.

