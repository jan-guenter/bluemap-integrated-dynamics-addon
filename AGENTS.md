# Agent guide for BlueMap Integrated Dynamics Add-on

Read `/root/work/allthemons/AGENTS.md` and this file before changing this
project. It is a standalone public MIT BlueMap add-on, not a NeoForge mod.

## Exact accepted profile

- All the Mons `1.2.0`, Minecraft `1.21.1`, NeoForge `21.1.248`, Java `21`.
- BlueMap feature backport
  `5.22-feature.backport-5.23-stateless-java-web-server-46` at commit
  `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac`; API commit
  `285c9a60eff3ac2b0cab308ce1058d1565be0971`.
- Adapter API `0.1.0-alpha.2` at commit
  `e81f08bc4bfbf02d810ec8949a019130e2e61634`, source tree
  `2f974c9bb2ba13888d69682f86f30f58922d30eb`.
- Integrated Dynamics `1.34.0`, Tunnels `1.9.4-652`, Terminals `1.7.0-800`,
  Crafting `1.4.6-605`, and Scripting `1.0.24-424`.
- Release `0.1.0-alpha.1`: 51,482 bytes, SHA-256
  `abbc6e4e910572a88323856d153d16cc7909542c553b2be0d89b5c2aa7d77b32`.
- Aggregate candidate `0.1.0-alpha.2`: 54,979 bytes, SHA-256
  `11fdae6eb18513d7d06bbca1973e2eded36ae12f30a69bd9e09af148f8e70f18`.

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
- Preserve normal texture-atlas priority. The installed cable texture is only
  a first-wins fallback when BlueMap's atlas omitted that exact key.

## Validation and release

Initialize both support modules, then run the migration candidate gate:

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-adapter-api
gradle --no-daemon clean check build \
  generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication
```

Release tags must equal `v<addon_version>`. `check` enforces archive and shared
adapter boundaries; the accepted production identity is sealed only after
owner visual acceptance. Never claim another runtime, publication, or pack
version without observing it directly.
