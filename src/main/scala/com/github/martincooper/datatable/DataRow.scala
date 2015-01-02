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

/** Allows access to the underlying data in a row format. */
class DataRow private (dataTable: DataTable, index: Int) {

  val table = dataTable
  val rowIndex = index

  /** Returns all the values in this row for the current row index. */
  def values: IndexedSeq[Any] = {
    table.columns.map(col => col.data(rowIndex))
  }

  /**
   * Returns all the values in this row for the current row index
   * in a ColumnName -> Value Map.
   */
  def valueMap: Map[String, Any] = {
    table.columns.map(col => col.name -> col.data(rowIndex)).toMap
  }

  /** Indexer using the column name. */
  def apply(columnIndex: Int): Any = table.columns(columnIndex).data(rowIndex)

  /** Indexer using the column index. */
  def apply(columnName: String): Any = table.columns(columnName).data(rowIndex)

  /** Gets the column by index, as Try[Any] in case it doesn't exist. */
  def get(columnIndex: Int): Try[Any] = colToValue(table.columns.get(columnIndex))

  /** Gets the column by name, as Try[Any] in case it doesn't exist. */
  def get(columnName: String): Try[Any] = colToValue(table.columns.get(columnName))

  /** Gets the typed column by index. */
  def as[T: TypeTag](columnIndex: Int): T = table.columns(columnIndex).data(rowIndex).asInstanceOf[T]

  /** Gets the typed column by name. */
  def as[T: TypeTag](columnName: String): T = table.columns(columnName).data(rowIndex).asInstanceOf[T]

  /** Gets the typed column by index, as Try[T] in case it doesn't exist or invalid type. */
  def getAs[T: TypeTag](columnIndex: Int): Try[T] = colToTypedValue(table.columns.get(columnIndex))

  /** Gets the typed column by name, as Try[T] in case it doesn't exist or invalid type. */
  def getAs[T: TypeTag](columnName: String): Try[T] = colToTypedValue(table.columns.get(columnName))

  private def colToValue(column: Try[GenericColumn]): Try[Any] = {
    column match {
      case Success(col) => Try(col.data(rowIndex))
      case Failure(ex) => Failure(ex)
    }
  }

  private def colToTypedValue[T: TypeTag](tryColumn: Try[GenericColumn]): Try[T] = {
    for {
      column <- tryColumn
      typeCol <- column.toDataColumn[T]
    } yield typeCol.data(rowIndex)
  }
}

object DataRow {

  /** Builds a DataRow validating the index. */
  def apply(dataTable: DataTable, rowIndex: Int): Try[DataRow] = {
    IndexedSeqExtensions.outOfBounds(dataTable.rowCount, rowIndex) match {
      case false => Success(new DataRow(dataTable, rowIndex))
      case _ => Failure(DataTableException("Invalid row index for DataRow."))
    }
  }
}
