package com.concurrent.chapter2.material.gaurdedblocks

import com.concurrent.util.Utils.{log, thread}

import scala.annotation.tailrec
import scala.collection.mutable

/**
  * A few things about [[Thread.interrupted()]]:
  * 1. If called on a waiting or timed waiting thread it throws [[InterruptedException]] which can be then handled.
  * 2. If called on a running thread then its sets the [[Thread.interrupted()]] flag.
  *
  */
object SynchronizedPoolWithInterrupt extends App {
  val tasks = mutable.Queue[() => Unit]()

  // Singleton object
  object Worker extends Thread {
    setName("worker")
    setDaemon(true)
    var terminated = false

    def poll(): Option[() => Unit] =
      tasks
        .synchronized { // guarded block which sends the thread into wait state rather than busy waiting
          while (tasks.isEmpty && !terminated) tasks.wait()

          if (!terminated) Some(tasks.dequeue()) else None
        }

    @tailrec override def run(): Unit =
      poll() match {
        case Some(task) => task(); run()
        case None       =>
      }

    def shutdown(): Unit =
      tasks.synchronized {
        terminated = true
        tasks.notify()
      }
  }

  Worker.start()

  private def asynchronous(body: => Unit): Unit =
    tasks.synchronized {
      tasks.enqueue(() => body)
      tasks.notify()
    }

  asynchronous(log("Hello"))
  asynchronous(log(" world!"))

  Thread.sleep(5000)
  Worker.shutdown()
}
