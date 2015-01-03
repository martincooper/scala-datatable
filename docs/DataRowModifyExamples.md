#Scala DataTable

# Row and Data modification.

The DataTable, although being immutable, contains functionality to be able to easily perform standard table
operations such as add / insert / update / delete, but without modifying the original structure or data in any way.
Each of these operations will return a new DataTable with the changes applied (or a Failure, if there was any invalid
values or value types provided).


## Adding Rows
To add a row, call row.add, providing a value of the correct type for each column.
If the incorrect number of values is provided or the type values don't match, it'll
return a Failure. Example :-

```scala
// Ensure this is imported to simplify calls to .add()
import TypedDataValueImplicits._

def addRow(): Try[DataTable] = {
  // First create a table with the required columns.
  val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
  val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
  val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

  val dataTable = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol)).get

  // Call rows.add, providing the correct number of values and of the correct type.
  // This will return a new DataTable with the data appended as a new row at the end.
  dataTable.rows.add("New Value", 1000, false)
}
```

## Removing Rows
To remove a row, call row.remove, providing the row index to remove.

```scala
def removeRow(): Try[DataTable] = {
  // First create a table with the required columns.
  val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
  val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
  val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

  val dataTable = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol)).get

  // Call rows.remove, providing the rowIndex.
  // This will return a new DataTable with the row at the specified index removed.
  val rowIndex = 50
  dataTable.rows.remove(rowIndex)
}
```

## Inserting Rows
To insert a row, call row.insert, providing a value of the correct type for each column.
If the incorrect number of values is provided or the type values don't match, it'll
return a Failure. Example :-

```scala
// Ensure this is imported to simplify calls to .insert()
import TypedDataValueImplicits._

def insertRow(): Try[DataTable] = {
  // First create a table with the required columns.
  val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
  val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
  val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

  val dataTable = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol)).get

  // Call rows.insert, providing the correct number of values and of the correct type.
  // This will return a new DataTable with the data provided inserted as a new row
  // at the specified index.
  val rowIndex = 50
  dataTable.rows.insert(rowIndex, "New Value", 1000, false)
}
```

## Updating Rows
To replace / update a row, call row.replace, providing a value of the correct type for each column.
If the incorrect number of values is provided or the type values don't match, it'll
return a Failure. Example :-

```scala
// Ensure this is imported to simplify calls to .replace()
import TypedDataValueImplicits._

def replaceRow(): Try[DataTable] = {
  // First create a table with the required columns.
  val stringCol = new DataColumn[String]("StringColumn", (1 to 100).map(i => "Cell Value " + i))
  val integerCol = new DataColumn[Int]("IntegerColumn", (1 to 100).map(i => i * 20))
  val booleanCol = new DataColumn[Boolean]("BooleanColumn", (1 to 100).map(i => true))

  val dataTable = DataTable("NewTable", Seq(stringCol, integerCol, booleanCol)).get

  // Call rows.replace, providing the correct number of values and of the correct type.
  // This will return a new DataTable with the data provided replacing the data at
  // the row at the specified index.
  val rowIndex = 50
  dataTable.rows.replace(rowIndex, "New Value", 1000, false)
}
```