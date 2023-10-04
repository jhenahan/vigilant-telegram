package tst

import scala.util.Random

object StressHelpers {

  /** Ugly test data generation method. Do not perceive me.
    *
    * Produce an infinite stream of random permutations of the seed data,
    * flatten the permutations into a single stream, take the desired number of
    * elements, and produce a Seq.
    */
  def unsafeRandomFill[T](seed: Seq[T], size: Int): Seq[T] = {
    import scala.util.Random

    Stream.continually(Random.shuffle(seed)).flatten.take(size).toSeq
  }

  /** Insert the elements of `subseq` between each element of `original`,
    * evaluating `subseq` for each element of `original`.
    */
  def intercalate[T](original: Seq[T], subseq: => Seq[T]): Seq[T] =
    original.flatMap(_ +: subseq)

  def unsafeIntercalateRandom[T](
      original: Seq[T],
      seed: Seq[T],
      size: Int
  ): Seq[T] =
    intercalate(original, unsafeRandomFill(seed, size))
}
