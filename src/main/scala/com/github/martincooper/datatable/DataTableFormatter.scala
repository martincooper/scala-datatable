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

/** Handles the output of a DataTable in a displayable format. */
object DataTableFormatter {

  private case class ColumnDetails(colIdx: Int, colName: String, width: Int)

  private val lineSeparator = System.getProperty("line.separator")

  /** Builds a string displaying the data in a DataView. */
  def prettyPrint(view: DataView): String = {
    prettyPrint(view.table, view.rows)
  }

  /** Builds a string displaying the data in a DataTable. */
  def prettyPrint(table: DataTable): String = {
    prettyPrint(table, table)
  }

  private def prettyPrint(table: DataTable, dataRows: IndexedSeq[DataRow]): String = {
    val colDetails = table.columns.zipWithIndex.map {
      case (column, index) => ColumnDetails(index, column.name, colWidth(index, column.name, dataRows))
    }

    val builder = new StringBuilder

    printHeader(builder, colDetails)
    printRows(builder, dataRows, colDetails)

    builder.mkString
  }

  private def printRows(builder: StringBuilder, dataRows: IndexedSeq[DataRow], colDetails: Seq[ColumnDetails]) = {
    dataRows.foreach(dataRow => printRow(builder, dataRow, colDetails))
  }

  private def printRow(builder: StringBuilder, dataRow: DataRow, colDetails: Seq[ColumnDetails]) = {
    val formattedValues = colDetails.map(details => {
      val textValue = dataRow(details.colIdx).toString
      "|" + colDataString(textValue, details.width)
    })

    writelnExt(builder, formattedValues)
  }

  private def printHeader(builder: StringBuilder, colDetails: Seq[ColumnDetails]) = {
    val totalLength = colDetails.map(_.width).sum + (colDetails.length * 2)
    val headerFooter = padString("", totalLength, '-')

    writeln(builder)
    writelnExt(builder, headerFooter)

    val formattedHeaders = colDetails.map(col => "|" + colDataString(col.colName, col.width))

    writelnExt(builder, formattedHeaders)
    writelnExt(builder, headerFooter)
  }

  /** Returns the value correctly formatted according to the width. */
  private def colDataString(data: String, width: Int): String = {
    padString(data, width, ' ')
  }

  /** Calculates the column width to use for a column. */
  private def colWidth(colIdx: Int, colName: String, dataRows: IndexedSeq[DataRow]) = {
    val columnData = dataRows.map(row => row(colIdx))
    maxValueLength(colName +: columnData) + 2
  }

  /** Returns the max data length of all the data in a collection. */
  private def maxValueLength(values: Iterable[Any]): Int = {
    values.map(_.toString.length).max
  }

  private def writeln(builder: StringBuilder) = {
    builder ++= lineSeparator
  }

  private def writelnExt(builder: StringBuilder, value: Traversable[Any]) = {
    builder ++= value.mkString
    builder ++= lineSeparator
  }

  private def padString(value: String, width: Int, padString: Char): String = {
    value.padTo(width, padString).mkString
  }
}
