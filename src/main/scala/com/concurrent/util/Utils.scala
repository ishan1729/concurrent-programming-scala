package com.concurrent.util

object Utils {
  def thread(body: => Unit): Thread = {
    val t = new Thread {
      override def run(): Unit = body
    }
    t.start()
    t
  }

  def log(s: String): Unit = println(s"${Thread.currentThread().getName}: $s")
}
