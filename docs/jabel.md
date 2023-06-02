## How to configure IDEA for Jabel

Jabel is a useful tool which can allow you to use many newer language features in later versions of Java not found in Java 8. This buildscript supports this feature by default, found in `gradle.properties` under the `enableModernJavaSyntax` option.

However, Jabel only supports *language features*, not APIs. This can sometimes lead to confusion when writing code if you do not know what version of Java a certain API call was added in.

### Configuring your Project SDK

In IDEA, you will navigate to `File > Project Structure > Project`. You should see a few options here.
- Set the `SDK` option to any Java 17 JDK of your choice. We recommend `zulu-17`, as this is what we use internally, but you can use whichever you'd like.
- Ensure that the `Language level` field is set to `SDK Default`

![image](https://github.com/GregTechCEu/Buildscripts/assets/10861407/2528012e-40b7-4c56-86ab-27e7d97f98dd)

### Configuring warnings for Java 9+ APIs

In IDEA, open the Settings panel (can be opened with `Ctrl + Alt + S`) and navigate to `Editor > Inspections > JVM Languages`. In the option `Usages of API which isn't available at the configured language level`:
- Set `Severety` to `Error`
- Under options, set `Forbid usages of API newer than` to `8 - Lambdas, type annotations etc.`

![image](https://github.com/GregTechCEu/Buildscripts/assets/10861407/b26650be-2987-49e7-a309-fa558b7554bb)

### Ensuring it works

If you now try to use a Java 9+ API, you should see it as an error in your IDE. For example, `List.of()`:

![image](https://github.com/GregTechCEu/Buildscripts/assets/10861407/92a384e9-cf7c-415c-b3c5-2391f0cba869)

However, using a language feature, like `enhanced instanceof`, works just fine:

![image](https://github.com/GregTechCEu/Buildscripts/assets/10861407/f232d4a2-3d5f-4899-aaa9-7ea6e2fe0764)

