#scala-datatable

Scala DataTable is an immutable table implementation allowing the access of data in a row / column format where the column data types may not be known at design time. The data is stored as a collection of immutable, strongly typed lists. Any modifications to the table or its data will always return a new table, leaving the existing one without change.


#build.sbt

```scala
libraryDependencies += "com.github.martincooper" %% "scala-datatable" % "0.1"
```


## Example




## License

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)