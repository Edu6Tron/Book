export type AartiCategory = "All" | "Morning" | "Evening" | "Ganesh" | "Devi" | "Vishnu" | "Shiva";

export type Aarti = {
  id: string;
  title: string;
  deity: string;
  category: Exclude<AartiCategory, "All">;
  languages: string[];
  duration: string;
  opening: string;
  summary: string;
  source: string;
  verses: string[];
};

export type Festival = {
  id: string;
  name: string;
  hinduMonth: string;
  dateNote: string;
  deity: string;
  significance: string;
  observance: string;
  source: string;
};

export type Temple = {
  id: string;
  name: string;
  city: string;
  state: string;
  tradition: string;
  address: string;
  note: string;
};

export const aartiCategories: AartiCategory[] = ["All", "Morning", "Evening", "Ganesh", "Devi", "Vishnu", "Shiva"];

export const aartis: Aarti[] = [
  {
    id: "om-jai-jagdish-hare",
    title: "Om Jai Jagdish Hare",
    deity: "Vishnu",
    category: "Vishnu",
    languages: ["Hindi", "Sanskrit"],
    duration: "5 min",
    opening: "Om jai Jagdish hare, swami jai Jagdish hare…",
    summary: "A widely sung evening Aarti offered to Vishnu as the sustaining presence in all beings.",
    source: "Gita Press devotional tradition",
    verses: [
      "Om jai Jagdish hare, swami jai Jagdish hare; bhakt jano ke sankat, kshan mein door kare.",
      "Jo dhyave phal pave, dukh binse man ka; sukh sampatti ghar aave, kasht mite tan ka.",
      "Mata pita tum mere, sharan gahoon kiski; tum bin aur na dooja, aas karoon jiski.",
    ],
  },
  {
    id: "jai-ganesh-deva",
    title: "Jai Ganesh Deva",
    deity: "Ganesha",
    category: "Ganesh",
    languages: ["Hindi"],
    duration: "4 min",
    opening: "Jai Ganesh, jai Ganesh, jai Ganesh deva…",
    summary: "A compact invocation to Ganesha, traditionally sung before beginning a new task or puja.",
    source: "North Indian Aarti tradition",
    verses: [
      "Jai Ganesh, jai Ganesh, jai Ganesh deva; mata jaki Parvati, pita Mahadeva.",
      "Ek dant dayavant, char bhuja dhari; mathe sindoor sohe, moose ki sawari.",
      "Andhan ko aankh det, kodhin ko kaya; baanjhan ko putra det, nirdhan ko maya.",
    ],
  },
  {
    id: "om-jai-shiv-omkara",
    title: "Om Jai Shiv Omkara",
    deity: "Shiva",
    category: "Shiva",
    languages: ["Hindi", "Sanskrit"],
    duration: "5 min",
    opening: "Om jai Shiv Omkara, prabhu jai Shiv Omkara…",
    summary: "A devotional praise of Shiva’s many forms, well suited to a quiet morning or Pradosh practice.",
    source: "Shaiva devotional tradition",
    verses: [
      "Om jai Shiv Omkara, prabhu jai Shiv Omkara; Brahma Vishnu Sadashiv, ardhangi dhara.",
      "Ek anan chaturanan panchanan raje; hansasan garudasan vrishvahan saje.",
      "Do bhuja char chaturbhuj, dashbhuj ati sohe; trin roop nirakhata, tribhuvan jan mohe.",
    ],
  },
  {
    id: "jai-amba-gauri",
    title: "Jai Ambe Gauri",
    deity: "Devi",
    category: "Devi",
    languages: ["Hindi"],
    duration: "5 min",
    opening: "Jai Ambe Gauri, maiya jai Shyama Gauri…",
    summary: "A reverent Aarti to the Divine Mother, often included in Navratri and daily Devi worship.",
    source: "Shakta devotional tradition",
    verses: [
      "Jai Ambe Gauri, maiya jai Shyama Gauri; tumko nishdin dhyavat, Hari Brahma Shivri.",
      "Maang sindoor virajat, tiko mrigmad ko; ujjwal se do naina, chandravadan niko.",
      "Kanak saman kalevar, raktambar raje; rakt pushp gal mala, kanthan par saje.",
    ],
  },
  {
    id: "om-jai-lakshmi-mata",
    title: "Om Jai Lakshmi Mata",
    deity: "Lakshmi",
    category: "Evening",
    languages: ["Hindi", "Sanskrit"],
    duration: "4 min",
    opening: "Om jai Lakshmi mata, maiya jai Lakshmi mata…",
    summary: "An evening prayer for inner abundance, gratitude, and responsible stewardship.",
    source: "Lakshmi puja devotional tradition",
    verses: [
      "Om jai Lakshmi mata, maiya jai Lakshmi mata; tumko nishdin sevat, Hari Vishnu vidhata.",
      "Uma Rama Brahmani, tum hi jag mata; surya chandrama dhyavat, Narad rishi gata.",
      "Durga roop niranjani, sukh sampatti data; jo koi tumko dhyata, riddhi siddhi dhana pata.",
    ],
  },
  {
    id: "kakad-aarti",
    title: "Kakad Aarti",
    deity: "Morning Invocation",
    category: "Morning",
    languages: ["Marathi", "Hindi"],
    duration: "6 min",
    opening: "Utha utha ho sakalika, vache smaraava Gajamukha…",
    summary: "A gentle dawn Aarti tradition that frames the day with remembrance and steadiness.",
    source: "Maharashtra temple tradition",
    verses: [
      "Utha utha ho sakalika, vache smaraava Gajamukha; riddhi siddhicha naayaka, sukhdaayaka bhaktansi.",
      "Angani ha vishwacha, jyoti prakatli bhavachi; prabhati naam smarata, shantata manachi.",
      "Karuna sindhu deva, dinancha tu aadhaar; prabhat vandana ghe, raksha kar sansar.",
    ],
  },
];

export const hinduMonths = ["All", "Chaitra", "Vaishakha", "Shravana", "Bhadrapada", "Ashwin", "Kartika", "Magha", "Phalguna"] as const;

export const festivals: Festival[] = [
  {
    id: "ram-navami",
    name: "Ram Navami",
    hinduMonth: "Chaitra",
    dateNote: "Chaitra Shukla Navami",
    deity: "Rama",
    significance: "Commemorates the birth of Rama and the ideal of dharma expressed through courage, compassion, and restraint.",
    observance: "Read a selected passage from the Ramayana, offer a simple fruit or flower, and conclude with a short Rama naam japa.",
    source: "Sahapedia festival overview",
  },
  {
    id: "hanuman-jayanti",
    name: "Hanuman Jayanti",
    hinduMonth: "Chaitra",
    dateNote: "Regional observance varies",
    deity: "Hanuman",
    significance: "Honours steadfast service, strength guided by humility, and devotion to Rama.",
    observance: "Recite a chosen Hanuman prayer and consider one act of helpful service for the day.",
    source: "Regional Hindu calendar traditions",
  },
  {
    id: "guru-purnima",
    name: "Guru Purnima",
    hinduMonth: "Ashadha",
    dateNote: "Ashadha Purnima",
    deity: "Guru tradition",
    significance: "A day to acknowledge teachers, lineages, and the discipline of learning.",
    observance: "Offer gratitude to a teacher, study a meaningful passage, and set one sincere learning intention.",
    source: "IGNCA cultural resources",
  },
  {
    id: "janmashtami",
    name: "Krishna Janmashtami",
    hinduMonth: "Bhadrapada",
    dateNote: "Bhadrapada Krishna Ashtami",
    deity: "Krishna",
    significance: "Celebrates Krishna’s birth and the call to act with devotion and discernment.",
    observance: "Read a verse from the Bhagavad Gita, sing a Krishna bhajan, or prepare a simple offering with gratitude.",
    source: "Gita Press devotional tradition",
  },
  {
    id: "ganesh-chaturthi",
    name: "Ganesh Chaturthi",
    hinduMonth: "Bhadrapada",
    dateNote: "Bhadrapada Shukla Chaturthi",
    deity: "Ganesha",
    significance: "Welcomes the remover of obstacles and honours new beginnings, learning, and community.",
    observance: "Begin a delayed task, offer durva or a flower where appropriate, and recite a brief Ganesha invocation.",
    source: "Sahapedia festival overview",
  },
  {
    id: "navratri",
    name: "Sharad Navratri",
    hinduMonth: "Ashwin",
    dateNote: "Nine nights in Ashwin",
    deity: "Devi",
    significance: "A nine-night observance of the Divine Mother and inner renewal through devotion and discipline.",
    observance: "Choose a sustainable daily practice such as a Devi Aarti, reflective journal entry, or act of kindness.",
    source: "Shakta devotional traditions",
  },
  {
    id: "diwali",
    name: "Deepavali",
    hinduMonth: "Kartika",
    dateNote: "Kartika Amavasya",
    deity: "Lakshmi",
    significance: "A festival of light that invites clarity, gratitude, and the cultivation of a welcoming home.",
    observance: "Light a lamp safely, clear one small space, and share appreciation with family or neighbours.",
    source: "IGNCA cultural resources",
  },
  {
    id: "maha-shivaratri",
    name: "Maha Shivaratri",
    hinduMonth: "Magha",
    dateNote: "Krishna Chaturdashi",
    deity: "Shiva",
    significance: "An evening of contemplation centred on Shiva, stillness, and freedom from habitual distraction.",
    observance: "Set aside a quiet interval for mantra, mindful breathing, or a simple Shiva Aarti.",
    source: "Shaiva ritual traditions",
  },
  {
    id: "holi",
    name: "Holi",
    hinduMonth: "Phalguna",
    dateNote: "Phalguna Purnima",
    deity: "Prahlada tradition",
    significance: "Marks the victory of sincere devotion and renewal at the turn toward spring.",
    observance: "Share goodwill, conserve water during celebrations, and take a moment to repair a relationship.",
    source: "Sahapedia festival overview",
  },
];

export const temples: Temple[] = [
  {
    id: "kashi-vishwanath",
    name: "Kashi Vishwanath Temple",
    city: "Varanasi",
    state: "Uttar Pradesh",
    tradition: "Shaiva",
    address: "Lahori Tola, Varanasi, Uttar Pradesh",
    note: "A prominent Shiva temple in the historic city of Varanasi.",
  },
  {
    id: "siddhivinayak",
    name: "Shree Siddhivinayak Temple",
    city: "Mumbai",
    state: "Maharashtra",
    tradition: "Ganapatya",
    address: "Prabhadevi, Mumbai, Maharashtra",
    note: "A widely visited Ganesha temple; check official notices before travel.",
  },
  {
    id: "iskcon-delhi",
    name: "ISKCON Temple Delhi",
    city: "New Delhi",
    state: "Delhi",
    tradition: "Vaishnava",
    address: "Sant Nagar, East of Kailash, New Delhi",
    note: "A Krishna temple with devotional programmes and a public cultural space.",
  },
  {
    id: "meenakshi",
    name: "Meenakshi Amman Temple",
    city: "Madurai",
    state: "Tamil Nadu",
    tradition: "Shakta",
    address: "Madurai Main, Madurai, Tamil Nadu",
    note: "A historic temple dedicated to Meenakshi and Sundareshwarar.",
  },
  {
    id: "jagannath-puri",
    name: "Shree Jagannath Temple",
    city: "Puri",
    state: "Odisha",
    tradition: "Vaishnava",
    address: "Grand Road, Puri, Odisha",
    note: "A major Jagannath pilgrimage centre; entry guidance may apply.",
  },
  {
    id: "golden-temple-amritsar",
    name: "Shri Durgiana Temple",
    city: "Amritsar",
    state: "Punjab",
    tradition: "Devi",
    address: "Goal Bagh, Amritsar, Punjab",
    note: "A Durga temple in Amritsar with a reflective waterfront setting.",
  },
];

export function filterAartis(items: Aarti[], category: AartiCategory, query: string): Aarti[] {
  const normalized = query.trim().toLocaleLowerCase();
  return items.filter((item) => {
    const categoryMatches = category === "All" || item.category === category;
    const queryMatches = !normalized || [item.title, item.deity, item.category, ...item.languages]
      .join(" ")
      .toLocaleLowerCase()
      .includes(normalized);
    return categoryMatches && queryMatches;
  });
}

export function filterFestivals(items: Festival[], month: string): Festival[] {
  return month === "All" ? items : items.filter((item) => item.hinduMonth === month);
}

export function filterTemples(items: Temple[], city: string, query: string): Temple[] {
  const normalized = query.trim().toLocaleLowerCase();
  return items.filter((item) => {
    const cityMatches = city === "All" || item.city === city;
    const queryMatches = !normalized || [item.name, item.city, item.state, item.tradition]
      .join(" ")
      .toLocaleLowerCase()
      .includes(normalized);
    return cityMatches && queryMatches;
  });
}
