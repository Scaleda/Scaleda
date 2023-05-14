package top.scaleda
package kernel.auth

import org.apache.commons.codec.digest.DigestUtils

object AuthorizationHasher {
  def encodeString(src: String): String = {
    DigestUtils.sha256Hex(src)
  }
}
