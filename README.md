# BlueMap Integrated Dynamics Add-on

[![CI](https://github.com/jan-guenter/bluemap-integrated-dynamics-addon/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/jan-guenter/bluemap-integrated-dynamics-addon/actions/workflows/ci.yml)

An exact-profile BlueMap 5.23 feature-backport add-on for the stable world
appearance of the Integrated Dynamics family.

## Status and compatibility

Version `0.1.0-alpha.4` is the render-core source migration candidate for this
environment:

- All the Mons `1.2.0`, Minecraft `1.21.1`, NeoForge `21.1.248`, Java `21`;
- BlueMap feature backport
  `5.22-feature.backport-5.23-stateless-java-web-server-46`, commit
  `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac`;
- Integrated Dynamics `1.34.0`, Tunnels `1.9.4-652`, Terminals `1.7.0-800`,
  Crafting `1.4.6-605`, and Scripting `1.0.24-424`.

It preserves the released alpha.3 renderer and the alpha.2 first-wins fallback
for the installed cable texture when malformed animation metadata elsewhere in
the same JAR aborts BlueMap's texture pass before that key. The exact
`FaceLighting` implementation now compiles from
`bluemap-addon-render-core` `0.1.0-alpha.2`, commit
`24b84efdc8235f3f1323e1a8e9fd033080e3a79e`, source tree
`424040931680fb82d37693f893ca887c0ed48eae`. Emitters, model selection,
fallbacks, routes, and gallery bytes are unchanged. Compatibility outside
these exact inputs is not asserted. Alpha.3 remains the latest published
release until this maintenance candidate passes review.

The candidate production JAR is exactly 58,352 bytes with SHA-256
`d6dcf7682244189d653708310b0a86d89d236f5be9f38d6f99f06371aa6e88f0`.

## Visual scope

The add-on renders stable structure that BlueMap cannot derive from the normal
blockstate alone:

- cable cores and persisted six-direction neighbor connections;
- 48 mounted part types from Integrated Dynamics, Tunnels, Terminals,
  Crafting, and Scripting, rotated onto their saved face;
- persisted facades selected from their saved block state and clipped around
  cable and part apertures.

Changing activity lights, transfer state, screen contents, displayed values,
and item or fluid contents are intentionally normalized to neutral/inactive
models or omitted. Missing resources and malformed saved state fail closed.

The add-on contains no third-party models or textures. It first respects the
normal BlueMap texture-atlas result; only a missing cable key is filled from
the first matching 16-by-16 texture in the operator-installed resource roots.
BlueMap must be able to read the installed family resource packs, either
through normal mod resource scanning or by making those exact JARs available
in `config/bluemap/packs`.

## Build and verification

Clone with submodules so the exact reviewed build convention is available:

```bash
git clone --recurse-submodules \
  https://github.com/jan-guenter/bluemap-integrated-dynamics-addon.git
```

For an existing checkout, initialize all exact support modules:

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-render-core \
  modules/bluemap-addon-adapter-api
```

The build rejects an uninitialized, dirty, incorrectly pinned, or
source-tree-mismatched support module. Then use Java 21, Gradle 9.6.1, and the
exact sibling BlueMap checkout:

```bash
gradle --no-daemon clean check build \
  generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication
```

`check` enforces the production and sources archive boundaries, including one
exact shared face-light source and no legacy local copy. The release process
seals the exact bytes before tagging. Tagged releases publish the
production/source JARs, POM, Gradle module
metadata, and checksums on GitHub Releases and Maven coordinates
`io.github.jan-guenter:bluemap-integrated-dynamics-addon:<version>` on GitHub
Packages. The tag must equal `v<addon_version>`.

## Installation

Place the reviewed add-on JAR in `config/bluemap/packs` and restart the JVM. It
is not a NeoForge mod and does not belong in the server's `mods` directory. It
writes no world or player data.

## License and provenance

The add-on is released under the [MIT License](LICENSE). Third-party software
and resources are not bundled; see [NOTICE.md](NOTICE.md),
[THIRD_PARTY.md](THIRD_PARTY.md), and
[provenance/upstreams.json](provenance/upstreams.json).
