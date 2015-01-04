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

package com.github.martincooper.datatable

import org.scalatest.{ Matchers, FlatSpec }

/** Additional tests around filtering a DataTable. */
class DataFilterSpec extends FlatSpec with Matchers {

  private def buildTestTable(): DataTable = {
    val dataColOne = new DataColumn[Int]("IntegerCol", (0 to 50) map { i => i })
    val dataColTwo = new DataColumn[String]("StringCol", (0 to 50) map { i => "Value : " + i })
    val dataColThree = new DataColumn[Boolean]("BoolCol", (0 to 50) map { i => i % 2 == 0 })
    val dataColFour = new DataColumn[Long]("LongCol", (0 to 50).map(i => i.toLong).reverse)

    DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree, dataColFour)).get
  }

  "A DataTable" can "be filtered by row values." in {
    val table = buildTestTable()

    // Simple single filter on table, returning collection of rows matching the filter.
    val dataView = table.filter(row => row.as[Int]("IntegerCol") > 10)

    dataView shouldBe a[DataView]

    dataView.length should be(40)
    dataView(0).as[Int]("IntegerCol") should be(11)
    dataView(0).as[String]("StringCol") should be("Value : 11")
    dataView(0).as[Boolean]("BoolCol") should be(false)
    dataView(0).as[Long]("LongCol") should be(39)
  }

  it can "be filtered multiple times by row values returning a DataView." in {
    val table = buildTestTable()

    // Perform filtering, and additional filtering on the previous DataRow results.
    val dataViewOne = table.filter(row => row.as[Int]("IntegerCol") > 10)
    val dataViewTwo = dataViewOne.filter(row => row.as[Long]("LongCol") > 10)
    val dataViewThree = dataViewTwo.filter(row => row.as[Boolean]("BoolCol"))

    dataViewThree shouldBe a[DataView]
    dataViewThree.length should be(14)

    dataViewThree(0).as[Int]("IntegerCol") should be(12)
    dataViewThree(0).as[String]("StringCol") should be("Value : 12")
    dataViewThree(0).as[Boolean]("BoolCol") should be(true)
    dataViewThree(0).as[Long]("LongCol") should be(38)
  }
}
