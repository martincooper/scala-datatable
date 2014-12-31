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
import scala.util.{ Failure, Try }

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

  /** Creates a new table with the column specified replaced with the new column. */
  override def replace(oldRow: DataRow, newRow: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the column at index replaced with the new column. */
  override def replace(index: Int, value: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the column inserted before the specified column. */
  override def insert(rowToInsertAt: DataRow, newColumn: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the column inserted at the specified index. */
  override def insert(index: Int, value: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Creates a new table with the column removed. */
  override def remove(rowToRemove: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Returns a new table with the column removed. */
  override def remove(index: Int): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }

  /** Returns a new table with the additional column. */
  override def add(newRow: DataRow): Try[DataTable] = {
    Failure(DataTableException("Not Implemented."))
  }
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