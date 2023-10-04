{
  inputs,
  cell,
} @ block: {
  java = import ./java.nix block;
  scala = import ./scala.nix block;
}
