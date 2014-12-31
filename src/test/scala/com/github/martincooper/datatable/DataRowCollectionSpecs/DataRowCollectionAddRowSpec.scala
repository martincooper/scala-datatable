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

package com.github.martincooper.datatable.DataRowCollectionSpecs

import com.github.martincooper.datatable.{ DataColumn, DataColumnCollection, DataTable, DataTableException }
import org.scalatest.{ FlatSpec, Matchers }

class DataRowCollectionAddRowSpec extends FlatSpec with Matchers {

  def createTestTable: DataTable = {
    val dataColOne = new DataColumn[Int]("ColOne", (0 to 3) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 3) map { i => "Val" + i })
    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 3) map { i => true })

    DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree)).get
  }
}
