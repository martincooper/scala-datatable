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

package com.github.martincooper.datatable.DataViewSpecs

import com.github.martincooper.datatable.{ DataColumn, DataTable }
import org.scalatest.{ FlatSpec, Matchers }

class DataViewToDataTableSpec extends FlatSpec with Matchers {

  private def buildTestTable(): DataTable = {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 99) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 99) map { i => "Value : " + i })
    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 99) map { i => i % 2 == 0 })

    DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree)).get
  }

  "A new DataView" can "be converted to a DataTable" in {
    val dataTable = buildTestTable()
    val dataView = dataTable.toDataView

    val newDataTable = dataView.toDataTable

    newDataTable.name should be("TestTable")
    newDataTable.rowCount should be(100)
    newDataTable.columns.length should be(3)
  }

  it can "be converted to a DataTable from a filtered set" in {
    val dataTable = buildTestTable()
    val filteredDataView = dataTable.filter(row => row.as[Int]("ColOne") > 49)

    val newDataTable = filteredDataView.toDataTable

    newDataTable.name should be("TestTable")
    newDataTable.rowCount should be(50)
    newDataTable.columns.length should be(3)
  }

  it can "be converted to a DataTable from multiple filtered sets" in {
    val dataTable = buildTestTable()

    val filteredDataOne = dataTable.filter(row => row.as[Int]("ColOne") > 49)
    val filteredDataTwo = filteredDataOne.filter(row => !row.as[Boolean]("ColThree"))
    val filteredDataThree = filteredDataTwo.filter(row => row.as[String]("ColTwo").endsWith("5"))

    val newDataTable = filteredDataThree.toDataTable

    newDataTable.name should be("TestTable")
    newDataTable.rowCount should be(5)
    newDataTable.columns.length should be(3)

    newDataTable.columns(0).data should be(Seq(55, 65, 75, 85, 95))
  }
}