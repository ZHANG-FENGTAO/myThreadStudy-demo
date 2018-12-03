package com.example.demo.actor;

import akka.actor.UntypedAbstractActor;

/**
 * @author zft
 * @date 2018/12/3.
 */
public class Greeter extends UntypedAbstractActor {

    public static enum Msg {
        GREET, DONE;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.GREET) {
            System.out.println("Hello World!");
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(message);
        }
    }
}
