{
  inputs,
  cell,
}: let
  inherit (cell.library) pkgs;
in {
  data = {
    commit-msg = {
      commands = {
        # Runs conform on commit-msg hook to ensure commit messages are
        # compliant.
        conform = {
          run = ''
            [[ "$(head -n 1 {1})" =~ ^WIP(:.*)?$|^wip(:.*)?$|fixup\!.*|squash\!.* ]] ||
            ${pkgs.conform}/bin/conform enforce --commit-msg-file {1}
          '';
          skip = ["merge" "rebase"];
        };
      };
    };
    pre-commit = {
      commands = {
        treefmt = {
          run = "${pkgs.treefmt}/bin/treefmt --fail-on-change {staged_files}";
          skip = ["merge" "rebase"];
        };
      };
    };
  };
}
