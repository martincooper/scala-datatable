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

import scala.util.{ Try, Success }

/** DataTable class. Handles the immutable storage of data in a Row / Column format. */
class DataTable(tableName: String, dataColumns: Iterable[GenericColumn]) {

  def name = tableName
  def columns = dataColumns.toIndexedSeq

  /** Returns the data column at the selected index. */
  def apply(index: Int) = {
    Try(columns(index)) match {
      case Success(col) => Some(col)
      case _ => None
    }
  }

  /** Returns the column with the specified name. */
  def apply(columnName: String) = columns.find(_.name == columnName)

  /** Outputs a more detailed toString implementation. */
  override def toString = {
    val tableDetails = "DataTable:" + name + "[Rows:" + columns.head.data.length + "]"
    val colDetails = columns.map(col => "[" + col.toString + "]").mkString(" ")

    tableDetails + colDetails
  }
}
