# Connected Discovery: Verified Provider Notes

## Temple discovery

Google Places API Text Search accepts a user-supplied text query and returns a matching place list; it can also apply a location bias. The app can therefore preserve its bundled temple directory for immediate rendering and issue a place lookup only after the user taps **Discover new** or explicitly searches for a temple. The service is documented as an HTTP `POST` endpoint and requires the expected request fields and a response field mask. This should be called from the app server so the provider credential is not bundled into the APK.

Source: [Google Maps Platform — Text Search (New)](https://developers.google.com/maps/documentation/places/web-service/text-search)

## Video discovery

The YouTube Data API documents a searchable resource that can return matching videos, channels, or playlists. The app can use a user-entered or contextual search query, then display source-labelled discovery cards that open the authorised provider playback route. A search result points to an identifiable underlying resource rather than being its own persistent media object. Requests require a project API key or OAuth token according to the operation, so provider access must remain server-side.

Source: [YouTube Data API — API Reference](https://developers.google.com/youtube/v3/docs)

## Product rule

The app will not perform an uncontrolled background crawl. It will show verified bundled content first, perform connected discovery only after a deliberate user action, label every connected result with its source, and allow the user to decide whether to open, save, or download content that the source authorises for download.

## Temple-directory scope

The research does **not** support claiming a single, complete national registry of every legally registered temple in India. Official information is published by different state departments and data portals with differing scope and fields. The directory must therefore describe records accurately as **government-source listed** and identify the publishing department, source URL, jurisdiction, record identifier where available, and last-checked date.

The Tamil Nadu Hindu Religious & Charitable Endowments Department exposes a Temple List with state, district, heritage, religious-institution, and endowment list categories. The Odisha Hindu Religious Endowments Department publishes a zone-wise indexed-institutions list that includes index number, institution name, and postal address. The National Portal of India also lists Telangana Endowments information services, and the Open Government Data Platform India contains a Karnataka A- and B-grade-temple catalogue entry. These sources demonstrate the state-by-state model required for a defensible national aggregation.

Sources:

- [Tamil Nadu HR&CE — Temple List](https://hrce.tn.gov.in/hrcehome/temple_list.php)
- [Odisha Hindu Religious Endowments — List of Indexed Institutions](https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/)
- [National Portal of India — Telangana Endowments Department information](http://services.india.gov.in/services/details/endowments-department-information-telangana)
- [Open Government Data Platform India — List of A and B Grade Temples in Karnataka](https://www.data.gov.in/catalog/list-and-b-grade-temples-karnataka)
