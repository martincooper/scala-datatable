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

import com.github.martincooper.datatable.DataColumn
import org.scalatest.{ Matchers, FlatSpec }

class DataColumnAddValueSpec extends FlatSpec with Matchers {

  "A Data Column" should "be able to be add a new data value" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.addAs(99)

    result.isSuccess should be(true)
    result.get.name should be("TestCol")
    result.get.data.length should be(6)
    result.get.data(5) should be(99)

    originalColumn.data.length should be(5)
  }

  it should "prevent a invalid value type being added" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i => i })

    val result = originalColumn.addAs("Invalid Value")

    result.isSuccess should be(false)
    result.failed.get.getMessage should be("Invalid value type on add.")
  }
}
