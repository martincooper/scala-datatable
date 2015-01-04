#Scala DataTable

## Overview

Scala DataTable is a lightweight, in-memory table structure written in Scala. The implementation is entirely immutable.
Modifying any part of the table, adding or removing columns, rows, or individual field values will create and return a
new structure, leaving the old one completely untouched. This is quite efficient due to structural sharing.

### Features :
 * Fully immutable implementation.
 * All changes use structural sharing for performance.
 * Table columns can be added, inserted, updated and removed.
 * Rows can be added, inserted, updated and removed.
 * Individual cell values can be updated.
 * Any inserts, updates or deletes keep the original structure and data completely unchanged.
 * Internal type checks and bounds checks to ensure data integrity.
 * RowData object allowing typed or untyped data access.
 * Full filtering and searching on row data.
 * Single and multi column quick sorting.
 * DataViews to store sets of filtered / sorted data.

## Implementation

The main focus for this project was to have the flexibility of a standard mutable table with all the common requirements,
add / remove columns, add / remove rows, update cells and values in individual cells, but with the benefit of immutable
and persistent data structures.

It allows access to the table data in a row / column format where the column data types may, or may not be known at
design time, for example a table read from a database, a CSV file or other dynamic data source.

Internally the data is stored as a collection of immutable Vector[T] ensuring type information is fully preserved, with
checks ensuring full type integrity is maintained at runtime.

The table data can be easily accessed, filtered and modified through a RowData object, providing a range of typed and
untyped methods depending on how much type info is known at design time.

Most methods have both checked and unchecked versions. The checked ones perform additional bounds checking and return
results as a Try[T] with detailed error information. The unchecked ones will just return a [T] and throw an exception
on any out of bounds errors but with potentially faster access on a significantly large amount of updates.

#Getting Scala DataTable

If you use SBT, you can include the following line into the build.sbt file.
```scala
libraryDependencies += "com.github.martincooper" %% "scala-datatable" % "0.7.0"
```

# Example Usage

## Creating DataTables
To create a new DataTable, create the DataColumns required (just specify a unique column name and
the data populating each one) as shown below.

```scala
def createDataTable() : Try[DataTable] = {

  // Data columns created using a unique column name and a collection of values.
  val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
  val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
  val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

  // DataTable created with using a table name and a collection of Data Columns.
  val dataTableOption = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol))

  // If any of the columns fail validation (duplicate column names, or columns contain
  // data of different lengths), then it'll return a Failure. Else Success[DataTable]
  dataTableOption
}
```

## Adding Columns
To add a new Column, create a new DataColumn and call the add method on the table.columns
collection. This will return a new DataTable structure including the additional column.

```scala
def addColumn(dataTable: DataTable): Try[DataTable] = {

  // Create a new column.
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))

  // Call columns.add to return a new Try[DataTable] structure with the additional column.
  val updatedTable = dataTable.columns.add(stringCol)

  // If adding the additional column fails validation (duplicate column names, or columns
  // contain data of different lengths), then it'll return a Failure. Else Success[DataTable]
  updatedTable
}
```

## Removing Columns
To remove a Column, call the remove method on the table.columns collection.
This will return a new DataTable structure with the column removed.

```scala
def removeColumn(dataTable: DataTable): Try[DataTable] = {

  // Call columns.remove to return a new DataTable structure with the additional column.
  val updatedTable = dataTable.columns.remove("ColumnToRemove")

  // If removing the column fails validation (column name not found),
  // then it'll return a Failure. Else Success[DataTable]
  updatedTable
}
```

## Row / Data Filtering
Access to the underlying data in the table the DataRow object can be used. This allows either typed or
untyped access depending if type info is known at design time. The DataTable object implements IndexSeq[DataRow]
so supports the standard filter, map operations etc. The results are returned in a DataView object which is a view
on the underlying table. To filter a table this can be done as follows...

```scala
def filterData(dataTable: DataTable) = {

  // Filter the data using the RowData object.
  val dataView = dataTable.filter(row => {
    row.as[String]("FirstName").startsWith("Ma") && row.as[Int]("Age") > 18
  })

  // Access the filtered results...
  println(dataView.length)

  // Row data can be accessed using indexers with no type information...
  dataView.foreach(row => println(row(0).toString + " : " + row(1).toString))

  // Or by specifying the columns by name and with full type info.
  dataView.foreach(row =>
    println(row.as[String]("AddressOne") + " : " + row.as[Int]("HouseNumber")))
}
```

## Row / Data Access
DataRow has a number of ways to access the underlying data, depending on the amount of information
known at design time about the data and it's type. The simplest way with no type information is
calling .value, or .valueMap on the DataRow item.

```scala
def simpleDataAccess(dataRow: DataRow) = {

  // Calling dataRow.values returns a IndexedSeq[Any] of all values in the row.
  println(dataRow.values)

  // Calling dataRow.valueMap returns a Map[String, Any] of all values
  // in the row mapping column name to value.
  println(dataRow.valueMap)
}
```

The DataRow has additional type checked and bounds checked methods allowing safer and
more composable access to the underlying data.

```scala
def typedAndCheckedDataAccess(dataRow: DataRow) = {

  // Each .getAs[T] is type checked and bounds / column name
  // checked so can be composed safely
  val checkedValue = for {
    name <- dataRow.getAs[String]("FirstName")
    age <- dataRow.getAs[Int]("Age")
  } yield name + " is " + age + " years old."

  checkedValue match {
    case Success(value) => println(value)
    case Failure(ex) => println("Error occurred : " + ex.getMessage)
  }
}
```

## Adding / Updating / Deleting rows.
Individual rows and field values in the table can be modified. As the implementation is fully
immutable, any add /remove / update / delete operation will return a new table with the changes
applied leaving the original unchanged.

The examples below assume a table with 4 columns of type [String, Int, Boolean, Double].

```scala
def addRow(dataTable: DataTable): Try[DataTable] = {
  // Add a new row containing 4 values.
  dataTable.rows.add("New Value", 100 , true, 5.5d)
}

def insertRow(dataTable: DataTable): Try[DataTable] = {
  // Insert a new row containing 4 values at row index 10.
  dataTable.rows.insert(10, "New Value", 100, true, 5.5d)
}

def replaceRow(dataTable: DataTable): Try[DataTable] = {
  // Replace the row values at row index 10 with the new values.
  dataTable.rows.replace(10, "New Value", 100, true, 5.5d)
}

def removeRow(dataTable: DataTable): Try[DataTable] = {
  // Remove the row at the specified index.
  dataTable.rows.remove(10)
}
```


## Sorting / Single and Multi Column
A DataTable can sort by specified column or columns, returning a sorted DataView.

```scala
def sortTableByColumnNameDescending(dataTable: DataTable): Try[DataView] = {
  dataTable.quickSort("ColumnOne", Descending)
}
```

Also on multiple columns.

```scala
def sortTableByMultipleColumns(dataTable: DataTable): Try[DataView] = {
  val sortItemOne = SortItem("FirstName")
  val sortItemTwo = SortItem(3, Descending)
  dataTable.quickSort(Seq(sortItemOne, sortItemTwo))
}
```

### Additional Examples :
 * [Accessing Data with the DataRow](https://github.com/martincooper/scala-datatable/blob/master/docs/DataRowAccessExamples.md)
 * [Modifying Columns](https://github.com/martincooper/scala-datatable/blob/master/docs/DataColumnModifyExamples.md)
 * [Updating Data](https://github.com/martincooper/scala-datatable/blob/master/docs/DataRowModifyExamples.md)
 * [Sorting Data](https://github.com/martincooper/scala-datatable/blob/master/docs/SortingDataExamples.md)

### Contributing

Building this project requires SBT 0.13.0.

After you launch SBT, you can run the following commands:

 * `compile` compile the project
 * `test` run the tests
 * `console` load a scala REPL with Scala DataTable on the classpath.

Tests are written with [ScalaTest](http://www.scalatest.org/)

### Credits

Scala DataTable is maintained by Martin Cooper : Copyright (c) 2014-2015

## License

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)
