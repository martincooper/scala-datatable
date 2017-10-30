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

package com.github.martincooper.datatable.DataColumnCollectionSpecs

import com.github.martincooper.datatable.{ DataColumn, DataTable, DataTableException }
import org.scalatest.{ FlatSpec, Matchers }

class DataColumnCollectionModificationSpec extends FlatSpec with Matchers {

  "A DataColumnCollection" should "allow a new column to be added" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })

    val originalTable = DataTable("TestTable", Seq(dataColOne, dataColTwo)).get

    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 10) map (i => if (i > 5) true else false))

    val newTable = originalTable.columns.add(dataColThree)

    newTable.isSuccess should be(true)
    newTable.get.columns.length should be(3)

    originalTable.columns.length should be(2)
  }

  it should "disallow a column with a duplicate name to be added" in {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })

    val originalTable = DataTable("TestTable", Seq(dataColOne, dataColTwo)).get

    val dataColThree = new DataColumn[Boolean]("ColOne", (0 to 10) map (i => true))

    val newTable = originalTable.columns.add(dataColThree)

    newTable.isSuccess should be(false)
    newTable.failed.get.getMessage should be("Error adding column at specified index.")
  }

  it should "allow a column to be removed by name" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })
    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 10) map (i => if (i > 5) true else false))

    val originalTable = DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree)).get

    val newTable = originalTable.columns.remove("ColTwo")

    newTable.isSuccess should be(true)
    newTable.get.columns.length should be(2)
    newTable.get.columns.map(_.name) should be(Seq("ColOne", "ColThree"))

    originalTable.columns.length should be(3)
  }

  it should "disallow an unknown column to be removed by name" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })

    val originalTable = DataTable("TestTable", Seq(dataColOne, dataColTwo)).get

    val newTable = originalTable.columns.remove("ColOneHundred")

    newTable.isSuccess should be(false)
    newTable.failed.get should be(DataTableException("Column ColOneHundred not found."))
  }
}
