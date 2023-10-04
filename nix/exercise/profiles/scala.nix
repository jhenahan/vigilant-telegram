{
  inputs,
  cell,
}: {
  config,
  lib,
  ...
}: let
  cfg = config.languages.scala;
  java = config.languages.java;
  inherit (cell.library) pkgs;
  inherit (lib) types mkEnableOption mkOption mkDefault mkIf optional literalExpression;
in {
  options.languages.scala = {
    enable = mkEnableOption "tools for Scala development";

    package = mkOption {
      type = types.package;
      default = pkgs.scala_2_13;
      defaultText = "pkgs.scala_2_13";
      description = ''
        The Scala package to use.
      '';
    };
  };

  config = mkIf cfg.enable {
    packages = with pkgs;
      [
        (cfg.package.override
          {jre = java.jdk.package;})
        (sbt.override
          {jre = java.jdk.package;})
        (mill.override
          {jre = java.jdk.package;})
        (metals.override
          {jre = java.jdk.package;})
        (coursier.override
          {jre = java.jdk.package;})
        (scalafmt.override
          {jre = java.jdk.package;})
        (ammonite.override
          {jre = java.jdk.package;})
      ]
      ++ lib.optionals (lib.versionAtLeast java.jdk.package.version "17") [
        (scala-cli.override
          {jre = java.jdk.package;})
      ];

    languages.java.enable = true;
  };
}
