package utils.helpers

import play.Configuration
import java.util.UUID

object CarersCrypto {
  val encrypt = Configuration.root().getBoolean("encryptFields", false)
  val staticSecret = Configuration.root().getBoolean("staticSecret", false)

  val secretKey = if (staticSecret) {
    "1234567890123456"
  } else {
    generateKey
  }

  def decryptAES(v: String): String = {
    if (encrypt) {
      play.api.libs.Crypto.decryptAES(v, secretKey)
    } else {
      v
    }
  }

  def encryptAES(v: String) = {
    if (encrypt) {
      play.api.libs.Crypto.encryptAES(v, secretKey)
    } else {
      v
    }
  }

  def generateKey: String = {
    UUID.randomUUID().toString.substring(0, 16)
  }

}
