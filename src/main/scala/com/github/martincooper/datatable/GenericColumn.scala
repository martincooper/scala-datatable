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

/**
 * Generic Column Trait.
 * Allows a collection of columns storing data of distinct types to be stored in a generic collection.
 */
trait GenericColumn {
  val name: String
  val data: Vector[Any]
  val columnType: Type
  val isComparable: Boolean

  def add[V: TypeTag](value: V): Try[GenericColumn]
  def insert[V: TypeTag](index: Int, value: V): Try[GenericColumn]
  def replace[V: TypeTag](index: Int, value: V): Try[GenericColumn]
  def remove(index: Int): Try[GenericColumn]
}

object GenericColumn {

  implicit class GenericColExt(val col: GenericColumn) extends AnyVal {

    /** Casts the generic column back to the specified type. */
    def as[T: TypeTag]: DataColumn[T] = toDataColumn[T].get

    /** Casts the generic column into the specified type after performing type checks. */
    def toDataColumn[T: TypeTag]: Try[DataColumn[T]] = {
      typeOf[T] =:= col.columnType match {
        case true => Success(col.asInstanceOf[DataColumn[T]])
        case _ => Failure(DataTableException("Column type doesn't match type requested."))
      }
    }
  }
}
