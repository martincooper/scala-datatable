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

import org.scalatest.{ Matchers, FlatSpec }

class DataSetSpec extends FlatSpec with Matchers {

  "A new DataSet" should "be creatable with a name and no tables" in {
    val dataSet = DataSet("TestDataSet", Seq())

    dataSet.isSuccess should be(true)
    dataSet.get.name should be("TestDataSet")
    dataSet.get.tables.length should be(0)
  }

  "A new DataSet" should "be creatable with a name and tables" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo))

    dataSet.isSuccess should be(true)
    dataSet.get.name should be("TestDataSet")
    dataSet.get.tables.length should be(2)
  }

  "A DataSet" should "allow a table to be added" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo)).get

    val tableThree = DataTable("TableThree").get

    val newDataSet = dataSet.add(tableThree)

    newDataSet.isSuccess should be(true)
    newDataSet.get.tables.length should be(3)
    newDataSet.get.tables.map(_.name) should be(Seq("TableOne", "TableTwo", "TableThree"))
  }

  "A DataSet" should "disallow a table with a duplicate name to be added" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo)).get

    val tableThree = DataTable("TableOne").get

    val newDataSet = dataSet.add(tableThree)

    newDataSet.isSuccess should be(false)
    newDataSet.failed.get.getMessage should be("Error adding table at specified index.")
  }

  "A DataSet" should "allow a table to be removed by name" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val tableThree = DataTable("TableThree").get

    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo, tableThree)).get

    val newDataSet = dataSet.remove("TableTwo")

    newDataSet.isSuccess should be(true)
    newDataSet.get.tables.length should be(2)
    newDataSet.get.tables.map(_.name) should be(Seq("TableOne", "TableThree"))
  }

  "A DataSet" should "disallow a table to be removed by name when its name is not found" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get

    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo)).get

    val newDataSet = dataSet.remove("TableOneHundred")

    newDataSet.isSuccess should be(false)
    newDataSet.failed.get.getMessage should be("Table TableOneHundred not found.")
  }
}
