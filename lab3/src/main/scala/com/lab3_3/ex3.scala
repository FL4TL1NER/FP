package com.lab3_3

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import scala.util.Try

import scala.math.*

object IntegrateActor:
    case class Message(integral: DefiniteIntegral, replyTo: ActorRef[Double])
    
    def apply(): Behavior[Message] = Behaviors.receive { (context, message) =>
        message match
            case Message(DefiniteIntegral(f, l, r, i), replyTo) => {
                    val n = i*2
                    val step: Double = (r-l)/n
                    
                    val values = Seq(f(l),
                                     Range.inclusive(1,n/2-1)
                                          .map(j => l + step*2*j)
                                          .map(x => f(x))
                                          .reduce(_ + _) * 4,
                                     Range.inclusive(1,n/2)
                                          .map(j => l + step*2*(j - 1))
                                          .map(x => f(x))
                                          .reduce(_ + _) * 2,
                                     f(r)) 
                    
                    replyTo ! values.reduce(_ + _)*(step/3)
                }
        Behaviors.same
    }

object SumActor:
    def apply(maxSteps: Int, replyTo: ActorRef[Double]):Behavior[Double] = Behaviors.setup { (context) =>
        receiver(0.0, maxSteps-1, replyTo)
    }
    
    private def receiver(sum: Double, remainingSteps: Int, replyTo: ActorRef[Double]): Behavior[Double] =
        Behaviors.receiveMessage { (message) =>
            if remainingSteps>0
                then {
                    receiver(sum + message, remainingSteps - 1, replyTo)
                }
                else {
                    replyTo ! sum
                    Behaviors.stopped
                }
        }

object DoubleLogger: 
    def apply():Behavior[Double] = Behaviors.receive { (context, message) =>
        context.log.info("received Double: " + message.toString())
        Behaviors.same
    }

object IntegrateSystem:
    case class Message(integral: DefiniteIntegral, t: Int,replyTo: ActorRef[Double])
    
    def apply():Behavior[Message] = Behaviors.setup { (context) => 
        val integrateActors = Seq(context.spawn(IntegrateActor(), "integrateActor0"),
                                  context.spawn(IntegrateActor(), "integrateActor1"),
                                  context.spawn(IntegrateActor(), "integrateActor2"),
                                  context.spawn(IntegrateActor(), "integrateActor3"))
        val nOfIntegrateActors = integrateActors.length
        
        var nOfMessages = 0
        Behaviors.receiveMessage { (message) =>
            message match
                case Message(DefiniteIntegral(f, l, r, i), t, replyTo) => {

                    val sumActor = context.spawn(SumActor(t,replyTo), "sumActor" + nOfMessages.toString())
                    nOfMessages += 1
                    
                    val step: Double = (r-l)/t
                    for (i2 <- Range(0,t)) {
                        integrateActors(i2 % nOfIntegrateActors) ! IntegrateActor.Message(DefiniteIntegral(f,l+i2*step,l+(i2+1)*step,i),
                                                                                          sumActor)
                    }
                }
            Behaviors.same
        }
    }

case class DefiniteIntegral(f: Double=>Double,l: Double,r: Double,i: Int)

@main def main(): Unit = {
    val integrateSystem: ActorSystem[IntegrateSystem.Message] = ActorSystem(IntegrateSystem(), "integrateSystem")
    val doubleLogger: ActorSystem[Double] = ActorSystem(DoubleLogger(), "doubleLogger")

    integrateSystem ! IntegrateSystem.Message(DefiniteIntegral(x => sin(x),0,Pi*2,100),100,doubleLogger)
    integrateSystem ! IntegrateSystem.Message(DefiniteIntegral(x => x*0.5,0,2,100),100,doubleLogger)
}
