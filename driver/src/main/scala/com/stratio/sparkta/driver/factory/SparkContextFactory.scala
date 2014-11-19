/**
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.sparkta.driver.factory

import java.io.File

import akka.event.slf4j.SLF4JLogging
import com.typesafe.config.Config
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._

/**
 * Created by ajnavarro on 14/10/14.
 */
object SparkContextFactory extends SLF4JLogging {
  private var sc: SparkContext = null

  def sparkContextInstance(generalConfig: Config, jars: Seq[File]): SparkContext = {
    synchronized {
      if (sc == null) {
        sc = new SparkContext(configToSparkConf(generalConfig))
        jars.foreach(f => sc.addJar(f.getAbsolutePath))

      }
      sc
    }
  }

  private def configToSparkConf(generalConfig: Config): SparkConf = {
    val c = generalConfig.getConfig("spark")
    val properties = c.entrySet()
    val conf = new SparkConf()

    properties.foreach(e => conf.set(e.getKey, c.getString(e.getKey)))

    conf.setIfMissing("spark.streaming.concurrentJobs", "20")

    conf
  }
}