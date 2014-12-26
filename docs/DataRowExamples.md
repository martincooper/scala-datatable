#Scala DataTable

# Data Row Examples

The DataRow is used the access the underlying table data in a standard row format, allowing iteration, filtering,
searching, mapping and other common functionality. There are a number of methods to access the data depending on
requirements and what details are known at design time.

## Data Access Untyped and Unchecked.
The following example shows how the data can be accessed using the DataRow when no type information is known
at design time, or not specified. Also no checking is done, so any out of bounds errors or invalid columns names
will throw an exception.

```scala
def accessRowDataAsUntypedAndUnchecked(table: DataTable): Unit = {

  // Iterate through all rows in the table, printing out the value (regardless of type)
  // of the column number specified.
  table.foreach(dataRow => println(dataRow(0).toString))

  // Iterate through all rows in the table, printing out the value (regardless of type)
  // of the named column.
  table.foreach(dataRow => println(dataRow("ColNameOne").toString))
}
```

## Data Access Typed and Unchecked.
The following example shows how the data can be accessed using the DataRow when type information is known
and provided at design time. Using the 'as[T]' method, no checking is done, so any out of bounds errors or invalid
columns names will throw an exception.

```scala
def accessRowDataAsTypedAndUnchecked(table: DataTable): Unit = {

  // Iterate through all rows in the table, specifying as explicit Integer col,
  // printing out value * 5 of the column number specified.
  table.foreach(dataRow => println(dataRow.as[Int](0) * 5))

  // Iterate through all rows in the table, specifying as explicit Integer col,
  // printing out value * 5 of the named column.
  table.foreach(dataRow => println(dataRow.as[Int]("ColNameTwo") * 5))
}
```

## Data Access Untyped and Checked.
The following example shows how the data can be accessed using the DataRow when no type information is known
at design time, or not specified, but bounds checking is required. Using the 'get' method, any out of bounds errors or invalid
column names will be safely handled and returned as a Try[T].

```scala
def accessRowDataAsUntypedAndChecked(table: DataTable): Unit = {

  // Iterate through all rows in the table, printing out the value (regardless of type)
  // of the column number specified.
  table.foreach(dataRow => {
    dataRow.get(0).map { cellValue => println(cellValue.toString) }
  })

  // Iterate through all rows in the table, printing out the value (regardless of type)
  // of the named column.
  table.foreach(dataRow => {
    dataRow.get("ColNameOne").map { cellValue => println(cellValue.toString) }
  })
}
```

## Data Access Typed and Checked.
The following example shows how the data can be accessed using the DataRow when type information is known
and provided at design time and full bounds and type checking is required. Using the 'getAs[T]' method, any out of
bounds errors or invalid column names will be safely handled and returned as a Try[T].

```scala
def accessRowDataAsTypedAndChecked(table: DataTable): Unit = {

  // Iterate through all rows in the table, printing out the value (regardless of type)
  // of the column number specified.
  table.foreach(dataRow => {
    dataRow.getAs[Int](0).map { cellValue => println(cellValue + 5) }
  })

  // Iterate through all rows in the table, printing out the value (regardless of type)
  // of the named column.
  table.foreach(dataRow => {
    dataRow.getAs[Int]("ColNameOne").map { cellValue => println(cellValue + 5) }
  })
}
```