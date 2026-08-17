# Original Maharashtra Devotional Calendar Design

**Author:** Manus AI  
**Status:** Approved implementation design for the first enriched native release  
**Scope:** Offline-first, Maharashtra-focused, original calendar experience

## Product Intent

The calendar should give a devotee the useful breadth people expect from a Maharashtra almanac: a month at a glance, selected-day Panchang context, public observances, and devotional prompts. It must not reproduce any Kalnirnay page, wording, artwork, commercial data set, or brand treatment. The design therefore adopts only the general product problem—finding relevant day information quickly—and solves it with the application’s own data, structure, visual system, and transparent provenance.

## Information Tiers

| Tier | What appears | Date certainty | Offline source treatment |
|---|---|---:|---|
| **Daily Panchang estimate** | Sunrise, sunset, moonrise, moonset, Brahma Muhurta, Tithi, Paksha, Nakshatra, lunar month, and Saka date | Calculated for the selected date and stored place | Existing bundled calculator; clearly labelled as an estimate rather than ritual authority |
| **Maharashtra public holidays** | 24 official 2026 public-holiday markers, including Gudhi Padwa, Maharashtra Din, Ganesh Chaturthi, Dasara, and Diwali | Exact for the published 2026 list | Bundled with a source label and original explanatory wording |
| **Devotional guides** | The existing 35 festival guides, grouped by lunar month | Lunar-month or regional guidance only where no authoritative fixed date is bundled | Reused local first-party catalogue with its existing guide source labels |
| **Personal practice** | Brahma Muhurta and Evening Prarthana hand-offs | User-controlled | Existing exact-alarm flow; no automatic alarm creation |

## Core User Flow

The user opens **Festivals** and lands on an original **Maharashtra calendar** surface. They move through months with labelled previous and next controls, then tap any day. The selected-day card provides the full local Panchang estimate and shows any official 2026 holiday or recurring lunar marker. From there, users can browse the full local festival-guide catalogue and temple directory without GPS, tracking, automatic network activity, or external media.

> The app should always state that personal observance is optional and that a locally published Panchang should be checked for temple-specific or ritual-critical timing.

## Interaction and Accessibility Requirements

| Element | Requirement |
|---|---|
| Month navigation | Previous and next buttons have explicit spoken labels and announce the displayed month in the heading. |
| Day cell | At least 48 dp visual/touch height, contains a spoken full date plus marker count, and visually differentiates today and the selected day. |
| Holiday marker | Uses text in selected-day detail, not colour alone; the grid dot is only a compact secondary cue. |
| Panchang detail | Each value has a stable label. Unknown calculations display “Unavailable” rather than invented times. |
| Source trail | Every government date shown in detail identifies the official 2026 MMRDA public-holiday list. |
| Performance | Month grid has a fixed maximum of 42 cells and only calculates the selected day plus visible month days, avoiding scrolling or network work on the main thread. |

## Source and Intellectual-Property Boundary

The public Kalnirnay website was used only to understand that users value daily Panchang information and month navigation. The app does not ingest, scrape, copy, or reproduce Kalnirnay material. The exact official 2026 public-holiday markers are independently derived from the MMRDA public-holiday list. The calendar screen uses the app’s own Material 3 composition, wording, and local data model.

## References

[1]: https://www.kalnirnay.com/ "Kalnirnay public website"
[2]: https://mmrda.maharashtra.gov.in/en/public-holidays "MMRDA: Public Holidays — 2026"
