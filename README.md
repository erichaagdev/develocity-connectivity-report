# Develocity Connectivity Report

This project generates a report of Gradle and Maven projects configured with Develocity.

An example report can be seen [here](https://scans.gradle.com/s/2iznweiyla3zq/console-log?page=1).

## Requirements

- Java 8 or later
- A [GitHub personal access token](https://github.com/settings/tokens)
  - The token does not require any additional scopes

## Usage

Set the environment variable `GITHUB_API_KEY` to the value of your access token.

```shell
export GITHUB_API_KEY="your_access_token"
```

Run the report by invoking the Gradle `run` task. 

```shell
./gradlew run
```

## Configuration

The report configuration can be found [here](src/main/kotlin/dev/erichaag/Main.kt).

## GitHub Actions

This report is run on a weekly basis using GitHub Actions.
View the latest report [here](https://github.com/erichaagdev/develocity-connectivity-report/actions).
