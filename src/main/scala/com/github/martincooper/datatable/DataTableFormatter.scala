/**
The MIT License (MIT)

Copyright (c) 2014 Martin Cooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package com.github.martincooper.datatable

/** Handles the output of a DataTable in a displayable format. */
object DataTableFormatter {

  private case class ColumnDetails(Col: GenericColumn, Width: Int)

  def prettyPrint(table: DataTable) = {

    /** Calculate column details, default widths for each. */
    val colDetails = table.columns.map(column => ColumnDetails(column, colWidth(column)))

    printHeader(colDetails)
    printRows(table, colDetails)
  }

  private def printRows(table: DataTable, colDetails: Seq[ColumnDetails]) = {
    val rowCount = table.columns.head.data.length
    (0 to rowCount - 1).foreach(rowIdx => printRow(rowIdx, colDetails))
  }

  private def printRow(rowIndex: Int, colDetails: Seq[ColumnDetails]) = {
    val formattedValues = colDetails.map(details => {
      val textValue = details.Col.data(rowIndex).toString
      "|" + colDataString(textValue, details.Width)
    })

    printlnExt(formattedValues)
  }

  private def printHeader(colDetails: Seq[ColumnDetails]) = {
    /** Calculate total length required for the header row. */
    val totalLength = colDetails.map(_.Width).sum + (colDetails.length  * 2)
    val headerFooter = "".padTo(totalLength, "-")

    println()
    printlnExt(headerFooter)

    val formattedHeaders = colDetails.map(col => "|" + colDataString(col.Col.name, col.Width))

    printlnExt(formattedHeaders)
    printlnExt(headerFooter)
  }

  /** Returns the value correctly formatted according to the width. */
  private def colDataString(data: String, width: Int): String = {
    data.padTo(width, " ").mkString
  }

  /** Calculates the column width to use for a column. */
  private def colWidth(column: GenericColumn) = {
    maxValueLength(column.name +: column.data) + 2
  }

  /** Returns the max data length of all the data in a collection. */
  private def maxValueLength(values: Iterable[Any]): Int = {
    values.map(_.toString.length).max
  }

  private def printlnExt(value: Traversable[Any]) = {
    println(value.mkString)
  }
}
