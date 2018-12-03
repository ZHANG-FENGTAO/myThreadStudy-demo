package com.example.demo.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @author zft
 * @date 2018/12/3.
 */
public class HelloMainSimple {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Hello", ConfigFactory.load("samplehello.conf"));
        ActorRef helloWorld = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
        System.out.println("HelloWorld Actor Path:" + helloWorld.path());
    }
}
