package edu.arizona.sista.bionlp.reach.ruler

import edu.arizona.sista.matcher.ExtractorEngine
import edu.arizona.sista.processors.bionlp.BioNLPProcessor
import DarpaEvalUtils._
import TestDarpaEval2015DryRun._
import org.junit.Assert._
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

/**
 * Unit test for rules tailored for the DARPA evaluation; using the dryrun corpus
 * User: mihais
 * Date: 1/5/15
 */
class TestDarpaEval2015DryRun extends AssertionsForJUnit {

  @Test def testRules1() {
    val doc = proc.annotate("We next considered the effect of Ras monoubiquitination on GAP–mediated hydrolysis")
    val mentions = extractor.extractFrom(doc)
    header("testRules1")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEventWithArguments("Ubiquitination", List("Ras"), mentions))
  }

  @Test def testRules2() {
    val doc = proc.annotate("To this end we compared the rate of GTP hydrolysis for Ras and mUbRas in the presence of the catalytic domains of two GAPs")
    val mentions = extractor.extractFrom(doc)
    header("testRules2")
    RuleShell.displayMentions(mentions, doc)
    // TODO: both these fail (DANE)
    assertTrue(hasEventWithArguments("Hydrolysis", List("Ras-GTP"), mentions))
    assertTrue(hasEventWithArguments("Hydrolysis", List("mUbRas-GTP"), mentions))

    // TODO: can we catch the UpRegulation by GAP here?
    //assertTrue(hasUpRegulationByEntity("GAPs", "Hydrolysis", List("Ras-GTP"), mentions))
    //assertTrue(hasUpRegulationByEntity("GAPs", "Hydrolysis", List("mUbRas-GTP"), mentions))
  }

  @Test def testRules3() {
    val doc = proc.annotate("We observed an order of magnitude increase in the rate of GTP hydrolysis for unmodified Ras relative to the intrinsic rate of GTP hydrolysis.")
    val mentions = extractor.extractFrom(doc)
    header("testRules3")
    RuleShell.displayMentions(mentions, doc)
    // TODO: this fails (DANE)
    assertTrue(hasEventWithArguments("Hydrolysis", List("Ras-GTP"), mentions))
  }

  @Test def testRules4() {
    val doc = proc.annotate("The effects of monoubiquitination on Ras are not isoform–specific.")
    val mentions = extractor.extractFrom(doc)
    header("testRules4")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEventWithArguments("Ubiquitination", List("Ras"), mentions))
  }

  @Test def testRules5() {
    val doc = proc.annotate("We measured the rate of GAP–mediated GTP hydrolysis and observed that the response of Ras ligated to Ubiquitin was identical")
    val mentions = extractor.extractFrom(doc)
    header("testRules5")
    RuleShell.displayMentions(mentions, doc)
    // TODO: this fails (DANE)
    assertTrue(hasEventWithArguments("Hydrolysis", List("GTP"), mentions))
    // TODO: appears as binding but it's ubiquitination (GUS + MARCO)
    assertTrue(hasEventWithArguments("Ubiquitination", List("Ras"), mentions))

    // TODO: up-regulation ( MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("GAP", "Hydrolysis", List("GTP"), mentions))
  }

  @Test def testRules6() {
    val doc = proc.annotate("monoubiquitinated K–Ras is less sensitive than the unmodified protein to GAP–mediated GTP hydrolysis")
    val mentions = extractor.extractFrom(doc)
    header("testRules6")
    RuleShell.displayMentions(mentions, doc)
    // TODO: this fails (DANE)
    assertTrue(hasEventWithArguments("Hydrolysis", List("GTP"), mentions))

    // TODO: missing keyword (GUS). Should "K-Ras" be "K - Ras" because of tokenization?
    assertTrue(hasEventWithArguments("Ubiquitination", List("K - Ras"), mentions))

    // TODO: up-regulation ( MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("GAP", "Hydrolysis", List("GTP"), mentions))
  }

  @Test def testRules7() {
    val doc = proc.annotate("Here we show that monoubiquitination decreases the sensitivity of Ras to GAP–mediated hydrolysis")
    val mentions = extractor.extractFrom(doc)
    header("testRules7")
    RuleShell.displayMentions(mentions, doc)
    // TODO: this fails (DANE)
    assertTrue(hasEventWithArguments("Hydrolysis", List("Ras"), mentions))

    // TODO: missing keyword, missing protein - needs coref (GUS)
    assertTrue(hasEventWithArguments("Ubiquitination", List("Ras"), mentions))

    // TODO: up-regulation ( MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("GAP", "Hydrolysis", List("GTP"), mentions))

    // TODO: another down-regulation controller the ubiquitination, and controlled the GAP up-regulation??? Not sure about this...
    // assertTrue(hasDownRegulationByEvent("Ubiquitination", List("Ras"), "UpRegulation", List("")))
  }

  @Test def testRules8() {
    val doc = proc.annotate("It has recently been shown that oncogenic RAS can enhance the apoptotic function of p53 via ASPP1 and ASPP2")
    val mentions = extractor.extractFrom(doc)
    header("testRules8")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEntity("RAS", mentions))
    assertTrue(hasEntity("p53", mentions))
    assertTrue(hasEntity("ASPP1", mentions))
    assertTrue(hasEntity("ASPP2", mentions))
  }

  @Test def testRules9() {
    val doc = proc.annotate("Mechanistically ASPP1 and ASPP2 bind RAS-GTP and potentiates RAS signalling to enhance p53 mediated apoptosis")
    val mentions = extractor.extractFrom(doc)
    header("testRules9")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEntity("RAS-GTP", mentions))
    assertTrue(hasEntity("RAS", mentions))
    assertTrue(hasEntity("p53", mentions))
    assertTrue(hasEntity("ASPP1", mentions))
    assertTrue(hasEntity("ASPP2", mentions))
    assertTrue(hasEventWithArguments("Binding", List("ASPP1", "ASPP2", "RAS-GTP"), mentions))
  }

  @Test def testRules10() {
    val doc = proc.annotate("Mechanistically ASPP1 and ASPP2 bind RAS-GTP and potentiates RAS signalling to enhance p53 mediated apoptosis")
    val mentions = extractor.extractFrom(doc)
    header("testRules10")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEntity("RAS-GTP", mentions))
    assertTrue(hasEntity("RAS", mentions))
    assertTrue(hasEntity("p53", mentions))
    assertTrue(hasEntity("ASPP1", mentions))
    assertTrue(hasEntity("ASPP2", mentions))
    assertTrue(hasEventWithArguments("Binding", List("ASPP1", "ASPP2", "RAS-GTP"), mentions))
  }

  @Test def testRules11() {
    val doc = proc.annotate("Interestingly, we observed two conserved putative MAPK phosphorylation sites in ASPP1 and ASPP2")
    val mentions = extractor.extractFrom(doc)
    header("testRules11")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEntity("MAPK", mentions))
    assertTrue(hasEntity("ASPP1", mentions))
    assertTrue(hasEntity("ASPP2", mentions))

    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP1"), mentions))
    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP2"), mentions))

    // TODO: missing regulations (MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("MAPK", "Phosphorylation", List("ASPP1"), mentions))
    assertTrue(hasUpRegulationByEntity("MAPK", "Phosphorylation", List("ASPP2"), mentions))
  }

  @Test def testRules12() {
    val doc = proc.annotate("We thus tested whether RAS activation may regulate ASPP2 phosphorylation")
    val mentions = extractor.extractFrom(doc)
    header("testRules12")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP2"), mentions))

    // TODO: missing regulations (MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("RAS", "Phosphorylation", List("ASPP2"), mentions))
  }

  @Test def testRules13() {
    val doc = proc.annotate("MAPK1 was clearly able to phosphorylate the ASPP2 fragment in vitro")
    val mentions = extractor.extractFrom(doc)
    header("testRules13")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP2"), mentions))

    // TODO: missing regulations (MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("MAPK1", "Phosphorylation", List("ASPP2"), mentions))
  }

  @Test def testRules14() {
    val doc = proc.annotate("Under the same conditions, ASPP2 (693-1128) fragment phosphorylated by p38 SAPK had very low levels of incorporated 32P")
    val mentions = extractor.extractFrom(doc)
    header("testRules14")
    RuleShell.displayMentions(mentions, doc)

    // TODO: missing Phospho (GUS)
    // TODO: Add Site rule for (\d+-\d+) fragment pattern
    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP2"), mentions))

    // TODO: missing regulations (MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("p38 SAPK", "Phosphorylation", List("ASPP2"), mentions))
  }

  @Test def testRules15() {
    val doc = proc.annotate("Indicating that p38 SAPK is not an efficient kinase for ASPP2 phosphorylation.")
    val mentions = extractor.extractFrom(doc)
    header("testRules15")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP2"), mentions))
  }

  @Test def testRules16() {
    val doc = proc.annotate("The phosphorylated ASPP2 fragment by MAPK1 was digested by trypsin and fractioned on a high performance liquid chromatography.")
    val mentions = extractor.extractFrom(doc)
    header("testRules16")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP2"), mentions))

    // TODO: missing regulations (MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("MAPK1", "Phosphorylation", List("ASPP2"), mentions))
  }

  @Test def testRules17() {
    val doc = proc.annotate("Hence ASPP2 can be phosphorylated at serine 827 by MAPK1 in vitro.")
    val mentions = extractor.extractFrom(doc)
    header("testRules17")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP2"), mentions))

    // TODO: missing regulations (MARCO + GUS)
    assertTrue(hasUpRegulationByEntity("MAPK1", "Phosphorylation", List("ASPP2"), mentions))
  }

  @Test def testRules18() {
    val doc = proc.annotate("Moreover, the RAS-ASPP interaction enhances the transcription function of p53 in cancer cells.")
    val mentions = extractor.extractFrom(doc)
    header("testRules18")
    RuleShell.displayMentions(mentions, doc)

    // TODO: Binding with 1 argument, which is a complex (MARCO)
    assertTrue(hasEventWithArguments("Binding", List("RAS", "ASPP"), mentions))
  }

  @Test def testRules19() {
    val doc = proc.annotate("We show here that ASPP2 is phosphorylated by the RAS/Raf/MAPK pathway and that this phosphorylation leads to its increased translocation to the cytosol/nucleus and increased binding to p53")
    val mentions = extractor.extractFrom(doc)
    header("testRules19")
    RuleShell.displayMentions(mentions, doc)

    assertTrue(hasEventWithArguments("Phosphorylation", List("ASPP2"), mentions))

    // TODO: missing transport (ENRIQUE)
    assertTrue(hasEventWithArguments("Transport", List("cytosol/nucleus"), mentions))

    // TODO: incomplete Binding with 1 argument; ideally we should add ASPP2 through coref... (MARCO)
    assertTrue(hasEventWithArguments("Binding", List("p53"), mentions))
  }

  @Test def testRules20() {
    val doc = proc.annotate("ASPP2 is transported from the membrane to the nucleus/cytosol")
    val mentions = extractor.extractFrom(doc)
    header("testRules19")
    RuleShell.displayMentions(mentions, doc)

    // TODO: missing transport (ENRIQUE)
    assertTrue(hasEventWithArguments("Transport", List("ASPP2", "membrane", "cytosol/nucleus"), mentions))
  }
}

object TestDarpaEval2015DryRun {
  val proc = new BioNLPProcessor

  val extractor = mkExtractor

  def mkExtractor = {
    val actions = new DarpaActions
    val entityRules = Ruler.readEntityRules
    val eventRules = Ruler.readEventRules // reads all entity/event rules for the DARPA eval
    val rules = entityRules + "\n\n" + eventRules
    new ExtractorEngine(rules, actions)
  }
}
