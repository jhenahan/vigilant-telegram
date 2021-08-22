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

      test("Provided data") {
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
      test("Richer data") {
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
      test("Sanity checking") {
        test("Shuffled rates") {
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
        test("Shuffled prices") {
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
        test("Shuffled data") {
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
      }
      test("Edge cases") {
        test("No rates") {
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
        test("No prices") {
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
        test("No data") {
          val rates = Seq.empty
          val prices = Seq.empty

          val expected = Seq.empty

          underTest(rates, prices, expected)
        }
        test("Lopsided data") {
          test("Rates without price data") {
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
          test("Prices without rate data") {
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
      }
    }
  }
}
