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

package com.github.martincooper.datatable.examples

import com.github.martincooper.datatable.{DataTable, DataColumn}

import scala.util.Random

class DataTableFilterExamples {

  def filterData() = {

    // Some random data to fill the table.
    val randString = () => Random.alphanumeric.take(10).mkString.toUpperCase
    val randInt = () => Random.nextInt()

    // Data columns created using a column name and a collection of values.
    val stringCol = new DataColumn[String]("StringColumn", (1 to 1000).map(i => randString()))
    val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 1000).map(i => randInt()))

    // DataTable created with using a table name and a collection of Data Columns.
    val dataTableOption = DataTable("NewTable", Seq(stringCol, integerCol))

    val dataTable = dataTableOption.get

    // Filter the data using the RowData object.
    val filteredData = dataTable.filter(row => {
      row.as[String]("StringColumn").startsWith("A") && row.as[Int]("IntegerColumn") > 10
    })

    // Filtered Results...
    println(filteredData.length)

    // Row data can be accessed using indexers and with no type information...
    filteredData.foreach(row => println(row(0).toString + " : " + row(1).toString))

    // Or by specifying named columns with type info.
    filteredData.foreach(row => println(row.as[String]("StringColumn") + " : " + row.as[Int]("IntegerColumn").toString))
  }

}
