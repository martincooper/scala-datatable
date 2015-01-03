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

import com.github.martincooper.datatable.TypedDataValueImplicits._
import com.github.martincooper.datatable.{DataColumn, DataTable}
import scala.util.Try

class DataRowModificationExamples {

  def addRow(dataTable: DataTable): Try[DataTable] = {
    // Add a new row containing 4 values.
    dataTable.rows.add("New Value", 100 , true, 5.5d)
  }

  def insertRow(dataTable: DataTable): Try[DataTable] = {
    // Insert a new row containing 4 values at row index 10.
    dataTable.rows.insert(10, "New Value", 100, true, 5.5d)
  }

  def replaceRow(dataTable: DataTable): Try[DataTable] = {
    // Replace the row values at row index 10 with the new row values.
    dataTable.rows.replace(10, "New Value", 100, true, 5.5d)
  }

  def removeRow(dataTable: DataTable): Try[DataTable] = {
    // Remove the row at the specified index.
    dataTable.rows.remove(10)
  }

  def addRow(): Try[DataTable] = {
    // First create a table with the required columns.
    val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
    val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
    val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

    val dataTable = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol)).get

    // Call rows.add, providing the correct number of values and of the correct type.
    // This will return a new DataTable with the data appended as a new row at the end.
    dataTable.rows.add("New Value", 1000, false)
  }

  def removeRow(): Try[DataTable] = {
    // First create a table with the required columns.
    val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
    val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
    val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

    val dataTable = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol)).get

    // Call rows.remove, providing the rowIndex.
    // This will return a new DataTable with the row at the specified index removed.
    val rowIndex = 50
    dataTable.rows.remove(rowIndex)
  }

  def insertRow(): Try[DataTable] = {
    // First create a table with the required columns.
    val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
    val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
    val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

    val dataTable = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol)).get

    // Call rows.insert, providing the correct number of values and of the correct type.
    // This will return a new DataTable with the data provided inserted as a new row
    // at the specified index.
    val rowIndex = 50
    dataTable.rows.insert(rowIndex, "New Value", 1000, false)
  }

  def replaceRow(): Try[DataTable] = {
    // First create a table with the required columns.
    val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
    val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
    val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

    val dataTable = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol)).get

    // Call rows.replace, providing the correct number of values and of the correct type.
    // This will return a new DataTable with the data provided replacing the data at
    // the row at the specified index.
    val rowIndex = 50
    dataTable.rows.replace(rowIndex, "New Value", 1000, false)
  }
}
