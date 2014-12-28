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

import scala.reflect.runtime.universe._
import scala.util.{ Failure, Try, Success }

/** DataTable class. Handles the immutable storage and access of data in a Row / Column format. */
class DataTable private (tableName: String, dataColumns: Iterable[GenericColumn])
    extends IndexedSeq[DataRow] {

  def name = tableName
  def columns = DataColumnCollection(this, dataColumns)

  /** Mappers, name to col and index to col. */
  private val columnNameMapper = columns.map(col => col.name -> col).toMap
  private val columnIndexMapper = columns.zipWithIndex.map { case (col, idx) => idx -> col }.toMap

  private def checkedColumnIndexMapper(columnIndex: Int): Try[GenericColumn] = {
    columnIndexMapper.get(columnIndex) match {
      case Some(column) => Success(column)
      case _ => Failure(DataTableException("Specified column index not found."))
    }
  }

  private def checkedColumnNameMapper(columnName: String): Try[GenericColumn] = {
    columnNameMapper.get(columnName) match {
      case Some(column) => Success(column)
      case _ => Failure(DataTableException("Specified column name not found."))
    }
  }

  /** Gets column by index / name. */
  def col(columnIndex: Int): GenericColumn = columnIndexMapper(columnIndex)
  def col(columnName: String): GenericColumn = columnNameMapper(columnName)

  /** Gets column by index as Try[GenericColumn] in case it doesn't exist. */
  def getCol(columnIndex: Int): Try[GenericColumn] = {
    checkedColumnIndexMapper(columnIndex)
  }

  /** Gets column by name as Try[GenericColumn] in case it doesn't exist. */
  def getCol(columnName: String): Try[GenericColumn] = {
    checkedColumnNameMapper(columnName)
  }

  /** Gets typed column by index. */
  def colAs[T: TypeTag](columnIndex: Int): DataColumn[T] = {
    getColAs[T](columnIndex) match {
      case Success(typedCol) => typedCol
      case Failure(ex) => throw ex
    }
  }

  /** Gets typed column by name. */
  def colAs[T: TypeTag](columnName: String): DataColumn[T] = {
    getColAs[T](columnName) match {
      case Success(typedCol) => typedCol
      case Failure(ex) => throw ex
    }
  }

  /** Gets typed column by index as Try[DataColumn[T]] in case it doesn't exist or invalid type. */
  def getColAs[T: TypeTag](columnIndex: Int): Try[DataColumn[T]] = {
    checkedColumnIndexMapper(columnIndex).flatMap { column =>
      column.toDataColumn[T]
    }
  }

  /** Gets typed column by name as Try[DataColumn[T]] in case it doesn't exist or invalid type. */
  def getColAs[T: TypeTag](columnName: String): Try[DataColumn[T]] = {
    checkedColumnNameMapper(columnName).flatMap { column =>
      column.toDataColumn[T]
    }
  }

  def rowCount(): Int = {
    columns.length match {
      case 0 => 0
      case _ => columns.head.data.length
    }
  }

  override def length: Int = rowCount()

  override def apply(idx: Int): DataRow = DataRow(this, idx).get

  /** Outputs a more detailed toString implementation. */
  override def toString() = {
    val tableDetails = "DataTable:" + name + "[Rows:" + rowCount() + "]"
    val colDetails = columns.map(col => "[" + col.toString + "]").mkString(" ")

    tableDetails + colDetails
  }
}

object DataTable {

  /** Builds an empty DataTable. */
  def apply(tableName: String): Try[DataTable] = {
    Success(new DataTable(tableName, Seq()))
  }

  /** Validates columns and builds a new DataTable. */
  def apply(tableName: String, columns: Iterable[GenericColumn]): Try[DataTable] = {
    validateDataColumns(columns) map { _ => new DataTable(tableName, columns) }
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
}
