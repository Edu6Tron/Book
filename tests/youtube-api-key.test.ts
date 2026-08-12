import "dotenv/config";
import { describe, expect, it } from "vitest";

describe("YouTube discovery credential", () => {
  it("accepts a lightweight authorised search request", async () => {
    const apiKey = process.env.YOUTUBE_DATA_API_KEY;
    expect(apiKey).toBeTruthy();

    const url = new URL("https://www.googleapis.com/youtube/v3/search");
    url.searchParams.set("part", "id");
    url.searchParams.set("type", "video");
    url.searchParams.set("maxResults", "1");
    url.searchParams.set("q", "Om Jai Jagdish Hare");
    url.searchParams.set("key", apiKey!);

    const response = await fetch(url);
    expect(response.status).toBe(200);
    const body = await response.json() as { items?: unknown[] };
    expect(Array.isArray(body.items)).toBe(true);
  }, 15_000);
});
