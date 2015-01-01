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

import com.github.martincooper.datatable.{ DataTable, DataColumn }

import scala.util.Try

class DataRowExamples {

  def accessRowDataAsUntypedAndUnchecked(table: DataTable): Unit = {

    // Iterate through all rows in the table, printing out the value (regardless of type)
    // of the column number specified.
    table.foreach(dataRow => println(dataRow(0).toString))

    // Iterate through all rows in the table, printing out the value (regardless of type)
    // of the named column.
    table.foreach(dataRow => println(dataRow("ColNameOne").toString))
  }

  def accessRowDataAsTypedAndUnchecked(table: DataTable): Unit = {

    // Iterate through all rows in the table, specifying as explicit Integer col,
    // printing out value * 5 of the column number specified.
    table.foreach(dataRow => println(dataRow.as[Int](0) * 5))

    // Iterate through all rows in the table, specifying as explicit Integer col,
    // printing out value * 5 of the named column.
    table.foreach(dataRow => println(dataRow.as[Int]("ColNameTwo") * 5))
  }

  def accessRowDataAsUntypedAndChecked(table: DataTable): Unit = {

    // Iterate through all rows in the table, printing out the value (regardless of type)
    // of the column number specified.
    table.foreach(dataRow => {
      dataRow.get(0).map { cellValue => println(cellValue.toString) }
    })

    // Iterate through all rows in the table, printing out the value (regardless of type)
    // of the named column.
    table.foreach(dataRow => {
      dataRow.get("ColNameOne").map { cellValue => println(cellValue.toString) }
    })
  }

  def accessRowDataAsTypedAndChecked(table: DataTable): Unit = {

    // Iterate through all rows in the table, printing out the value (regardless of type)
    // of the column number specified.
    table.foreach(dataRow => {
      dataRow.getAs[Int](0).map { cellValue => println(cellValue + 5) }
    })

    // Iterate through all rows in the table, printing out the value (regardless of type)
    // of the named column.
    table.foreach(dataRow => {
      dataRow.getAs[Int]("ColNameOne").map { cellValue => println(cellValue + 5) }
    })
  }
}
