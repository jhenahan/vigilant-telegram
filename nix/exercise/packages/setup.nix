{
  inputs,
  cell,
}: let
  inherit (cell.library) pkgs;
  millw = pkgs.fetchurl {
    url = "https://raw.githubusercontent.com/lefou/millw/0.4.10/millw";
    sha256 = "sha256-2klNpzce0n+lcD13Cf40st2MP2FLYWI7ByAMKLUFQH0=";
  };
  jabbaw = pkgs.fetchurl {
    url = "https://raw.githubusercontent.com/nicerloop/jabba-wrapper/main/jabbaw";
    sha256 = "sha256-/5uLQMiiwYwSvdwudFUo6Kg1FZmcJCuKSpRCDLO+fC0=";
  };
in
  pkgs.writeShellScriptBin "setup-exercise" ''
    cat ${millw} > ./mill
    cat ${jabbaw} > ./jabba

    echo "${pkgs.mill.version}" > .mill-version
    INSTALLED_JDK="zulu@${pkgs.jdk17.version}"
    echo "''${INSTALLED_JDK%.*}-''${INSTALLED_JDK##*.}" > .jabbarc

    chmod +x ./mill
    chmod +x ./jabba
  ''
