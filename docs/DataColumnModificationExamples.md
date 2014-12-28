#Scala DataTable

# Data Column Modification Examples

Data Columns can be added / replaced / inserted and removed from an existing DataTable.
Each of these modifications will return a new DataTable with the new column state without
any changes to the original. All changes return a Try[DataTable] wrapping any errors which may
occur in a composable way. This could be if specifying a column name which doesn't exist, an
out of bounds error, or duplicate column names.

## Adding Columns
To add a new column, call the add method on the table.columns collection.
This will return a new DataTable structure including the additional column.

```scala
def addColumn(dataTable: DataTable): Try[DataTable] = {
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
  dataTable.columns.add(stringCol)
}
```

## Removing Columns
To remove a column, call the remove method on the table.columns collection.
This will return a new DataTable structure with the column removed. The column to remove
can be specified by name, by index, or the column itself.

```scala
def removeColumnByName(dataTable: DataTable): Try[DataTable] = {
  dataTable.columns.remove("ColumnToRemove")
}

def removeColumnByIndex(dataTable: DataTable): Try[DataTable] = {
  dataTable.columns.remove(1)
}

def removeColumn(dataTable: DataTable, columnToRemove: GenericColumn): Try[DataTable] = {
  dataTable.columns.remove(columnToRemove)
}
```

## Inserting Columns
To insert a column, call the insert method on the table.columns collection.
This will return a new DataTable structure with the column inserted. The column
to insert can be specified by name, by index, or the column itself.

```scala
def insertColumnByName(dataTable: DataTable): Try[DataTable] = {
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
  dataTable.columns.insert("ColumnTwo", stringCol)
}

def insertColumnByIndex(dataTable: DataTable): Try[DataTable] = {
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
  dataTable.columns.insert(2, stringCol)
}

def insertColumn(dataTable: DataTable, insertBeforeColumn: GenericColumn): Try[DataTable] = {
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
  dataTable.columns.insert(insertBeforeColumn, stringCol)
}
```

## Replacing Columns
To replace a column, call the replace method on the table.columns collection.
This will return a new DataTable structure with the column replaced. The column
to replace can be specified by name, by index, or the column itself.

```scala
def replaceColumnByName(dataTable: DataTable): Try[DataTable] = {
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
  dataTable.columns.replace("ColumnTwo", stringCol)
}

def replaceColumnByIndex(dataTable: DataTable): Try[DataTable] = {
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
  dataTable.columns.replace(2, stringCol)
}

def replaceColumn(dataTable: DataTable, columnToReplace: GenericColumn): Try[DataTable] = {
  val stringCol = new DataColumn[String]("New Column", (1 to 100).map(i => "Another " + i))
  dataTable.columns.replace(columnToReplace, stringCol)
}
```