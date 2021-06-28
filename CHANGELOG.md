# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

## [3.0.1] - 2021-06-28
### Changed
- Remove minimal and current version Firebase remote config defaults. This is a behaviour change.
If you do not specify these properties explicitly in remote config, 0 will be returned instead of -1
by default.

## [3.0.0] - 2021-06-28
### Changed
- Migration from jCenter to Maven Central 🎉
- ‼️ Important ‼️ groupId has changed. New groupId is `io.github.ackeecz`.
- Package name changed as well from `com.ackee.versionupdatehandler` to `io.github.ackeecz.versionupdatehandler`
