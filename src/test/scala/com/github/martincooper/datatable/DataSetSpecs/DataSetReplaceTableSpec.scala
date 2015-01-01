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

package com.github.martincooper.datatable.DataSetSpecs

import com.github.martincooper.datatable.{ DataSet, DataTable }
import org.scalatest.{ FlatSpec, Matchers }

class DataSetReplaceTableSpec extends FlatSpec with Matchers {

  "A DataSet" should "allow a table to be replaced by name" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val tableThree = DataTable("TableThree").get

    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo, tableThree)).get

    val newDataSet = dataSet.replace("TableTwo", DataTable("NewTable").get)

    newDataSet.isSuccess should be(true)
    newDataSet.get.tables.length should be(3)
    newDataSet.get.tables.map(_.name) should be(Seq("TableOne", "NewTable", "TableThree"))
  }

  it should "disallow a table to be replaced by name when its name is not found" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val tableThree = DataTable("TableThree").get

    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo, tableThree)).get

    val newDataSet = dataSet.replace("TableOneHundred", DataTable("NewTable").get)

    newDataSet.isSuccess should be(false)
    newDataSet.failed.get.getMessage should be("Table TableOneHundred not found.")
  }

  it should "allow a table to be replaced by index" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val tableThree = DataTable("TableThree").get

    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo, tableThree)).get

    val newDataSet = dataSet.replace(1, DataTable("NewTable").get)

    newDataSet.isSuccess should be(true)
    newDataSet.get.tables.length should be(3)
    newDataSet.get.tables.map(_.name) should be(Seq("TableOne", "NewTable", "TableThree"))
  }

  it should "disallow a table to be replaced by index when it is not found" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val tableThree = DataTable("TableThree").get

    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo, tableThree)).get

    val newDataSet = dataSet.replace(100, DataTable("NewTable").get)

    newDataSet.isSuccess should be(false)
    newDataSet.failed.get.getMessage should be("Item index out of bounds for replace.")
  }

  it should "allow a table to be replaced by reference" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val tableThree = DataTable("TableThree").get

    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo, tableThree)).get

    val newDataSet = dataSet.replace(tableTwo, DataTable("NewTable").get)

    newDataSet.isSuccess should be(true)
    newDataSet.get.tables.length should be(3)
    newDataSet.get.tables.map(_.name) should be(Seq("TableOne", "NewTable", "TableThree"))
  }

  it should "disallow a table to be replaced by reference when it is not found" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val tableThree = DataTable("TableThree").get

    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo, tableThree)).get

    val unknownTable = DataTable("UnknownTableTable").get
    val newDataSet = dataSet.replace(unknownTable, DataTable("NewTable").get)

    newDataSet.isSuccess should be(false)
    newDataSet.failed.get.getMessage should be("Item index out of bounds for replace.")
  }
}
