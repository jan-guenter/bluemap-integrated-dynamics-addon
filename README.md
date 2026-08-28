# BlueMap Integrated Dynamics Add-on

[![CI](https://github.com/jan-guenter/bluemap-integrated-dynamics-addon/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/jan-guenter/bluemap-integrated-dynamics-addon/actions/workflows/ci.yml)

An exact-profile BlueMap 5.22 add-on for the stable world appearance of the
Integrated Dynamics family.

## Status and compatibility

Version `0.1.0-alpha.2` is the aggregate-test release candidate for this
environment:

- All the Mons `1.2.0`, Minecraft `1.21.1`, NeoForge `21.1.248`, Java `21`;
- BlueMap backport `5.22-agent.backport-5.22-mc1.21.1-2`, commit
  `9be321df995a1103808621d529eb72773e719d4d`;
- Integrated Dynamics `1.34.0`, Tunnels `1.9.4-652`, Terminals `1.7.0-800`,
  Crafting `1.4.6-605`, and Scripting `1.0.24-424`.

The candidate is exactly 54,979 bytes with SHA-256
`11fdae6eb18513d7d06bbca1973e2eded36ae12f30a69bd9e09af148f8e70f18`.
It preserves the accepted alpha.1 rendering and adds a first-wins fallback for
the installed cable texture when malformed animation metadata elsewhere in the
same JAR aborts BlueMap's texture pass before that key.
Compatibility outside these exact inputs is not asserted. Alpha.1 remains the
latest owner-accepted release until the aggregate gate finishes.

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

Use Java 21, Gradle 9.6.1, and the exact sibling BlueMap checkout:

```bash
gradle --no-daemon clean check build \
  generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication
```

`check` rejects any production JAR that differs from the recorded size or
SHA-256. Tagged releases publish the production/source JARs, POM, Gradle module
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
