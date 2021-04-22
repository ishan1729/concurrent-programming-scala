package com.concurrent.chapter2.material.communication

import com.concurrent.util.Utils.{log, thread}

object ThreadsUnprotectedUid extends App {
  var uidCount = 0L

  def getUniqueId: Long = {
    // following two statements are not atomic because another thread could simply access uidCount before the freshUid
    // value is assigned back to uidCount
    val freshUid = uidCount + 1
    uidCount = freshUid
    freshUid
  }

  def printUniqueIds(n: Int): Unit = {
    val uids = List.fill(n)(getUniqueId)
    log(s"Generated uids: $uids")
  }

  val t = thread { printUniqueIds(5) }

  printUniqueIds(5)

  t.join()
}
