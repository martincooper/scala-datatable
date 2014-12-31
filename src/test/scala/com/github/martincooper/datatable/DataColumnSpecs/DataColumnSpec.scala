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

package com.github.martincooper.datatable.DataColumnSpecs

import com.github.martincooper.datatable.{ DataColumn, GenericColumn }
import org.scalatest._

class DataColumnSpec extends FlatSpec with Matchers {

  /** Case Class implementing Ordered[T] */
  private case class TestOrderedInt(i: Int) extends Ordered[TestOrderedInt] {
    def compare(that: TestOrderedInt) = this.i - that.i
  }

  /** Case Class NOT implementing Ordered[T] */
  private case class TestNotOrderedInt(i: Int) {}

  "A Data Column" should "be able to be created with a name and default data" in {
    val newSeq = (0 to 19) map { i => i }
    val dataColumn = new DataColumn[Int]("TestCol", newSeq)

    dataColumn.name should be("TestCol")
    dataColumn.data.length should be(20)
    dataColumn.data(11) should be(11)
  }

  "A Generic Column" should "be able to be cast back to its original type" in {
    val newSeq = (0 to 19) map { i => i }
    val dataColumn = new DataColumn[Int]("TestCol", newSeq)

    val genericColumn = dataColumn.asInstanceOf[GenericColumn]

    genericColumn.name should be("TestCol")
    genericColumn.data.length should be(20)

    val typedCol = genericColumn.as[Int]
    typedCol.data(10) should be(10)
  }

  it should "be be comparable for standard types" in {
    new DataColumn[Int]("TestCol", Seq()).isComparable should be(true)
    new DataColumn[String]("TestCol", Seq()).isComparable should be(true)
    new DataColumn[Long]("TestCol", Seq()).isComparable should be(true)
  }

  it should "be be comparable for Comparable[T] types" in {
    new DataColumn[TestOrderedInt]("TestCol", Seq()).isComparable should be(true)
  }

  it should "not be be comparable for non Comparable[T] types" in {
    new DataColumn[TestNotOrderedInt]("TestCol", Seq()).isComparable should be(false)
  }
}
