import { describe, expect, it } from "vitest";
import { aartis, festivals, filterAartis, filterFestivals, filterTemples, temples } from "../lib/spiritual-data";

describe("Spiritual Companion local-first content", () => {
  it("filters Aartis by category and a deity query without a remote request", () => {
    const ganeshAartis = filterAartis(aartis, "Ganesh", "ganesha");
    expect(ganeshAartis).toHaveLength(1);
    expect(ganeshAartis[0]?.id).toBe("jai-ganesh-deva");
  });

  it("keeps the festival month selector deterministic", () => {
    const ashwinFestivals = filterFestivals(festivals, "Ashwin");
    expect(ashwinFestivals.map((festival) => festival.id)).toEqual(["navratri"]);
  });

  it("filters temple directions locally before opening an external map", () => {
    const mumbaiTemples = filterTemples(temples, "Mumbai", "");
    expect(mumbaiTemples).toHaveLength(1);
    expect(mumbaiTemples[0]?.id).toBe("siddhivinayak");
  });
});
