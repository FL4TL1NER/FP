package com.lab3_2

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import scala.util.Random

object ActorServer:
    case class Message(a: Int, b: Int, replyTo: ActorRef[Int])
    def apply(): Behavior[Message] = Behaviors.receive { (context, message) =>
        print(message.a); print(' '); print(message.b); print('\n')
        message.replyTo ! message.a + message.b
        Behaviors.same
    }

object ActorClient:
    def apply(serverRef: ActorRef[ActorServer.Message]): Behavior[Int] = Behaviors.setup { (context) =>
        val server = serverRef
        Behaviors.receiveMessage { (message) =>
            print(message); print('\n')
            Thread.sleep(1000)
            server ! ActorServer.Message(Random.between(0,1000),Random.between(0,1000),context.self)
            Behaviors.same
        }
    }

object MyActorSystem:
    case class Message()
    def apply(): Behavior[Message] = Behaviors.setup{ (context) =>
        val server = context.spawn(ActorServer(),"server")
        val client = context.spawn(ActorClient(server),"client")
        client ! 0
        
        Behaviors.empty
    }
@main def main(): Unit = {
    val actorSystem: ActorSystem[MyActorSystem.Message] = ActorSystem(MyActorSystem(), "actorSystem")
}
