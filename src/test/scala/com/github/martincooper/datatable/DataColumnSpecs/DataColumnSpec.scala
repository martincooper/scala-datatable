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

package com.github.martincooper.datatable.DataColumnSpecs

import com.github.martincooper.datatable.{ DataColumn, GenericColumn }
import org.scalatest._

class DataColumnSpec extends FlatSpec with Matchers {

  "A Data Column" should "be able to be created with a name and default data" in {
    val newSeq = (0 to 19) map { i => i }
    val dataColumn = new DataColumn[Int]("TestCol", newSeq)

    dataColumn.name should be("TestCol")
    dataColumn.data.length should be(20)
    dataColumn.data(11) should be(11)
  }

  "A Generic Column" should "be able to be cast back to its original type" in {
    val newSeq = (0 to 19) map { i => i }
    val dataColumn = new DataColumn[Int]("TestCol", newSeq)

    val genericColumn = dataColumn.asInstanceOf[GenericColumn]

    genericColumn.name should be("TestCol")
    genericColumn.data.length should be(20)

    val typedCol = genericColumn.as[Int]
    typedCol.data(10) should be(10)
  }

  "A Data Column" should "be able to be add a new data value" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 19) map { i => i })

    val result = originalColumn.add(99)

    result.isSuccess should be(true)
    result.get.name should be("TestCol")
    result.get.data.length should be(21)
    result.get.data(20) should be(99)

    originalColumn.data.length should be(20)
  }

  "A Data Column" should "be able to be replace an existing data value" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.replace(2, 99)

    result.isSuccess should be(true)
    result.get.name should be("TestCol")
    result.get.data.length should be(5)
    result.get.data(2) should be(99)

    originalColumn.data(2) should be(2)
  }

  "A Data Column" should "not allow replace with invalid index" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.replace(99, 99)
    result.isSuccess should be(false)
  }

  "A Data Column" should "be able to be insert an existing data value" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 3) map { i => i })

    val result = originalColumn.insert(2, 99)

    result.isSuccess should be(true)
    result.get.name should be("TestCol")
    result.get.data.length should be(5)
    result.get.data(2) should be(99)

    originalColumn.data.length should be(4)
  }

  "A Data Column" should "not allow insert with invalid index" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.insert(99, 99)
    result.isSuccess should be(false)
  }

  "A Data Column" should "not allow insert data of invalid type" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.insert(2, "String Value")
    result.isSuccess should be(false)
  }

  "A Data Column" should "be able to remove an existing data value" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.remove(2)

    result.isSuccess should be(true)
    result.get.name should be("TestCol")
    result.get.data.length should be(4)

    originalColumn.data.length should be(5)
  }

  "A Data Column" should "not allow remove with invalid index" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.remove(99)
    result.isSuccess should be(false)
  }
}
