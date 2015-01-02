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

package com.github.martincooper.datatable.DataTableSpecs

import com.github.martincooper.datatable.{ DataColumn, DataTable, DataTableException, GenericColumn }
import org.scalatest.{ FlatSpec, Matchers }

class DataTableSpec extends FlatSpec with Matchers {

  "A new DataTable" can "be created with a name and no columns" in {
    val dataTable = DataTable("TestTable")

    dataTable.isSuccess should be(true)
    dataTable.get.name should be("TestTable")
    dataTable.get.columns.length should be(0)
  }

  it can "be created with a name and default columns" in {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 19) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 19) map { i => "Value : " + i })

    val result = DataTable("TestTable", Seq(dataColOne, dataColTwo))

    result.isSuccess should be(true)
    val dataTable = result.get

    dataTable.name should be("TestTable")
    dataTable.columns.length should be(2)

    dataTable.columns(0).data(4) shouldBe a[Integer]
    dataTable.columns(0).data(4) should be(4)

    dataTable.columns(1).data(4) shouldBe a[String]
    dataTable.columns(1).data(4) should be("Value : 4")
  }

  it should "return correct row count" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 19) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 19) map { i => "Value : " + i })

    val result = DataTable("TestTable", Seq(dataColOne, dataColTwo))

    result.isSuccess should be(true)
    result.get.rowCount should be(20)
  }

  it should "return correct row count when it has no columns" in {

    val result = DataTable("TestTable", Seq[GenericColumn]())

    result.isSuccess should be(true)
    result.get.rowCount should be(0)
  }

  it should "prevent different column lengths" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 20) map { i => "Value : " + i })

    val result = DataTable("TestTable", Seq(dataColOne, dataColTwo))

    result.isFailure should be(true)
    result.failed.get should be(DataTableException("Columns have uneven row count."))
  }

  it should "prevent duplicate column names" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColOne", (0 to 10) map { i => "Value : " + i })

    val result = DataTable("TestTable", Seq(dataColOne, dataColTwo))

    result.isFailure should be(true)
    result.failed.get should be(DataTableException("Columns contain duplicate names."))
  }
}
