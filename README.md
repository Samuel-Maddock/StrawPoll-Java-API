# StrawPoll-Java-API
A Lightweight Java API Wrapper for [StrawPoll.me](http://www.strawpoll.me)

## Getting Started

### Prerequisites

In order to use this API wrapper, it depends on the [GSON Library](https://github.com/google/gson) for JSON manipulation. If you are using Maven to build
your application you can add this dependency by adding the following to your pom.xml

```
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.1</version>
</dependency>
```

### Installing

TODO

## Examples

### Quickly Creating a StrawPoll:

Creating a StrawPoll is very simple. There are multiple ways to do this. 


The easiest way is to pass the StrawPoll object all of its data at construction. You can quickly create a StrawPoll by passing it a title (the question of the poll) and a list of options that users will vote for. 

Once the data has been added to the poll, you need to call the ```create()``` method to send a request to the StrawPoll API to create the poll.

```java
StrawPoll strawPoll = new StrawPoll("StrawPoll Title", "Option 1", "Option 2");
strawPoll.create(); //Creates the StrawPoll, by sending a request to the API
```
After the StrawPoll has been created, the StrawPoll object will have been updated with the poll's id, the url of the poll and the votes of the poll.

```java
strawPoll.getId(); //Will retrieve the id of the poll.
strawPoll.getPollURL(); //Will return the full string url of the poll that has been created. You can view this poll in your browser.
strawPoll.getVotes(); //Returns a List<Integer> of the votes for each option. Obviously after creation these are all 0.
```
```NOTE: In order to create a StrawPoll, you must include a title and at least one option.```

The StrawPoll constructor also allows for more freedom. You can pass a ```List<String>``` of options instead. 
```java
List<String> options = new ArrayList<>();
options.add("Option 1");
options.add("Option 2");
StrawPoll strawPoll = new StrawPoll("This is my Poll", options);
strawPoll.create();
```
```NOTE: The StrawPoll API has a limitation on the number of options that it can use. There is a maximum of 30 options per poll.```

If you wish for the poll to allow multiple votes from the same user or to have a captcha to verify that users are actually humans you can use the full constructor to create a poll.

The format of the full constructor is as follows:
```java
StrawPoll strawPoll = new StrawPoll(title, options, isMulti, hasCaptcha, DupCheck)
```
Constructor Parameters:

* ```title``` - The title of the  StrawPoll
* ```options``` - The list of options as a ```List<String>```
* ```isMulti``` - A boolean that determines if a user is allowed to vote multiple times. If true, then the same person can vote multiple times. This is false by default.
* ```hasCaptcha``` - A boolean that determines if there is a captcha to verify if a user is human. True enables the captcha. This is false by default.
* ```DupCheck``` - This is an enum of DupCheckType that determines the type of duplication checking to be used. This is ```DupCheckType.NORMAL``` by default

```DupCheck``` has three different values:
* ```DupCheckType.NORMAL``` - This is the normal duplication checking. This is enabled by default
* ```DupCheckType.PERMISSIVE``` - This makes the poll more lenient in vote duplication checking.
* ```DupCheckType.DISABLED``` - This disables duplication checking for the poll.

You can also easily clone StrawPoll objects by doing the following:
```java
StrawPoll strawPoll = new StrawPoll("Title", "Option1", "Option2");
StrawPoll pollCopy = new StrawPoll(strawPoll);
```

### Updating and Retrieving a StrawPoll

If you want to retrieve data from a StrawPoll with a known URL or Poll ID then you can call the ```retrieve()``` method.

```java
StrawPoll strawPoll = new StrawPoll(1); //Retrieves strawpoll with id 1

StrawPoll myPoll = new StrawPoll("www.strawpoll.me/1"); //Retrieves strawpoll with id 1
```

You can then retrieve information about the poll using it's getters:
```java
strawPoll.getId(); //String id of the poll
strawPoll.getTitle(); //String title of the poll
strawPoll.getOptions(); //List<String> of options
strawPoll.getVotes(); //List<Integer> of votes
strawPoll.isMulti(); //Boolean
strawPoll.hasCaptcha(); //Boolean
strawPoll.getDupCheck(); //Enum of type DupCheckType
strawPoll.getPollURL(); //URL of the StrawPoll
```

If you want to update a StrawPoll's information (eg the number of votes) then simply call the ```update()``` method.

```java
StrawPoll myPoll = new StrawPoll("Is StrawPoll good?", "Yes", "No");
myPoll.create(); //Votes are [0,0]
// Someone votes on the poll eg two people vote yes, one votes no
myPoll.update();
myPoll.getVotes(); //Votes are [2,1]
```

### Using Setters to Create a Poll

Most StrawPoll fields have setters which you can use instead of the constructor. 
All of these methods support method chaining which allow us to create a poll like this:

```java
StrawPoll strawPoll = new StrawPoll();
strawPoll
    .setTitle("This is my poll")
    .addOptions("Option 1", "Option 2");
    .setIsMulti(false);
    .setHasCaptcha(true);
    .setDupCheck(DupCheckType.NORMAL)
    .create();
```
When adding options we have two different methods:
```java
List<String> options = Contains some options...
strawPoll.addOptions("Option 1", "Option 2"); //Add these to the current options already added.
strawPoll.addOptions(options.toArray()) //Used for adding a List<String> to the current options list
strawPoll.setOptions(options); //Replace the current list with a new list
```

Note that you do not need to set every field. You only need to set the required fields of a title and an option. Everything else will be set to a default value.

### More Information
The StrawPoll API itself has a rate limit of creating 100 poll by any given user within 60 minute. To view more information visit the [StrawPoll API Wiki](https://github.com/strawpoll/strawpoll/wiki/API)

You can also view the raw JSON of any StrawPoll object. This could be one that you have updated/retrieved or one that you have just created. Some examples are shown below:
```java
String rawJSON = strawPoll.toRawJSON();
```
## Built With

* [GSON](http://www.dropwizard.io/1.0.2/docs/) - JSON Manipulation Library
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

If you feel like this API could be improved in any way, open an issue or make a pull request!

## Authors

* **Samuel Maddock -** [Github Page](https://github.com/Samuel-Maddock)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
