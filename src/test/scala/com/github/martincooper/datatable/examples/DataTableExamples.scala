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

import com.github.martincooper.datatable.{ DataTable, DataColumn }

import scala.util.Try

class DataTableExamples {

  def createDataTable() : Try[DataTable] = {

    // Data columns created using a unique column name and a collection of values.
    val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
    val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
    val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

    // DataTable created with using a table name and a collection of Data Columns.
    val dataTableOption = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol))

    // If any of the columns fail validation (duplicate column names, or columns contain
    // data of different lengths), then it'll return a Failure. Else Success[DataTable]
    dataTableOption
  }

  def addColumn(dataTable: DataTable): Try[DataTable] = {

    // Create a new column.
    val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))

    // Call add to return a new Try[DataTable] structure with the additional column.
    val updatedTable = dataTable.add(stringCol)

    // If adding the additional column fails validation (duplicate column names, or columns
    // contain data of different lengths), then it'll return a Failure. Else Success[DataTable]
    updatedTable
  }

  def removeColumn(dataTable: DataTable): Try[DataTable] = {

    // Call remove to return a new DataTable structure with the additional column.
    val updatedTable = dataTable.remove("ColumnToRemove")

    // If removing the column fails validation (column name not found),
    // then it'll return a Failure. Else Success[DataTable]
    updatedTable
  }
}
