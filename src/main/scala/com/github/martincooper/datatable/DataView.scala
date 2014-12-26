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

import scala.util.{ Failure, Success, Try }

/** Provides a view over a DataTable to store filtered data sets. */
class DataView private (dataTable: DataTable, dataRows: Iterable[DataRow]) extends IndexedSeq[DataRow] {

  def table = dataTable
  def rows = dataRows.toVector

  override def length: Int = rows.length

  override def apply(idx: Int): DataRow = rows(idx)
}

object DataView {

  def apply(sourceDataTable: DataTable, dataRows: Iterable[DataRow]): Try[DataView] = {
    val indexedData = dataRows.toIndexedSeq

    /** Validate all data rows belong to the same DataTable to ensure data integrity. */
    val tableValid = indexedData.forall(row => row.table eq sourceDataTable)

    tableValid match {
      case true => Success(new DataView(sourceDataTable, indexedData))
      case _ => Failure(DataTableException("DataRows do not all belong to the specified table."))
    }
  }
}