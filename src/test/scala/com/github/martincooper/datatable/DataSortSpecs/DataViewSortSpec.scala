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

package com.github.martincooper.datatable.DataSortSpecs

import com.github.martincooper.datatable.DataSort.SortEnum.{ Ascending, Descending }
import com.github.martincooper.datatable.DataSort.SortItem
import com.github.martincooper.datatable.{ DataColumn, DataTable }
import org.scalatest.{ FlatSpec, Matchers }

class DataViewSortSpec extends FlatSpec with Matchers {

  "A DataView" can "be sorted by single string column using default sort order" in {
    val dataCol = new DataColumn[String]("ColumnOne", Seq("FF", "AA", "ZZ", "FF", "BB", "YY", "FF", "GG"))
    val table = DataTable("TestTable", Seq(dataCol)).get

    // Filter out all the values "FF" and return results as a DataView.
    val filteredView = table.filterNot(row => row.as[String]("ColumnOne") == "FF")

    // Sort the DataView containing the filtered results.
    val sortedView = filteredView.quickSort(0)

    sortedView.isSuccess should be(true)

    sortedView.get.rowCount should be(5)

    val columnData = sortedView.get.map(row => row.as[String]("ColumnOne"))
    columnData should be(Seq("AA", "BB", "GG", "YY", "ZZ"))
  }

  it can "be sorted by multiple string columns using different sort orders" in {
    val dataColOne = new DataColumn[String]("ColumnOne", Seq("AA", "AA", "AA", "BB", "BB", "BB", "CC", "CC", "CC", "DD", "DD", "DD"))
    val dataColTwo = new DataColumn[String]("ColumnTwo", Seq("Z", "Y", "X", "W", "V", "U", "T", "S", "R", "A", "B", "C"))

    val table = DataTable("TestTable", Seq(dataColOne, dataColTwo)).get

    // Filter out all the values "BB" and return results as a DataView.
    val filteredView = table.filterNot(row => row.as[String]("ColumnOne") == "BB")

    val sortItems = Seq(SortItem("ColumnOne", Descending), SortItem("ColumnTwo", Ascending))

    // Sort the DataView containing the filtered results.
    val sortedView = filteredView.quickSort(sortItems)

    sortedView.isSuccess should be(true)
    sortedView.get.rowCount should be(9)

    val columnDataOne = sortedView.get.map(row => row.as[String]("ColumnOne"))
    columnDataOne should be(Seq("DD", "DD", "DD", "CC", "CC", "CC", "AA", "AA", "AA"))

    val columnDataTwo = sortedView.get.map(row => row.as[String]("ColumnTwo"))
    columnDataTwo should be(Seq("A", "B", "C", "R", "S", "T", "X", "Y", "Z"))
  }
}
