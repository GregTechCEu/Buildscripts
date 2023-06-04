## Dependency shadowing

Shadowing is a useful tool which allows you to include other projects into your builds, so that users of your mod do not need to manually install them some other way.

Some use-cases for this include:
- Using Java libraries which are not normally available in an MC environment
- Including a non-released mod, such as a library mod of yours that you do not want to separately manage releases of
- Using newer versions of certain Java libraries than MC provides

However, this feature should be used carefully. Some common issues that can occur:
- If you include a shadowed mod in your project, and a modpack contains that mod, the game will not launch as Forge will see them as duplicate mods (matching mod id)
- If you include a shadowed Java library in your project, users creating addon mods for your mod may have difficulties with their environment if the 'relocateShadowedDependencies' option is enabled, as import paths will be changed
