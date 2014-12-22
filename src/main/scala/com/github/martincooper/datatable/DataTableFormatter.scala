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

/** Handles the output of a DataTable in a displayable format. */
object DataTableFormatter {

  private case class ColumnDetails(Col: GenericColumn, Width: Int)

  private val lineSeparator = System.getProperty("line.separator")

  def prettyPrint(table: DataTable): String = {

    val builder = new StringBuilder

    /** Calculate column details, default widths for each. */
    val colDetails = table.columns.map(column => ColumnDetails(column, colWidth(column)))

    printHeader(builder, colDetails)
    printRows(builder, table, colDetails)

    builder.mkString
  }

  private def printRows(builder: StringBuilder, table: DataTable, colDetails: Seq[ColumnDetails]) = {
    val rowCount = table.columns.head.data.length
    (0 to rowCount - 1).foreach(rowIdx => printRow(builder, rowIdx, colDetails))
  }

  private def printRow(builder: StringBuilder, rowIndex: Int, colDetails: Seq[ColumnDetails]) = {
    val formattedValues = colDetails.map(details => {
      val textValue = details.Col.data(rowIndex).toString
      "|" + colDataString(textValue, details.Width)
    })

    writelnExt(builder, formattedValues)
  }

  private def printHeader(builder: StringBuilder, colDetails: Seq[ColumnDetails]) = {
    /** Calculate total length required for the header row. */
    val totalLength = colDetails.map(_.Width).sum + (colDetails.length * 2)
    val headerFooter = padString("", totalLength, '-')

    writeln(builder)
    writelnExt(builder, headerFooter)

    val formattedHeaders = colDetails.map(col => "|" + colDataString(col.Col.name, col.Width))

    writelnExt(builder, formattedHeaders)
    writelnExt(builder, headerFooter)
  }

  /** Returns the value correctly formatted according to the width. */
  private def colDataString(data: String, width: Int): String = {
    padString(data, width, ' ')
  }

  /** Calculates the column width to use for a column. */
  private def colWidth(column: GenericColumn) = {
    maxValueLength(column.name +: column.data) + 2
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
