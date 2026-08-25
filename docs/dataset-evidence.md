# Dataset evidence note

**Organisation:** Korle-Bu Teaching Hospital, Accra, plus the Greater Accra referral ring (Ridge, La General, 37 Military, Achimota, Tema General, Lister, Trust).

**How the data was constructed.** No live patient, staff, or NHIS records were copied. Locations are publicly known Korle-Bu departments/wards and named referral hospitals. Corridor distances inside campus are synthetic but order-of-magnitude realistic (tens to hundreds of metres). Referral roads use publicly known Accra corridors (Guggisberg Ave, Liberation Road, N1, Spintex Road) with approximate kilometres. Service requests and resources are fabricated operational events (triage, admission, transfer, pharmacy, referral) with invented IDs (`SR001`…, `RES001`…). Urgency labels (CRITICAL / HIGH / MEDIUM / LOW) are operational categories, not diagnoses.

**Personal data.** There are no real names, phone numbers, NHIS numbers, or dates of birth. Names that appear in traces (Ama, Kofi, Esi) are illustrative Ghanaian given names used only in unit-test fixtures.

**Sizes (seed CSVs).** 55 locations, 105 roads, 310 service requests, 40 resources — all above the brief minima. Coordinates use WGS84 around 5.53°N, 0.23°W.

**Loading.** `HospitalBootstrap` applies `schema.sql` and imports the CSVs from the classpath into SQLite `hospital.db` on every examiner run. Import skips orphan foreign keys.
