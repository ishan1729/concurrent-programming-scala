package com.concurrent.chapter2.material.basic

import com.concurrent.util.Utils.{log, thread}

object ThreadNonDeterminism extends App {
  val t = thread { log("New thread running!") }

  log("....")
  log("....")
  t.join()
  log("New thread joined.")
}
