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
    AartiItem("sukhkarta-dukhharta", "Sukhkarta Dukhharta", "Ganesha", "Ganesh", listOf("Marathi"), "5 min", "Sukhkarta dukhharta varta vighnachi…", "A beloved Marathi Ganesha Aarti for beginnings, study, and community worship.", "Maharashtra devotional tradition", listOf("Sukhkarta dukhharta varta vighnachi; nurvi purvi prem kripa jayachi.", "Sarvangi sundar uti shendurachi; kanthi jhalke maal mukta phalanchi.", "Jai dev jai dev jai mangal murti; darshan matre mankamana purti.")),
    AartiItem("durge-durgat-bhari", "Durge Durgat Bhari", "Devi", "Devi", listOf("Marathi"), "5 min", "Durge durgat bhari tujavina sansari…", "A Marathi Devi Aarti centred on courage, refuge, and compassion.", "Maharashtra Devi worship tradition", listOf("Durge durgat bhari tujavina sansari; anath nathe ambe karuna vistari.", "Vari vari janma maranate vari; hari padalo ata sankat nivari.", "Jai devi jai devi mahishaasurmardini; survar ishvar varde tarak sanjivani.")),
    AartiItem("raghupati-raghav", "Raghupati Raghav Raja Ram", "Rama", "Morning", listOf("Hindi", "Sanskrit"), "3 min", "Raghupati Raghav Raja Ram…", "A concise Rama bhajan suited to a morning intention or a peaceful closing prayer.", "Rama bhajan tradition", listOf("Raghupati Raghav Raja Ram; patit pavan Sita Ram.", "Ishwar Allah tero naam; sabko sanmati de Bhagwan.", "Sita Ram, Sita Ram; bhajo pyare tu Sita Ram.")),
    AartiItem("shree-ramchandra-kripalu", "Shree Ramchandra Kripalu", "Rama", "Vishnu", listOf("Hindi", "Awadhi"), "6 min", "Shree Ramchandra kripalu bhaj man…", "A contemplative devotional praise of Rama’s compassion and grace.", "Tulsidas devotional tradition", listOf("Shree Ramchandra kripalu bhaj man, haran bhav bhay darunam.", "Nav kanj lochan kanj mukh, kar kanj pad kanjarunam.", "Kandarp aganit amit chhavi, nav neel neerad sundaram.")),
    AartiItem("hare-krishna-mahamantra", "Hare Krishna Maha Mantra", "Krishna", "Vishnu", listOf("Sanskrit", "Hindi"), "8 min", "Hare Krishna Hare Krishna…", "A mantra-based kirtan practice that can be repeated at a comfortable pace.", "Vaishnava kirtan tradition", listOf("Hare Krishna Hare Krishna, Krishna Krishna Hare Hare.", "Hare Rama Hare Rama, Rama Rama Hare Hare.", "Repeat gently, keeping attention on sound and breath.")),
    AartiItem("aigiri-nandini", "Aigiri Nandini", "Devi", "Devi", listOf("Sanskrit"), "7 min", "Ayi giri nandini nandita medini…", "A Sanskrit hymn celebrating the strength and compassion of the Divine Mother.", "Devi Mahatmya inspired hymn tradition", listOf("Ayi giri nandini nandita medini vishva vinodini nandinute.", "Giri var vindhya shirodhi nivasini vishnu vilasini jishnunute.", "Bhagavati he shitikantha kutumbini bhuri kutumbini bhuri krute.")),
    AartiItem("govind-bolo", "Govind Bolo Hari Gopal Bolo", "Krishna", "Evening", listOf("Hindi"), "4 min", "Govind bolo Hari Gopal bolo…", "A short call-and-response bhajan for a calm devotional gathering.", "North Indian bhajan tradition", listOf("Govind bolo Hari Gopal bolo; Radha Raman Hari Gopal bolo.", "Govind bolo Hari Gopal bolo; Hari naam ka amrit bolo.", "Let the refrain settle into an unhurried rhythm.")),
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
    FestivalItem("makar-sankranti", "Makar Sankranti", "Magha", "Solar transition into Makara", "Surya", "A solar observance marking a seasonal turning point and gratitude for light, harvest, and steady effort.", "Offer thanks, share seasonal food responsibly, and begin the day with sunlight and a simple intention.", "Indian cultural calendar traditions"),
    FestivalItem("vishwakarma-puja", "Vishwakarma Puja", "Bhadrapada", "Regional observance varies", "Vishwakarma", "Honours skill, careful work, craft, and the ethical use of tools.", "Clean one work area, inspect a tool safely, and dedicate focused effort to a meaningful task.", "Indian labour and craft traditions"),
    FestivalItem("tulsi-vivah", "Tulsi Vivah", "Kartika", "Kartika Shukla Ekadashi to Dwadashi", "Vishnu and Tulsi", "Marks a devotional transition toward the wedding season in many Vaishnava traditions.", "Water a plant mindfully, offer a simple prayer, and practise gratitude for the natural world.", "Vaishnava observance traditions"),
    FestivalItem("datta-jayanti", "Datta Jayanti", "Margashirsha", "Margashirsha Purnima", "Dattatreya", "A day of remembrance for the unity of learning, service, and contemplative discipline.", "Read an uplifting teaching, offer food where appropriate, and complete one act of quiet service.", "Datta tradition resources"),
  )

  val temples = listOf(
    TempleItem("alarnath-dev-3-ap", "Sri Alarnath Dev", "Puri", "Odisha", "At Alarapur, PO/P.S. Brahmagiri, Puri district, Odisha", "Indexed institution record", "Odisha Hindu Religious Endowments Department", "https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/"),
    TempleItem("arjuneswar-dev-4-ap", "Sri Arjuneswar Dev", "Puri", "Odisha", "At Kalikabadi, PO/P.S. Brahmagiri, Puri district, Odisha", "Indexed institution record", "Odisha Hindu Religious Endowments Department", "https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/"),
    TempleItem("ananteswar-dev-17-ap", "Ananteswar Dev", "Puri", "Odisha", "At Samjaipur, PO Biragobindapur, Puri district, Odisha", "Indexed institution record", "Odisha Hindu Religious Endowments Department", "https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/"),
    TempleItem("akhandalmani-temple-67-ap", "Akhandalmani Temple", "Puri", "Odisha", "At Khanjipur, PO Nayahat (Jogeswarpur), Puri district, Odisha", "Indexed institution record", "Odisha Hindu Religious Endowments Department", "https://hinduendowments.odisha.gov.in/list-of-indexed-institutions-zone-wise/"),
    TempleItem("siddhivinayak", "Shree Siddhivinayak Temple", "Mumbai", "Maharashtra", "Kakasaheb Gadgil Marg, Prabhadevi, Mumbai", "Public religious institution", "Shree Siddhivinayak Ganapati Temple Trust", "https://www.siddhivinayak.org/"),
    TempleItem("trimbakeshwar", "Trimbakeshwar Shiva Temple", "Nashik", "Maharashtra", "Trimbak, Nashik district, Maharashtra", "Public temple record", "Maharashtra temple administration", "https://www.maharashtratourism.gov.in/"),
    TempleItem("dagdusheth", "Shreemant Dagdusheth Halwai Ganpati", "Pune", "Maharashtra", "Budhwar Peth, Pune", "Public temple institution", "Dagdusheth Halwai Ganpati Trust", "https://www.dagdushethganpati.com/"),
    TempleItem("moreshwar-morgaon", "Shri Moreshwar Temple", "Morgaon", "Maharashtra", "Morgaon, Baramati taluka, Pune district", "Pilgrimage temple record", "Maharashtra tourism and temple tradition", "https://www.maharashtratourism.gov.in/"),
    TempleItem("jagannath", "Shri Jagannath Temple", "Puri", "Odisha", "Puri, Odisha", "Temple administration record", "Shree Jagannath Temple Administration", "https://shreejagannatha.in/"),
    TempleItem("lingaraj", "Lingaraj Temple", "Bhubaneswar", "Odisha", "Old Town, Bhubaneswar", "Heritage and temple record", "Odisha tourism and heritage resources", "https://odishatourism.gov.in/"),
    TempleItem("kashi-vishwanath", "Kashi Vishwanath Temple", "Varanasi", "Uttar Pradesh", "Lahori Tola, Varanasi", "Temple trust record", "Shri Kashi Vishwanath Temple Trust", "https://www.shrikashivishwanath.org/"),
    TempleItem("tirumala", "Sri Venkateswara Temple", "Tirumala", "Andhra Pradesh", "Tirumala, Tirupati district", "Temple administration record", "Tirumala Tirupati Devasthanams", "https://www.tirumala.org/"),
  )

  fun suggestionsFor(location: String): List<AartiItem> {
    val normalized = location.lowercase()
    return when {
      normalized.contains("pune") || normalized.contains("mumbai") || normalized.contains("maharashtra") -> aartis.filter { it.category == "Ganesh" || "Marathi" in it.languages || it.category == "Shiva" }
      normalized.contains("puri") || normalized.contains("odisha") -> aartis.filter { it.category == "Vishnu" || "Sanskrit" in it.languages }
      normalized.contains("varanasi") || normalized.contains("uttar pradesh") -> aartis.filter { it.category == "Shiva" || it.deity == "Rama" }
      normalized.contains("tirupati") || normalized.contains("andhra") -> aartis.filter { it.category == "Vishnu" || it.deity == "Krishna" }
      else -> aartis
    }
  }
}
