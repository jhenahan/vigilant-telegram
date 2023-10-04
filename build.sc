import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._, scalalib._, scalafmt._

object tst extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.12"
  override def ivyDeps = Agg(
    ivy"co.fs2::fs2-core:3.9.2"
  )

  object tests  extends TstTests
  object stress extends TstTests

  trait TstTests extends ScalaTests {
    override def ivyDeps = Agg(ivy"org.scalameta::munit:1.0.0-M10")
    override def testFramework: T[String] = "munit.Framework"
  }
}
