-- Team 1: Database & Integration
CREATE TABLE IF NOT EXISTS locations (
    locationId      TEXT PRIMARY KEY,
    name            TEXT NOT NULL,
    area            TEXT,
    type            TEXT,
    latitude        REAL,
    longitude       REAL
);

CREATE TABLE IF NOT EXISTS roads (
    roadId               TEXT PRIMARY KEY,
    fromLocationId       TEXT NOT NULL REFERENCES locations(locationId),
    toLocationId         TEXT NOT NULL REFERENCES locations(locationId),
    distance             REAL,
    travelTime           REAL,
    roadConditionWeight  REAL
);

CREATE TABLE IF NOT EXISTS service_requests (
    requestId       TEXT PRIMARY KEY,
    source          TEXT REFERENCES locations(locationId),
    destination     TEXT REFERENCES locations(locationId),
    category        TEXT,
    urgency         TEXT,
    timeSubmitted   TEXT,
    deadline        TEXT,
    status          TEXT
);

CREATE TABLE IF NOT EXISTS resources (
    resourceId          TEXT PRIMARY KEY,
    type                TEXT,
    homeLocation        TEXT REFERENCES locations(locationId),
    capacity            INTEGER,
    availabilityStatus  TEXT
);

CREATE TABLE IF NOT EXISTS algorithm_runs (
    run_id          INTEGER PRIMARY KEY,
    algorithm_name  TEXT,
    input_size      INTEGER,
    time_ns         INTEGER,
    memory_kb       INTEGER,
    date_run        TEXT
);

CREATE TABLE IF NOT EXISTS audit_events (
    event_id        INTEGER PRIMARY KEY,
    event_type      TEXT,
    entity_table    TEXT,
    entity_id       TEXT,
    performed_by    TEXT,
    timestamp       TEXT
);
