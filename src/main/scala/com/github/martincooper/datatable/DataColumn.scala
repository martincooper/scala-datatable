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

/** Generic Column Trait.
  * Allows a collection of columns storing data of distinct types to be stored
  * in a generic collection. */
trait GenericColumn {
  def name: String
  def data: IndexedSeq[Any]
}

/** Strongly typed data column. */
class DataColumn[T](columnName: String, columnData: Iterable[T]) extends GenericColumn {
  def name = columnName
  def data = Vector.empty ++ columnData
  override def toString = "Col : " + name
}

object GenericColumn {

  implicit class GenericColExt(val col: GenericColumn) extends AnyVal {

    /** Casts the generic column back to the specified type. */
    def as[T] : DataColumn[T] =
      col.asInstanceOf[DataColumn[T]]
  }
}