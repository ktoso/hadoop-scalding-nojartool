// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Assemble a single jar with all dependencies.
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.10.1")
