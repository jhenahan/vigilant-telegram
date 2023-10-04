{
  inputs,
  cell,
} @ block: let
  inherit (inputs) nixpkgs std;
  inherit (inputs.std) lib;
  l = nixpkgs.lib // builtins;
  inherit (inputs.std.data) configs;
  inherit (inputs.std.lib.dev) mkNixago;
  conform-config = import ./conform-config.nix block;
  lefthook-config = import ./lefthook-config.nix block;
  treefmt-config = import ./treefmt-config.nix block;
in {
  conform = (mkNixago lib.cfg.conform) conform-config;
  lefthook = (mkNixago lib.cfg.lefthook) lefthook-config;
  treefmt = (mkNixago lib.cfg.treefmt) treefmt-config;
}
