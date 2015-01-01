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

import com.github.martincooper.datatable.{ GenericColumn, DataColumn, DataTable }
import scala.util.Try

class DataColumnModificationExamples {

  def addColumn(dataTable: DataTable): Try[DataTable] = {
    val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
    dataTable.columns.add(stringCol)
  }

  def removeColumnByName(dataTable: DataTable): Try[DataTable] = {
    dataTable.columns.remove("ColumnToRemove")
  }

  def removeColumnByIndex(dataTable: DataTable): Try[DataTable] = {
    dataTable.columns.remove(1)
  }

  def removeColumn(dataTable: DataTable, columnToRemove: GenericColumn): Try[DataTable] = {
    dataTable.columns.remove(columnToRemove)
  }

  def insertColumnByName(dataTable: DataTable): Try[DataTable] = {
    val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
    dataTable.columns.insert("ColumnTwo", stringCol)
  }

  def insertColumnByIndex(dataTable: DataTable): Try[DataTable] = {
    val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
    dataTable.columns.insert(2, stringCol)
  }

  def insertColumn(dataTable: DataTable, insertBeforeColumn: GenericColumn): Try[DataTable] = {
    val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
    dataTable.columns.insert(insertBeforeColumn, stringCol)
  }

  def replaceColumnByName(dataTable: DataTable): Try[DataTable] = {
    val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
    dataTable.columns.replace("ColumnTwo", stringCol)
  }

  def replaceColumnByIndex(dataTable: DataTable): Try[DataTable] = {
    val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
    dataTable.columns.replace(2, stringCol)
  }

  def replaceColumn(dataTable: DataTable, columnToReplace: GenericColumn): Try[DataTable] = {
    val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
    dataTable.columns.replace(columnToReplace, stringCol)
  }
}
