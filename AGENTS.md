# Agent guide for BlueMap Integrated Dynamics Add-on

Read `/root/work/allthemons/AGENTS.md` and this file before changing this
project. It is a standalone public MIT BlueMap add-on, not a NeoForge mod.

## Exact accepted profile

- All the Mons `1.2.0`, Minecraft `1.21.1`, NeoForge `21.1.248`, Java `21`.
- BlueMap backport `5.22-agent.backport-5.22-mc1.21.1-2` at commit
  `9be321df995a1103808621d529eb72773e719d4d`.
- Integrated Dynamics `1.34.0`, Tunnels `1.9.4-652`, Terminals `1.7.0-800`,
  Crafting `1.4.6-605`, and Scripting `1.0.24-424`.
- Release `0.1.0-alpha.1`: 51,482 bytes, SHA-256
  `abbc6e4e910572a88323856d153d16cc7909542c553b2be0d89b5c2aa7d77b32`.

Do not treat a matching version string as proof for another artifact or pack.

## Boundaries

- Preserve stable cable topology, mounted parts, and persisted facades.
- Keep changing activity, lights, values, screens, transfer state, and displayed
  contents neutral or absent.
- Do not bundle third-party classes, models, textures, or JARs. Consume
  operator-installed resource packs.
- Keep the production artifact a plain BlueMap add-on with no NeoForge
  metadata, Mixins, nested JARs, client bootstrap, or world writes.
- Unknown or malformed data must fail closed without corrupting the tile mesh.

## Validation and release

The accepted production JAR identity is enforced by `check`:

```bash
gradle --no-daemon clean check build \
  generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication
```

Release tags must equal `v<addon_version>`. CI and release use the exact
BlueMap backport commit. Never claim another runtime, publication, or pack
version without observing it directly.
