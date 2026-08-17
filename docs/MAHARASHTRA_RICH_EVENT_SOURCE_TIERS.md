# Maharashtra Rich Event and Panchang Calendar — Source Tiers

**Author:** Manus AI  
**Scope:** Original offline-first event schema for Spiritual Companion’s Maharashtra calendar.  
**Updated:** 17 August 2026

## Purpose and boundaries

The calendar is designed to provide a rich monthly experience without representing a proprietary almanac or religious authority. Each event must carry a visible provenance tier. Civil observances are bundled only where their published source supplies an exact date. Panchang cues are generated locally from the app’s existing calculator and remain explicitly labelled as personal estimates that depend on the selected place.

## Verified civic-event source tier

The Maharashtra Metropolitan Region Development Authority’s **Public Holidays — 2026** page publishes 24 exact state public-holiday markers, including Mahashivratri (15 February), Gudhi Padwa (19 March), Ganesh Chaturthi (14 September), Dasara (20 October), and the Diwali observances (8 and 10 November). These facts are appropriate for a bundled `GOVERNMENT_PUBLISHED` event tier, provided the source name and URL remain attached to every record. [1]

The National Portal of India separately identifies its Maharashtra holiday calendar and distinguishes Gazetted (`G`) from Restricted (`R`) holiday classifications. Its visible monthly data are controlled dynamically, so it is retained as a verification reference rather than copied into the app as a duplicate static dataset. [2]

| Tier | Data held in the app | Presentation requirement | Update rule |
|---|---|---|---|
| `GOVERNMENT_PUBLISHED` | Exact dated civic or state public-holiday record | Show source and year | Replace only from a new official published list |
| `CURATED_DEVOTIONAL_GUIDE` | Non-date-specific festival context from the app’s existing guide catalogue | Label as devotional guide, not official calendar fact | Editorial review with source note |
| `LOCAL_PANCHANG_ESTIMATE` | Tithi, Paksha, Nakshatra, lunar-month and related practice cues calculated for selected day and place | State that it is an offline personal estimate, not ritual authority | Recalculate locally; do not persist a date as a universal fact |
| `USER_PERSONAL` | Future optional personal reminder or note | Clearly user-created and local-only | Never publish or include in a bundled dataset |

## Explicit exclusions

The app must not copy Kalnirnay tables, visual layout, descriptions, wording, paid data, images, scans, or any source that does not grant reuse rights. It must not turn a calculated Panchang cue into a fixed statewide festival date. It must not imply official, legal, astronomical, or ritual authority where the source does not support that claim.

## References

[1]: https://mmrda.maharashtra.gov.in/en/public-holidays "MMRDA — Public Holidays 2026"

[2]: https://www.india.gov.in/calendar/maharashtra "National Portal of India — Maharashtra Holiday Calendar"
