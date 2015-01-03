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

package com.github.martincooper.datatable.DataRowCollectionSpecs

import com.github.martincooper.datatable.TypedDataValueImplicits._
import com.github.martincooper.datatable.{ DataValue, DataColumn, DataTable }
import org.scalatest.{ FlatSpec, Matchers }

class DataRowCollectionReplaceRowSpec extends FlatSpec with Matchers {

  def createTestTable: DataTable = {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 5) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 5) map { i => "Val" + i })
    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 5) map { i => false })

    DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree)).get
  }

  "DataRowCollection.replace" should "allow a valid row to be replaced at the specified row index" in {
    val originalTable = createTestTable

    // Pass the values as a set of DataValue objects.
    val newTable = originalTable.rows.replace(3, DataValue(100), DataValue("TestVal"), DataValue(true))

    newTable.isSuccess should be(true)

    originalTable.rowCount should be(6)
    newTable.get.rowCount should be(6)

    newTable.get.columns(0).data should be(Seq(0, 1, 2, 100, 4, 5))
    newTable.get.columns(1).data should be(Seq("Val0", "Val1", "Val2", "TestVal", "Val4", "Val5"))
    newTable.get.columns(2).data should be(Seq(false, false, false, true, false, false))
  }

  it should "allow a valid row to be replaced in the table using implicit value converters" in {
    val originalTable = createTestTable

    // Pass the values using the implicit value converter.
    val newTable = originalTable.rows.replace(3, 100, "TestVal", true)

    newTable.isSuccess should be(true)

    originalTable.rowCount should be(6)
    newTable.get.rowCount should be(6)

    newTable.get.columns(0).data should be(Seq(0, 1, 2, 100, 4, 5))
    newTable.get.columns(1).data should be(Seq("Val0", "Val1", "Val2", "TestVal", "Val4", "Val5"))
    newTable.get.columns(2).data should be(Seq(false, false, false, true, false, false))
  }

  it should "fail when a row is requested to be replaced with invalid index" in {
    val originalTable = createTestTable

    val newTable = originalTable.rows.replace(99, 100, "TestVal", true)

    newTable.isSuccess should be(false)
    newTable.failed.get.getMessage should be("Item index out of bounds for replace.")
  }

  it should "fail to replace a row when a value of invalid type is specified" in {
    val originalTable = createTestTable

    val newTable = originalTable.rows.replace(3, "SomeStringValue", "TestVal", true)

    newTable.isSuccess should be(false)
    newTable.failed.get.getMessage should be("Invalid value type on replace.")
  }

  it should "fail to replace a row when the number of values is less than the number of columns" in {
    val originalTable = createTestTable

    val newTable = originalTable.rows.replace(3, 100, "TestVal")

    newTable.isSuccess should be(false)
    newTable.failed.get.getMessage should be("Number of values does not match number of columns.")
  }

  it should "fail to replace a row when the number of values is more than the number of columns" in {
    val originalTable = createTestTable

    val newTable = originalTable.rows.replace(3, 100, "TestVal", true, "Another")

    newTable.isSuccess should be(false)
    newTable.failed.get.getMessage should be("Number of values does not match number of columns.")
  }
}
