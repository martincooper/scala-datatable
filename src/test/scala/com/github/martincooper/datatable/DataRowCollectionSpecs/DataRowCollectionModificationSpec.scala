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

package com.github.martincooper.datatable.DataRowCollectionSpecs

import com.github.martincooper.datatable.{ DataColumn, DataColumnCollection, DataTable, DataTableException }
import org.scalatest.{ FlatSpec, Matchers }

class DataRowCollectionModificationSpec extends FlatSpec with Matchers {

  "A DataRowCollection" should "allow a row to be removed by valid index" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 5) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 5) map { i => "Val" + i })

    val originalTable = DataTable("TestTable", Seq(dataColOne, dataColTwo)).get

    val newTable = originalTable.rows.remove(2)

    newTable.isSuccess should be(true)
    newTable.get.columns.length should be(2)

    originalTable.rowCount() should be(6)
    newTable.get.rowCount should be(5)

    newTable.get.columns(0).data should be(Seq(0, 1, 3, 4, 5))
    newTable.get.columns(1).data should be(Seq("Val0", "Val1", "Val3", "Val4", "Val5"))
  }

  "A DataRowCollection" should "allow a row to be removed by valid DataRow item" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 5) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 5) map { i => "Val" + i })

    val originalTable = DataTable("TestTable", Seq(dataColOne, dataColTwo)).get

    val dataRow = originalTable.find(row => row.as[Int](0) == 2).get
    val newTable = originalTable.rows.remove(dataRow)

    newTable.isSuccess should be(true)
    newTable.get.columns.length should be(2)

    originalTable.rowCount() should be(6)
    newTable.get.rowCount should be(5)

    newTable.get.columns(0).data should be(Seq(0, 1, 3, 4, 5))
    newTable.get.columns(1).data should be(Seq("Val0", "Val1", "Val3", "Val4", "Val5"))
  }
}
