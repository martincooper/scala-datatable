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

package com.github.martincooper.datatable.DataColumnSpecs

import com.github.martincooper.datatable.{DataValue, DataColumn}
import org.scalatest.{ Matchers, FlatSpec }

class DataColumnInsertValueSpec extends FlatSpec with Matchers {

  "A Data Column" should "be able to be insert an data value" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.insert(2, 99)

    result.isSuccess should be(true)
    result.get.name should be("TestCol")
    result.get.data should be(Seq(0, 1, 99, 2, 3, 4))

    originalColumn.data should be(Seq(0, 1, 2, 3, 4))
  }

  it should "not allow insert with invalid index" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.insert(99, 99)
    result.isSuccess should be(false)
    result.failed.get.getMessage should be("Item index out of bounds for insert.")
  }

  it should "not allow insert with value of invalid type" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.insert(2, "Invalid Value")
    result.isSuccess should be(false)
    result.failed.get.getMessage should be("Invalid value type on insert.")
  }

  it should "be able to be insert a new value using a DataValue item" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.insert(2, DataValue(99))

    result.isSuccess should be(true)
    result.get.name should be("TestCol")
    result.get.data.length should be(6)
    result.get.data(2) should be(99)

    originalColumn.data.length should be(5)
  }

  it should "prevent a invalid DataValue item type being inserted" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.insert(2, DataValue("Invalid Value"))

    result.isSuccess should be(false)
    result.failed.get.getMessage should be("Invalid value type on insert.")
  }
}
