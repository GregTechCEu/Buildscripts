# Relations
You can declare Modrinth and Curse relations via the properties in `gradle.properties`.
However, with more than 3 dependencies that gets really messy.

## Json File
To solve that problem you can declare your dependencies in a relations json file. With that you can parse Forge, Curse
and Modrinth dependencies from one file.
By default, the path is `relations.json`, but it can be configured to anything with the `forgeRelations`, 
`modrinthRelarions` and `curseForgeRelations` properties.
That file must be placed in the root folder.

Example:
````json5
{
  // the dependency type
  // valued values are [required, optional, embedded, incompatible, tool] (tool is Curse only)
  // the value must be a list with all dependencies of that type
  "required": [
    // the base format of a dependency
    "order;modid:curse-id:modrinth-id/modrinth-version@version"
  ],
  "optional": [
    // the syntax is the same in all types
    // if other types are empty they can be removed
  ],
  "embedded": [
  ],
  "incompatible": [
  ],
  "tool": [ // Curse only
  ]
}
````
This explains the structure of the file.

Now let's look at the dependency itself:`"order;modid:curse-id:modrinth-id/modrinth-version@version"`

- `order` is the prefix for forge: `[required]-[after, before]-[client, server]`.
  The values must be seperated with `-`. `required` is automatically added if the type is `required`.
  If the string is empty (just a `;`) then `after` is automatically added. If you don't want the dep to be added to
  you can remove the `order;`.
- `modid` is the modid for forge. By default, this is also the Curse and Modrinth id.
- `curse-id` is the Curse slug if the project. Leaving it empty will not add the dep to Curse.
- `modrinth-id` is the Modrinth slug if the project. Leaving it empty will not add the dep to Modrinth.
- `modrinth-version` is the optional Modrinth version slug.
- `@version` is the version range for the forge dependency.

Examples:
- ````json
  ";modularui@[2.4.0,)"
  ````
  Adds ModularUI dep to Forge, Curse and Modrinth with a Forge version range of 2.4.0 to any

- ````json
  "modularui"
  ````
  Adds ModularUI dep to Curse and Modrinth with any version.

- ````json
  ";mixinbooter:mixin-booter:mixinbooter@[8.0,)"
  ````
  Adds Mixinbooter dep to Forge (with `modrinth`), Curse (`mixin-booter`) and Modrinth (`mixinbooter`).

- ````json
  ";modularui::@[2.4.0,)"
  ````
  Adds ModularUI dep only to forge (since curse and modrinth id are empty).

- ````json
  ";modularui:modularui-cf-mr@[2.4.0,)"
  ````
  Adds ModularUI dep to Forge (`modularui`), Curse (`modularui-cf-mr`) and Modrinth (`modularui-cf-mr`).

- ````json
  ";mixinbooter::mixinbooter/8.9@[8.0,)"
  ````
  Adds Mixinbooter dep to Forge (with `modrinth`) and Modrinth (`mixinbooter`) with a specific dep on version 8.9.

- ````json
  ";modularui:modularui:/Q8TApfEv@2.4.1"
  ````
  Adds ModularUI dep to forge, Curse (`modularui`) and Modrinth (with the version id of the 2.4.1 file) for only version 2.4.1.
- 