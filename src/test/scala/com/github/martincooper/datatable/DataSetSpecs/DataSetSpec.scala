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

package com.github.martincooper.datatable.DataSetSpecs

import com.github.martincooper.datatable.{ DataSet, DataTable }
import org.scalatest.{ FlatSpec, Matchers }

class DataSetSpec extends FlatSpec with Matchers {

  "A new DataSet" should "be creatable with a name and no tables" in {
    val dataSet = DataSet("TestDataSet", Seq())

    dataSet.isSuccess should be(true)
    dataSet.get.name should be("TestDataSet")
    dataSet.get.tables.length should be(0)
  }

  "A new DataSet" should "be creatable with a name and tables" in {
    val tableOne = DataTable("TableOne").get
    val tableTwo = DataTable("TableTwo").get
    val dataSet = DataSet("TestDataSet", Seq(tableOne, tableTwo))

    dataSet.isSuccess should be(true)
    dataSet.get.name should be("TestDataSet")
    dataSet.get.tables.length should be(2)
  }
}
