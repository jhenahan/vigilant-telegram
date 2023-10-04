{
  inputs,
  cell,
}: let
  inherit (cell.library) pkgs;
in {
  data = {
    formatter = {
      nix = {
        command = "alejandra";
        includes = ["*.nix"];
      };
      scala = {
        command = "scalafmt";
        includes = ["*.scala" "*.sbt" "*.sc"];
      };
    };
  };
  packages = [
    pkgs.alejandra
    (pkgs.lib.lowPrio pkgs.scalafmt)
  ];
}
