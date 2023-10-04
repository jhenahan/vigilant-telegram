package tst
object BestGroupPrices {

  /** Just what it says on the tin.
    *
    * Build some fancier price objects, dump them in the pipeline. The sort is
    * entirely for the benefit of the test data.
    */
  def getBestGroupPrices(
      rates: Seq[Rate],
      prices: Seq[CabinPrice]
  ): Seq[BestGroupPrice] = {
    (cheapFlights _)
      .compose(groupPrices)(enrichPrices(rates, prices))
      .sortBy(bgp => (bgp.cabinCode, bgp.rateCode))
  }

  /** Relate cabin prices to their associated rate groups.
    *
    * Two flavors, according to taste:
    *
    * Option 1 drops any prices with rate codes not corresponding to a rate
    * group on the floor. This isn't my favorite approach, really. I'd rather
    * not throw information away, but such is life, and that may be the correct
    * thing to do in this domain. It also does that thing I don't like where we
    * mix our functors (List and Option) and Scala does what I mean rather than
    * yelling at me. Importantly, this prevents us from simply listing the
    * cheapest rate for each cabin at the end. It may be very interesting that
    * senior discount in cabin B is the cheapest available rate, but that
    * doesn't help me if I'm not a senior.
    *
    * Option 2 (commented) keeps all data, mapping the unmappable to a default.
    * This makes some kind of sense (I can't imagine the airline or whatever
    * would list a discount they didn't want to honor, whether or not it has a
    * nice name). Bonus: No implicit Seq<=>Option wackiness. If there is a
    * meaningful way to work with this (say, the rateGroup string is entirely
    * decorative, and the whole world runs on rateCodes), this would be my
    * preferred solution. Given rateCodes you belong to, I can still tell you
    * your cheapest rate even if the code doesn't map to anything.
    *
    * Option 3 would be to drop the .getOrElse and just carry around the None,
    * but then I gotta ask myself if the Option overhead balances out carrying
    * around a bunch of strings. I would *hope* that the JVM would just intern
    * the thing, so this is probably fine.
    */
  private def enrichPrices(
      rates: Seq[Rate],
      prices: Seq[CabinPrice]
  ): Seq[RateGroupPrice] = for {
    price <- prices
    rate  <- rates.find(_.rateCode == price.rateCode).map(_.rateGroup)
    // rate = rates
    //   .find(_.rateCode == price.rateCode)
    //   .map(_.rateGroup)
    //   .getOrElse("Unknown")
  } yield RateGroupPrice(rate, price)

  /** Given some enriched prices, link the rate group to the cabin code and
    * relate those pairs back to the cabin price.
    *
    * I don't *love* the duplication here (cabinCode is on both sides of the
    * Map), but it's necessary to avoid dropping too much on the floor when we
    * get the cheap ones. There's probably a cleaner representation lurking here
    * that avoids all that, but this is at least a very obvious transformation.
    *
    * As discussed above, this should be fine if I decided to carry around the
    * None instead of the string, though I still have to run it down to a string
    * in cheapFlights to make BestGroupPrice happy, anyway, so shrug on that.
    */
  private def groupPrices(
      prices: Seq[RateGroupPrice]
  ): Map[CabinGroup, Seq[CabinPrice]] =
    prices.groupMap { p =>
      CabinGroup(p.rateGroup, p.cabinPrice.cabinCode)
    }(_.cabinPrice)

  /** With our slightly redundant price data in hand, get the cheapest price for
    * each of the cabin groups.
    *
    * Since cabinCode is on both sides of this map, the decision about where to
    * pull that value from is kind of arbitrary, but I err on the side of the
    * "canonical" type CabinPrice for mostly gut reasons.
    */
  private def cheapFlights(
      priceMap: Map[CabinGroup, Seq[CabinPrice]]
  ): Seq[BestGroupPrice] =
    priceMap.view
      .mapValues(_.minBy(_.price))
      .map { case (group, cabin) =>
        BestGroupPrice(
          cabin.cabinCode,
          cabin.rateCode,
          cabin.price,
          group.rateGroup
        )
      }
      .toSeq

  case class RateGroupPrice(rateGroup: String, cabinPrice: CabinPrice)

  case class CabinGroup(rateGroup: String, cabinCode: String)

  case class Rate(rateCode: String, rateGroup: String)

  case class CabinPrice(cabinCode: String, rateCode: String, price: BigDecimal)

  case class BestGroupPrice(
      cabinCode: String,
      rateCode: String,
      price: BigDecimal,
      rateGroup: String
  ) {
    override def toString: String =
      f"BestGroupPrice($cabinCode, $rateCode, $price%.2f, $rateGroup)"
  }
}
