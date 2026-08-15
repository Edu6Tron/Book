package com.edu6tron.spiritualcompanion.nativepreview.data

data class AartiItem(
  val id: String,
  val title: String,
  val deity: String,
  val category: String,
  val languages: List<String>,
  val duration: String,
  val opening: String,
  val summary: String,
  val source: String,
  val verses: List<String>,
)

data class FestivalItem(
  val id: String,
  val name: String,
  val hinduMonth: String,
  val dateNote: String,
  val deity: String,
  val significance: String,
  val observance: String,
  val source: String,
)

data class TempleItem(
  val id: String,
  val name: String,
  val city: String,
  val state: String,
  val address: String,
  val registryStatus: String,
  val authority: String,
  val sourceUrl: String,
)

object NativeCatalogue {
  val aartiCategories = listOf("All", "Morning", "Evening", "Ganesh", "Devi", "Vishnu", "Shiva")
  val hinduMonths = listOf("All", "Chaitra", "Vaishakha", "Ashadha", "Shravana", "Bhadrapada", "Ashwin", "Kartika", "Magha", "Phalguna")

  val aartis = listOf(
    AartiItem("om-jai-jagdish-hare", "Om Jai Jagdish Hare", "Vishnu", "Vishnu", listOf("Hindi", "Sanskrit"), "5 min", "Om jai Jagdish hare, swami jai Jagdish hare…", "A widely sung evening Aarti offered to Vishnu as the sustaining presence in all beings.", "Gita Press devotional tradition", listOf("Om jai Jagdish hare, swami jai Jagdish hare; bhakt jano ke sankat, kshan mein door kare.", "Jo dhyave phal pave, dukh binse man ka; sukh sampatti ghar aave, kasht mite tan ka.", "Mata pita tum mere, sharan gahoon kiski; tum bin aur na dooja, aas karoon jiski.")),
    AartiItem("jai-ganesh-deva", "Jai Ganesh Deva", "Ganesha", "Ganesh", listOf("Hindi"), "4 min", "Jai Ganesh, jai Ganesh, jai Ganesh deva…", "A compact invocation to Ganesha, traditionally sung before beginning a new task or puja.", "North Indian Aarti tradition", listOf("Jai Ganesh, jai Ganesh, jai Ganesh deva; mata jaki Parvati, pita Mahadeva.", "Ek dant dayavant, char bhuja dhari; mathe sindoor sohe, moose ki sawari.", "Andhan ko aankh det, kodhin ko kaya; baanjhan ko putra det, nirdhan ko maya.")),
    AartiItem("om-jai-shiv-omkara", "Om Jai Shiv Omkara", "Shiva", "Shiva", listOf("Hindi", "Sanskrit"), "5 min", "Om jai Shiv Omkara, prabhu jai Shiv Omkara…", "A devotional praise of Shiva’s many forms, suited to a quiet morning or Pradosh practice.", "Shaiva devotional tradition", listOf("Om jai Shiv Omkara, prabhu jai Shiv Omkara; Brahma Vishnu Sadashiv, ardhangi dhara.", "Ek anan chaturanan panchanan raje; hansasan garudasan vrishvahan saje.", "Do bhuja char chaturbhuj, dashbhuj ati sohe; trin roop nirakhata, tribhuvan jan mohe.")),
    AartiItem("jai-ambe-gauri", "Jai Ambe Gauri", "Devi", "Devi", listOf("Hindi"), "5 min", "Jai Ambe Gauri, maiya jai Shyama Gauri…", "A reverent Aarti to the Divine Mother, often included in Navratri and daily Devi worship.", "Shakta devotional tradition", listOf("Jai Ambe Gauri, maiya jai Shyama Gauri; tumko nishdin dhyavat, Hari Brahma Shivri.", "Maang sindoor virajat, tiko mrigmad ko; ujjwal se do naina, chandravadan niko.", "Kanak saman kalevar, raktambar raje; rakt pushp gal mala, kanthan par saje.")),
    AartiItem("om-jai-lakshmi-mata", "Om Jai Lakshmi Mata", "Lakshmi", "Evening", listOf("Hindi", "Sanskrit"), "4 min", "Om jai Lakshmi mata, maiya jai Lakshmi mata…", "An evening prayer for inner abundance, gratitude, and responsible stewardship.", "Lakshmi puja devotional tradition", listOf("Om jai Lakshmi mata, maiya jai Lakshmi mata; tumko nishdin sevat, Hari Vishnu vidhata.", "Uma Rama Brahmani, tum hi jag mata; surya chandrama dhyavat, Narad rishi gata.", "Durga roop niranjani, sukh sampatti data; jo koi tumko dhyata, riddhi siddhi dhana pata.")),
    AartiItem("kakad-aarti", "Kakad Aarti", "Morning Invocation", "Morning", listOf("Marathi", "Hindi"), "6 min", "Utha utha ho sakalika, vache smaraava Gajamukha…", "A gentle dawn Aarti tradition that frames the day with remembrance and steadiness.", "Maharashtra temple tradition", listOf("Utha utha ho sakalika, vache smaraava Gajamukha; riddhi siddhicha naayaka, sukhdaayaka bhaktansi.", "Angani ha vishwacha, jyoti prakatli bhavachi; prabhati naam smarata, shantata manachi.", "Karuna sindhu deva, dinancha tu aadhaar; prabhat vandana ghe, raksha kar sansar.")),
  )

  val festivals = listOf(
    FestivalItem("ram-navami", "Ram Navami", "Chaitra", "Chaitra Shukla Navami", "Rama", "Commemorates the birth of Rama and the ideal of dharma expressed through courage, compassion, and restraint.", "Read a selected passage from the Ramayana, offer a simple fruit or flower, and conclude with a short Rama naam japa.", "Sahapedia festival overview"),
    FestivalItem("hanuman-jayanti", "Hanuman Jayanti", "Chaitra", "Regional observance varies", "Hanuman", "Honours steadfast service, strength guided by humility, and devotion to Rama.", "Recite a chosen Hanuman prayer and consider one act of helpful service for the day.", "Regional Hindu calendar traditions"),
    FestivalItem("guru-purnima", "Guru Purnima", "Ashadha", "Ashadha Purnima", "Guru tradition", "A day to acknowledge teachers, lineages, and the discipline of learning.", "Offer gratitude to a teacher, study a meaningful passage, and set one sincere learning intention.", "IGNCA cultural resources"),
    FestivalItem("janmashtami", "Krishna Janmashtami", "Bhadrapada", "Bhadrapada Krishna Ashtami", "Krishna", "Celebrates Krishna’s birth and the call to act with devotion and discernment.", "Read a verse from the Bhagavad Gita, sing a Krishna bhajan, or prepare a simple offering with gratitude.", "Gita Press devotional tradition"),
    FestivalItem("ganesh-chaturthi", "Ganesh Chaturthi", "Bhadrapada", "Bhadrapada Shukla Chaturthi", "Ganesha", "Welcomes the remover of obstacles and honours new beginnings, learning, and community.", "Begin a delayed task, offer durva or a flower where appropriate, and recite a brief Ganesha invocation.", "Sahapedia festival overview"),
    FestivalItem("navratri", "Sharad Navratri", "Ashwin", "Nine nights in Ashwin", "Devi", "A nine-night observance of the Divine Mother and inner renewal through devotion and discipline.", "Choose a sustainable daily practice such as a Devi Aarti, reflective journal entry, or act of kindness.", "Shakta devotional traditions"),
    FestivalItem("diwali", "Deepavali", "Kartika", "Kartika Amavasya", "Lakshmi", "A festival of light that invites clarity, gratitude, and the cultivation of a welcoming home.", "Light a lamp safely, clear one small space, and share appreciation with family or neighbours.", "IGNCA cultural resources"),
    FestivalItem("maha-shivaratri", "Maha Shivaratri", "Magha", "Krishna Chaturdashi", "Shiva", "An evening of contemplation centred on Shiva, stillness, and freedom from habitual distraction.", "Set aside a quiet interval for mantra, mindful breathing, or a simple Shiva Aarti.", "Shaiva ritual traditions"),
    FestivalItem("holi", "Holi", "Phalguna", "Phalguna Purnima", "Prahlada tradition", "Marks the victory of sincere devotion and renewal at the turn toward spring.", "Share goodwill, conserve water during celebrations, and take a moment to repair a relationship.", "Sahapedia festival overview"),
  )

  val temples = listOf(
    TempleItem("alarnath-dev-3-ap", "Sri Alarnath Dev", "Puri", "Odisha", "At Alarapur, PO/P.S. Brahmagiri, Puri district, Odisha", "Indexed institution record", "Odisha Hindu Religious Endowments Department", "https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/"),
    TempleItem("arjuneswar-dev-4-ap", "Sri Arjuneswar Dev", "Puri", "Odisha", "At Kalikabadi, PO/P.S. Brahmagiri, Puri district, Odisha", "Indexed institution record", "Odisha Hindu Religious Endowments Department", "https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/"),
    TempleItem("ananteswar-dev-17-ap", "Ananteswar Dev", "Puri", "Odisha", "At Samjaipur, PO Biragobindapur, Puri district, Odisha", "Indexed institution record", "Odisha Hindu Religious Endowments Department", "https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/"),
    TempleItem("akhandalmani-temple-67-ap", "Akhandalmani Temple", "Puri", "Odisha", "At Khanjipur, PO Nayahat (Jogeswarpur), Puri district, Odisha", "Indexed institution record", "Odisha Hindu Religious Endowments Department", "https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/"),
    TempleItem("siddhivinayak", "Shree Siddhivinayak Temple", "Mumbai", "Maharashtra", "Kakasaheb Gadgil Marg, Prabhadevi, Mumbai", "Public religious institution", "Shree Siddhivinayak Ganapati Temple Trust", "https://www.siddhivinayak.org/"),
    TempleItem("trimbakeshwar", "Trimbakeshwar Shiva Temple", "Nashik", "Maharashtra", "Trimbak, Nashik district, Maharashtra", "Public temple record", "Maharashtra temple administration", "https://www.maharashtratourism.gov.in/"),
  )

  fun suggestionsFor(location: String): List<AartiItem> {
    val normalized = location.lowercase()
    return when {
      normalized.contains("pune") || normalized.contains("mumbai") || normalized.contains("maharashtra") -> aartis.filter { it.category == "Ganesh" || "Marathi" in it.languages || it.category == "Shiva" }
      normalized.contains("puri") || normalized.contains("odisha") -> aartis.filter { it.category == "Vishnu" || "Sanskrit" in it.languages }
      else -> aartis
    }
  }
}
