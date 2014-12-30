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

package com.github.martincooper.datatable.DataSortSpecs

import com.github.martincooper.datatable.DataSort.SortEnum._
import com.github.martincooper.datatable.DataSort.{ SortItem, DataSort }
import com.github.martincooper.datatable.{ DataColumn, DataTable }

import org.scalatest.{ Matchers, FlatSpec }

class DataSortSpec extends FlatSpec with Matchers {

  "A DataTable" can "be sorted by single string column using default sort order" in {
    val dataCol = new DataColumn[String]("ColumnOne", Seq("AA", "ZZ", "BB", "YY", "GG"))
    val table = DataTable("TestTable", Seq(dataCol)).get

    val sortedView = DataSort.quickSort(table, SortItem("ColumnOne"))

    sortedView.isSuccess should be(true)

    val columnData = sortedView.get.map(row => row.as[String]("ColumnOne"))
    columnData should be(Seq("AA", "BB", "GG", "YY", "ZZ"))
  }

  "A DataTable" can "be sorted by single string column using explicit Ascending sort order" in {
    val dataCol = new DataColumn[String]("ColumnOne", Seq("AA", "ZZ", "BB", "YY", "GG"))
    val table = DataTable("TestTable", Seq(dataCol)).get

    val sortedView = DataSort.quickSort(table, SortItem("ColumnOne", Ascending))

    sortedView.isSuccess should be(true)

    val columnData = sortedView.get.map(row => row.as[String]("ColumnOne"))
    columnData should be(Seq("AA", "BB", "GG", "YY", "ZZ"))
  }

  "A DataTable" can "be sorted by single string column using explicit Descending sort order" in {
    val dataCol = new DataColumn[String]("ColumnOne", Seq("AA", "ZZ", "BB", "YY", "GG"))
    val table = DataTable("TestTable", Seq(dataCol)).get

    val sortedView = DataSort.quickSort(table, SortItem("ColumnOne", Descending))

    sortedView.isSuccess should be(true)

    val columnData = sortedView.get.map(row => row.as[String]("ColumnOne"))
    columnData should be(Seq("ZZ", "YY", "GG", "BB", "AA"))
  }

  "A DataTable" can "be sorted by multiple string columns using explicit sort order of Ascending" in {
    val dataColOne = new DataColumn[String]("ColumnOne", Seq("AA", "AA", "AA", "BB", "BB", "BB", "CC", "CC", "CC"))
    val dataColTwo = new DataColumn[String]("ColumnTwo", Seq("Z", "Y", "X", "W", "V", "U", "T", "S", "R"))

    val table = DataTable("TestTable", Seq(dataColOne, dataColTwo)).get

    val sortItems = Seq(SortItem("ColumnOne", Ascending), SortItem("ColumnTwo", Ascending))
    val sortedView = DataSort.quickSort(table, sortItems)

    sortedView.isSuccess should be(true)

    val columnDataOne = sortedView.get.map(row => row.as[String]("ColumnOne"))
    columnDataOne should be(Seq("AA", "AA", "AA", "BB", "BB", "BB", "CC", "CC", "CC"))

    val columnDataTwo = sortedView.get.map(row => row.as[String]("ColumnTwo"))
    columnDataTwo should be(Seq("X", "Y", "Z", "U", "V", "W", "R", "S", "T"))
  }

  "A DataTable" can "be sorted by multiple string columns using different sort orders" in {
    val dataColOne = new DataColumn[String]("ColumnOne", Seq("AA", "AA", "AA", "BB", "BB", "BB", "CC", "CC", "CC"))
    val dataColTwo = new DataColumn[String]("ColumnTwo", Seq("Z", "Y", "X", "W", "V", "U", "T", "S", "R"))

    val table = DataTable("TestTable", Seq(dataColOne, dataColTwo)).get

    val sortItems = Seq(SortItem("ColumnOne", Descending), SortItem("ColumnTwo", Ascending))
    val sortedView = DataSort.quickSort(table, sortItems)

    sortedView.isSuccess should be(true)

    val columnDataOne = sortedView.get.map(row => row.as[String]("ColumnOne"))
    columnDataOne should be(Seq("CC", "CC", "CC", "BB", "BB", "BB", "AA", "AA", "AA"))

    val columnDataTwo = sortedView.get.map(row => row.as[String]("ColumnTwo"))
    columnDataTwo should be(Seq("R", "S", "T", "U", "V", "W", "X", "Y", "Z"))
  }
}
