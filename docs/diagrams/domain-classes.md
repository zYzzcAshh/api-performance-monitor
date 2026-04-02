# Domain Class Diagram

This document describes the domain model and points to the PlantUML source for the class diagram.

PlantUML: `docs/diagrams/domain-classes.puml`

## Classes

- MonitoredEndpoint (data)
  - id: UInt
  - userId: UInt (references User.id)
  - url: EndpointUrl
  - name: String
  - interval: IntervalSeconds
  - createdAt: Instant

- User (data)
  - id: UInt
  - username: Username
  - passwordHash: PasswordHash
  - createdAt: Instant

- EndpointUrl (value)
  - value: String
  - invariants: starts with http(s)://, length > 10, no spaces
  - normalized(): String removes trailing '/'

- IntervalSeconds (value)
  - value: Long
  - allowed values: 60, 120, 180, 300, 600, 900, 1200, 1800

- Username (value)
  - value: String
  - invariant: length >= 3

- PasswordHash (value)
  - value: String

- Password (class)
  - value: String
  - invariants: length >= 6, contains uppercase, contains digit

## Notes
- Associations are 1..1 unless otherwise specified.
- `userId` in `MonitoredEndpoint` is a foreign key to `User.id` at the persistence level.

## How to render the diagram
Install PlantUML and Graphviz, then run:

```bash
plantuml docs/diagrams/domain-classes.puml
```

This will produce `domain-classes.png` alongside the `.puml` file.
