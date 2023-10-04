{
  inputs,
  cell,
}: let
  inherit (inputs) nixpkgs;
  l = nixpkgs.lib // builtins;
in {
  data = {
    commit = {
      header = {length = 89;};
      conventional = {
        # Only allow these types of conventional commits (inspired by Angular)
        types = [
          "build"
          "chore"
          "ci"
          "docs"
          "feat"
          "fix"
          "perf"
          "refactor"
          "style"
          "test"
        ];
        scopes = [
          "nix"
          "code"
        ];
      };
    };
  };
}
