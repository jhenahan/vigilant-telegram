package tst

import tst.CombinablePromotions._
import munit._

class CombinablePromotionsTest extends FunSuite {
  def underTest(
      allPromotions: Seq[Promotion],
      expected: Seq[PromotionCombo],
      targetedPromotion: Option[String] = None
  )(implicit loc: Location) = {
    val theWork =
      targetedPromotion.fold(allCombinablePromotions(allPromotions))(
        combinablePromotions(_, allPromotions)
      )
    assertEquals(theWork, expected)
  }

  /** Pathological scenario: Produce promotion combos out of data that's nearly
    * all identical, with only the final element differing from the rest.
    */
  val promotions: Seq[Promotion] =
    Seq.fill(10000000)(Promotion("P1", Seq.empty)) :+ Promotion("P2", Seq.empty)

  test("pathological case: massively duplicated data - all combinations") {
    val expected = Seq(PromotionCombo(Seq("P1", "P2")))
    underTest(promotions, expected)
  }
  test("pathological case: massively duplicated data - P1 combinations") {
    val expected = Seq(PromotionCombo(Seq("P1", "P2")))
    underTest(promotions, expected, Some("P1"))
  }
  test("pathological case: massively duplicated data - nonexistent promotion") {
    val expected = Seq.empty
    underTest(promotions, expected, Some("P3"))
  }
}
