#Scala DataTable

# Sorting Data

DataTables have the functionality to be sortable on any column, specifying sort direction and also multi column sorting.
Any column can be sorted which stores a value type (AnyVal / Int, Float, Long, etc), or any reference type which
implements 'Comparable[_]'

The quickSort method can be called on the table passing in either the column index
or the column name. Optionally the sort direction can be specified Ascending (the default) or Descending. The result
is returned as a Try[DataView] containing a collection of DataRows in the sorted order. Any errors will be returned as
a Failure, for example if any columns specified were not found or the column type was not comparable.

## Single column sorting.
To sort the table by a single column all that is required is to pass the column name or index and optionally the
sort direction.

```scala
// Sorts by the specified column index using the default sort order (Ascending)
def sortTableByColumnIndex(dataTable: DataTable): Try[DataView] = {

  dataTable.quickSort(0)
}

// Sorts by the specified column index using Descending sort order.
def sortTableByColumnIndexDescending(dataTable: DataTable): Try[DataView] = {

  dataTable.quickSort(0, Descending)
}

// Sorts by the specified column name using the default sort order (Ascending)
def sortTableByColumnName(dataTable: DataTable): Try[DataView] = {

  dataTable.quickSort("ColumnOne")
}

// Sorts by the specified column name using Descending sort order.
def sortTableByColumnNameDescending(dataTable: DataTable): Try[DataView] = {

  dataTable.quickSort("ColumnOne", Descending)
}
```

## Multi column sorting.
To perform multi column sorting, for each column, a SortItem is required, which takes a column name or index and
optionally a sort direction.

```scala
// Sorts on multiple columns.
def sortTableByMultipleColumns(dataTable: DataTable): Try[DataView] = {

  // Create Sort Items for each column required to sort.
  // Can specify column by name or index along with optional sort order.
  val sortColumnOne = SortItem("FirstName")
  val sortColumnTwo = SortItem(3, Descending)

  dataTable.quickSort(Seq(sortItemOne, sortItemTwo))
}
```