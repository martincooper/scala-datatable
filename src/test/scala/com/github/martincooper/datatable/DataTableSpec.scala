/**
The MIT License (MIT)

Copyright (c) 2014 Martin Cooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package com.github.martincooper.datatable

import org.scalatest.{Matchers, FlatSpec}

class DataTableSpec extends FlatSpec with Matchers {

  "A new DataTable" should "be created with a name and no data" in {
    val dataTable = new DataTable("TestTable", Array().toIndexedSeq)

    dataTable.name should be ("TestTable")
    dataTable.columns.length should be (0)
  }

  "A new DataTable" should "be created with a name and default columns" in {

    val seqOne = (0 to 19) map { i => i }
    val dataColOne = new DataColumn[Int]("ColOne", seqOne)

    val seqTwo = (0 to 19) map { i => "Value : " + i }
    val dataColTwo = new DataColumn[String]("ColTwo", seqTwo)

    val dataTable = new DataTable("TestTable", Array(dataColOne, dataColTwo))

    dataTable.name should be ("TestTable")
    dataTable.columns.length should be (2)

    dataTable.columns(0).data(4) shouldBe a [Integer]
    dataTable.columns(0).data(4) should be (4)

    dataTable.columns(1).data(4) shouldBe a [String]
    dataTable.columns(1).data(4) should be ("Value : 4")
  }
}
