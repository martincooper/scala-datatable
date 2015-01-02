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

import com.github.martincooper.datatable.{ ItemIdentity, ItemByIndex, ItemByName, DataRow }
import com.github.martincooper.datatable.DataSort.SortEnum.{ Descending, Ascending }

/** Handles the multi column sorting on a DataRow. */
object DataRowSorter {

  /** Implements a Ordering[DataRow] for QuickSort. */
  def dataRowOrdering(sortItem: SortItem): Ordering[DataRow] = {
    dataRowOrdering(Seq(sortItem))
  }

  /** Implements a Ordering[DataRow] for QuickSort. */
  def dataRowOrdering(sortItems: Iterable[SortItem]): Ordering[DataRow] = {
    Ordering.fromLessThan { (rowOne: DataRow, rowTwo: DataRow) =>
      compare(rowOne, rowTwo, sortItems) > 0
    }
  }

  /** Method to support 'Ordered[DataRow]' for DataRows. */
  def compare(rowOne: DataRow, rowTwo: DataRow, sortItem: SortItem): Int = {
    compare(rowOne, rowTwo, Seq(sortItem))
  }

  /** Method to support 'Ordered[DataRow]' for DataRows. */
  def compare(rowOne: DataRow, rowTwo: DataRow, sortItems: Iterable[SortItem]): Int = {
    compareBySortItem(rowOne, rowTwo, sortItems)
  }

  /** Recursive sort method, handles multi-sort on columns. */
  private def compareBySortItem(rowOne: DataRow, rowTwo: DataRow, sortItems: Iterable[SortItem]): Int = {
    sortItems match {
      case Nil => 0
      case firstItem :: tail => compareValues(rowTwo, rowOne, firstItem) match {
        case 0 => compareBySortItem(rowOne, rowTwo, tail)
        case result => result
      }
    }
  }

  /** Compares the two values in each specified column. */
  private def compareValues(rowOne: DataRow, rowTwo: DataRow, sortItem: SortItem): Int = {
    val valueOne = valueFromIdentity(rowOne, sortItem.columnIdentity)
    val valueTwo = valueFromIdentity(rowTwo, sortItem.columnIdentity)

    sortItem.order match {
      case Ascending => compareValues(valueOne, valueTwo)
      case Descending => compareValues(valueTwo, valueOne)
    }
  }

  /** Gets a value from a DataRow by ItemIdentity. */
  private def valueFromIdentity(dataRow: DataRow, itemIdentity: ItemIdentity): Any = {
    itemIdentity match {
      case ItemByName(name) => dataRow(name)
      case ItemByIndex(index) => dataRow(index)
    }
  }

  /** Cast each value to Comparable Type before comparing. */
  def compareValues(valueOne: Any, valueTwo: Any) = {
    valueOne.asInstanceOf[Comparable[Any]].compareTo(valueTwo.asInstanceOf[Comparable[Any]])
  }
}
