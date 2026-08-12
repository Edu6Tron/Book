import { COOKIE_NAME } from "../shared/const.js";
import { z } from "zod";
import { getSessionCookieOptions } from "./_core/cookies";
import { ENV } from "./_core/env";
import { systemRouter } from "./_core/systemRouter";
import { publicProcedure, router } from "./_core/trpc";

export const appRouter = router({
  // if you need to use socket.io, read and register route in server/_core/index.ts, all api should start with '/api/' so that the gateway can route correctly
  system: systemRouter,
  auth: router({
    me: publicProcedure.query((opts) => opts.ctx.user),
    logout: publicProcedure.mutation(({ ctx }) => {
      const cookieOptions = getSessionCookieOptions(ctx.req);
      ctx.res.clearCookie(COOKIE_NAME, { ...cookieOptions, maxAge: -1 });
      return {
        success: true,
      } as const;
    }),
  }),
  media: router({
    discover: publicProcedure
      .input(z.object({
        query: z.string().trim().min(2).max(120),
        limit: z.number().int().min(1).max(12).default(6),
      }))
      .query(async ({ input }) => {
        if (!ENV.youtubeDataApiKey) {
          return { available: false, items: [] };
        }

        const params = new URLSearchParams({
          part: "snippet",
          type: "video",
          videoEmbeddable: "true",
          safeSearch: "moderate",
          maxResults: String(input.limit),
          q: `${input.query} devotional`,
          key: ENV.youtubeDataApiKey,
        });

        try {
          const response = await fetch(`https://www.googleapis.com/youtube/v3/search?${params.toString()}`);
          if (!response.ok) {
            return { available: false, items: [] };
          }
          const payload = await response.json() as {
            items?: Array<{
              id?: { videoId?: string };
              snippet?: { title?: string; channelTitle?: string; publishedAt?: string; description?: string };
            }>;
          };
          const items = (payload.items ?? [])
            .map((item) => ({
              videoId: item.id?.videoId ?? "",
              title: item.snippet?.title ?? "Untitled video",
              channelTitle: item.snippet?.channelTitle ?? "Unknown channel",
              publishedAt: item.snippet?.publishedAt ?? "",
              description: item.snippet?.description ?? "",
            }))
            .filter((item) => Boolean(item.videoId));
          return { available: true, items };
        } catch {
          return { available: false, items: [] };
        }
      }),
  }),

  // TODO: add feature routers here, e.g.
  // todo: router({
  //   list: protectedProcedure.query(({ ctx }) =>
  //     db.getUserTodos(ctx.user.id)
  //   ),
  // }),
});

export type AppRouter = typeof appRouter;
