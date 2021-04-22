package com.concurrent.chapter2.material.gaurdedblocks

import com.concurrent.util.Utils.log

import scala.collection.mutable

// () => Unit -> Represents block of code
object SynchronizedBadPoll extends App {
  private val tasks = mutable.Queue[() => Unit]()

  // accepts some work
  // Note: This thread can keep busy-waiting and can end up consuming a whole CPU.
  val worker: Thread = new Thread {
    def poll(): Option[() => Unit] =
      tasks.synchronized {
        if (tasks.nonEmpty) Some(tasks.dequeue()) else None
      }

    override def run(): Unit = {
      while (true) poll() match {
        case Some(task) => task()
        case None       =>
      }
    }
  }

  def asynchronous(body: => Unit) =
    tasks.synchronized {
      tasks.enqueue(() => body)
    }

  worker.setName("Worker")
  worker.setDaemon(true)
  worker.start()

  asynchronous(log("Hello"))
  asynchronous(log(" world!"))
  Thread.sleep(5000)
}
