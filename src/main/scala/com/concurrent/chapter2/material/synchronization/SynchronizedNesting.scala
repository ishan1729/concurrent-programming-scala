package com.concurrent.chapter2.material.synchronization

import com.concurrent.util.Utils.{log, thread}

object SynchronizedNesting extends App {
  import scala.collection.mutable
  private val transfers = mutable.ArrayBuffer[String]()

  def logTransfer(name: String, n: Int): Unit =
    transfers.synchronized {
      transfers += s"transfer to account '$name' = $n"
    }

  class Account(val name: String, var money: Int)

  def add(account: Account, n: Int): Unit =
    account.synchronized {
      account.money += n

      if (n > 10) logTransfer(account.name, n)
    }

  val jane = new Account("Jane", 100)
  val john = new Account("John", 200)

  val t1 = thread { add(jane, 5) }
  val t2 = thread { add(john, 50) }
  val t3 = thread { add(jane, 70) }

  t1.join(); t2.join(); t3.join()

  log(s"Jane's money: ${jane.money}")
  log(s"s---- transfers ----\n$transfers")
}
