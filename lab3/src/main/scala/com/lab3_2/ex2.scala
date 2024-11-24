package com.lab3_2

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.util.Random

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ActorServer:
    case class Message(a: Int, b: Int, replyTo: ActorRef[Int])
    def apply(): Behavior[Message] = Behaviors.receive { (context, message) =>
        context.log.info(message.a.toString() + ' ' + message.b.toString() + '\n')
        message.replyTo ! message.a + message.b
        Behaviors.same
    }

object ActorClient:
    def apply(serverRef: ActorRef[ActorServer.Message]): Behavior[Int] = Behaviors.setup { (context) =>
        val server = serverRef
        val senderFuture: Future[Unit] = Future {
            while (true) {
                server ! ActorServer.Message(Random.between(0,1000),Random.between(0,1000),context.self)
                Thread.sleep(1000)
            }
        }
        Behaviors.receiveMessage { (message) =>
            context.log.info(message.toString() + '\n')
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
