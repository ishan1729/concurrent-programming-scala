package com.concurrent.chapter2.material.basic

object BasicThreadCreation extends App {
  val currentThread = Thread.currentThread()
  val name = currentThread.getName

  println(s"I am the thread $name")

  val t = new Thread {
    override def run(): Unit = println("New thread running.!")
  }

  t.start()
  t.join() // sends the main thread to a waiting state and wait for the thread t to terminate.

  println("New thread joined!")
}
