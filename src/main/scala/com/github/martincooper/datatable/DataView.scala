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

package com.github.martincooper.datatable

import scala.util.{ Failure, Success, Try }

/** Provides a view over a DataTable to store filtered data sets. */
class DataView private (dataTable: DataTable, dataRows: Iterable[DataRow]) extends IndexedSeq[DataRow] {

  val table = dataTable
  val rows = dataRows.toVector

  override def length: Int = rows.length

  override def apply(idx: Int): DataRow = rows(idx)
}

object DataView {

  def apply(sourceDataTable: DataTable): Try[DataView] = {
    Success(new DataView(sourceDataTable, Seq()))
  }

  def apply(sourceDataTable: DataTable, dataRows: Iterable[DataRow]): Try[DataView] = {
    val indexedData = dataRows.toIndexedSeq

    validateDataRows(sourceDataTable, indexedData) match {
      case Success(_) => Success(new DataView(sourceDataTable, indexedData))
      case Failure(ex) => Failure(ex)
    }
  }

  /** Validate all data rows belong to the same DataTable to ensure data integrity. */
  def validateDataRows(sourceDataTable: DataTable, dataRows: Iterable[DataRow]): Try[Unit] = {
    val indexedData = dataRows.toIndexedSeq

    indexedData.forall(row => row.table eq sourceDataTable) match {
      case false => Failure(DataTableException("DataRows do not all belong to the specified table."))
      case true => Success(Unit)
    }
  }
}