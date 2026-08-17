# Online Astronomical Timing Source Research

## Purpose

This note records the externally verified provider options considered after users reported a difference between the app's offline timing estimates and a Maharashtra reference calendar. It is a design record, not a claim that any source establishes ritual authority.

## Findings

| Source | Verified capability | Design relevance |
| --- | --- | --- |
| U.S. Naval Observatory Astronomical Applications API | The official API accepts a requested date, decimal latitude/longitude, and time-zone offset. Its complete one-day Sun and Moon service returns rise, set, and transit data for the Sun and Moon; the service documentation also identifies a one-year rise/set table for a fixed location. | Strong no-key authoritative astronomical reference for a **user-tapped** refresh. The app should request only the already selected coordinates, requested date range, and IST offset; it must not send a city name, device identifier, or automatically refresh in the background. |
| India Meteorological Department, Indian Astronomical Ephemeris | IMD publishes the annual Indian Astronomical Ephemeris for astronomical users and Panchang makers. It includes tables of sunrise, sunset, moonrise, and moonset. | An authoritative Indian reference for transparency and manual comparison. The public page currently exposes a publication download rather than a documented, location-query API appropriate for in-app automatic refresh. |
| IPGeolocation Astronomy API | The documented API returns sunrise, sunset, moonrise, and moonset for a supplied latitude/longitude and date. It requires an API key. | Technically complete but not suitable as the default because it would require a third-party API key and add a commercial dependency. It may be considered only as an opt-in provider if the user later supplies and chooses a key. |

## Current Recommendation

The first implementation should use the U.S. Naval Observatory endpoint for **user-initiated** refreshes of the saved place and a bounded date range. A successful response is cached locally with source URL, refreshed-at time, date coverage, and a clear “online astronomical reference” label. The existing offline calculation remains available when there is no connection, the source is unavailable, or the cache does not cover the requested date.

The screen must clearly state that astronomical rise/set times can differ from a Panchang publisher due to the publisher's selected coordinates, elevation, horizon/refraction convention, rounding policy, and calendar-specific convention. It must not present the online figures as ritual authority or silently replace an existing user-configured alarm.

## Sources

1. [U.S. Naval Observatory — Astronomical Applications API](https://aa.usno.navy.mil/data/api)
2. [U.S. Naval Observatory — Complete Sun and Moon Data for One Day](https://aa.usno.navy.mil/data/RS_OneDay)
3. [India Meteorological Department — Indian Astronomical Ephemeris](https://mausam.imd.gov.in/responsive/indianAstronomicalEphemeris.php)
4. [IPGeolocation — Astronomy API](https://ipgeolocation.io/astronomy-api.html)
