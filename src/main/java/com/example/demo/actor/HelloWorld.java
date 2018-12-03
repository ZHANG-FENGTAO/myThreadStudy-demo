package com.example.demo.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

/**
 * @author zft
 * @date 2018/12/3.
 */
public class HelloWorld extends UntypedAbstractActor {

    ActorRef greeter;

    @Override
    public void preStart() {
        greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        System.out.println("Greeter Actor Path:" + greeter.path());
        greeter.tell(Greeter.Msg.GREET, getSelf());
    }

    @Override
    public void onReceive(Object message) {
        if (message == Greeter.Msg.DONE) {
            greeter.tell(Greeter.Msg.GREET, getSelf());
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }
}
