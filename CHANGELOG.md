# Changelog

## 0.1.0-alpha.4 - 2026-09-02

- Pin `bluemap-addon-render-core` `0.1.0-alpha.2` and compile its BlueMap 5.23
  `FaceLighting` source instead of the local duplicate.
- Preserve the released cable, mounted-part, facade, texture-fallback, route,
  and gallery behavior.
- Reject a missing, changed, dirty, incorrectly pinned, or source-tree-mismatched
  render-core checkout and reject legacy or unexpected shared classes in both
  publication JARs.

## 0.1.0-alpha.3 - 2026-08-31

- Target only BlueMap feature-backport commit
  `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac` and API commit
  `285c9a60eff3ac2b0cab308ce1058d1565be0971`.
- Move the local adapter boundary from `bluemap522` to `bluemap523`.
- Compile the four shared Adapter API sources and replace the local runtime,
  registry, and resource-extension helpers.
- Preserve the accepted cable, part, facade, and texture-fallback behavior.

## 0.1.0-alpha.2 - 2026-08-28

- Retain the installed `integrateddynamics:block/cable` texture when unrelated
  malformed animation metadata aborts BlueMap's JAR texture pass before the
  otherwise valid 16-by-16 resource.
- Preserve the normal atlas winner and use the installed texture only as a
  first-wins fallback.
- Add directory, JAR-root, pack-priority, and non-overwrite regression tests.

## 0.1.0-alpha.1 - 2026-08-17

- Render stable Integrated Dynamics family cable topology and all 48 mounted
  part types from persisted multipart data.
- Render saved facades with state-selected textures, layers, tint, rotation,
  and cable or part apertures.
- Normalize changing screens, values, lights, contents, and activity.
- Correct top and bottom primitive winding after Minecraft/BlueMap comparison.
- Pass the six-check disposable gallery and owner visual acceptance.
