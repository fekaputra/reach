package org.clulab.reach.export.apis

import java.util.{List => JList}
import scala.collection.JavaConverters._
import com.typesafe.config.ConfigFactory
import ai.lum.nxmlreader._
import org.clulab.reach._
import org.clulab.reach.coserver.ProcessorCoreClient
import org.clulab.reach.mentions._

/**
  * External interface class to accept and process text strings and NXML documents,
  * returning Reach results as a sequence of BioMentions.
  *   Author: Tom Hicks. 10/19/2015.
  *   Last Modified: Update for processor core client instance.
  */
object Api {
  // Reach results for Scala consumption are a sequence of BioMentions
  type ReachResults = Seq[BioMention]

  // Reach results for Java consumption are a java.util.List of BioMentions
  type JReachResults = JList[BioMention]

  // some internal defaults for parameters required in lower layers
  private val NoSec = "NoSection"
  private val Prefix = "api"
  private val Suffix = "Reach"

  // read configuration to determine processing parameters
  val config = ConfigFactory.load()
  val ignoreSections = config.getStringList("ignoreSections").asScala.toList
  val encoding = config.getString("encoding")

  val reader = new NxmlReader(ignoreSections.toSet)

  val reach = new ReachSystem(processor=ProcessorCoreClient.instance) // start reach system

  //
  // Scala API
  //

  /** Extracts raw text from given nxml string and returns Reach results. */
  def runOnNxml (nxml: String): ReachResults = {
    val nxmlDoc = reader.parse(nxml)
    reach.extractFrom(nxmlDoc)
  }

  /** Annotates text by converting it to a FriesEntry and calling runOnFriesEntry(). */
  def runOnText (text: String, docId: String=Prefix, chunkId: String=Suffix): ReachResults = {
    reach.extractFrom(text, docId, chunkId)
  }

  /** Annotates a single FriesEntry and returns Reach results. */
  def runOnFriesEntry (entry: FriesEntry): ReachResults =
    reach.extractFrom(entry)


  //
  // Java API
  //

  /** Extracts raw text from given nxml string and returns Java Reach results. */
  def runOnNxmlToJava (nxml: String): JReachResults = {
    runOnNxml(nxml).asJava
  }

  /** Annotates text by converting it to a FriesEntry and calling
      runOnFriesEntryToJava(). Uses fake document ID and chunk ID. */
  def runOnTextToJava (text: String): JReachResults = {
    runOnText(text).asJava
  }

  /** Annotates text by converting it to a FriesEntry and calling
      runOnFriesEntryToJava(). */
  def runOnTextToJava (text: String, docId: String): JReachResults = {
    runOnText(text, docId).asJava
  }

  /** Annotates text by converting it to a FriesEntry and calling
      runOnFriesEntryToJava(). */
  def runOnTextToJava (text: String, docId: String, chunkId: String): JReachResults = {
    runOnText(text, docId, chunkId).asJava
  }

}
