import sbt._
import java.io.File

class Project(info: ProjectInfo) extends DefaultProject(info)
{
  
  // define a root-level environment file local.properties
  // from which scala.virtualized.home will be read
  lazy val local = new BasicEnvironment {
      def log = Project.this.log
      def envBackingPath = info.projectPath / "local.properties"
      lazy val scalaVirtualizedHome = property[String]
  }
  
  // use the local scala-virtualized compiler and library
  // override def localScala =
  //   defineScala("2.10.x-virtualized-SNAPSHOT", new File(local.scalaVirtualizedHome.get.getOrElse {
  //     log.error("scala.virtualized.home needs to be defined in local.properties and "+
  //     "must point to a valid scala-virtualized home directory"); "<undefined>"
  //   }))::Nil

  // source directory layout
  override def mainScalaSourcePath = "src"
  override def mainResourcesPath = "resources"

  override def testScalaSourcePath = "test-src"
  override def testResourcesPath = "test-resources"

  // target directory layout (standard for now)
  
  // dependencies
  val scalaToolsSnapshots = ScalaToolsSnapshots
  val dropbox = "Dropbox" at "http://dl.dropbox.com/u/12870350/scala-virtualized"
  val scalatest = "org.scalatest" % "scalatest_2.10.0-virtualized-SNAPSHOT" % "1.6.1-SNAPSHOT" % "test"
  val virtualization_lms_core = "scala" % "virtualization-lms-core_2.10.0-virtualized-SNAPSHOT" % "0.1"

  // compile options
  override def compileOptions = super.compileOptions ++ Seq(/*Unchecked, */Deprecation) ++ compileOptions("-Xexperimental")

  override def testCompileOptions = super.testCompileOptions ++ compileOptions("-Xplugin:"+
    local.scalaVirtualizedHome.get.get+"/misc/scala-devel/plugins/continuations.jar", "-P:continuations:enable")

}
