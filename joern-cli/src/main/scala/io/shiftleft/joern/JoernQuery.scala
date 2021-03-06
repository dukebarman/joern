package io.shiftleft.joern

import better.files.File

object JoernQuery extends App {

  parseConfig.foreach { config =>
    val e = new JoernScriptExecutor()

    if (config.isFile) {
      val script = File(config.query).lines.mkString(System.lineSeparator())
      println(e.runScript(script, config.cpgFilename))
    } else {
      val script = config.query + ".l.mkString(\"\\n\")"
      println(e.runScript(script, config.cpgFilename))
    }
  }

  case class Config(cpgFilename: String, query: String, isFile: Boolean = false)
  def parseConfig: Option[Config] =
    new scopt.OptionParser[Config]("joern-query") {
      opt[String]('c', "cpg")
        .text("the code property graph to run queries on (default: cpg.bin.zip)")
        .action((x, c) => c.copy(cpgFilename = x))
      arg[String]("<query|filename>")
        .text("query to execute, or script file to execute if -f is set")
        .action((x, c) => c.copy(query = x))
      opt[Unit]('f', "isFile")
        .text("if specified, the second parameter is assumed to be a script file")
        .action((_, c) => c.copy(isFile = true))
    }.parse(args, Config("cpg.bin.zip", ""))

}
