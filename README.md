#Scala DataTable

### Overview

Scala DataTable is an in memory table written in Scala. The implementation is entirely immutable.
Adding and removing table columns, adding and removing rows, and modifying row data will create a new table, leaving the old one untouched.
This is quite efficient due to structural sharing.

It allows the access of data in a row / column format where the column data types may, or may not be known at design time.
Internally the data is stored as a collection of immutable Vector[T] ensuring type information is preserved.
The data can be accessed and filtered through a RowData object.

#Getting Scala DataTable

If you use SBT, you can include the following line into the build.sbt file.
```scala
libraryDependencies += "com.github.martincooper" %% "scala-datatable" % "0.3.0"
```

# Example Usage

## Creating DataTables
To create a new DataTable, create the DataColumns required (just specify a unique column name and
the data for each column) as shown below.

```scala
def createDataTable() : Try[DataTable] = {

  // Data columns created using a column name and a collection of values.
  val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
  val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
  val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

  // DataTable created with using a table name and a collection of Data Columns.
  val dataTableOption = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol))

  // If any of the columns fail validation (duplicate column names, or columns containing
  // data of different lengths), then it'll return a Failure. Else Success[DataTable]
  dataTableOption
}
```

## Adding Columns
To add a new Column, create the new DataColumn required and call the .add method on the
table which will return a new DataTable structure with the additional column in.

```scala
def addColumn(dataTable: DataTable): Try[DataTable] = {

  // Create a new column.
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))

  // Call add to return a new DataTable structure with the additional column.
  val updatedTable = dataTable.add(stringCol)

  // If adding the additional column fails validation (duplicate column names, or columns
  // contain data of different lengths), then it'll return a Failure. Else Success[DataTable]
  updatedTable
}
```

## Removing Columns
To remove a Column, call the .remove method on the
table which will return a new DataTable structure with the column removed.

```scala
def removeColumn(dataTable: DataTable): Try[DataTable] = {

  // Call remove to return a new DataTable structure with the additional column.
  val updatedTable = dataTable.remove("ColumnToRemove")

  // If removing the column fails validation (column name not found),
  // then it'll return a Failure. Else Success[DataTable]
  updatedTable
}
```

## Filtering and Data Access
Access to the underlying data in the table the DataRow object can be used. This allows either typed or
untyped access depending if type info is known as design time. The DataTable object implements IndexSeq[DataRow]
and can be used as shown in the example below.

```scala
def filterData() = {

  // Some random data to fill the table.
  val randString = () => Random.alphanumeric.take(10).mkString.toUpperCase
  val randInt = () => Random.nextInt()

  // Data columns created using a column name and a collection of values.
  val stringCol = new DataColumn[String]("StringColumn", (1 to 1000).map(i => randString()))
  val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 1000).map(i => randInt()))

  // DataTable created with using a table name and a collection of Data Columns.
  val dataTableOption = DataTable("NewTable", Seq(stringCol, integerCol))

  val dataTable = dataTableOption.get

  // Filter the data using the RowData object.
  val filteredData = dataTable.filter(row => {
    row.as[String]("StringColumn").startsWith("A") && row.as[Int]("IntegerColumn") > 10
  })

  // Access the filtered results...
  println(filteredData.length)

  // Row data can be accessed using indexers with no type information...
  filteredData.foreach(row => println(row(0).toString + " : " + row(1).toString))

  // Or by specifying the columns by name and with full type info.
  filteredData.foreach(row => println(row.as[String]("StringColumn") + " : " + row.as[Int]("IntegerColumn").toString))
}
```

### Contributing

Building this project requires SBT 0.13.0.

After you launch SBT, you can run the following commands:

 * `compile` compile the project
 * `test` run the tests
 * `console` load a scala REPL with Scala DataTable on the classpath.

Tests are written with [ScalaTest](http://www.scalatest.org/)

### Credits

Scala DataTable is maintained by Martin Cooper : Copyright (c) 2014

## License

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)
