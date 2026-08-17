# BlueMap Integrated Dynamics Add-on

[![CI](https://github.com/jan-guenter/bluemap-integrated-dynamics-addon/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/jan-guenter/bluemap-integrated-dynamics-addon/actions/workflows/ci.yml)

An exact-profile BlueMap 5.22 add-on for the stable world appearance of the
Integrated Dynamics family.

## Status and compatibility

Version `0.1.0-alpha.1` is the owner-accepted prerelease for this environment:

- All the Mons `1.2.0`, Minecraft `1.21.1`, NeoForge `21.1.248`, Java `21`;
- BlueMap backport `5.22-agent.backport-5.22-mc1.21.1-2`, commit
  `9be321df995a1103808621d529eb72773e719d4d`;
- Integrated Dynamics `1.34.0`, Tunnels `1.9.4-652`, Terminals `1.7.0-800`,
  Crafting `1.4.6-605`, and Scripting `1.0.24-424`.

The corrected production JAR was accepted on 2026-08-17. It is exactly 51,482
bytes with SHA-256
`abbc6e4e910572a88323856d153d16cc7909542c553b2be0d89b5c2aa7d77b32`.
Compatibility outside these exact inputs is not asserted.

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

The add-on contains no third-party models or textures. BlueMap must be able to
read the operator-installed family resource packs, either through normal mod
resource scanning or by making those exact JARs available in
`config/bluemap/packs`.

## Build and verification

Use Java 21, Gradle 9.6.1, and the exact sibling BlueMap checkout:

```bash
gradle --no-daemon clean check build \
  generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication
```

`check` rejects any production JAR that differs from the accepted size or
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
