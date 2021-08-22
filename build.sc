import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._, scalalib._

object tst extends ScalaModule {
  def scalaVersion = "2.13.6"
  override def ivyDeps = Agg(
    ivy"org.typelevel::cats-effect:3.2.3"
  )

  object tests extends TstTests
  object stress extends TstTests

  trait TstTests extends Tests {
    override def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.7.10")

    override def testFramework: T[String] = "tst.CustomFramework"
  }
}
