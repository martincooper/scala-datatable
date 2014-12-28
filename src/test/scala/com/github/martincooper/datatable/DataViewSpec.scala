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

class DataViewSpec extends FlatSpec with Matchers {

  private def buildTestTable(): DataTable = {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })
    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 10) map { i => true })

    DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree)).get
  }

  "A new DataView" can "be created with valid data rows" in {
    val dataTable = buildTestTable()

    val dataView = DataView(dataTable, dataTable.take(5))

    dataView.isSuccess should be(true)
    dataView.get.length should be(5)
  }

  "A new DataView" can "be created with no data rows" in {
    val dataTable = buildTestTable()

    val dataView = DataView(dataTable)

    dataView.isSuccess should be(true)
    dataView.get.length should be(0)
  }

  "A new DataView" should "fail if table different to row table reference" in {
    val dataTable = buildTestTable()

    val dataRows = dataTable.take(5)
    val dataView = DataView(DataTable("DifferentTable").get, dataRows)

    dataView.isSuccess should be(false)
    dataView.failed.get.getMessage should be("DataRows do not all belong to the specified table.")
  }
}