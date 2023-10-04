package tst

import tst.CombinablePromotions._
import munit._

class CombinablePromotionsTest extends FunSuite {
  def underTest(
      allPromotions: Seq[Promotion],
      expected: Seq[PromotionCombo],
      targetedPromotion: Option[String] = None
  )(implicit loc: Location) = {
    val theWork = targetedPromotion.fold(
      allCombinablePromotions(allPromotions)
    )(combinablePromotions(_, allPromotions))

    assertEquals(theWork, expected)
  }

  def acceptance(
      expected: Seq[PromotionCombo],
      targetedPromotion: Option[String] = None
  )(implicit loc: Location) = {
    val promotions: Seq[Promotion] = Seq(
      Promotion("P1", Seq("P3")),
      Promotion("P2", Seq("P4", "P5")),
      Promotion("P3", Seq("P1")),
      Promotion("P4", Seq("P2")),
      Promotion("P5", Seq("P2"))
    )
    underTest(promotions, expected, targetedPromotion)
  }

  test("acceptance test: all combinations") {
    val expected = Seq(
      PromotionCombo(Seq("P1", "P2")),
      PromotionCombo(Seq("P1", "P4", "P5")),
      PromotionCombo(Seq("P2", "P3")),
      PromotionCombo(Seq("P3", "P4", "P5"))
    )

    acceptance(expected)
  }

  test("acceptance test: P1 combinations") {
    val expected = Seq(
      PromotionCombo(Seq("P1", "P2")),
      PromotionCombo(Seq("P1", "P4", "P5"))
    )

    acceptance(expected, Some("P1"))
  }
  test("acceptance test: P3 combinations") {
    val expected = Seq(
      PromotionCombo(Seq("P3", "P2")),
      PromotionCombo(Seq("P3", "P4", "P5"))
    )

    acceptance(expected, Some("P3"))
  }

  test("edge case: no data implies no promotion combos") {
    val promotions = Seq.empty
    val expected   = Seq.empty

    underTest(promotions, expected)
  }

  test("edge case: nonexistent promotions have no promotion combos") {
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

  test("edge case: promotions may exclude themselves") {
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

// Edge cases, but like in a graph lol

  test(
    "graph properties: a complete graph yields a single promotion combo containing all promotions"
  ) {
    val promotions: Seq[Promotion] = Seq(
      Promotion("P1", Seq.empty),
      Promotion("P2", Seq.empty),
      Promotion("P3", Seq.empty)
    )

    val expected = Seq(
      PromotionCombo(promotions.map(_.code))
    )

    underTest(promotions, expected)
  }
  test(
    "graph properties: a bipartite graph yields two disjoint promotion combos"
  ) {
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
  test(
    "graph properties: distinct subgraphs connected by a shared vertex yield distinct promotion combos sharing the connecting promotion"
  ) {
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
  test(
    "graph properties: unconnected elements cannot be part of a promotion combo"
  ) {
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
