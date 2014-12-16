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

package com.github.martincooper.datatable

import org.scalatest.{FlatSpec, Matchers}

/** Tests relating to accessing a table.  */
class DataTableAccessSpec extends FlatSpec with Matchers {

  "A DataTable" should "allow a column returned by name" in {

    val dataColOne = new DataColumn[Int]("ColOne", (0 to 10) map { i => i })
    val dataColTwo = new DataColumn[String]("ColTwo", (0 to 10) map { i => "Value : " + i })
    val dataColThree = new DataColumn[Boolean]("ColThree", (0 to 10) map { i => true })

    val table = DataTable("TestTable", Seq(dataColOne, dataColTwo, dataColThree)).get

    val column = table("ColTwo")

    column.isDefined should be(true)
    column.get.name should be("ColTwo")
    column.get.data(5) should be("Value : 5")
  }

}
