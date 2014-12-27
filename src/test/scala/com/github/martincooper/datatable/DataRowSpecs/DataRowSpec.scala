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

package com.github.martincooper.datatable.DataRowSpecs

import com.github.martincooper.datatable.{ DataColumn, DataRow, DataTable }
import org.scalatest.{ FlatSpec, Matchers }

class DataRowSpec extends FlatSpec with Matchers {

  private def buildTestTable(): DataTable = {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })
    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 10) map { i => true })

    DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree)).get
  }

  "A new DataRow" can "be created with a valid table and index" in {
    val testTable = buildTestTable()
    val dataRow = DataRow(testTable, 5)

    dataRow.isSuccess should be(true)
    dataRow.get.rowIndex should be(5)
    dataRow.get.table should be(testTable)
  }

  "Creating a new DataRow" should "fail with an invalid index" in {
    val testTable = buildTestTable()
    val dataRow = DataRow(testTable, 500)

    dataRow.isSuccess should be(false)
    dataRow.failed.get.getMessage should be("Invalid row index for DataRow.")
  }
}
