{
  inputs,
  cell,
}: {
  config,
  lib,
  ...
}: let
  cfg = config.languages.java;
  inherit (cell.library) pkgs;
  inherit (lib) types mkEnableOption mkOption mkDefault mkIf optional literalExpression;
in {
  options.languages.java = {
    enable = mkEnableOption "tools for Java development";
    jdk.package = mkOption {
      type = types.package;
      example = literalExpression "pkgs.jdk8";
      default = pkgs.jdk17;
      defaultText = literalExpression "pkgs.jdk17";
      description = ''
        The JDK package to use.
        This will also become available as `JAVA_HOME`.
      '';
    };
    maven = {
      enable = mkEnableOption "maven";
      package = mkOption {
        type = types.package;
        defaultText = "pkgs.maven.override { jdk = cfg.jdk.package; }";
        description = ''
          The Maven package to use.
          The Maven package by default inherits the JDK from `languages.java.jdk.package`.
        '';
      };
    };
    gradle = {
      enable = mkEnableOption "gradle";
      package = mkOption {
        type = types.package;
        defaultText = literalExpression "pkgs.gradle.override { jdk = cfg.jdk.package; }";
        description = ''
          The Gradle package to use.
          The Gradle package by default inherits the JDK from `languages.java.jdk.package`.
        '';
      };
    };
  };

  config = mkIf cfg.enable {
    languages.java.maven.package = mkDefault (pkgs.maven.override {jdk = cfg.jdk.package;});
    languages.java.gradle.package = mkDefault (pkgs.gradle.override {java = cfg.jdk.package;});
    packages =
      (optional cfg.enable cfg.jdk.package)
      ++ (optional cfg.maven.enable cfg.maven.package)
      ++ (optional cfg.gradle.enable cfg.gradle.package);

    env = [
      {
        name = "JAVA_HOME";
        value = cfg.jdk.package.home;
      }
    ];
  };
}
