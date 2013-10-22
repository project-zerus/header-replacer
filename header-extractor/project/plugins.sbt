resolvers += "miliao-public" at "http://maven.n.miliao.com:8081/nexus/content/groups/public"

resolvers += Resolver.url("miliao-public-ivy", url("http://maven.n.miliao.com:8081/nexus/content/groups/public"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.2.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.5.4")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.4.0")

addSbtPlugin("com.github.sdb" % "xsbt-filter" % "0.3")
