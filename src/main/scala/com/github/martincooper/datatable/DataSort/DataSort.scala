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

package com.github.martincooper.datatable.DataSort

import com.github.martincooper.datatable.{ DataRow, DataView, DataTable}
import scala.util.Try

/** Wraps a DataRow allowing it to be compared to other rows. */
case class OrderedDataRow(dataRow: DataRow, sortItems: Iterable[SortItem]) extends Ordered[DataRow] {

  def compare(that: DataRow): Int = {
    compareBySortItem(that, sortItems)
  }

  /** Recursive sort method, handles multi-sort on columns. */
  def compareBySortItem(that: DataRow, sortItems: Iterable[SortItem]): Int = {
    sortItems match {
      case Nil => 0
      case firstItem :: tail => compareValues(firstItem, dataRow, that) match {
        case 0 => compareBySortItem(that, tail)
        case result => result
      }
    }
  }

  private def compareValues(sortItem: SortItem, thisDataRow: DataRow, thatDataRow: DataRow): Int = {
    val thisValue = valueFromIdentity(thisDataRow, sortItem.columnIdentity)
    val thatValue = valueFromIdentity(thatDataRow, sortItem.columnIdentity)

    thisValue.toString.compareTo(thatValue.toString)
  }

  /** Gets a value from a DataRow by ItemIdentity. */
  private def valueFromIdentity(dataRow: DataRow, itemIdentity: ItemIdentity): Try[Any] = {
    itemIdentity match {
      case ItemByName(name) => dataRow.get(name)
      case ItemByIndex(index) => dataRow.get(index)
    }
  }
}

object DataSort {

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(table: DataTable, sortItems: Iterable[SortItem]): Try[DataView] = {
    DataView(DataTable("Not Yet Implemented").get)
  }

  /** Performs a quick sort of a DataView, returning a sorted DataView. */
  def quickSort(dataView: DataView, sortItems: Iterable[SortItem]): Try[DataView] = {
    DataView(DataTable("Not Yet Implemented").get)
  }
}
