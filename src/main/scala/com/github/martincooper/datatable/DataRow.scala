/**
 * Copyright 2014 Martin Cooper
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

import scala.util.Try

/** Allows access to the underlying data in a row format. */
class DataRow(dataTable: DataTable, index: Int) {

  val table = dataTable
  val rowIndex = index

  /** Indexer using the column name. */
  def apply(columnIndex: Int): Any = table.col(columnIndex).data(rowIndex)

  /** Indexer using the column index. */
  def apply(columnName: String): Any = table.col(columnName).data(rowIndex)

  /** Gets the column by index, as Option in case it doesn't exist. */
  def get(columnIndex: Int): Option[Any] = colToValue(table.getCol(columnIndex))

  /** Gets the column by name, as Option in case it doesn't exist. */
  def get(columnName: String): Option[Any] = colToValue(table.getCol(columnName))

  /** Gets the typed column by index. */
  def as[T](columnIndex: Int): T = table.col(columnIndex).data(rowIndex).asInstanceOf[T]

  /** Gets the typed column by name. */
  def as[T](columnName: String): T = table.col(columnName).data(rowIndex).asInstanceOf[T]

  /** Gets the typed column by index, as Option in case it doesn't exist or invalid type. */
  def getAs[T](columnIndex: Int): Option[T] = colToTypedValue(table.getCol(columnIndex))

  /** Gets the typed column by name, as Option in case it doesn't exist or invalid type. */
  def getAs[T](columnName: String): Option[T] = colToTypedValue(table.getCol(columnName))

  private def colToValue(column: Option[GenericColumn]): Option[Any] = {
    column match {
      case Some(col) => Try(col.data(rowIndex)).toOption
      case _ => None
    }
  }

  private def colToTypedValue[T](column: Option[GenericColumn]): Option[T] = {
    column match {
      case Some(col) => Try(col.data(rowIndex).asInstanceOf[T]).toOption
      case _ => None
    }
  }
}
