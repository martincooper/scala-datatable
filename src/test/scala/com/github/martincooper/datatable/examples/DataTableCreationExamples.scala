package com.github.martincooper.datatable.examples

import com.github.martincooper.datatable.{DataTable, DataColumn}

import scala.util.Try

class DataTableCreationExamples {

  def createDataTable() : Try[DataTable] = {

    /** Data column created using a column name and a collection of values. */
    val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
    val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
    val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

    /** DataTable created with using a table name and a collection of Data Columns. */
    val dataTableOption = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol))

    /** If any of the columns fail validation (duplicate column names or columns contain data
      * of different lengths), then it'll return a Failure. Else Success[DataTable] */
    dataTableOption
  }

}
