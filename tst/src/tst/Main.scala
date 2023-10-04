package tst

import cats.syntax.all._
import cats.effect.{IO, IOApp}
import tst.BestGroupPrices.{CabinPrice, Rate, getBestGroupPrices}
import tst.CombinablePromotions.{Promotion, allCombinablePromotions}

object Main extends IOApp.Simple {
  override def run: IO[Unit] =
    Seq(bestGroupPrices, combinablePromotions).sequence_

  def bestGroupPrices: IO[Unit] = {
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

    val result = getBestGroupPrices(rates, prices)

    for {
      _ <- IO.println("Exercise 1: Get Best Group Prices")
      _ <- IO.println(result.map(_.toString).mkString("", "\n", "\n"))
    } yield ()
  }

  def combinablePromotions: IO[Unit] = {
    val promotions: Seq[Promotion] = Seq(
      Promotion("P1", Seq("P3")),
      Promotion("P2", Seq("P4", "P5")),
      Promotion("P3", Seq("P1")),
      Promotion("P4", Seq("P2")),
      Promotion("P5", Seq("P2"))
    )

    val allCombos = allCombinablePromotions(promotions)
    val p1        = CombinablePromotions.combinablePromotions("P1", promotions)
    val p2        = CombinablePromotions.combinablePromotions("P2", promotions)
    val results = Seq(
      "Exercise 2: Get Valid Promo Combinations",
      "All Combinations",
      allCombos.map(_.toString).mkString("", "\n", "\n"),
      "Combinations for P1",
      p1.map(_.toString).mkString("", "\n", "\n"),
      "Combinations for P2",
      p2.map(_.toString).mkString("", "\n", "\n")
    )

    results.traverse_(IO.println)
  }

}
