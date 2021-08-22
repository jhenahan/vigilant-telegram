package tst

import tst.BestGroupPrices._
import utest._

object BestGroupPricesTest extends TestSuite {
  override val tests = Tests {
    test("BestGroupPrices") {
      def underTest(
          rates: Seq[Rate],
          prices: Seq[CabinPrice],
          expected: Seq[BestGroupPrice]
      ) = {
        val theWork = getBestGroupPrices(rates, prices)
        assert(theWork == expected)
      }
      test("Huge data") {
        val rates =
          Seq.fill(10000000)(Rate("M1", "Military"))

        val prices = Seq.fill(10000000)(CabinPrice("CA", "M1", 200.00))

        val expected = Seq(
          BestGroupPrice("CA", "M1", 200.00, "Military")
        )

        underTest(rates, prices, expected)
      }
    }
  }
}
