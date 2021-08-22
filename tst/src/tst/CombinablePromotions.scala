package tst

import scala.collection.immutable.ListMap

object CombinablePromotions {

  /** All the hard work happens down below, this is the easy bit! Just give me
    * all the valid combinations with my code.
    */
  def combinablePromotions(
      promotionCode: String,
      allPromotions: Seq[Promotion]
  ): Seq[PromotionCombo] = {
    allCombinablePromotions(allPromotions).filter(
      _.promotionCodes.contains(promotionCode)
    )
  }

  /** There's a union-find (disjoint set) implementation lurking in here, but
    * the representative function didn't fall out as nicely as I might have
    * liked, so instead we'll just walk the graph.
    */
  def allCombinablePromotions(
      allPromotions: Seq[Promotion]
  ): Seq[PromotionCombo] = {

    /** We're sort of building an anti-graph here, where each node in the graph
      * knows which elements it _cannot_ connect to. This is small enough that
      * we could inline it into the left fold where it's used, but it seems like
      * the kind of thing that might be a) usable in other contexts and b) a not
      * unlikely place for business logic to creep in for more exotic
      * exclusions.
      *
      * I sort of wanted to write a cute little monoid here to canonicalize
      * exclusions instead of relying on Map.+ semantics and .distinct, but I
      * didn't have time to make it pleasant.
      */
    def excludeCombination(
        knownExclusions: ListMap[String, Seq[String]],
        promo: Promotion
    ): ListMap[String, Seq[String]] = {
      val promoExclusions: Seq[String] = promo.notCombinableWith
      val newExclusions: Seq[String] =
        knownExclusions
          .get(promo.code)
          .fold(promoExclusions)(_.concat(promoExclusions).distinct)

      knownExclusions + (promo.code -> newExclusions)
    }

    /** Walk down the known promos and collect the ones we can't connect to. */
    val antiEdges: ListMap[String, Seq[String]] =
      allPromotions.foldLeft(ListMap.empty[String, Seq[String]])(
        excludeCombination
      )

    val promos: Seq[String] = antiEdges.keys.toSeq

    /** Now we know all our promo codes, and all the things they _can't_ connect
      * to, we can turn all that inside out and build an adjacency map by
      * mapping each promo code to all the codes which don't appear in its
      * anti-edges.
      */
    val adjacent: ListMap[String, Seq[String]] =
      promos.foldLeft(ListMap.empty[String, Seq[String]]) { (adj, promo) =>
        adj + (promo -> promos.filterNot(antiEdges(promo).contains(_)))
      }

    /** Pick a promo code out of the ether, collect its edges, and then walk
      * down each edge looking for valid combinations. We know a combination is
      * valid if
      *
      * a) the chosen promo code and the promo under inspection share an edge
      *
      * b) the adjacent codes are fully connected (that is, the shared edges of
      * the two codes and the common edges of their neighbors are equal)
      *
      * The .reduce is safe here since the adjacency list for any particular
      * promo always contains at least itself, so the collection can never be
      * empty.
      *
      * There's probably a clever way to prune duplicates here rather than at
      * the end, but I couldn't get one behaving nicely that wasn't equivalent
      * to distinctBy(_.toSet) but uglier.
      *
      * The length check feels hacky, to be honest. There's an edge case where a
      * totally unconnected element is its own entire adjacency group, so it'll
      * show up as a single-element promo combo. The wording of the exercise is
      * promotion combinations that can be "applied together", which sounds like
      * "at least two", and there's no representative data to decide what's
      * correct.
      *
      * To the product owner: Ought a unique and uncombinable discount be
      * excluded or included?
      */
    val combinations = for {
      promo <- promos
      edges = adjacent(promo)
      valid <- for {
        edge <- edges
        sharedEdges = edges.intersect(adjacent(edge))
        commonEdges = sharedEdges.map(adjacent).reduce(_.intersect(_))
        if sharedEdges == commonEdges
      } yield sharedEdges
    } yield valid

    combinations.distinctBy(_.toSet).filter(_.lengthIs > 1).map(PromotionCombo)
  }

  case class Promotion(code: String, notCombinableWith: Seq[String])
  case class PromotionCombo(promotionCodes: Seq[String])
}
