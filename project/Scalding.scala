import sbt._
import Types._
import Path._
import Keys._

/**
 * Run a Scalding script in local mode.
 * Assumes the scripts are in the src tree and will be compiled by the compile task.
 */
object Scalding {

  val scalding = inputKey[Unit]("Run one of the Scalding scripts.")

  val scaldingTask = scalding := {
    import complete.DefaultParsers._
    val log = streams.value.log
    val args: Vector[String] = spaceDelimited("script>").parsed.toVector
    if (args.size > 0) {
      val mainClass = "com.twitter.scalding.Tool"
      val actualArgs = Vector[String](args.head, "--local") ++ args.tail
      log.info(s"Running scala $mainClass ${actualArgs.mkString(" ")}")
      output(log, 
        (runner in run).value.run(mainClass, Attributed.data((fullClasspath in Runtime).value), 
          actualArgs, streams.value.log))
    } else {
      // Find the available scripts and build a help message.
      log.error("Please specify one of the following commands (example arguments shown):")
      val scriptCmds = (sources in Compile).value
        .filter(file => rootName(file) != "RunAll")
        .map(file => extractCommand(file))
      scriptCmds foreach (s => log.error(s"  $s"))
      log.error("scalding requires arguments.")
    }
  }

  scalding <<= scalding.dependsOn (compile in Compile)

  val scaldingSettings = Seq(scaldingTask)

  private def output(log: Logger, os: Option[String]): Unit = 
    os foreach (s => log.info(s"|  $s"))

  // Each script has a comment with the required sbt command. We find it,
  // extract the interesting part or use a default message.
  private def extractCommand(file: File): String = 
    scala.io.Source.fromFile(file).getLines.find(_.matches("""^\s+\*\s+scalding\s+.*$""")) match {
      case None => 
        val name = rootName(file)
        s"scalding $name [arguments] (see the source file for details)"
      case Some(s) => s.replaceAll("""^\s+\*\s+""", "")
    }

  private def rootName(file: File) = {
    val fname = file.getName
    fname.substring(fname.lastIndexOf("/")+1, fname.lastIndexOf("."))
  }
}