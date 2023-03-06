package top.criwits.scaleda
package kernel.utils.serialise
import com.google.protobuf.ByteString

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

object BinarySerializeHelper {
  def apply(value: Any): Array[Byte] = {
    val dataOut = new ByteArrayOutputStream();
    val out     = new ObjectOutputStream(dataOut)
    out.writeObject(value)
    out.close()
    dataOut.close()
    dataOut.toByteArray
  }

  def fromBytes[T](bytes: Array[Byte]): T = {
    val dataIn = new ByteArrayInputStream(bytes)
    val in     = new ObjectInputStream(dataIn)
    val data   = in.readObject().asInstanceOf[T]
    in.close()
    dataIn.close()
    data
  }

  def fromGrpcBytes[T](byteString: ByteString): T =
    fromBytes(byteString.toByteArray)
}
