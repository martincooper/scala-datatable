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

package com.github.martincooper.datatable.examples

import com.github.martincooper.datatable.DataSort.SortEnum.Descending
import com.github.martincooper.datatable.DataSort.SortItem
import com.github.martincooper.datatable.{ DataView, DataTable }

import scala.util.Try

class SortTableExamples {

  // Sorts by the specified column index using the default sort order (Ascending)
  def sortTableByColumnIndex(dataTable: DataTable): Try[DataView] = {

    dataTable.quickSort(0)
  }

  // Sorts by the specified column index using Descending sort order.
  def sortTableByColumnIndexDescending(dataTable: DataTable): Try[DataView] = {

    dataTable.quickSort(0, Descending)
  }

  // Sorts by the specified column name using the default sort order (Ascending)
  def sortTableByColumnName(dataTable: DataTable): Try[DataView] = {

    dataTable.quickSort("ColumnOne")
  }

  // Sorts by the specified column name using Descending sort order.
  def sortTableByColumnNameDescending(dataTable: DataTable): Try[DataView] = {

    dataTable.quickSort("ColumnOne", Descending)
  }

  // Sorts on multiple columns.
  def sortTableByMultipleColumns(dataTable: DataTable): Try[DataView] = {

    // Create Sort Items for each column required to sort.
    // Can specify column by name or index along with optional sort order.
    val sortItemOne = SortItem("FirstName")
    val sortItemTwo = SortItem(3, Descending)

    dataTable.quickSort(Seq(sortItemOne, sortItemTwo))
  }
}
