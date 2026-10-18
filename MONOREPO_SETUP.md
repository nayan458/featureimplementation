# Multi-Module Maven Project Setup

## Current Structure

```
featureimplementation/                  <- root
├── pom.xml                             <- "parent" pom (packaging not set, defaults to jar)
├── mvnw, mvnw.cmd, .mvn/
├── addroomshotelmanagement/
│   ├── pom.xml                         <- parent = spring-boot-starter-parent (NOT root pom)
│   ├── mvnw, mvnw.cmd, .mvn/           <- duplicated wrapper
│   └── src/
└── generateinvoices/
    ├── pom.xml                         <- parent = spring-boot-starter-parent (NOT root pom)
    ├── mvnw, mvnw.cmd, .mvn/           <- duplicated wrapper
    └── src/
```

Key observations:

1. **Root `pom.xml` already declares `<modules>`** for `addroomshotelmanagement` and `generateinvoices`, and its own parent is `spring-boot-starter-parent`. So Maven *will* build both modules when you run `mvn install` from the root — in that sense this is already a reactor/aggregator build.
2. **However, the child poms don't point back to the root pom as their parent.** Each child's `<parent>` is still `spring-boot-starter-parent` directly, with its own independent `<groupId>`/`<version>`. This means:
   - Shared config (Java version, dependency versions, plugin config) is duplicated in 3 places instead of defined once.
   - Children don't inherit the root's `groupId`/`version` — if you bump the project version, you must edit every child pom individually.
   - You can't centralize common dependencies (e.g., lombok version, a shared `dependencyManagement` BOM) in the root and have children just inherit it.
3. **Root pom has no `<packaging>pom</packaging>`.** For an aggregator/parent pom this should be explicit (Maven infers it when `<modules>` is present, but it's best practice to be explicit, and it's *required* on the parent if you also want to use it in `<dependencyManagement>`).
4. **Each module has its own copy of the Maven wrapper (`mvnw`, `.mvn/wrapper/...`) and `HELP.md`.** These are redundant once the project is truly a multi-module build — you only need one wrapper at the root.
5. Both children currently repeat the *exact same* `<dependencies>` and `<build><plugins>` blocks (spring-boot-starter, lombok, spring-boot-starter-test, maven-compiler-plugin config for lombok annotation processing). This is a textbook case for moving into the parent pom.

So: you're not starting from scratch — you need to convert this from "aggregator pom + 2 independent Spring Boot poms" into a **true parent-child multi-module Maven project**.

---

## Target Structure

```
featureimplementation/                  <- true parent pom, packaging=pom
├── pom.xml
├── mvnw, mvnw.cmd, .mvn/               <- single shared wrapper
├── addroomshotelmanagement/
│   ├── pom.xml                         <- parent = com.example:featureimplementation
│   └── src/
└── generateinvoices/
    ├── pom.xml                         <- parent = com.example:featureimplementation
    └── src/
```

---

## Changes Needed

### 1. Root `pom.xml` — make it a real parent, not just an aggregator

- Add `<packaging>pom</packaging>` (right after `<version>` or near the top metadata).
- Keep `<modules>` as is.
- Move shared dependencies (`spring-boot-starter`, `lombok`, `spring-boot-starter-test`) and the shared `maven-compiler-plugin` config here, since every module currently uses them identically.
  - Option A (simplest, matches current duplication): put them under `<dependencies>` directly in the root pom — all children inherit them automatically.
  - Option B (more correct for a growing monorepo): put them under `<dependencyManagement>` in the root, and let each child pom still declare `<dependency>` (without versions) for what it actually needs. This avoids forcing every module to pull in dependencies it doesn't use. Given both current modules use the exact same deps, Option A is fine for now; switch to Option B if modules start to diverge.
- Move the `maven-compiler-plugin` lombok-annotation-processor config into `<build><pluginManagement>` (or plain `<build><plugins>` if every child needs it) in the root pom so it's defined once.
- Fill in `<name>`, `<description>`, `<url>` etc. if you care about them (currently empty placeholders) — not required for the build to work.

### 2. Child `pom.xml` files — point them at the root pom

For both `addroomshotelmanagement/pom.xml` and `generateinvoices/pom.xml`:

- Replace:
  ```xml
  <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>4.1.1</version>
      <relativePath/>
  </parent>
  ```
  with:
  ```xml
  <parent>
      <groupId>com.example</groupId>
      <artifactId>featureimplementation</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <relativePath>../pom.xml</relativePath>
  </parent>
  ```
- Remove the `<groupId>` from the child pom (it will inherit `com.example` from the parent) — keep `<artifactId>` as-is (`addroomshotelmanagement` / `generateinvoices`), and you can drop `<version>` too since it will inherit `0.0.1-SNAPSHOT` from the parent (only override if a module needs to version independently).
- Remove the `<properties><java.version>17</java.version></properties>` block if you moved it to the root (keep it in root only).
- Remove the `<dependencies>` block entirely if you moved those to the root pom's `<dependencies>` (Option A above). If you went with Option B (`dependencyManagement`), keep the `<dependency>` entries here but drop any `<version>` tags.
- Remove the `<build><plugins>` block (spring-boot-maven-plugin + maven-compiler-plugin lombok config) if centralized in root. Note: `spring-boot-maven-plugin` (for building an executable/runnable jar) should stay declared in **each module that is actually a runnable Spring Boot app** (not the parent), since the parent itself is `packaging=pom` and isn't executable. You can still centralize its *version*/config via `<pluginManagement>` in the root and just re-declare the bare `<plugin>` tag (no version/config) in each child.

### 3. Maven wrapper cleanup

- Keep `mvnw`, `mvnw.cmd`, `.mvn/` **only at the root**.
- Delete the duplicated `addroomshotelmanagement/mvnw`, `addroomshotelmanagement/.mvn/`, `generateinvoices/mvnw`, `generateinvoices/.mvn/` (and the per-module `HELP.md` if you don't need them) — all builds should be run from the root (`./mvnw clean install` builds both modules via the reactor).

### 4. Build/verify

From the root directory:
```bash
./mvnw clean install
```
This should build the parent pom first, then `addroomshotelmanagement` and `generateinvoices` in dependency order (Maven reactor), each inheriting config from root. Run each app individually with:
```bash
./mvnw spring-boot:run -pl addroomshotelmanagement
./mvnw spring-boot:run -pl generateinvoices
```

### 5. (Optional, if modules need to depend on each other)

If, say, `generateinvoices` needs classes/DTOs from `addroomshotelmanagement` (or you extract a shared `common`/`core` module), add a plain `<dependency>` block in the consuming module's pom pointing at the other module's `groupId:artifactId:version` — Maven's reactor will resolve it locally without needing to publish to a repo first, as long as both are modules of the same multi-module build.

### 6. IDE note

After restructuring, re-import the root `pom.xml` as a Maven project in your IDE (IntelliJ: right-click root `pom.xml` → "Add as Maven Project", or just reopen the project) so it picks up the new parent-child relationship instead of treating each module as a standalone project.

---

## Summary Checklist

- [ ] Add `<packaging>pom</packaging>` to root pom
- [ ] Move shared `<dependencies>` (spring-boot-starter, lombok, spring-boot-starter-test) to root
- [ ] Move shared `<properties>` (java.version) to root only
- [ ] Move lombok annotation-processor compiler config to root (`pluginManagement` or `plugins`)
- [ ] Update both child `pom.xml` `<parent>` blocks to point to `com.example:featureimplementation`
- [ ] Remove duplicated `groupId`/`version`/`properties`/`dependencies` from children (inherit instead)
- [ ] Keep `spring-boot-maven-plugin` declared per-app module (it's the runnable one), manage its version via root `pluginManagement`
- [ ] Delete duplicated `mvnw`/`.mvn`/`HELP.md` from each submodule, keep only at root
- [ ] Run `./mvnw clean install` from root to verify the reactor build works
- [ ] Re-import project in IDE
