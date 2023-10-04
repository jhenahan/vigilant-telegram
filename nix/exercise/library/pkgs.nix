{
  inputs,
  cell,
}:
# When necessary, package overlays can be imported here.
let
  pkgs = import inputs.nixpkgs.path {
    system = inputs.nixpkgs.system;
    overlays = [];
  };
in
  pkgs
