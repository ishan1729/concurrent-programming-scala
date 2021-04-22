package com.concurrent.chapter2.material.basic

import com.concurrent.util.Utils.{log, thread}

// Deterministic program
object ThreadSleep extends App {
  private val t = thread {
    // sends the current thread to timed-waiting state. Os can reuse process for other tasks
    Thread.sleep(1000)
    log("New thread running.")
    Thread.sleep(1000)
    log("Still running.")
    Thread.sleep(1000)
    log("Completed.")
  }

  t.join()
  log("New thread joined.")
}
