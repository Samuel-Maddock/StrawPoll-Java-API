package com.samuelmaddock.strawpollwrapper;

/**
 * Created by Samuel-Maddock on 09/08/2017.
 */
public class Example {
    public static void main(String[] args) {
        StrawPoll strawPoll = new StrawPoll("Test title", "Option 1", "Option 2");
        strawPoll.setDupCheck(DupCheckType.DISABLED);
        strawPoll.create();
        System.out.println(strawPoll.toRawJSON());  //Raw JSON of the created poll
        System.out.println(strawPoll.getPollURL()); //URL of created poll

        StrawPoll myPoll = new StrawPoll();

        myPoll = myPoll.retrieve(1);
        System.out.println(myPoll.toRawJSON());

        myPoll = myPoll.retrieve("https://www.strawpoll.me/1"); //Same as using retrieve(1)
        System.out.println(myPoll); // toString() calls toRawJSON()

        System.out.println(" ");
        System.out.println("You have 50 seconds to vote on the poll via the below URl to see the update() method work!");
        System.out.println(strawPoll.getPollURL()); //URL of created poll

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        strawPoll.update();
        System.out.println(strawPoll);

        // Builder Pattern
        StrawPoll strawPoll2 = new StrawPoll();
        strawPoll2
                .setTitle("This is my poll")
                .addOptions("Option 1", "Option 2")
                .setIsMulti(false)
                .setHasCaptcha(true)
                .setDupCheck(DupCheckType.NORMAL)
                .create();


    }
}
