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

import scala.util.{ Failure, Try, Success }

/** DataTable class. Handles the immutable storage of data in a Row / Column format. */
class DataTable private (tableName: String, dataColumns: Iterable[GenericColumn]) {

  def name = tableName
  def columns = dataColumns.toIndexedSeq

  def rowCount() = {
    columns.length == 0 match {
      case false => columns.head.data.length
      case _ => 0
    }
  }

  /** Returns the data column at the selected index. */
  def apply(index: Int) = {
    Try(columns(index)) match {
      case Success(col) => Some(col)
      case _ => None
    }
  }

  /** Returns the column with the specified name. */
  def apply(columnName: String) = columns.find(_.name == columnName)

  /** Outputs a more detailed toString implementation. */
  override def toString = {
    val tableDetails = "DataTable:" + name + "[Rows:" + columns.head.data.length + "]"
    val colDetails = columns.map(col => "[" + col.toString + "]").mkString(" ")

    tableDetails + colDetails
  }
}

object DataTable {

  /** Validates columns and builds a new DataTable. */
  def apply(tableName: String, columns: Iterable[GenericColumn]): Try[DataTable] = {

    validateDataColumns(columns) match {
      case Failure(ex) => new Failure(ex)
      case Success(_) => Success(new DataTable(tableName, columns))
    }
  }

  def validateDataColumns(columns: Iterable[GenericColumn]): Try[Unit] = {
    val colSeq = columns.toSeq

    /** Check all columns have the same number of rows. */
    if (colSeq.groupBy(_.data.length).toSeq.length > 1)
      return Failure(DataTableException("Columns have uneven row count."))

    /** Check all columns have distinct column names. */
    if (colSeq.groupBy(_.name).toSeq.length != colSeq.length)
      return Failure(DataTableException("Columns contain duplicate names."))

    Success(Unit)
  }

  /** Creates a new table with the additional column. */
  def addColumn(table: DataTable, newColumn: GenericColumn): Try[DataTable] = {
    val newColSet = table.columns :+ newColumn

    validateDataColumns(newColSet) match {
      case Failure(ex) => new Failure(ex)
      case Success(_) => Success(new DataTable(table.name, newColSet))
    }
  }

  /** Creates a new table with the column removed. */
  def removeColumn(table: DataTable, columnName: String): Try[DataTable] = {

    table.columns.exists(_.name == columnName) match {
      case true => Success(new DataTable(table.name, table.columns.filterNot(_.name == columnName)))
      case _ => Failure(DataTableException("Column " + columnName + " not found."))
    }
  }

}
