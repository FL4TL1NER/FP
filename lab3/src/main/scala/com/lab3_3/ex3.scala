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
        message match
            case Message(integral, _) => integral match
                case DefiniteIntegral(f, l, r, i) => {
                    val values = Range(0,i).map(l + step/2 + step*_)
                                           .map(f(_)*step)
                    message.replyTo ! values.reduce(_ + _)
                }
        Behaviors.same
    }

object SumActor:
    def apply(maxSteps: Int, replyTo: ActorRef[Double]):Behavior[Double] = Behaviors.setup { (context) =>
        var sum: Double = 0
        var step: Int = 0
        
        Behaviors.receiveMessage { (message) =>
            sum += message
            step += 1
            if step<maxSteps
                then {
                    Behaviors.same
                }
                else {
                    replyTo ! sum
                    Behaviors.stopped
                }
        }
    }

object DoubleLogger: 
    def apply():Behavior[Double] = Behaviors.receive { (context, message) =>
        context.log.info("received Double: " + message.toString())
        Behaviors.same
    }

object IntegrateSystem:
    case class Message(integral: DefiniteIntegral,t: Int, replyTo: ActorRef[Double])
    
    def apply():Behavior[Message] = Behaviors.setup { (context) => 
        val integrateActor = context.spawn(IntegrateActor(), "integrateActor")
        var nOfMessages = 0
        
        Behaviors.receiveMessage { (message) =>
            val sumActor = context.spawn(SumActor(message.t,message.replyTo), "sumActor" + nOfMessages.toString())
            nOfMessages += 1
            message match
            case Message(integral, t, _) => integral match
                case DefiniteIntegral(f, l, r, i) => {
                    val step: Double = (r-l)/t
                    for (i2 <- Range(0,t)) {
                        integrateActor ! IntegrateActor.Message(DefiniteIntegral(f,l+i2*step,l+(i2+1)*step,i),sumActor)
                    }
                }
            Behaviors.same
        }
    }

case class DefiniteIntegral(f: Double=>Double,l: Double,r: Double,i: Int)

@main def main(): Unit = {
    val integrateSystem: ActorSystem[IntegrateSystem.Message] = ActorSystem(IntegrateSystem(), "integrateSystem")
    val doubleLogger: ActorSystem[Double] = ActorSystem(DoubleLogger(), "doubleLogger")

    integrateSystem ! IntegrateSystem.Message(DefiniteIntegral(_*0.5,1,2,10),10,doubleLogger)
    integrateSystem ! IntegrateSystem.Message(DefiniteIntegral(_*0.5,0,1,10),10,doubleLogger)
}
