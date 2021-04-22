package com.concurrent.chapter2.material.gaurdedblocks

import scala.collection._
import com.concurrent.util.Utils.{log, thread}

/** Note: The Worker thread remains on the CPU until the main thread terminates.
  * Now, even if it doesn't consume the CPU, it still occupies thread stack space.
  * We can use graceful shutdown to terminate it. See [[SynchronizedPoolWithInterrupt]].
  */
object SynchronizedPoll extends App {
  val tasks = mutable.Queue[() => Unit]()

  // Singleton object
  object Worker extends Thread {
    setName("worker")
    setDaemon(true)

    def poll(): () => Unit =
      tasks
        .synchronized { // guarded block which sends the thread into wait state rather than busy waiting
          while (tasks.isEmpty) tasks.wait()

          tasks.dequeue()
        }

    override def run(): Unit = {
      while (true) {
        val task = poll()
        task()
      }
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
}
