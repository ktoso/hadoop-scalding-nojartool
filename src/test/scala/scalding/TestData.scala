/**
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package scalding
import java.io._

object TestData {


  def tempFile(prefix: String = "activator-scalding"): File = {
    val file = File.createTempFile(prefix, ".txt")
    file.deleteOnExit()
    file
  }

  def writeTempFile(prefix: String, text: String): File = {
    val file = tempFile(prefix)
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    writer.write(text)
    writer.close()
    file
	}

  val activatorText = """
Typesafe Activator is a browser-based or command-line tool that helps developers get started with the Typesafe Reactive Platform.

A new addition to the Typesafe Reactive Platform is Typesafe Activator, a unique, browser-based tool that helps developers get started with Typesafe technologies quickly and easily. Activator is a hub for developers wanting to build Reactive applications. Unlike previous developer-focused offerings that are delivered simply via a website, Activator breaks new ground by delivering a rich application directly to the desktop. Activator updates in real-time with new content from Typesafe and value-add third parties, helping developers engage and adopt Typesafe technologies in an entirely frictionless manner.

Getting started is a snap; just download, extract and run the executable to start building applications immediately via the easy to use wizard-based interface. Common development patterns are presented through reusable templates that are linked to in-context tutorials, which explain step-by-step exactly how things work. The Activator environment supports each stage of the application development lifecycle: Code, Compile, Test and Run. At the appropriate time, Activator can generate fully fledged projects for the leading IDEs so that application development can continue in these environments.

The rich developer content in Typesafe Activator is dynamic and customizable. New templates are published regularly on the Typesafe website, and anyone can contribute new templates!"""

	val religiousText = 
"""Gen|1|1|And, lo, upon high was the diety!
Gen|1|2|And then a miracle occurred.
Gen|1|3|And there was much rejoicing!
Gen|2|1|And it came to pass, that another miracle occurred.
Exo|1|1|Thus spake the Diety, Go forth and defeat the other gods!.
Exo|1|2|The people defeated the other gods."""

	// Fragile, but we need to compare as carefully formatted strings, due to the nature of the
	// data written to the file.
	val activatorNGramExpected = List(
		"activator breaks new ground",
		"activator can generate fully",
		"activator environment supports each",
		"activator is a hub",
		"activator is dynamic and").map(s => s"($s ,1)").mkString("List(", ", ", ")")

	val activatorWordCountExpected = List (
		("", 4),
    ("a", 7),
    ("activator", 8),
    ("add", 1),
    ("addition", 1),
    ("adopt", 1),
    ("an", 1),
    ("and", 7),
    ("anyone", 1),
    ("application", 3),
    ("applications", 2),
    ("appropriate", 1),
    ("are", 4),
    ("at", 1),
    ("based", 3),
    ("breaks", 1),
    ("browser", 2),
    ("build", 1),
    ("building", 1),
    ("by", 2),
    ("can", 3),
    ("code", 1),
    ("command", 1),
    ("common", 1),
    ("compile", 1),
    ("content", 2),
    ("context", 1),
    ("continue", 1),
    ("contribute", 1),
    ("customizable", 1),
    ("delivered", 1),
    ("delivering", 1),
    ("desktop", 1),
    ("developer", 2),
    ("developers", 4),
    ("development", 3),
    ("directly", 1),
    ("download", 1),
    ("dynamic", 1),
    ("each", 1),
    ("easily", 1),
    ("easy", 1),
    ("engage", 1),
    ("entirely", 1),
    ("environment", 1),
    ("environments", 1),
    ("exactly", 1),
    ("executable", 1),
    ("explain", 1),
    ("extract", 1),
    ("fledged", 1),
    ("focused", 1),
    ("for", 2),
    ("frictionless", 1),
    ("from", 1),
    ("fully", 1),
    ("generate", 1),
    ("get", 2),
    ("getting", 1),
    ("ground", 1),
    ("helping", 1),
    ("helps", 2),
    ("how", 1),
    ("hub", 1),
    ("ides", 1),
    ("immediately", 1),
    ("in", 5),
    ("interface", 1),
    ("is", 5),
    ("just", 1),
    ("leading", 1),
    ("lifecycle", 1),
    ("line", 1),
    ("linked", 1),
    ("manner", 1),
    ("new", 5),
    ("of", 1),
    ("offerings", 1),
    ("on", 1),
    ("or", 1),
    ("parties", 1),
    ("patterns", 1),
    ("platform", 2),
    ("presented", 1),
    ("previous", 1),
    ("projects", 1),
    ("published", 1),
    ("quickly", 1),
    ("reactive", 3),
    ("real", 1),
    ("regularly", 1),
    ("reusable", 1),
    ("rich", 2),
    ("run", 2),
    ("simply", 1),
    ("snap", 1),
    ("so", 1),
    ("stage", 1),
    ("start", 1),
    ("started", 3),
    ("step", 2),
    ("supports", 1),
    ("technologies", 2),
    ("templates", 3),
    ("test", 1),
    ("that", 5),
    ("the", 11),
    ("these", 1),
    ("things", 1),
    ("third", 1),
    ("through", 1),
    ("time", 2),
    ("to", 6),
    ("tool", 2),
    ("tutorials", 1),
    ("typesafe", 9),
    ("unique", 1),
    ("unlike", 1),
    ("updates", 1),
    ("use", 1),
    ("value", 1),
    ("via", 2),
    ("wanting", 1),
    ("website", 2),
    ("which", 1),
    ("with", 3),
    ("wizard", 1),
    ("work", 1))

	val religiousTfIdfExpected = List(
		("Exodus",  "the",	   12.417346639258941),
		("Exodus",  "gods",	    8.852529509404196),
		("Exodus",  "other",	  8.852529509404196),
		("Exodus",  "thus",	    5.426264754702098),
		("Exodus",  "defeat",	  5.426264754702098),
		("Exodus",  "defeated",	5.426264754702098),
		("Exodus",  "people",	  5.426264754702098),
		("Exodus",  "forth",	  5.426264754702098),
		("Exodus",  "go",	      5.426264754702098),
		("Exodus",  "spake",	  5.426264754702098),
		("Genesis", "and",	   12.417346639258941),
		("Genesis", "occurred",	8.852529509404196),
		("Genesis", "miracle", 	8.852529509404196),
		("Genesis", "was",	    8.852529509404196),
		("Genesis", "lo",	      5.426264754702098),
		("Genesis", "came",	    5.426264754702098),
		("Genesis", "another",  5.426264754702098),
		("Genesis", "much",	    5.426264754702098),
		("Genesis", "high",	    5.426264754702098),
		("Genesis", "it",	      5.426264754702098))
}