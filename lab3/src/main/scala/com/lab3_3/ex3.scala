package com.lab3_3

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import scala.util.Try

object IntegrateActor:
    case class Message(integral: DefiniteIntegral, replyTo: ActorRef[Double])
    
    def apply(): Behavior[Message] = Behaviors.receive { (context, message) =>
        val step: Double = (message.integral.r-message.integral.l)/message.integral.i
        val values = Range(0,message.integral.i).map(message.integral.l + step/2 + step*_).map(message.integral.f(_)*step)
        message.replyTo ! values.reduce(_ + _)
        Behaviors.same
    }


object SumActor:
    case class Message(t: Int, replyTo: ActorRef[Double])
    
    def apply():Behavior[Message|Double] = Behaviors.setup { (context) =>
        var replyTo: Option[ActorRef[Double]] = None
        var sum: Double = 0
        var step: Int = 0
        var maxSteps: Int = 0
        
        Behaviors.receiveMessage { (message) =>
            message.match
                case message: Message =>
                    replyTo = Some(message.replyTo)
                    sum = 0
                    step = 0
                    maxSteps = message.t
                case a: Double =>
                    step += 1
                    sum += a
                    if (step >= maxSteps) {
                        replyTo.match
                            case Some(value) => {value ! sum}
                            case None => {}
                        replyTo = None
                        sum = 0
                        step = 0
                        maxSteps = 0
                    }
            Behaviors.same
        }
    }

object DoublePrinter: 
    def apply():Behavior[Double] = Behaviors.receive { (context, message) =>
        print(message.toString())
        print("\n")
        Behaviors.same
    }

object IntegrateSystem:
    case class Message(integral: DefiniteIntegral,t: Int, replyTo: ActorRef[Double])
    
    def apply():Behavior[Message] = Behaviors.setup { (context) => 
        val integrateActor = context.spawn(IntegrateActor(), "integrateActor")
        val sumActor = context.spawn(SumActor(), "sumActor")
        
        Behaviors.receiveMessage { (message) =>
            sumActor ! SumActor.Message(message.t,message.replyTo)
            val step: Double = (message.integral.r-message.integral.l)/message.t
            for (i <- Range(0,message.t)) {
                integrateActor ! IntegrateActor.Message(DefiniteIntegral(message.integral.f,message.integral.l+i*step,message.integral.l+(i+1)*step,message.integral.i),sumActor)
            }
            Behaviors.same
        }
    }

case class DefiniteIntegral(f: Double=>Double,l: Double,r: Double,i: Int)

@main def main(): Unit = {
    val integrateSystem: ActorSystem[IntegrateSystem.Message] = ActorSystem(IntegrateSystem(), "integrateSystem")
    val doublePrinter: ActorSystem[Double] = ActorSystem(DoublePrinter(), "doublePrinter")

    integrateSystem ! IntegrateSystem.Message(DefiniteIntegral(_*0.5,1,2,10),10,doublePrinter)
}
