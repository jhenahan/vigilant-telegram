package tst

import tst.BestGroupPrices._
import tst.StressHelpers._
import munit._

class BestGroupPricesTest extends FunSuite {
  def underTest(
      rates: Seq[Rate],
      prices: Seq[CabinPrice],
      expected: Seq[BestGroupPrice]
  )(implicit loc: Location) = {
    val theWork = getBestGroupPrices(rates, prices)
    assertEquals(theWork, expected)
  }

  test("pathological case: massively duplicated data") {
    val rates =
      Seq.fill(10000000)(Rate("M1", "Military"))

    val prices = Seq.fill(10000000)(CabinPrice("CA", "M1", 200.00))

    val expected = Seq(
      BestGroupPrice("CA", "M1", 200.00, "Military")
    )

    underTest(rates, prices, expected)
  }

  test("pathological case: massively duplicated randomly distributed data") {
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

    val testRates = unsafeIntercalateRandom(rates, rates, 10000000 / rates.size)

    val testPrices =
      unsafeIntercalateRandom(prices, prices, 10000000 / prices.size)

    val expected = Seq(
      BestGroupPrice("CA", "M1", 200.00, "Military"),
      BestGroupPrice("CA", "S1", 225.00, "Senior"),
      BestGroupPrice("CB", "M1", 230.00, "Military"),
      BestGroupPrice("CB", "S1", 245.00, "Senior")
    )

    underTest(testRates, testPrices, expected)
  }
}
