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

import scala.util.{ Success, Failure, Try }

/** DataSet stores a collection of DataTables */
case class DataSet private (name: String, tables: Vector[DataTable]) {
}

/** DataSet companion object. */
object DataSet {

  /** Validates tables and builds a new DataSet. */
  def apply(name: String, tables: Iterable[DataTable]): Try[DataSet] = {

    validateDataTables(tables) match {
      case Failure(ex) => new Failure(ex)
      case Success(_) => Success(new DataSet(name, tables.toVector))
    }
  }

  private def validateDataTables(tables: Iterable[DataTable]): Try[Unit] = {
    val tableSeq = tables.toSeq

    /** Check all tables have distinct table names. */
    if (tableSeq.groupBy(_.name).toSeq.length != tableSeq.length)
      return Failure(DataTableException("Tables contain duplicate names."))

    Success(Unit)
  }

  implicit class DataSetExt(val dataSet: DataSet) extends AnyVal {

    /** Creates a new DataSet with the additional table. */
    def addTable(newTable: DataTable): Try[DataSet] = {
      val newTableSet = dataSet.tables :+ newTable

      validateDataTables(newTableSet) match {
        case Failure(ex) => new Failure(ex)
        case Success(_) => Success(new DataSet(dataSet.name, newTableSet))
      }
    }

    /** Creates a new DataSet with the specified table removed. */
    def removeTable(tableName: String): Try[DataSet] = {

      dataSet.tables.exists(_.name == tableName) match {
        case true => Success(new DataSet(dataSet.name, dataSet.tables.filterNot(_.name == tableName)))
        case _ => Failure(DataTableException("Table " + tableName + " not found."))
      }
    }

    /** Creates a new DataSet with the specified table removed. */
    def removeTable(table: DataTable): Try[DataSet] = {

      dataSet.tables.exists(_ eq table) match {
        case true => Success(new DataSet(dataSet.name, dataSet.tables.filterNot(_ eq table)))
        case _ => Failure(DataTableException("Table not found."))
      }
    }
  }
}
