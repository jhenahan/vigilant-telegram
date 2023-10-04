{
  description = "Exercise Shell";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
    std = {
      url = "github:divnix/std";
      inputs.nixpkgs.follows = "nixpkgs";
      inputs = {
        devshell.url = "github:numtide/devshell";
        nixago.url = "github:nix-community/nixago";
      };
    };
    gitignore = {
      url = "github:hercules-ci/gitignore.nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = {
    std,
    gitignore,
    self,
    ...
  } @ inputs:
    std.growOn {
      inherit inputs;
      cellsFrom = ./nix;
      cellBlocks = with std.blockTypes; [
        (installables "packages" {ci.build = true;})
        (runnables "tools" {ci.build = true;})
        (devshells "devshells" {ci.build = true;})
        (functions "library")
        (functions "profiles")
        (nixago "configs")
      ];
    }
    {
      devShells = std.harvest self ["exercise" "devshells"];
      packages = std.harvest self ["exercise" "packages"];
    };
}
