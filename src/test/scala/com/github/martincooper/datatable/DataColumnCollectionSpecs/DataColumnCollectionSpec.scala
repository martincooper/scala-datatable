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

package com.github.martincooper.datatable.DataColumnCollectionSpecs

import com.github.martincooper.datatable.{ DataColumn, DataColumnCollection, DataTable }
import org.scalatest.{FlatSpec, Matchers}

class DataColumnCollectionSpec extends FlatSpec with Matchers {

  "A new DataColumnCollection" can "be correctly created" in {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })
    val dataTable = DataTable("TestTable").get

    val dataColumnCollection = DataColumnCollection(dataTable, Seq(dataColOne, dataColTwo))

    dataColumnCollection.length should be(2)
    dataColumnCollection.columns(0) should be(dataColOne)
    dataColumnCollection.columns(1) should be(dataColTwo)
    dataColumnCollection.table should be(dataTable)
  }
}
