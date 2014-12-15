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

import org.scalatest.{Matchers, FlatSpec}

class DataTableSpec extends FlatSpec with Matchers {

  "A new DataTable" should "be created with a name and no data" in {
    val dataTable = new DataTable("TestTable", Array().toIndexedSeq)

    dataTable.name should be ("TestTable")
    dataTable.columns.length should be (0)
  }

  "A new DataTable" should "be created with a name and default columns" in {

    val seqOne = (0 to 19) map { i => i }
    val dataColOne = new DataColumn[Int]("ColOne", seqOne)

    val seqTwo = (0 to 19) map { i => "Value : " + i }
    val dataColTwo = new DataColumn[String]("ColTwo", seqTwo)

    val dataTable = new DataTable("TestTable", Array(dataColOne, dataColTwo))

    dataTable.name should be ("TestTable")
    dataTable.columns.length should be (2)

    dataTable.columns(0).data(4) shouldBe a [Integer]
    dataTable.columns(0).data(4) should be (4)

    dataTable.columns(1).data(4) shouldBe a [String]
    dataTable.columns(1).data(4) should be ("Value : 4")
  }
}
