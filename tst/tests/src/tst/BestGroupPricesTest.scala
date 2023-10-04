package tst

import tst.BestGroupPrices._
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

  test("acceptance tests") {
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

    val expected = Seq(
      BestGroupPrice("CA", "M1", 200.00, "Military"),
      BestGroupPrice("CA", "S1", 225.00, "Senior"),
      BestGroupPrice("CB", "M1", 230.00, "Military"),
      BestGroupPrice("CB", "S1", 245.00, "Senior")
    )

    underTest(rates, prices, expected)
  }

  test("demonstration: extended test data") {
    val rates = Seq(
      Rate("M1", "Military"),
      Rate("M2", "Military"),
      Rate("S1", "Senior"),
      Rate("S2", "Senior"),
      Rate("P1", "Promo"),
      Rate("P2", "Promo")
    )
    val prices = Seq(
      CabinPrice("CA", "M1", 200.00),
      CabinPrice("CA", "M2", 250.00),
      CabinPrice("CA", "S1", 225.00),
      CabinPrice("CA", "S2", 260.00),
      CabinPrice("CA", "P1", 275.00),
      CabinPrice("CA", "P2", 260.00),
      CabinPrice("CB", "M1", 230.00),
      CabinPrice("CB", "M2", 260.00),
      CabinPrice("CB", "S1", 245.00),
      CabinPrice("CB", "S2", 270.00),
      CabinPrice("CB", "P1", 245.00),
      CabinPrice("CB", "P2", 270.00)
    )

    val expected = Seq(
      BestGroupPrice("CA", "M1", 200.00, "Military"),
      BestGroupPrice("CA", "P2", 260.00, "Promo"),
      BestGroupPrice("CA", "S1", 225.00, "Senior"),
      BestGroupPrice("CB", "M1", 230.00, "Military"),
      BestGroupPrice("CB", "P1", 245.00, "Promo"),
      BestGroupPrice("CB", "S1", 245.00, "Senior")
    )

    underTest(rates, prices, expected)
  }

  test("results are independent of the order of rates") {
    val rates = Seq(
      Rate("M2", "Military"),
      Rate("S1", "Senior"),
      Rate("S2", "Senior"),
      Rate("M1", "Military")
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

    val expected = Seq(
      BestGroupPrice("CA", "M1", 200.00, "Military"),
      BestGroupPrice("CA", "S1", 225.00, "Senior"),
      BestGroupPrice("CB", "M1", 230.00, "Military"),
      BestGroupPrice("CB", "S1", 245.00, "Senior")
    )

    underTest(rates, prices, expected)
  }

  test("results are independent of the order of prices") {
    val rates = Seq(
      Rate("M1", "Military"),
      Rate("M2", "Military"),
      Rate("S1", "Senior"),
      Rate("S2", "Senior")
    )
    val prices = Seq(
      CabinPrice("CA", "M1", 200.00),
      CabinPrice("CB", "M2", 260.00),
      CabinPrice("CA", "S1", 225.00),
      CabinPrice("CB", "S1", 245.00),
      CabinPrice("CA", "S2", 260.00),
      CabinPrice("CB", "M1", 230.00),
      CabinPrice("CA", "M2", 250.00),
      CabinPrice("CB", "S2", 270.00)
    )

    val expected = Seq(
      BestGroupPrice("CA", "M1", 200.00, "Military"),
      BestGroupPrice("CA", "S1", 225.00, "Senior"),
      BestGroupPrice("CB", "M1", 230.00, "Military"),
      BestGroupPrice("CB", "S1", 245.00, "Senior")
    )

    underTest(rates, prices, expected)
  }

  test("results are independent of the order of inputs") {
    val rates = Seq(
      Rate("M2", "Military"),
      Rate("S1", "Senior"),
      Rate("S2", "Senior"),
      Rate("M1", "Military")
    )
    val prices = Seq(
      CabinPrice("CA", "M1", 200.00),
      CabinPrice("CB", "M2", 260.00),
      CabinPrice("CA", "S1", 225.00),
      CabinPrice("CB", "S1", 245.00),
      CabinPrice("CA", "S2", 260.00),
      CabinPrice("CB", "M1", 230.00),
      CabinPrice("CA", "M2", 250.00),
      CabinPrice("CB", "S2", 270.00)
    )

    val expected = Seq(
      BestGroupPrice("CA", "M1", 200.00, "Military"),
      BestGroupPrice("CA", "S1", 225.00, "Senior"),
      BestGroupPrice("CB", "M1", 230.00, "Military"),
      BestGroupPrice("CB", "S1", 245.00, "Senior")
    )

    underTest(rates, prices, expected)
  }

  test("edge case: No rates implies no best group price") {
    val rates = Seq.empty
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

    val expected = Seq.empty

    underTest(rates, prices, expected)
  }
  test("edge case: No prices implies no best group price") {
    val rates = Seq(
      Rate("M1", "Military"),
      Rate("M2", "Military"),
      Rate("S1", "Senior"),
      Rate("S2", "Senior")
    )
    val prices = Seq.empty

    val expected = Seq.empty

    underTest(rates, prices, expected)
  }
  test("edge case: No data implies no best group prices") {
    val rates  = Seq.empty
    val prices = Seq.empty

    val expected = Seq.empty

    underTest(rates, prices, expected)
  }
  test(
    "edge case: Rates without price data are not mapped to best group prices"
  ) {
    val rates = Seq(
      Rate("M1", "Military"),
      Rate("M2", "Military"),
      Rate("S1", "Senior"),
      Rate("S2", "Senior")
    )
    val prices = Seq(
      CabinPrice("CA", "M1", 210.00),
      CabinPrice("CA", "M2", 220.00),
      CabinPrice("CB", "M1", 120.00),
      CabinPrice("CB", "M2", 240.00)
    )

    val expected = Seq(
      BestGroupPrice("CA", "M1", 210.00, "Military"),
      BestGroupPrice("CB", "M1", 120.00, "Military")
    )

    underTest(rates, prices, expected)
  }
  test(
    "edge case: Prices without rate data are not mapped to best group prices"
  ) {
    val rates = Seq(
      Rate("S1", "Senior"),
      Rate("S2", "Senior")
    )
    val prices = Seq(
      CabinPrice("CA", "M1", 200.00),
      CabinPrice("CA", "M2", 100.00),
      CabinPrice("CA", "S1", 225.00),
      CabinPrice("CA", "S2", 260.00),
      CabinPrice("CB", "M1", 230.00),
      CabinPrice("CB", "M2", 190.00),
      CabinPrice("CB", "S1", 200.00),
      CabinPrice("CB", "S2", 230.00)
    )

    val expected = Seq(
      BestGroupPrice("CA", "S1", 225.00, "Senior"),
      BestGroupPrice("CB", "S1", 200.00, "Senior")
    )

    underTest(rates, prices, expected)
  }
}
