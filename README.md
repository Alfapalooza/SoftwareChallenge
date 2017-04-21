# Software Developer Challenge

## Challenge 1: 
Read through the rest of the assignment, decide which technologies to use and explain why you chose what you chose. As a reminder we at Paytm are dealing with millions of concurrent users, just sayin’ :)

_I chose the Play framework as the underlying web framework because of how fluid it is in respect to high traffic. The non-blocking IO aspect of the framework backed by Akka (do I need to say more?) really makes this framework standout in 
comparison to other non-blocking web frameworks (thinking of nodejs, and other java based frameworks that use netty under the hood). I do love nodejs for rapid prototyping, but Scala takes the cake for long-term sustainable projects 
(even if javascript can mimic the functional / type-safety of Scala with immutablejs & typescript). AkkaHttp was also an option, but I'm more comfortable with the Play framework._

## Challenge 2: 
Create a deployable “Hello World” Server exposing simple REST “Hello World” API. It is going to be a base for your application for this assignment.

## Challenge 3:
Pick one of the available online API’s such as Twitter (https://dev.twitter.com/overview/api), LCBO (https://lcboapi.com/) or Weather (https://openweathermap.org/api), create and implement a flow involving that API and user of your application. For example, your application might have following UI:

![Sample UI](./sample-ui.png?raw=true "Sample UI")


User will insert text and click the button `Search` and your application will search tweets that contain submitted string. Note: it is only an example, go wilder than that :)

_Implemented a very crude sentiment analysis using the 'Stanford NLP' library. By searching on 'Noodle', we'll fetch the top tweets, run sentiment analysis on each, and then run sentiment analysis on the tweets bunched together to get 
a general consensus on sentiments. All of this is served up in an interface similar to Google's search interface_

## Challenge 4:
Make your application secure and personalized by making people to have to sign-up / login. Bonus points, if users will be able to reset their passwords.

_Used jwt to secure the endpoints with a custom action. If I had more time I would have implemented the reset password._

## Challenge 5:
Make your application persistent. Whatever functionality your application has, after restart, make it possible to view a history of user activity or anything else you deem necessary.

_Redis isn't idea to persist users lol, if I had more time to work on this I would have integrated Cassandra as the persistent data store._

## Challenge 6:
Test your application.

_There's still a few things left to figure out with Guice and how DI plays in to tests, such as the 'Crypto' class requiring a running Play application. Spinning up a Play application is relatively easy (compared to not, and creating the injector yourself),
the reason this doesn't work is because we override 'WSClient' with our own implementation for test purposes, to achieve that our modules bundles requires some assisted injection (so a totally different type than what's injected in the controllers.)_

## Challenge 7:
Let us know how we can use it. You could either provide us with a zipped file containing your solution or a link to your Github repository containing one.

_I would have implemented a docker container for the project, again if I had more time. I realize Play has a built in mechanism to dockerize itself, but would need to find a way to include
the redis server._

## Installation
    1. Install 'Redis' (3.2.5 at time of writing): http://redis.io
        * For Win, follow the graphical installer, use the recommended settings. You can also install a graphical clients for interacting with the cache: https://redisdesktop.com/ (0.8.8 at time of writing)
        * For Mac / Linux, you'll need to build from sources, instructions here: http://redis.io/download#installation
    2. Install 'Java 8 JDK' (8u111 at time of writing): http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
        * The installer is a straight forward installer for Mac / Win / Linux, use the recommended settings.
    3. Install 'sbt': http://www.scala-sbt.org/download.html
  
## Setting up and launching the application
    1. Start by cloning the application by opening a console
        * Windows, press the 'Windows' key, type 'cmd', hit 'Enter'
        * Mac, press the 'Command' + 'Space' key, type 'terminal', hit 'Enter'
    2. Navigate to your home folder
        * Windows, type 'cd C:/Users/<user_name>/', hit 'Enter'
        * Mac, type 'cd ~', hit 'Return'
    3. Clone the project
        * Windows / Mac, type 'git https://github.com/Alfapalooza/SoftwareChallenge.git'
    4. Navigate to project folder
        * Windows / Mac, 'cd SoftwareChallenge'
    5. Run the application *(this process can take a while on first launch, may need to restart the process on dependency download
        * Windows / Mac, 'activator run' or 'sbt run'
        * Navigate to 'localhost:9000' in a browser

## Bonus (optional):
Add an “I’m feeling lucky button” that does a random search, but make sure that same result is not returned twice or that you don’t return a page that the user already viewed. Use the user stored history to do so. Since going through the history can potentially be costly, suggest and optionally implement optimization mechanism to avoid hitting the storage every time.

_I implemented this, minus the optional skip out on the round trip to the database to figure out which search results to omit. The way my 'I'm feeling lucky' button works, it fetches the trending hashtag, shuffles them and picks one at random to run sentiment analysis against, provided that hashtag doesn't conflict with the user's previous searches. I would have used
a cache to save on a trip to the data store I suppose, but my data store is already a cache._

