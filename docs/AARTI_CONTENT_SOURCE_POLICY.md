# Offline Aarti Content Source Policy

**Purpose.** This policy governs the full devotional text, transliteration, translation, recording, and metadata that may be bundled into Spiritual Companion. It is a product-control document, not legal advice. Any uncertainty keeps the material out of the bundled catalogue until rights are verified.

## Product boundary

The application may become a broad offline **Aarti guide**, but it must not become an unverified copy of commercial Aarti books, website databases, lyric pages, recordings, scans, or videos. A source being visible online, downloadable, or indexed by a library is **not**, by itself, proof that an app may reproduce it.

The Government of India copyright handbook states that copyright protects literary and musical works, sound recordings, and related expressions. It also distinguishes rights held by lyricists, composers, performers, and recording producers. The handbook is an information booklet rather than a substitute for the Act and Rules; therefore the project must preserve a conservative review gate. [1]

## Allowed source tiers

| Tier | May bundle full text? | Evidence required in repository | Attribution shown in app |
|---|---:|---|---|
| **A — verified public domain** | Yes | Author/composer identity or traditional-status evidence, date/death-date rationale where relevant, stable source URL, reviewer decision | “Public-domain devotional text; source and provenance” |
| **B — explicit compatible open licence** | Yes, only after licence terms are checked | Exact licence, version, source URL, required author/attribution text, modification status | Author, licence name/version, licence URL, source URL |
| **C — permission-backed contribution** | Yes | Written permission or contributor grant stored outside the APK and approval identifier in the catalogue | Rights-holder or contributor credit as agreed |
| **D — user-provided local media/text** | Kept on the user’s device only | No bundling or upload | No catalogue attribution needed |
| **Unverified / commercial / availability-only** | **No** | Do not ingest | Use a discoverable external reference only, if suitable |

The initial implementation accepts only verified **Tier A** text and exact, compatible **Tier B** text. It does not infer a licence from a search result, a streaming-provider page, a PDF, a book scan, or an archive download.

## Source findings retained for review

1. The Government of India copyright handbook explains that the general rule for original literary, dramatic, musical, and artistic works is a 60-year term counted from the beginning of the calendar year following the author’s death. It also notes that a recording may involve multiple right-holders. This supports separate review of text, composition, translation, and recording. [1]
2. Marathi Wikisource describes itself as an open library and states that it aims to host Marathi public-domain literature, including devotional categories. An individual page still requires page-level provenance and licence review; the project must not assume that every search result is eligible. [2]
3. The Internet Archive items **GP0153 Aarti Sangrah** and **Saraswati Aarti** expose download options and OCR metadata, but the returned item pages do not supply a reusable app licence or rights proof. They are therefore recorded as **discovery references only**, not sources for bundled text or audio. [3] [4]
4. Marathi Wikisource’s Aarti category explicitly describes its intended scope as Marathi public-domain Aartis. The category page also says that its own page content is available under CC BY-SA 4.0. This means the project must either meet the CC BY-SA obligations for a reused transcription or independently establish and enter a public-domain text; the category page itself does not provide blanket permission for all app content. [5]
5. The Wikisource page for **गणपतीची आरती/सुखकर्ता दुखहर्ता** identifies the author as Samarth Ramdas, provides a public-domain-in-India notice based on the 60-year term, and contains the full Marathi text. However, the same page carries a warning that its copyright/source status is under verification and is categorised as material to be checked. It is therefore a **lead only** and not an approved bundling source in this release. [6]
6. The Wikisource **दासबोध** page identifies Samarth Ramdas as its author, carries the same public-domain-in-India notice, and also marks source checking as pending. It supports historical authorship research but is not an automatic text-ingestion approval. [7]
7. Internet Archive’s 1935 *Goswami Tulsidas* item explicitly carries the metadata field `dc.rights: In Public Domain`. It is a historical study by Ramchandra Shukla, not a verified edition of each devotional work. It can support authorship and public-domain research, but it must not be treated as a text source for a specific Aarti unless the relevant edition and exact text are separately verified. [8]
8. Sanskrit Documents’ Marathi index publishes complete Unicode e-text collections of *Dnyaneshwari* and *Dasbodh* and credits the named volunteer contributors. Its landing page also points to recordings with separate Creative Commons terms. The observed index does **not** state one licence granting unrestricted republication of every Marathi text. Therefore it is a discovery and cross-check source only; do not bundle its transcriptions until a work-level licence, public-domain statement, or rights-holder permission is recorded. [9]
9. The reviewed Marathi Wikisource permanent revision of **गणपतीची आरती/सुखकर्ता दुखहर्ता** contains a complete text attributed to Samarth Ramdas, identifies both an India public-domain rationale and CC BY-SA 4.0 site terms, but also displays an explicit notice that the page’s source/copyright status is under review and that the material may be deleted pending a sourced historical edition. The full text is therefore **not approved for bundling** under the project’s conservative policy. It may remain a research lead only until a verifiable historical edition or independent rights review resolves the warning. [10]
10. Internet Archive items cannot be treated as reusable merely because their files are downloadable. The reviewed *GP0153 Aarti Sangrah* record contains no licence field or publication-rights statement; it is excluded. *Aarti Laxmi* identifies CC BY-ND 3.0, which does not allow the app’s text-normalisation/adaptation workflow; it is excluded. One *Aarti Keejei Hanuman Lala Ki* audio item claims CC0, but its metadata does not establish that the uploader controls the performance rights and its public review records a third-party copyright claim; it is excluded from bundled audio and is not a reliable text source. [11] [12] [13]
11. Sanskrit Wikisource’s **शिव आरती** permanent revision 33009 presents a complete Sanskrit Shiva Aarti, identifies the stable source revision, and explicitly states that the page text is available under CC BY-SA 4.0. The reviewed page has no unresolved source-warning banner. It is approved as a **Tier B text candidate**, subject to preserving source attribution, the exact CC BY-SA 4.0 notice and URL, stable revision URL, and an explicit “adapted only for accessible line breaks” indication in the app. It does not clear any recording, tune, performance, artwork, translation, or synchronised-media rights. [14]

## First verified whole-text inclusion

The first bundled full transcription is **Om Jai Shiv Omkara**. The Android data model retains the permanent source revision, contributor attribution, CC BY-SA 4.0 licence name and URL, and a `includesWholeText` indicator. The reader presents these details in a dedicated attribution card and opens the source or licence only after a user-initiated tap.

Every other existing library entry remains a **short guided reading excerpt** until its exact edition, source chain, and rights are reviewed individually. Commercial lyric sites, publisher scans without an explicit reusable licence, recordings, and pages whose public availability does not prove permission remain excluded from whole-text ingestion.

## Required catalogue provenance fields

Every bundled Aarti must carry:

| Field | Requirement |
|---|---|
| `id` | Stable original app identifier |
| `title` and `deity` | Editorially verified devotional metadata |
| `language` and `script` | Explicit language/script value; separate text variants when applicable |
| `textRightsTier` | A, B, or C only |
| `textSourceName` and `textSourceUrl` | Stable, displayable provenance |
| `rightsNote` | Short human-readable inclusion explanation |
| `licenceName` and `licenceUrl` | Required for Tier B; blank only for verified Tier A |
| `reviewedOn` and `reviewDecisionId` | Internal provenance audit fields; never user data |
| `audioRights` | “No bundled recording”, “original”, or an independently verified compatible licence |

## Audio and synchronised lyrics

The app may continue to play its existing original/offline soundscapes and user-selected local audio. It must not bundle a third-party Aarti recording, extract provider audio, or attach lyrics to a third-party recording unless each relevant right is expressly cleared. For a new bundled Aarti recording, text/composition, performance, recording, artwork, and synchronised-text rights must all be independently documented.

## Review workflow

1. Create a candidate record with its exact source URL and author/tradition claim.
2. Verify the rights tier and capture evidence before copying a full text into source control.
3. Normalise only formatting needed for accessibility; record any adaptation or transcription correction.
4. Add the in-app attribution surface and a catalogue integrity test.
5. Obtain an editorial review for language accuracy and an independent rights review for any non-public-domain material.
6. Exclude the item when the author, edition, translation, or licence cannot be established.

## References

[1] Government of India, Department for Promotion of Industry and Internal Trade, *Hand Book of Copyright Law*: https://copyright.gov.in/documents/handbook.html

[2] Marathi Wikisource, *मुखपृष्ठ*: https://mr.wikisource.org/wiki/%E0%A4%AE%E0%A5%81%E0%A4%96%E0%A4%AA%E0%A5%83%E0%A4%B7%E0%A5%8D%E0%A4%A0

[3] Internet Archive, *GP0153 Aarti Sangrah*: https://archive.org/details/0153-aarti-sangrah

[4] Internet Archive, *Saraswati Aarti*: https://archive.org/details/saraswati-aarti

[5] Marathi Wikisource, *वर्ग:आरती*: https://mr.wikisource.org/wiki/%E0%A4%B5%E0%A4%B0%E0%A5%8D%E0%A4%97:%E0%A4%86%E0%A4%B0%E0%A4%A4%E0%A5%80

[6] Marathi Wikisource, *गणपतीची आरती/सुखकर्ता दुखहर्ता*, permanent revision 157142: https://mr.wikisource.org/w/index.php?title=%E0%A4%97%E0%A4%A3%E0%A4%AA%E0%A4%A4%E0%A5%80%E0%A4%9A%E0%A5%80_%E0%A4%86%E0%A4%B0%E0%A4%A4%E0%A5%80/%E0%A4%B8%E0%A5%81%E0%A4%96%E0%A4%95%E0%A4%B0%E0%A5%8D%E0%A4%A4%E0%A4%BE_%E0%A4%A6%E0%A5%81%E0%A4%96%E0%A4%B9%E0%A4%B0%E0%A5%8D%E0%A4%A4%E0%A4%BE&oldid=157142

[7] Marathi Wikisource, *दासबोध*, permanent revision 180766: https://mr.wikisource.org/w/index.php?title=%E0%A4%A6%E0%A4%BE%E0%A4%B8%E0%A4%AC%E0%A5%8B%E0%A4%A7&oldid=180766

[8] Internet Archive, *Goswami Tulsidas* (1935, `dc.rights: In Public Domain`): https://archive.org/details/in.ernet.dli.2015.479522

[9] Sanskrit Documents, Marathi Documents index: https://sanskritdocuments.org/marathi/

[10] Marathi Wikisource, *गणपतीची आरती/सुखकर्ता दुखहर्ता*, permanent revision 157142: https://mr.wikisource.org/w/index.php?title=%E0%A4%97%E0%A4%A3%E0%A4%AA%E0%A4%A4%E0%A5%80%E0%A4%9A%E0%A5%80_%E0%A4%86%E0%A4%B0%E0%A4%A4%E0%A5%80/%E0%A4%B8%E0%A5%81%E0%A4%96%E0%A4%95%E0%A4%B0%E0%A5%8D%E0%A4%A4%E0%A4%BE_%E0%A4%A6%E0%A5%81%E0%A4%96%E0%A4%B9%E0%A4%B0%E0%A5%8D%E0%A4%A4%E0%A4%BE&oldid=157142

[11] Internet Archive, *GP0153 Aarti Sangrah* metadata: https://archive.org/metadata/0153-aarti-sangrah

[12] Internet Archive, *Aarti Laxmi* metadata: https://archive.org/metadata/AartiLaxmi

[13] Internet Archive, *Aarti Keejei Hanuman Lala Ki* metadata: https://archive.org/metadata/fptu_aarti-keejei-hanuman-lala-ki

[14] Sanskrit Wikisource, *शिव आरती*, permanent revision 33009: https://sa.wikisource.org/w/index.php?title=%E0%A4%B6%E0%A4%BF%E0%A4%B5_%E0%A4%86%E0%A4%B0%E0%A4%A4%E0%A5%80&oldid=33009
