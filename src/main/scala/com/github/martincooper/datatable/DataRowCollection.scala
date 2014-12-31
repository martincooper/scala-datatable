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
import scala.util.{ Success, Failure, Try }

/** ModifiableByDataRow : ModifiableByIndex, with additional item (DataRow) indexer. */
trait ModifiableByDataRow[V, R] extends ModifiableByIndex[V, R] {
  def replace(oldItem: DataRow, newItem: V): Try[R]
  def insert(itemToInsertAt: DataRow, newItem: V): Try[R]
  def remove(itemToRemove: DataRow): Try[R]
}

/** Implements a collection of DataRows with additional immutable modification methods implemented. */
class DataRowCollection(dataTable: DataTable)
    extends IndexedSeq[DataRow]
    with IndexedSeqLike[DataRow, DataRowCollection]
    with ModifiableByDataRow[DataRow, DataTable] {

  def table = dataTable

  override def apply(columnIndex: Int): DataRow = table(columnIndex)

  override def length: Int = dataTable.rowCount()

  override def newBuilder: mutable.Builder[DataRow, DataRowCollection] =
    DataRowCollection.newBuilder(table)

  /** Returns a new table with the additional row. */
  override def add(newRow: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the column specified replaced with the new column. */
  override def replace(oldRow: DataRow, newRow: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the column at index replaced with the new column. */
  override def replace(rowIndex: Int, value: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the column inserted before the specified column. */
  override def insert(rowToInsertAt: DataRow, newColumn: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the row inserted at the specified index. */
  override def insert(rowIndex: Int, value: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the row removed. */
  override def remove(rowToRemove: DataRow): Try[DataTable] = {
    indexFromRow(rowToRemove).map(remove).flatten
  }

  /** Returns a new table with the row removed. */
  override def remove(rowIndex: Int): Try[DataTable] = {
    removeRowItems(rowIndex)
  }

  /** Removes the item from each column and builds a new DataTable. */
  private def removeRowItems(rowIndex: Int): Try[DataTable] = {
    val newCols = allOrFirstFail(table.columns.map(col => col.remove(rowIndex)))

    newCols match {
      case Success(columns) => DataTable(table.name, columns)
      case Failure(ex) => Failure(ex)
    }
  }

  /** Gets the row index from the DataRow, ensuring it belongs to the correct table. */
  private def indexFromRow(dataRow: DataRow): Try[Int] = {
    dataRow.table eq table match {
      case true => Success(dataRow.rowIndex)
      case _ => Failure(DataTableException("DataRow specified does not belong to this table."))
    }
  }

  /** Returns all the values, or the first failure (if any). */
  private def allOrFirstFail[A](trySeq: Seq[Try[A]]) =
    Try(trySeq.map(_.get))
}

object DataRowCollection {

  /** Builder for a new DataRowCollection. */
  def newBuilder(dataTable: DataTable): mutable.Builder[DataRow, DataRowCollection] =
    Vector.newBuilder[DataRow] mapResult (vector => new DataRowCollection(dataTable))

  /** Builds a DataRowCollection. */
  def apply(dataTable: DataTable): DataRowCollection = {
    new DataRowCollection(dataTable)
  }
}