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
  def columns = dataColumns.toVector

  private val columnNameMapper = columns.map(col => col.name -> col).toMap
  private val columnIndexMapper = columns.zipWithIndex.map { case (col, idx) => idx -> col }.toMap

  def col(columnIndex: Int) = columnIndexMapper(columnIndex)
  def col(columnName: String) = columnNameMapper(columnName)

  def getCol(columnIndex: Int) = columnIndexMapper.get(columnIndex)
  def getCol(columnName: String) = columnNameMapper.get(columnName)

  def colAs[T](columnIndex: Int): DataColumn[T] = {
    columnIndexMapper(columnIndex).asInstanceOf[DataColumn[T]]
  }

  def colAs[T](columnName: String): DataColumn[T] = {
    columnNameMapper(columnName).asInstanceOf[DataColumn[T]]
  }

  def getColAs[T](columnIndex: Int): Option[DataColumn[T]] = {
    toTypedCol(getCol(columnIndex))
  }

  def getColAs[T](columnName: String): Option[DataColumn[T]] = {
    toTypedCol(getCol(columnName))
  }

  private def toTypedCol[T](column: Option[GenericColumn]): Option[DataColumn[T]] = {
    column match {
      case Some(col) => Try(col.asInstanceOf[DataColumn[T]]).toOption
      case _ => None
    }
  }

  def rowCount() = {
    columns.length match {
      case 0 => 0
      case _ => columns.head.data.length
    }
  }

  /** Outputs a more detailed toString implementation. */
  override def toString = {
    val tableDetails = "DataTable:" + name + "[Rows:" + columns.head.data.length + "]"
    val colDetails = columns.map(col => "[" + col.toString + "]").mkString(" ")

    tableDetails + colDetails
  }
}

object DataTable {

  /** Builds an empty DataTable. */
  def apply(tableName: String): Try[DataTable] = {
    Success(new DataTable(tableName, Array().toIndexedSeq))
  }

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

  implicit class DataTableExt(val table: DataTable) extends AnyVal {

    /** Creates a new table with the additional column. */
    def addColumn(newColumn: GenericColumn): Try[DataTable] = {
      val newColSet = table.columns :+ newColumn

      validateDataColumns(newColSet) match {
        case Failure(ex) => new Failure(ex)
        case Success(_) => Success(new DataTable(table.name, newColSet))
      }
    }

    /** Creates a new table with the column removed. */
    def removeColumn(columnName: String): Try[DataTable] = {
      table.columns.exists(_.name == columnName) match {
        case true => Success(new DataTable(table.name, table.columns.filterNot(_.name == columnName)))
        case _ => Failure(DataTableException("Column " + columnName + " not found."))
      }
    }

    /** Creates a new table with the column removed. */
    def removeColumn(column: GenericColumn): Try[DataTable] = {
      table.columns.exists(_ eq column) match {
        case true => Success(new DataTable(table.name, table.columns.filterNot(_ eq column)))
        case _ => Failure(DataTableException("Column not found."))
      }
    }
  }
}
