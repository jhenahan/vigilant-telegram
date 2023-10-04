package tst

import cats.syntax.all._
import fs2._
import cats.effect.{IO, IOApp}
import tst.BestGroupPrices.{CabinPrice, Rate, getBestGroupPrices}
import tst.CombinablePromotions._

/** This is a fairly overwrought way to write a tiny application like this one,
  * but the pattern we're demonstrating is quite scalable. Nearly any
  * application structure you can imagine can be neatly expressed as a
  * `Stream[IO, Inputs] => Stream[IO, Outputs]`, and what goes on in that `=>`
  * can be as complex as you like. This leads to a common program structure:
  *
  * {{{
  * Stream.eval(inputs) // All your effectful configuration logic (env vars, flags, etc.)
  *   .through(businessLogic) // A Pipe[IO, Inputs, Outputs]
  *   .compile
  *   .drain
  *   .as(ExitCode.Success)
  * }}}
  *
  * where `.compile.drain` just serves to run all the effectful pipeline work
  * down to an `IO[Unit]`.
  */
object Main extends IOApp.Simple {
  override def run: IO[Unit] =
    (exercise1 ++ exercise2).compile.drain

  import ExerciseData._

  val exercise1: Stream[IO, Nothing] = exerciseSection(
    """Exercise 1: Get Best Group Prices
    """.stripMargin,
    getBestGroupPrices(rates, prices)
  )

  val exercise2: Stream[IO, Nothing] =
    exerciseSection(
      """
      |Exercise 2: Get Valid Promo Combinations
      |
      |All Combinations""".stripMargin,
      allCombinablePromotions(promotions)
    ) ++
      exerciseSection(
        "Combinations for P1",
        combinablePromotions("P1", promotions)
      ) ++
      exerciseSection(
        "Combinations for P3",
        combinablePromotions("P3", promotions)
      )

  def exerciseSection[A](header: String, data: Seq[A]): Stream[IO, Nothing] =
    Stream.exec(IO.println(header)) ++
      Stream.emits(data).covary[IO].printlns ++
      Stream.exec(IO.print("\n"))

}

object ExerciseData {
  val rates = Seq(
    Rate("M1", "Military"),
    Rate("M2", "Military"),
    Rate("S1", "Senior"),
    Rate("S2", "Senior")
  )

  val prices = Seq(
    CabinPrice("CA", "M1", 200.00),
    CabinPrice("CA", "M2", 250.00),
    CabinPrice("CA", "S1", 225.00),
    CabinPrice("CA", "S2", 260.00),
    CabinPrice("CB", "M1", 230.00),
    CabinPrice("CB", "M2", 260.00),
    CabinPrice("CB", "S1", 245.00),
    CabinPrice("CB", "S2", 270.00)
  )

  val promotions: Seq[Promotion] = Seq(
    Promotion("P1", Seq("P3")),
    Promotion("P2", Seq("P4", "P5")),
    Promotion("P3", Seq("P1")),
    Promotion("P4", Seq("P2")),
    Promotion("P5", Seq("P2"))
  )
}
