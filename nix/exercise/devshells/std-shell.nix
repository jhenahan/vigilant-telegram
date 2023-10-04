{
  inputs,
  cell,
} @ block: {config, ...}: let
  inherit (inputs.std) std lib;
  inherit (cell.library) pkgs;
  inherit (cell.configs) conform lefthook treefmt;
  inherit (cell.packages) setup;
  inherit (config.languages) java scala;
in {
  name = "Exercise Devshell";
  imports = [
    std.devshellProfiles.default
    cell.profiles.scala
    cell.profiles.java
  ];
  nixago = [
    conform
    lefthook
    treefmt
  ];
  packages = [
    setup
  ];
  motd = pkgs.lib.mkForce ''
    Java Version is ${java.jdk.package.version}
    Scala Version is ${scala.package.version}
    Mill Version is ${pkgs.mill.version}

    $(type -p menu &>/dev/null && menu)
  '';
  languages.scala.enable = true;
}
