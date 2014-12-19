#scala-datatable

Scala DataTable is an immutable table implementation allowing the access of data in a row / column format where the column data types may not be known at design time. 

The underlying data is stored as a collection of strongly typed lists (one per column). Any modifications to the table or its data will always return a new structure, leaving the existing one without change.


#build.sbt

```scala
libraryDependencies += "com.github.martincooper" %% "scala-datatable" % "0.1"
```

# Examples

## Create DataTable
```scala
def createDataTable() : Try[DataTable] = {

  // Data columns created using a column name and a collection of values.
  val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
  val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
  val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

  // DataTable created with using a table name and a collection of Data Columns.
  val dataTableOption = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol))

  // If any of the columns fail validation (duplicate column names or columns contain data
  // of different lengths), then it'll return a Failure. Else Success[DataTable]
  dataTableOption
}
```



## License

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)