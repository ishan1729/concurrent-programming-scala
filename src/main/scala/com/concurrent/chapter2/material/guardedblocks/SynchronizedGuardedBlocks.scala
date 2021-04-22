package com.concurrent.chapter2.material.gaurdedblocks

import com.concurrent.util.Utils.{log, thread}

/**
  * Notes:
  * wait() can cause spurious wakeups.
  * This means that the JVM can cause the thread which is waiting to wake up even if no notify() was issued.
  * To prevent this we use the `while` clause instead of `if`.
  */
object SynchronizedGuardedBlocks extends App {
  val lock = new AnyRef

  var message: Option[String] = None
  val greeter = thread { // acquires a lock, releases the lock
    lock.synchronized {
      while (message.isEmpty) lock.wait()
      log(message.get)
    }
  }

  lock.synchronized { // acquires the lock when the greeter thread releases it
    message = Some("Hello!")
    lock.notify() // notifies greeter thread that there is a new message
  }

  // at this point the greeter thread acquires lock on lock object again and prints the message

  greeter.join()
}
