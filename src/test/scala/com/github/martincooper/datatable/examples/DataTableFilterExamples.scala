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

package com.github.martincooper.datatable.examples

import com.github.martincooper.datatable.{ DataRow, DataTable, DataColumn }

import scala.util.{ Failure, Success, Random }

class DataTableFilterExamples {

  def filterData(dataTable: DataTable) = {

    // Filter the data using the RowData object.
    val dataRows = dataTable.filter(row => {
      row.as[String]("FirstName").startsWith("Ma") && row.as[Int]("Age") > 18
    })

    // Access the filtered results...
    println(dataRows.length)

    // Row data can be accessed using indexers with no type information...
    dataRows.foreach(row => println(row(0).toString + " : " + row(1).toString))

    // Or by specifying the columns by name and with full type info.
    dataRows.foreach(row =>
      println(row.as[String]("AddressOne") + " : " + row.as[Int]("HouseNumber")))
  }

  def simpleDataAccess(dataRow: DataRow) = {

    // Calling dataRow.values returns a IndexedSeq[Any] of all values in the row.
    println(dataRow.values)

    // Calling dataRow.valueMap returns a Map[String, Any] of all values
    // in the row mapping column name to value.
    println(dataRow.valueMap)
  }

  def typedAndCheckedDataAccess(dataRow: DataRow) = {

    // Each .getAs[T] is type checked and bounds / column name
    // checked so can be composed safely.
    val checkedValue = for {
      name <- dataRow.getAs[String]("FirstName")
      age <- dataRow.getAs[Int]("Age")
    } yield name + " is " + age + " years old."

    checkedValue match {
      case Success(value) => println(value)
      case Failure(ex) => println("Error occurred : " + ex.getMessage)
    }
  }
}
