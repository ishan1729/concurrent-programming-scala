package com.concurrent.chapter2.material.communication

import com.concurrent.util.Utils.{log, thread}

object ThreadCommunicate extends App {
  var result: String = null

  val t = thread { result = "\nTitle\n" + "=" * 5 }
  t.join()
  // can never be null; because of the way communication happens but it is a one way communication
  log(result)
}
