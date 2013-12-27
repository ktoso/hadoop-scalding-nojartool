NoJarTool
=========

NoJarTool is an implementation of `org.apache.hadoop.util.Tool` which aims to allow running Scalding Map Reduce jobs right from your SBT console. The difference with `NoJarTool` as the name implies, means that it's not required to run `assembly` and then submit jobs using the "fat jar".

**Why?** The assembly step sometimes can take very long, and we've got enough very long things already. During development, it's way better if you're able to submit jobs right away from where you're writing them - your `sbt` console.

**How?** The Tool is rather simple. It uses the `-libjars` option, which you can use from the command line for `hadoop` to add all dependencies and Job classes to the classpath (on all nodes executing the job). The small problem here is to put Cascading/Scalding into these deps (build an `assembly` for only _dependencies_) once and then keep using this Tool to submit the `*.class` files of your project.

More information about this option and other options to distribute your other jar's is nicely written up here: http://grepalex.com/2013/02/25/hadoop-libjars/

Usage / code
============

**Read the source, Luke: https://github.com/ktoso/hadoop-scalding-nojartool/blob/master/src/main/scala/pl/project13/hadoop/NoJarTool.scala**

Call it so:

```scala
import org.apache.hadoop.util.ToolRunner
import org.apache.hadoop.conf.Configuration
import pl.project13.hadoop.scalding.NoJarTool
import com.twitter.scalding

val conf = new Configuration

// make suse these are set, otherwise Cascading will use "LocalJobRunner"
conf.setStrings("fs.default.name", s"hdfs://$masterIp:9000")
conf.setStrings("mapred.job.tracker", s"$masterIp:9001")

val tool = new NoJarTool(
  wrappedTool = new scalding.Tool,
  collectClassesFrom = Some(new File("target/scala-2.10/classes")),
  libJars = List(new File("/home/kmalawski/all-deps.jar"))
)

ToolRunner.run(conf, tool, argsWithName)
```

This will collect all classes from your project, and the jar dependencies, send them over to hadoop and execute.
If you don't set the `mapred.job.tracker` prop, the Tool will complain that you're probably forgetting about it (see the long thread on [cascading-user](https://groups.google.com/forum/#!topic/cascading-user/WaHc5MaIlIs) about devs being confused why jobs are running locally).

License
=======

Same as Scalding, too lazy to check right now. It's just one file anyway ;-)
