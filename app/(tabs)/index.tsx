import { useMemo } from "react";
import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { router } from "expo-router";
import { ScreenContainer } from "@/components/screen-container";
import { IconCircle, SectionHeading, SoftCard } from "@/components/spiritual-ui";
import { IconSymbol } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";
import { haptic } from "@/lib/haptics";
import { festivals } from "@/lib/spiritual-data";
import { useSpiritualStore } from "@/lib/spiritual-store";

export default function HomeScreen() {
  const colors = useColors();
  const { completedPracticeIds, japaCount } = useSpiritualStore();
  const greeting = useMemo(() => {
    const hour = new Date().getHours();
    if (hour < 12) return "Good morning";
    if (hour < 17) return "Good afternoon";
    return "Good evening";
  }, []);
  const nextFestival = festivals.find((festival) => festival.id === "janmashtami") ?? festivals[0];
  const progress = Math.min(completedPracticeIds.length / 3, 1);

  const open = (path: "/(tabs)/aartis" | "/(tabs)/festivals" | "/temple-finder" | "/(tabs)/practice") => {
    haptic.light();
    router.push(path as never);
  };

  return (
    <ScreenContainer>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <View style={styles.heroRow}>
          <View>
            <Text style={[styles.greeting, { color: colors.muted }]}>{greeting}</Text>
            <Text style={[styles.heading, { color: colors.foreground }]}>A quiet place for{"\n"}your daily practice.</Text>
          </View>
          <IconCircle name="sparkles" color={colors.primary} background={`${colors.primary}18`} />
        </View>

        <SoftCard style={[styles.intentionCard, { backgroundColor: colors.foreground, borderColor: colors.foreground }]}>
          <View style={styles.intentionTopline}>
            <Text style={[styles.intentionLabel, { color: colors.warning }]}>DAILY INTENTION</Text>
            <IconSymbol name="clock.fill" size={18} color={colors.warning} />
          </View>
          <Text style={[styles.intentionText, { color: colors.background }]}>“May my actions today be steady, kind, and clear.”</Text>
          <Text style={[styles.intentionCaption, { color: `${colors.background}B3` }]}>Take one slow breath before you begin.</Text>
        </SoftCard>

        <SectionHeading eyebrow="Begin here" title="Your essentials" />
        <View style={styles.actionGrid}>
          <QuickAction title="Aartis" caption="Read offline" icon="music.note.list" onPress={() => open("/(tabs)/aartis")} />
          <QuickAction title="Festivals" caption="Explore dates" icon="calendar" onPress={() => open("/(tabs)/festivals")} />
          <QuickAction title="Temple Finder" caption="Local directory" icon="building.columns" onPress={() => open("/temple-finder")} />
          <QuickAction title="Practice" caption="Japa & rituals" icon="figure.mind.and.body" onPress={() => open("/(tabs)/practice")} />
        </View>

        <SectionHeading eyebrow="Today" title="A small, steady rhythm" actionLabel="Practice" onAction={() => open("/(tabs)/practice")} />
        <SoftCard>
          <View style={styles.progressRow}>
            <View style={styles.progressCopy}>
              <Text style={[styles.cardTitle, { color: colors.foreground }]}>{completedPracticeIds.length === 0 ? "Begin your daily ritual" : "Your practice is taking shape"}</Text>
              <Text style={[styles.cardDetail, { color: colors.muted }]}>{completedPracticeIds.length}/3 ritual steps complete · {japaCount} japa</Text>
            </View>
            <Text style={[styles.progressNumber, { color: colors.primary }]}>{Math.round(progress * 100)}%</Text>
          </View>
          <View style={[styles.progressTrack, { backgroundColor: colors.border }]}>
            <View style={[styles.progressFill, { backgroundColor: colors.success, width: `${progress * 100}%` }]} />
          </View>
        </SoftCard>

        <SectionHeading eyebrow="Calendar" title="A festival to explore" actionLabel="All festivals" onAction={() => open("/(tabs)/festivals")} />
        <Pressable onPress={() => { haptic.light(); router.push({ pathname: "/festival/[id]", params: { id: nextFestival.id } } as never); }} style={({ pressed }) => [pressed && styles.pressed]}>
          <SoftCard>
            <View style={styles.festivalRow}>
              <IconCircle name="calendar" color={colors.warning} background={`${colors.warning}1D`} />
              <View style={styles.festivalCopy}>
                <Text style={[styles.cardTitle, { color: colors.foreground }]}>{nextFestival.name}</Text>
                <Text style={[styles.cardDetail, { color: colors.muted }]}>{nextFestival.dateNote} · {nextFestival.deity}</Text>
              </View>
              <IconSymbol name="chevron.right" size={21} color={colors.muted} />
            </View>
          </SoftCard>
        </Pressable>
      </ScrollView>
    </ScreenContainer>
  );
}

function QuickAction({ title, caption, icon, onPress }: { title: string; caption: string; icon: "music.note.list" | "calendar" | "building.columns" | "figure.mind.and.body"; onPress: () => void }) {
  const colors = useColors();
  return (
    <Pressable onPress={onPress} style={({ pressed }) => [styles.quickAction, { backgroundColor: colors.surface, borderColor: colors.border }, pressed && styles.pressed]}>
      <IconCircle name={icon} color={colors.primary} background={`${colors.primary}18`} />
      <Text style={[styles.quickTitle, { color: colors.foreground }]}>{title}</Text>
      <Text style={[styles.quickCaption, { color: colors.muted }]}>{caption}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 38, gap: 26 },
  heroRow: { flexDirection: "row", justifyContent: "space-between", alignItems: "flex-start", paddingTop: 8 },
  greeting: { fontSize: 14, fontWeight: "700", marginBottom: 5 },
  heading: { fontSize: 29, lineHeight: 35, fontWeight: "900", letterSpacing: -0.8 },
  intentionCard: { gap: 11, padding: 20 },
  intentionTopline: { flexDirection: "row", alignItems: "center", justifyContent: "space-between" },
  intentionLabel: { fontSize: 11, fontWeight: "900", letterSpacing: 1.25 },
  intentionText: { fontSize: 21, lineHeight: 29, fontWeight: "800", letterSpacing: -0.35 },
  intentionCaption: { fontSize: 14, lineHeight: 20 },
  actionGrid: { flexDirection: "row", flexWrap: "wrap", gap: 10 },
  quickAction: { width: "48.4%", minHeight: 137, padding: 14, borderRadius: 18, borderWidth: StyleSheet.hairlineWidth, gap: 8 },
  quickTitle: { marginTop: "auto", fontSize: 15, fontWeight: "800" },
  quickCaption: { fontSize: 12, lineHeight: 17 },
  progressRow: { flexDirection: "row", gap: 12, alignItems: "center", justifyContent: "space-between" },
  progressCopy: { flex: 1, gap: 4 },
  cardTitle: { fontSize: 16, lineHeight: 21, fontWeight: "800" },
  cardDetail: { fontSize: 13, lineHeight: 19 },
  progressNumber: { fontSize: 20, fontWeight: "900" },
  progressTrack: { marginTop: 16, height: 7, borderRadius: 4, overflow: "hidden" },
  progressFill: { height: 7, borderRadius: 4 },
  festivalRow: { flexDirection: "row", gap: 12, alignItems: "center" },
  festivalCopy: { flex: 1, gap: 3 },
  pressed: { opacity: 0.74, transform: [{ scale: 0.985 }] },
});
