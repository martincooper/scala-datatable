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

/** ModifiableByName, with additional item (GenericColumn) indexer. */
trait ModifiableByColumn[V, R] extends ModifiableByName[V, R] {
  def replace(oldItem: GenericColumn, newItem: V): Try[R]
  def insert(itemToInsertAt: GenericColumn, newItem: V): Try[R]
  def remove(itemToRemove: GenericColumn): Try[R]
}

/** DataTable class. Handles the immutable storage of data in a Row / Column format. */
class DataTable private (tableName: String, dataColumns: Iterable[GenericColumn])
    extends IndexedSeq[DataRow] with ModifiableByColumn[GenericColumn, DataTable] {

  def name = tableName
  def columns = dataColumns.toVector

  /** Mappers, name to col and index to col. */
  private val columnNameMapper = columns.map(col => col.name -> col).toMap
  private val columnIndexMapper = columns.zipWithIndex.map { case (col, idx) => idx -> col }.toMap

  /** Gets column by index / name. */
  def col(columnIndex: Int): GenericColumn = columnIndexMapper(columnIndex)
  def col(columnName: String): GenericColumn = columnNameMapper(columnName)

  /** Gets column by index / name as Option in case it doesn't exist. */
  def getCol(columnIndex: Int): Option[GenericColumn] = columnIndexMapper.get(columnIndex)
  def getCol(columnName: String): Option[GenericColumn] = columnNameMapper.get(columnName)

  /** Gets typed column by index / name. */
  def colAs[T](columnIndex: Int): DataColumn[T] = columnIndexMapper(columnIndex).asInstanceOf[DataColumn[T]]
  def colAs[T](columnName: String): DataColumn[T] = columnNameMapper(columnName).asInstanceOf[DataColumn[T]]

  /** Gets typed column by index / name as Option in case it doesn't exist or invalid type. */
  def getColAs[T](columnIndex: Int): Option[DataColumn[T]] = toTypedCol(getCol(columnIndex))
  def getColAs[T](columnName: String): Option[DataColumn[T]] = toTypedCol(getCol(columnName))

  private def toTypedCol[T](column: Option[GenericColumn]): Option[DataColumn[T]] = {
    column match {
      case Some(col) => Try(col.asInstanceOf[DataColumn[T]]).toOption
      case _ => None
    }
  }

  def rowCount(): Int = {
    columns.length match {
      case 0 => 0
      case _ => columns.head.data.length
    }
  }

  override def length: Int = rowCount()

  override def apply(idx: Int): DataRow = new DataRow(this, idx)

  /** Creates a new table with the column specified replaced with the new column. */
  override def replace(oldColumn: GenericColumn, newColumn: GenericColumn): Try[DataTable] = {
    replace(columns.indexOf(oldColumn), newColumn)
  }

  /** Creates a new table with the column specified replaced with the new column. */
  override def replace(columnName: String, value: GenericColumn): Try[DataTable] = {
    actionByColumnName(columnName, colIdx => replace(colIdx, value))
  }

  /** Creates a new table with the column at index replaced with the new column. */
  override def replace(index: Int, value: GenericColumn): Try[DataTable] = {
    checkColsAndBuild("replacing", () => VectorExtensions.replaceItem(columns, index, value))
  }

  /** Creates a new table with the column inserted before the specified column. */
  override def insert(columnToInsertAt: GenericColumn, newColumn: GenericColumn): Try[DataTable] = {
    insert(columns.indexOf(columnToInsertAt), newColumn)
  }

  /** Creates a new table with the column inserted before the specified column. */
  override def insert(columnName: String, value: GenericColumn): Try[DataTable] = {
    actionByColumnName(columnName, colIdx => insert(colIdx, value))
  }

  /** Creates a new table with the column inserted at the specified index. */
  override def insert(index: Int, value: GenericColumn): Try[DataTable] = {
    checkColsAndBuild("inserting", () => VectorExtensions.insertItem(columns, index, value))
  }

  /** Creates a new table with the column removed. */
  override def remove(columnToRemove: GenericColumn): Try[DataTable] = {
    remove(columns.indexOf(columnToRemove))
  }

  /** Creates a new table with the column removed. */
  override def remove(columnName: String): Try[DataTable] = {
    actionByColumnName(columnName, colIdx => remove(colIdx))
  }

  /** Returns a new table with the column removed. */
  override def remove(index: Int): Try[DataTable] = {
    checkColsAndBuild("removing", () => VectorExtensions.removeItem(columns, index))
  }

  /** Returns a new table with the additional column. */
  override def add(newColumn: GenericColumn): Try[DataTable] = {
    checkColsAndBuild("adding", () => VectorExtensions.addItem(columns, newColumn))
  }

  private def actionByColumnName(columnName: String, action: Int => Try[DataTable]): Try[DataTable] = {
    columns.indexWhere(_.name == columnName) match {
      case -1 => Failure(DataTableException("Column " + columnName + " not found."))
      case colIdx: Int => action(colIdx)
    }
  }

  /** Checks that the new column set is valid, and builds a new DataTable. */
  private def checkColsAndBuild(modification: String, checkCols: () => Try[Vector[GenericColumn]]): Try[DataTable] = {

    val newCols = for {
      newColSet <- checkCols()
      result <- DataTable.validateDataColumns(newColSet)
    } yield newColSet

    newCols match {
      case Success(modifiedCols) => new Success[DataTable](new DataTable(name, modifiedCols))
      case Failure(ex) => Failure(DataTableException("Error " + modification + " column at specified index.", ex))
    }
  }

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
