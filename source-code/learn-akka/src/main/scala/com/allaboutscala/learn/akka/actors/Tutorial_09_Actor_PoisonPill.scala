package com.allaboutscala.learn.akka.actors

import akka.actor._

/**
  * Created by Nadim Bahadoor on 28/06/2016.
  *
  *  Tutorial: Learn How To Use Akka
  *
  * [[http://allaboutscala.com/scala-frameworks/akka/]]
  *
  * Copyright 2016 Nadim Bahadoor (http://allaboutscala.com)
  *
  * Licensed under the Apache License, Version 2.0 (the "License"); you may not
  * use this file except in compliance with the License. You may obtain a copy of
  * the License at
  *
  *  [http://www.apache.org/licenses/LICENSE-2.0]
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  * License for the specific language governing permissions and limitations under
  * the License.
  */
object Tutorial_09_Actor_PoisonPill extends App {

  println("Step 1: Create an actor system")
  val system = ActorSystem("DonutStoreActorSystem")



  println("\nStep 2: Define the message passing protocol for our DonutStoreActor")
  object DonutStoreProtocol {
    case class Info(name: String)
  }



  println("\nStep 4: Create DonutInfoActor")
  val donutInfoActor = system.actorOf(Props[DonutInfoActor], name = "DonutInfoActor")



  println("\nStep 5: Akka Tell Pattern")
  import DonutStoreProtocol._
  donutInfoActor ! Info("vanilla")

  donutInfoActor ! PoisonPill

  donutInfoActor ! Info("plain")

  Thread.sleep(5000)



  println("\nStep 6: close the actor system")
  val isTerminated = system.terminate()


  println("\nStep 3: Define a BakingActor and a DonutInfoActor")
  class BakingActor extends Actor with ActorLogging {

    override def preStart(): Unit = log.info("prestart")

    override def postStop(): Unit = log.info("postStop")

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = log.info("preRestart")

    override def postRestart(reason: Throwable): Unit = log.info("postRestart")

    def receive = {
      case Info(name) =>
        log.info(s"BakingActor baking $name donut")
    }
  }



  class DonutInfoActor extends Actor with ActorLogging {

    override def preStart(): Unit = log.info("prestart")

    override def postStop(): Unit = log.info("postStop")

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = log.info("preRestart")

    override def postRestart(reason: Throwable): Unit = log.info("postRestart")

    val bakingActor = context.actorOf(Props[BakingActor], name = "BakingActor")

    def receive = {
      case msg @ Info(name) =>
        log.info(s"Found $name donut")
        bakingActor forward msg
    }
  }
}
