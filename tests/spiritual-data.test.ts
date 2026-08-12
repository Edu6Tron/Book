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

  it("filters government-source temple records locally without map or GPS access", () => {
    const puriRecords = filterTemples(temples, "Puri", "Alarnath");
    expect(puriRecords).toHaveLength(1);
    expect(puriRecords[0]?.registryId).toBe("3-A.P");
    expect(puriRecords[0]?.authority).toContain("Odisha Hindu Religious Endowments Department");
  });
});
