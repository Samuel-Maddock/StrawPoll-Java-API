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

        myPoll = myPoll.retrieve("http://www.strawpoll.me/1"); //Same as using retrieve(1)
        System.out.println(myPoll.toRawJSON());
    }
}
