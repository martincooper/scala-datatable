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

package com.github.martincooper.datatable.DataSort

import com.github.martincooper.datatable._
import scala.util.{ Failure, Success, Try, Sorting }

object DataSort {

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(table: DataTable, sortItem: SortItem): Try[DataView] = {
    quickSort(table, Seq(sortItem))
  }

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(table: DataTable, sortItems: Iterable[SortItem]): Try[DataView] = {
    quickSort(table, table, sortItems)
  }

  /** Performs a quick sort of a DataView, returning a sorted DataView. */
  def quickSort(dataView: DataView, sortItem: SortItem): Try[DataView] = {
    quickSort(dataView, Seq(sortItem))
  }

  /** Performs a quick sort of a DataView, returning a sorted DataView. */
  def quickSort(dataView: DataView, sortItems: Iterable[SortItem]): Try[DataView] = {
    quickSort(dataView.table, dataView.rows, sortItems)
  }

  /** Performs a quick sort of the DataRows, returning a sorted DataView. */
  def quickSort(table: DataTable, dataRows: Iterable[DataRow], sortItems: Iterable[SortItem]): Try[DataView] = {
    validateSortColumns(table, sortItems) match {
      case Success(_) => performQuickSort(table, dataRows, sortItems)
      case Failure(ex) => Failure(ex)
    }
  }

  private def performQuickSort(table: DataTable, dataRows: Iterable[DataRow], sortItems: Iterable[SortItem]): Try[DataView] = {
    val dataRowArray = dataRows.toArray
    val dataRowOrdering = DataRowSorter.dataRowOrdering(sortItems)

    Sorting.quickSort(dataRowArray)(dataRowOrdering)
    DataView(table, dataRowArray)
  }

  /** Ensure that all columns specified are valid for sorting. */
  private def validateSortColumns(table: DataTable, sortItems: Iterable[SortItem]): Try[Unit] = {
    validateSortColumnIdentity(table, sortItems) match {
      case Success(cols) => validateColumnsAreComparable(cols)
      case Failure(ex) => Failure(ex)
    }
  }

  /** Ensure that all columns specified are recognised (by name or index). */
  private def validateSortColumnIdentity(table: DataTable, sortItems: Iterable[SortItem]): Try[Iterable[GenericColumn]] = {
    val columns = sortItems.map(item => columnFromIdentity(table, item.columnIdentity))
    Try(columns.map(_.get))
  }

  /** Ensure that all columns specified are comparable. */
  private def validateColumnsAreComparable(columns: Iterable[GenericColumn]): Try[Unit] = {
    columns.find(!_.isComparable) match {
      case None => Success(Unit)
      case Some(invalidCol) => Failure(DataTableException("Column '" + invalidCol.name + "' doesn't support comparable."))
    }
  }

  /** Gets a column from a DataTable by ItemIdentity. */
  private def columnFromIdentity(dataTable: DataTable, itemIdentity: ItemIdentity): Try[GenericColumn] = {
    itemIdentity match {
      case ItemByName(name) => dataTable.columns.get(name)
      case ItemByIndex(index) => dataTable.columns.get(index)
    }
  }
}
