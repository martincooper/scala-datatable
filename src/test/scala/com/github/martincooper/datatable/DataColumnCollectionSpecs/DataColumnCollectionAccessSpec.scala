/**
 * Copyright 2014 Martin Cooper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.martincooper.datatable.DataColumnCollectionSpecs

import com.github.martincooper.datatable.{ DataColumn, DataTableException, DataTable }
import org.scalatest.{ FlatSpec, Matchers }

class DataColumnCollectionAccessSpec extends FlatSpec with Matchers {

  private def buildDefaultTestTable(): DataTable = {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })
    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 10) map { i => true })

    DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree)).get
  }

  "On a DataTable, columns" should "return a column requested by name" in {
    val table = buildDefaultTestTable()
    val column = table.columns("ColTwo")

    column.name should be("ColTwo")
    column.data(5) should be("Value : 5")
  }

  "On a DataTable, columns" should "fail when an column requested by invalid name" in {
    val table = buildDefaultTestTable()

    val result = intercept[NoSuchElementException] {
      table.columns("XXX")
    }

    result.getMessage should be("key not found: XXX")
  }

  "On a DataTable, columns" should "allow a column returned by index" in {
    val table = buildDefaultTestTable()

    val column = table.columns(1)

    column.name should be("ColTwo")
    column.data(5) should be("Value : 5")
  }

  "On a DataTable, columns" should "fail when an column requested by invalid index" in {
    val table = buildDefaultTestTable()

    val result = intercept[NoSuchElementException] {
      table.columns(99)
    }

    result.getMessage should be("key not found: 99")
  }

  "On a DataTable, columns.as[T]" should "return a column requested by name" in {
    val table = buildDefaultTestTable()
    val column = table.columns.as[String]("ColTwo")

    column.name should be("ColTwo")
    column.data(5) should be("Value : 5")
  }

  "On a DataTable, columns.as[T]" should "fail when an column requested by invalid name" in {
    val table = buildDefaultTestTable()

    val result = intercept[DataTableException] {
      table.columns.as[String]("XXX")
    }

    result.getMessage should be("Specified column name not found.")
  }

  "On a DataTable, columns.as[T]" should "fail when an column requested by name with incorrect type" in {
    val table = buildDefaultTestTable()

    val result = intercept[DataTableException] {
      table.columns.as[Int]("ColTwo")
    }

    result.getMessage should be("Column type doesn't match type requested.")
  }

  "On a DataTable, columns.as[T]" should "allow a column returned by index" in {
    val table = buildDefaultTestTable()

    val column = table.columns.as[String](1)

    column.name should be("ColTwo")
    column.data(5) should be("Value : 5")
  }

  "On a DataTable, columns.as[T]" should "fail when an column requested by invalid index" in {
    val table = buildDefaultTestTable()

    val result = intercept[DataTableException] {
      table.columns.as[String](99)
    }

    result.getMessage should be("Specified column index not found.")
  }

  "On a DataTable, columns.as[T]" should "fail when an column requested by index with incorrect type" in {
    val table = buildDefaultTestTable()

    val result = intercept[DataTableException] {
      table.columns.as[Int](1)
    }

    result.getMessage should be("Column type doesn't match type requested.")
  }

  "On a DataTable, columns.get" should "return a column requested by name" in {
    val table = buildDefaultTestTable()
    val column = table.columns.get("ColTwo")

    column.isSuccess should be(true)
    column.get.name should be("ColTwo")
    column.get.data(5) should be("Value : 5")
  }

  "On a DataTable, columns.get" should "fail when an column requested by invalid name" in {
    val table = buildDefaultTestTable()
    val column = table.columns.get("XXX")

    column.isSuccess should be(false)
  }

  "On a DataTable, columns.get" should "allow a column returned by index" in {
    val table = buildDefaultTestTable()

    val column = table.columns.get(1)

    column.isSuccess should be(true)
    column.get.name should be("ColTwo")
    column.get.data(5) should be("Value : 5")
  }

  "On a DataTable, columns.get" should "fail when an column requested by invalid index" in {
    val table = buildDefaultTestTable()
    val column = table.columns.get(99)

    column.isSuccess should be(false)
  }

  "On a DataTable, columns.getAs[T]" should "return a column requested by name" in {
    val table = buildDefaultTestTable()
    val column = table.columns.getAs[String]("ColTwo")

    column.isSuccess should be(true)
    column.get.name should be("ColTwo")
    column.get.data(5) should be("Value : 5")
  }

  "On a DataTable, columns.getAs[T]" should "fail when an column requested by invalid name" in {
    val table = buildDefaultTestTable()

    val column = table.columns.getAs[String]("XXX")
    column.isSuccess should be(false)
    column.failed.get.getMessage should be("Specified column name not found.")
  }

  "On a DataTable, columns.getAs[T]" should "fail when an column requested by name with incorrect type" in {
    val table = buildDefaultTestTable()

    val column = table.columns.getAs[Int]("ColTwo")

    column.isSuccess should be(false)
    column.failed.get.getMessage should be("Column type doesn't match type requested.")
  }

  "On a DataTable, columns.getAs[T]" should "allow a column returned by index" in {
    val table = buildDefaultTestTable()

    val column = table.columns.getAs[String](1)

    column.isSuccess should be(true)
    column.get.name should be("ColTwo")
    column.get.data(5) should be("Value : 5")
  }

  "On a DataTable, columns.getAs[T]" should "fail when an column requested by invalid index" in {
    val table = buildDefaultTestTable()

    val column = table.columns.getAs[String](99)

    column.isSuccess should be(false)
    column.failed.get.getMessage should be("Specified column index not found.")
  }

  "On a DataTable, columns.getAs[T]" should "fail when an column requested by index with incorrect type" in {
    val table = buildDefaultTestTable()

    val column = table.columns.getAs[Int](1)

    column.isSuccess should be(false)
    column.failed.get.getMessage should be("Column type doesn't match type requested.")
  }
}
