import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { Stack, router, useLocalSearchParams } from "expo-router";
import * as WebBrowser from "expo-web-browser";
import { ScreenContainer } from "@/components/screen-container";
import { IconCircle, SoftCard } from "@/components/spiritual-ui";
import { IconSymbol } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";
import { haptic } from "@/lib/haptics";
import { aartis } from "@/lib/spiritual-data";
import { useSpiritualStore } from "@/lib/spiritual-store";

export default function AartiReaderScreen() {
  const colors = useColors();
  const { id } = useLocalSearchParams<{ id: string }>();
  const item = aartis.find((aarti) => aarti.id === id);
  const { favourites, toggleFavourite } = useSpiritualStore();

  if (!item) {
    return (
      <ScreenContainer className="p-5">
        <Text style={[styles.errorTitle, { color: colors.foreground }]}>This Aarti is not available.</Text>
        <Pressable onPress={() => router.back()}><Text style={[styles.backLink, { color: colors.primary }]}>Return to library</Text></Pressable>
      </ScreenContainer>
    );
  }
  const favourite = favourites.includes(item.id);
  return (
    <ScreenContainer>
      <Stack.Screen options={{ title: "Aarti", headerBackTitle: "Library" }} />
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <View style={styles.headingRow}>
          <IconCircle name="book.fill" color={colors.primary} background={`${colors.primary}18`} />
          <Pressable onPress={() => { haptic.medium(); toggleFavourite(item.id); }} style={({ pressed }) => [styles.favouriteButton, { borderColor: colors.border, backgroundColor: colors.surface }, pressed && styles.pressed]}>
            <IconSymbol name={favourite ? "heart.fill" : "heart"} size={20} color={favourite ? colors.primary : colors.muted} />
            <Text style={[styles.favouriteText, { color: coloursForFavourite(favourite, colors.primary, colors.muted) }]}>{favourite ? "Saved" : "Save"}</Text>
          </Pressable>
        </View>
        <Text style={[styles.title, { color: colors.foreground }]}>{item.title}</Text>
        <Text style={[styles.meta, { color: colors.muted }]}>{item.deity} · {item.duration} · {item.languages.join(" / ")}</Text>
        <Text style={[styles.summary, { color: colors.muted }]}>{item.summary}</Text>
        <SoftCard style={styles.openingCard}>
          <Text style={[styles.openingLabel, { color: colors.primary }]}>OPENING</Text>
          <Text style={[styles.opening, { color: colors.foreground }]}>{item.opening}</Text>
        </SoftCard>
        <View style={styles.verses}>
          {item.verses.map((verse, index) => (
            <View key={`${item.id}-${index}`} style={[styles.verse, { borderLeftColor: index === 0 ? colors.warning : colors.border }]}>
              <Text style={[styles.verseNumber, { color: colors.primary }]}>{String(index + 1).padStart(2, "0")}</Text>
              <Text style={[styles.verseText, { color: colors.foreground }]}>{verse}</Text>
            </View>
          ))}
        </View>
        <View style={[styles.sourceBox, { borderColor: colors.border }]}>
          <Text style={[styles.sourceLabel, { color: colors.muted }]}>TRADITIONAL SOURCE</Text>
          <Text style={[styles.source, { color: colors.foreground }]}>{item.source}</Text>
          <View style={styles.sourceActions}>
            <Pressable onPress={() => { haptic.light(); void WebBrowser.openBrowserAsync(`https://www.google.com/search?q=${encodeURIComponent(`${item.title} ${item.deity} source and meaning`)}`); }} style={({ pressed }) => [styles.knowMoreButton, { borderColor: colors.border, backgroundColor: colors.surface }, pressed && styles.pressed]}>
              <Text style={[styles.knowMoreText, { color: colors.primary }]}>Know more</Text>
              <IconSymbol name="chevron.right" size={16} color={colors.primary} />
            </Pressable>
            <Pressable onPress={() => { haptic.light(); router.push(`/media-discovery?query=${encodeURIComponent(`${item.title} Aarti`)}` as never); }} style={({ pressed }) => [styles.videoButton, { backgroundColor: colors.primary }, pressed && styles.pressed]}>
              <Text style={[styles.videoText, { color: colors.background }]}>Discover videos</Text>
              <IconSymbol name="chevron.right" size={16} color={colors.background} />
            </Pressable>
          </View>
        </View>
      </ScrollView>
    </ScreenContainer>
  );
}

function coloursForFavourite(favourite: boolean, active: string, muted: string) {
  return favourite ? active : muted;
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 38, gap: 16 },
  headingRow: { flexDirection: "row", alignItems: "center", justifyContent: "space-between" },
  favouriteButton: { minHeight: 39, paddingHorizontal: 12, gap: 6, borderRadius: 20, borderWidth: StyleSheet.hairlineWidth, flexDirection: "row", alignItems: "center" },
  favouriteText: { fontSize: 13, fontWeight: "800" },
  title: { fontSize: 29, lineHeight: 35, fontWeight: "900", letterSpacing: -0.7 },
  meta: { marginTop: -10, fontSize: 14, lineHeight: 20, fontWeight: "600" },
  summary: { fontSize: 15, lineHeight: 22 },
  openingCard: { gap: 7 },
  openingLabel: { fontSize: 11, fontWeight: "900", letterSpacing: 1.1 },
  opening: { fontSize: 17, lineHeight: 25, fontWeight: "700" },
  verses: { gap: 15 },
  verse: { paddingLeft: 15, borderLeftWidth: 3, gap: 4 },
  verseNumber: { fontSize: 11, fontWeight: "900", letterSpacing: 1.1 },
  verseText: { fontSize: 17, lineHeight: 27, fontWeight: "600" },
  sourceBox: { borderTopWidth: StyleSheet.hairlineWidth, marginTop: 6, paddingTop: 16, gap: 4 },
  sourceLabel: { fontSize: 10, fontWeight: "900", letterSpacing: 1.1 },
  source: { fontSize: 13, lineHeight: 19, fontWeight: "700" },
  sourceActions: { marginTop: 9, flexDirection: "row", gap: 8 },
  knowMoreButton: { minHeight: 38, flex: 1, borderRadius: 11, borderWidth: StyleSheet.hairlineWidth, alignItems: "center", justifyContent: "center", flexDirection: "row", gap: 4 },
  knowMoreText: { fontSize: 13, fontWeight: "800" },
  videoButton: { minHeight: 38, flex: 1.15, borderRadius: 11, alignItems: "center", justifyContent: "center", flexDirection: "row", gap: 4 },
  videoText: { fontSize: 13, fontWeight: "800" },
  errorTitle: { fontSize: 22, fontWeight: "800" },
  backLink: { marginTop: 12, fontSize: 15, fontWeight: "800" },
  pressed: { opacity: 0.74, transform: [{ scale: 0.985 }] },
});
