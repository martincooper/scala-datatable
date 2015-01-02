package com.github.martincooper.datatable

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/** DataValue trait used to handle TypedDataValues of various types. */
trait DataValue {
  val value: Any
  val valueType: Type
}

/** TypedDataValue used to capture values along with required type information. */
case class TypedDataValue[T: TypeTag: ClassTag](itemValue: T) extends DataValue {
  val value = itemValue
  val valueType = typeOf[T]
}

object DataValue {

  /** Builds an new TypedDataValue. */
  def apply[T: TypeTag: ClassTag](value: T): DataValue = {
    new TypedDataValue[T](value)
  }
}

object TypedDataValueImplicits {

  /** Implicit conversion from value into a DataValue item. */
  implicit def ValueToTypedDataValue[T: TypeTag: ClassTag](value: T): DataValue =
    TypedDataValue[T](value)
}
