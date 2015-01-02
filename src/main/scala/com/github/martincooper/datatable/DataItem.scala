package com.github.martincooper.datatable

import TypedDataValueImplicits._

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/** DataValue trait used to handle TypedDataValues of various types. */
trait DataValue {
  val value: Any
  val valueType : Type
}

/** TypedDataValue used to capture values along with required type information. */
case class TypedDataValue[T : TypeTag : ClassTag](itemValue: T) extends DataValue {
  val value = itemValue
  val valueType = typeOf[T]
}

object TypedDataValueImplicits {

  /** Implicit conversion from value into a DataValue item. */
  implicit def ValueToTypedDataValue[T : TypeTag : ClassTag](value: T): DataValue =
    new TypedDataValue[T](value)
}

object DataItem {

  def setter(vv: DataValue): Unit = {

  }

  def setters(vv: DataValue*): Unit = {

  }

  def foo(args: String*): Unit = {

  }

  def test() = {

    val fd = new TypedDataValue(123)
    println(fd)


    val get = setter("some test")
    println(get)

    val gets = setters(1, 34, "dataValue")
    println(gets)
  }


}