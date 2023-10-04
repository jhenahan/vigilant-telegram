{
  inputs,
  cell,
} @ block: let
  inherit (inputs) nixpkgs;
  inherit (inputs.std) lib;
  l = nixpkgs.lib // builtins;
in
  l.mapAttrs (_: lib.dev.mkShell) {
    # `default` is a special target in newer nix versions
    default = import ./std-shell.nix block;
  }
