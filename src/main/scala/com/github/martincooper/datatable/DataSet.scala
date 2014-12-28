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

/** ModifiableByName, with additional item (DataTable) indexer. */
trait ModifiableByTable[V, R] extends ModifiableByName[V, R] {
  def replace(oldItem: DataTable, newItem: V): Try[R]
  def insert(itemToInsertAt: DataTable, newItem: V): Try[R]
  def remove(itemToRemove: DataTable): Try[R]
}

/** DataSet class. Stores a collection of DataTables */
class DataSet private (dataSetName: String, dataTables: Iterable[DataTable])
    extends IndexedSeq[DataTable] with ModifiableByTable[DataTable, DataSet] {

  def name = dataSetName
  def tables = dataTables.toVector

  /** Creates a new DataSet with the table specified replaced with the new table. */
  override def replace(oldTable: DataTable, newTable: DataTable): Try[DataSet] = {
    replace(tables.indexWhere(table => table eq oldTable), newTable)
  }

  /** Creates a new DataSet with the table specified replaced with the new table. */
  override def replace(tableName: String, table: DataTable): Try[DataSet] = {
    actionByTableName(tableName, colIdx => replace(colIdx, table))
  }

  /** Creates a new DataSet with the table specified replaced with the new table. */
  override def replace(index: Int, table: DataTable): Try[DataSet] = {
    checkTablesAndBuild("replacing", () => IndexedSeqExtensions.replaceItem(tables, index, table))
  }

  /** Creates a new DataSet with the table inserted at the specified location. */
  override def insert(tableToInsertAt: DataTable, newTable: DataTable): Try[DataSet] = {
    insert(tables.indexWhere(table => table eq tableToInsertAt), newTable)
  }

  /** Creates a new DataSet with the table inserted at the specified location. */
  override def insert(tableName: String, table: DataTable): Try[DataSet] = {
    actionByTableName(tableName, colIdx => insert(colIdx, table))
  }

  /** Creates a new DataSet with the table inserted at the specified location. */
  override def insert(index: Int, table: DataTable): Try[DataSet] = {
    checkTablesAndBuild("inserting", () => IndexedSeqExtensions.insertItem(tables, index, table))
  }

  /** Creates a new DataSet with the table at the specified location removed. */
  override def remove(tableToRemove: DataTable): Try[DataSet] = {
    remove(tables.indexWhere(table => table eq tableToRemove))
  }

  /** Creates a new DataSet with the table at the specified location removed. */
  override def remove(tableName: String): Try[DataSet] = {
    actionByTableName(tableName, colIdx => remove(colIdx))
  }

  /** Creates a new DataSet with the table at the specified location removed. */
  override def remove(index: Int): Try[DataSet] = {
    checkTablesAndBuild("removing", () => IndexedSeqExtensions.removeItem(tables, index))
  }

  /** Creates a new DataSet with the additional table. */
  override def add(table: DataTable): Try[DataSet] = {
    checkTablesAndBuild("adding", () => IndexedSeqExtensions.addItem(tables, table))
  }

  override def length: Int = tables.length

  override def apply(idx: Int): DataTable = tables(idx)

  private def actionByTableName(tableName: String, action: Int => Try[DataSet]): Try[DataSet] = {
    tables.indexWhere(_.name == tableName) match {
      case -1 => Failure(DataTableException("Table " + tableName + " not found."))
      case tableIdx: Int => action(tableIdx)
    }
  }

  /** Checks that the new table set is valid, and builds a new DataSet. */
  private def checkTablesAndBuild(modification: String, checkTables: () => Try[IndexedSeq[DataTable]]): Try[DataSet] = {

    val newTables = for {
      newTableSet <- checkTables()
      result <- DataSet.validateDataTables(newTableSet)
    } yield newTableSet

    newTables match {
      case Success(modifiedTableSet) => new Success[DataSet](new DataSet(name, modifiedTableSet))
      case Failure(ex) => Failure(ex)
    }
  }
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

  def validateDataTables(tables: Iterable[DataTable]): Try[Unit] = {
    val tableSeq = tables.toSeq

    /** Check all tables have distinct table names. */
    tableSeq.groupBy(_.name).toSeq.length != tableSeq.length match {
      case true => Failure(DataTableException("Tables contain duplicate names."))
      case false => Success(Unit)
    }
  }
}
