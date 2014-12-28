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

import scala.collection.{ mutable, IndexedSeqLike }
import scala.util.{Success, Failure, Try}

/** ModifiableByColumn : ModifiableByName, with additional item (GenericColumn) indexer. */
trait ModifiableByColumn[V, R] extends ModifiableByName[V, R] {
  def replace(oldItem: GenericColumn, newItem: V): Try[R]
  def insert(itemToInsertAt: GenericColumn, newItem: V): Try[R]
  def remove(itemToRemove: GenericColumn): Try[R]
}

/** Implements a collection GenericColumns with additional modification methods implemented. */
class DataColumnCollection(dataTable: DataTable, dataColumns: Iterable[GenericColumn])
  extends IndexedSeq[GenericColumn]
  with IndexedSeqLike[GenericColumn, DataColumnCollection]
  with ModifiableByColumn[GenericColumn, DataTable] {

  def table = dataTable
  def columns = dataColumns.toVector

  override def length: Int = columns.length

  override def apply(idx: Int): GenericColumn = columns(idx)

  override def newBuilder: mutable.Builder[GenericColumn, DataColumnCollection] =
    DataColumnCollection.newBuilder(table)

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
    checkColsAndBuild("replacing", () => IndexedSeqExtensions.replaceItem(columns, index, value))
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
    checkColsAndBuild("inserting", () => IndexedSeqExtensions.insertItem(columns, index, value))
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
    checkColsAndBuild("removing", () => IndexedSeqExtensions.removeItem(columns, index))
  }

  /** Returns a new table with the additional column. */
  override def add(newColumn: GenericColumn): Try[DataTable] = {
    checkColsAndBuild("adding", () => IndexedSeqExtensions.addItem(columns, newColumn))
  }

  private def actionByColumnName(columnName: String, action: Int => Try[DataTable]): Try[DataTable] = {
    columns.indexWhere(_.name == columnName) match {
      case -1 => Failure(DataTableException("Column " + columnName + " not found."))
      case colIdx: Int => action(colIdx)
    }
  }

  /** Checks that the new column set is valid, and builds a new DataTable. */
  private def checkColsAndBuild(modification: String, checkCols: () => Try[IndexedSeq[GenericColumn]]): Try[DataTable] = {

    val newCols = for {
      newColSet <- checkCols()
      result <- DataTable.validateDataColumns(newColSet)
    } yield newColSet

    newCols match {
      case Success(modifiedCols) => DataTable(table.name, modifiedCols)
      case Failure(ex) => Failure(DataTableException("Error " + modification + " column at specified index.", ex))
    }
  }
}

object DataColumnCollection {

  /** Builder for a new DataColumnCollection. */
  def newBuilder(dataTable: DataTable): mutable.Builder[GenericColumn, DataColumnCollection] =
    Vector.newBuilder[GenericColumn] mapResult(vector => new DataColumnCollection(dataTable, vector))

  /** Builds a DataColumnCollection. */
  def apply(dataTable: DataTable, dataColumns: Iterable[GenericColumn]): DataColumnCollection = {
    new DataColumnCollection(dataTable, dataColumns)
  }
}