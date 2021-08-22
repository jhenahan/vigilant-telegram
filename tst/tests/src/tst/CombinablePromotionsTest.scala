package tst

import tst.CombinablePromotions._
import utest._

object CombinablePromotionsTest extends TestSuite {

  val tests = Tests {
    test("CombinablePromotions") {
      def underTest(
          allPromotions: Seq[Promotion],
          expected: Seq[PromotionCombo],
          targetedPromotion: Option[String] = None
      ) = {
        val theWork =
          targetedPromotion.fold(allCombinablePromotions(allPromotions))(
            combinablePromotions(_, allPromotions)
          )
        assert(theWork == expected)
      }

      test("Provided data") {
        val promotions: Seq[Promotion] = Seq(
          Promotion("P1", Seq("P3")),
          Promotion("P2", Seq("P4", "P5")),
          Promotion("P3", Seq("P1")),
          Promotion("P4", Seq("P2")),
          Promotion("P5", Seq("P2"))
        )

        test("All combinations") {
          val expected = Seq(
            PromotionCombo(Seq("P1", "P2")),
            PromotionCombo(Seq("P1", "P4", "P5")),
            PromotionCombo(Seq("P2", "P3")),
            PromotionCombo(Seq("P3", "P4", "P5"))
          )

          underTest(promotions, expected)
        }
        test("P1 combinations") {
          val expected = Seq(
            PromotionCombo(Seq("P1", "P2")),
            PromotionCombo(Seq("P1", "P4", "P5"))
          )

          underTest(promotions, expected, Some("P1"))
        }
        test("P3 combinations") {
          val expected = Seq(
            PromotionCombo(Seq("P2", "P3")),
            PromotionCombo(Seq("P3", "P4", "P5"))
          )

          underTest(promotions, expected, Some("P3"))
        }

      }
      test("Edge cases") {
        test("No data") {
          val promotions = Seq.empty
          val expected = Seq.empty

          underTest(promotions, expected)
        }
        test("Nonexistent promotion requested") {
          val promotions: Seq[Promotion] = Seq(
            Promotion("P1", Seq("P3")),
            Promotion("P2", Seq("P4", "P5")),
            Promotion("P3", Seq("P1")),
            Promotion("P4", Seq("P2")),
            Promotion("P5", Seq("P2"))
          )

          val expected = Seq.empty

          underTest(promotions, expected, Some("P6"))
        }
        test("Self-exclusion") {
          val promotions: Seq[Promotion] = Seq(
            Promotion("P1", Seq("P1", "P3")),
            Promotion("P2", Seq("P4", "P5")),
            Promotion("P3", Seq("P1")),
            Promotion("P4", Seq("P2")),
            Promotion("P5", Seq("P2"))
          )

          val expected = Seq(
            PromotionCombo(Seq("P2", "P3")),
            PromotionCombo(Seq("P3", "P4", "P5"))
          )

          underTest(promotions, expected)
        }
      }
      test("Edge cases, but like in a graph lol") {
        test("Complete graph") {
          val promotions: Seq[Promotion] = Seq(
            Promotion("P1", Seq.empty),
            Promotion("P2", Seq.empty),
            Promotion("P3", Seq.empty)
          )

          val expected = Seq(
            PromotionCombo(Seq("P1", "P2", "P3"))
          )

          underTest(promotions, expected)
        }
        test("Bipartite graph") {
          val promotions: Seq[Promotion] = Seq(
            Promotion("P1", Seq("P3", "P4")),
            Promotion("P2", Seq("P3", "P4")),
            Promotion("P3", Seq("P1", "P2")),
            Promotion("P4", Seq("P1", "P2"))
          )

          val expected = Seq(
            PromotionCombo(Seq("P1", "P2")),
            PromotionCombo(Seq("P3", "P4"))
          )

          underTest(promotions, expected)
        }
        test("Triangles") {
          // The intersection of P1 <-> P2 <-> P3 and P1 <-> P4 <-> P5
          val promotions = Seq(
            Promotion("P1", Seq.empty),
            Promotion("P2", Seq("P4", "P5")),
            Promotion("P3", Seq("P4", "P5")),
            Promotion("P4", Seq("P2", "P3")),
            Promotion("P5", Seq("P2", "P3"))
          )

          val expected = Seq(
            PromotionCombo(Seq("P1", "P2", "P3")),
            PromotionCombo(Seq("P1", "P4", "P5"))
          )

          underTest(promotions, expected)
        }
        test("Unconnected element") {
          val promotions: Seq[Promotion] = Seq(
            Promotion("P1", Seq("P3", "P6")),
            Promotion("P2", Seq("P4", "P5", "P6")),
            Promotion("P3", Seq("P1", "P6")),
            Promotion("P4", Seq("P2", "P6")),
            Promotion("P5", Seq("P2", "P6")),
            Promotion("P6", Seq("P1", "P2", "P3", "P4", "P5"))
          )

          val expected = Seq(
            PromotionCombo(Seq("P1", "P2")),
            PromotionCombo(Seq("P1", "P4", "P5")),
            PromotionCombo(Seq("P2", "P3")),
            PromotionCombo(Seq("P3", "P4", "P5"))
          )

          underTest(promotions, expected)
        }
      }
    }
  }
}
