# Third-party components

| Component | Exact accepted identity | Declared license | Bundled |
| --- | --- | --- | --- |
| BlueMap | Feature backport `5.22-feature.backport-5.23-stateless-java-web-server-46`, commit `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac`; API `285c9a60eff3ac2b0cab308ce1058d1565be0971` | MIT | No |
| BlueMap Add-on Adapter API | `0.1.0-alpha.2`, commit `e81f08bc4bfbf02d810ec8949a019130e2e61634` | MIT | Four source files |
| CyclopsCore | `1.21.1-neoforge-1.29.2`, SHA-256 `1d36aaa3d9abb71d0151bb5edd02f5580dfb020a229c63f6534a87a04d1cfab6` | MIT | No |
| Integrated Dynamics | `1.21.1-neoforge-1.34.0`, SHA-256 `2e6afc62a572cf3e1bd6b91321946790103c72793bc5fe1d0295c1138c072e87` | MIT | No |
| Integrated Tunnels | `1.21.1-neoforge-1.9.4-652`, SHA-256 `90dfa97a5666e377197e83fb3b90bdc4bb4a337eac61c7baa2336d8ce0573ca0` | MIT | No |
| Integrated Terminals | `1.21.1-neoforge-1.7.0-800`, SHA-256 `add10be15370234fd1491c318f1fadc2d7b621aa31ecf674a9c38f8b5befec92` | MIT | No |
| Integrated Crafting | `1.21.1-neoforge-1.4.6-605`, SHA-256 `25651914d0e59120129829687ad8f9a8ab44e6fd2c3176c53bb219b3764d58a4` | MIT | No |
| Integrated Scripting | `1.21.1-neoforge-1.0.24-424`, SHA-256 `10cafdeeece71175741f8b6c405c1bbfca09c7dfff416b565b87720af4f40545` | MIT | No |
| JetBrains annotations | `23.0.0`, compile only | Apache-2.0 | No |
| Checkstyle | `10.18.2`, build only | LGPL-2.1-or-later | No |
| Gradle | `9.6.1`, build only | Apache-2.0 | No |

The production JAR contains first-party classes, four MIT Adapter API source
classes, one first-party blockstate route, add-on metadata, and the project
MIT license.

## First-party reuse

MIT infrastructure was reused from the author's BlueMap Pipez Add-on at
commit `fa3e773a7d1b7e9af52277bf104e70f704b0bb2a`; the primitive emitter follows
the author's BlueMap Sophisticated Add-on pattern at commit
`a75b1d82c3987fa9360a1e8a5910eedf90aca7cb`. No profile-specific resources or
observations from either add-on are bundled.
