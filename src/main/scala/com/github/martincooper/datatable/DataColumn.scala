/**
 * Copyright 2014-2015 Martin Cooper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.martincooper.datatable

import scala.reflect.runtime.universe._
import scala.util.{ Failure, Success, Try }

/** Strongly typed data column. */
class DataColumn[T: TypeTag](columnName: String, columnData: Iterable[T]) extends GenericColumn {
  val name = columnName
  val data = columnData.toVector
  val columnType = typeOf[T]

  /** Checks if the current type for this column has a default compare method. */
  val isComparable: Boolean = {
    typeOf[T] match {
      case t if t <:< typeOf[Comparable[_]] => true
      case t if t <:< typeOf[AnyVal] => true
      case _ => false
    }
  }

  /** Returns a new DataColumn[T] with the value added at the end. */
  override def add(value: DataValue): Try[GenericColumn] = {
    validateType("add", value).flatMap { typedValue =>
      checkAndCreateNewColumn(() => IndexedSeqExtensions.addItem(data, typedValue))
    }
  }

  /** Returns a new DataColumn[T] with the value inserted at the specified index. */
  override def insert(index: Int, value: DataValue): Try[GenericColumn] = {
    validateType("insert", value).flatMap { typedValue =>
      checkAndCreateNewColumn(() => IndexedSeqExtensions.insertItem(data, index, typedValue))
    }
  }

  /** Returns a new DataColumn[T] with the new value replacing the one at the specified index. */
  override def replace(index: Int, value: DataValue): Try[GenericColumn] = {
    validateType("replace", value).flatMap { typedValue =>
      checkAndCreateNewColumn(() => IndexedSeqExtensions.replaceItem(data, index, typedValue))
    }
  }

  /** Returns a new DataColumn[T] with the value added at the end. */
  def addAs[V: TypeTag](value: V): Try[GenericColumn] = {
    validateType[V]("add", value).flatMap { typedValue =>
      checkAndCreateNewColumn(() => IndexedSeqExtensions.addItem(data, typedValue))
    }
  }

  /** Returns a new DataColumn[T] with the value inserted at the specified index. */
  def insertAs[V: TypeTag](index: Int, value: V): Try[GenericColumn] = {
    validateType[V]("insert", value).flatMap { typedValue =>
      checkAndCreateNewColumn(() => IndexedSeqExtensions.insertItem(data, index, typedValue))
    }
  }

  /** Returns a new DataColumn[T] with the new value replacing the one at the specified index. */
  def replaceAs[V: TypeTag](index: Int, value: V): Try[GenericColumn] = {
    validateType[V]("replace", value).flatMap { typedValue =>
      checkAndCreateNewColumn(() => IndexedSeqExtensions.replaceItem(data, index, typedValue))
    }
  }

  /** Returns a new DataColumn[T] with the value at the specified removed. */
  def remove(index: Int): Try[DataColumn[T]] = {
    checkAndCreateNewColumn(() => IndexedSeqExtensions.removeItem(data, index))
  }

  private def checkAndCreateNewColumn(transformData: () => Try[IndexedSeq[T]]): Try[DataColumn[T]] = {
    transformData().map { modifiedData => new DataColumn[T](name, modifiedData) }
  }

  private def validateType(reason: String, dataValue: DataValue): Try[T] = {
    dataValue.valueType =:= columnType match {
      case true => Success(dataValue.value.asInstanceOf[T])
      case _ => Failure(DataTableException(s"Invalid value type on $reason."))
    }
  }

  private def validateType[V: TypeTag](reason: String, value: V): Try[T] = {
    typeOf[V] =:= columnType match {
      case true => Success(value.asInstanceOf[T])
      case _ => Failure(DataTableException(s"Invalid value type on $reason."))
    }
  }

  override def toString = "Col : " + name
}

object DataColumn {

  /** Builds an new DataColumn. */
  def apply[T: TypeTag](columnName: String, columnData: Iterable[T]): Try[DataColumn[T]] = {
    Success(new DataColumn[T](columnName, columnData))
  }

  /** Builds an new DataColumn. */
  def apply[T: TypeTag](columnName: String): Try[DataColumn[T]] = {
    Success(new DataColumn[T](columnName, Seq()))
  }
}
