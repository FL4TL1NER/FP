//#full-example
package com.example


import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.GreeterMain.SayHello

//#greeter-actor
object Greeter {
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])//case class странное название для структуры
  final case class Greeted(whom: String, from: ActorRef[Greet])

  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>// функция выполняющяся при получении сообщения
    context.log.info("Hello {}!", message.whom)
    //#greeter-send-messages
    message.replyTo ! Greeted(message.whom, context.self)// отправка сообщения Greeted по replyTo
    //#greeter-send-messages
    Behaviors.same//возвращает сам себя
  }
}
//#greeter-actor

//#greeter-bot
object GreeterBot {

  def apply(max: Int): Behavior[Greeter.Greeted] = {
    bot(0, max)
  }

  private def bot(greetingCounter: Int, max: Int): Behavior[Greeter.Greeted] =//фабричный метод, сделан для рекурсии на 41
    Behaviors.receive { (context, message) =>
      val n = greetingCounter + 1
      context.log.info("Greeting {} for {}", n, message.whom)
      if (n == max) {
        Behaviors.stopped // остановка актора, очень странное название для деструктора
      } else {
        message.from ! Greeter.Greet(message.whom, context.self) // отправка сообщения обратно Greeter
        bot(n, max) // каждый раз увеличивает параметр n на 37 строчке
      }
    }
}
//#greeter-bot

//#greeter-main
object GreeterMain {// актор спавнящий другие акторы

  final case class SayHello(name: String)

  def apply(): Behavior[SayHello] =
    Behaviors.setup { context => //setup поведение при создании объекта
      //#create-actors
      val greeter = context.spawn(Greeter(), "greeter") //spawn создает потомка у актора
      //#create-actors

      Behaviors.receiveMessage { message => //
        //#create-actors
        val replyTo = context.spawn(GreeterBot(max = 3), message.name)
        //#create-actors
        greeter ! Greeter.Greet(message.name, replyTo)
        Behaviors.same
      }
    }
}
//#greeter-main

//#main-class
object AkkaQuickstart extends App {
  //#actor-system
  val greeterMain: ActorSystem[GreeterMain.SayHello] = ActorSystem(GreeterMain(), "AkkaQuickStart")
  //#actor-system

  //#main-send-messages
  greeterMain ! SayHello("Charles")
  //#main-send-messages
}
//#main-class
//#full-example
