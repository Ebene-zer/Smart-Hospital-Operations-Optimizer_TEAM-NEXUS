-- Team 1: Database & Integration
CREATE TABLE IF NOT EXISTS locations (
    location_id     INTEGER PRIMARY KEY,
    name            TEXT NOT NULL,
    area            TEXT,
    type            TEXT,
    latitude        REAL,
    longitude       REAL
);

CREATE TABLE IF NOT EXISTS roads (
    road_id             INTEGER PRIMARY KEY,
    from_location_id    INTEGER NOT NULL REFERENCES locations(location_id),
    to_location_id      INTEGER NOT NULL REFERENCES locations(location_id),
    distance_km         REAL,
    travel_time_min     REAL,
    road_condition_weight REAL
);

CREATE TABLE IF NOT EXISTS service_requests (
    request_id      INTEGER PRIMARY KEY,
    source_id       INTEGER REFERENCES locations(location_id),
    destination_id  INTEGER REFERENCES locations(location_id),
    category        TEXT,
    urgency         INTEGER,
    time_submitted  TEXT,
    deadline        TEXT,
    status          TEXT
);

CREATE TABLE IF NOT EXISTS resources (
    resource_id         INTEGER PRIMARY KEY,
    type                TEXT,
    home_location_id    INTEGER REFERENCES locations(location_id),
    capacity            INTEGER,
    availability_status TEXT
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
    entity_id       INTEGER,
    performed_by    TEXT,
    timestamp       TEXT
);
