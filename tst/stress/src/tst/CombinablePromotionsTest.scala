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

      test("Huge data") {
        val promotions: Seq[Promotion] =
          Seq.fill(10000000)(Promotion("P1", Seq.empty)).appended(Promotion("P2", Seq.empty))
        test("All combinations") {
          val expected = Seq(PromotionCombo(Seq("P1", "P2")))
          underTest(promotions, expected)
        }
        test("Get P1") {
          val expected = Seq(PromotionCombo(Seq("P1", "P2")))
          underTest(promotions, expected, Some("P1"))
        }
        test("Get nonexistent") {
          val expected = Seq.empty
          underTest(promotions, expected, Some("P3"))
        }
      }
    }
  }
}
