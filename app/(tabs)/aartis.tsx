import { useMemo, useState } from "react";
import { FlatList, Pressable, ScrollView, StyleSheet, Text, TextInput, View } from "react-native";
import { router } from "expo-router";
import { ScreenContainer } from "@/components/screen-container";
import { EmptyState, IconCircle, Pill, SectionHeading, SoftCard } from "@/components/spiritual-ui";
import { IconSymbol } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";
import { haptic } from "@/lib/haptics";
import { aartiCategories, aartis, filterAartis, type AartiCategory } from "@/lib/spiritual-data";
import { useSpiritualStore } from "@/lib/spiritual-store";

export default function AartisScreen() {
  const colors = useColors();
  const [category, setCategory] = useState<AartiCategory>("All");
  const [query, setQuery] = useState("");
  const { favourites, toggleFavourite } = useSpiritualStore();
  const results = useMemo(() => filterAartis(aartis, category, query), [category, query]);

  return (
    <ScreenContainer>
      <FlatList
        data={results}
        keyExtractor={(item) => item.id}
        contentContainerStyle={styles.content}
        showsVerticalScrollIndicator={false}
        ListHeaderComponent={
          <View style={styles.header}>
            <SectionHeading eyebrow="Offline library" title="Aartis for every day" actionLabel="Discover new" onAction={() => { haptic.light(); router.push("/media-discovery?query=Hindu%20Aarti" as never); }} />
            <Text style={[styles.subtitle, { color: colors.muted }]}>Your devotional library opens from bundled content, so there is no wait for a connection.</Text>
            <View style={[styles.search, { backgroundColor: colors.surface, borderColor: colors.border }]}>
              <IconSymbol name="sliders.horizontal.3" size={19} color={colors.muted} />
              <TextInput
                value={query}
                onChangeText={setQuery}
                placeholder="Search by title, deity, or language"
                placeholderTextColor={colors.muted}
                style={[styles.searchInput, { color: colors.foreground }]}
                returnKeyType="done"
                accessibilityLabel="Search Aartis"
              />
            </View>
            <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chips}>
              {aartiCategories.map((item) => <Pill key={item} label={item} selected={category === item} onPress={() => { haptic.selection(); setCategory(item); }} />)}
            </ScrollView>
            <Text style={[styles.resultLabel, { color: colors.muted }]}>{results.length} {results.length === 1 ? "Aarti" : "Aartis"} ready to read</Text>
            <Pressable onPress={() => { haptic.light(); router.push(`/media-discovery?query=${encodeURIComponent(query.trim() || "Hindu Aarti")}` as never); }} style={({ pressed }) => [styles.discoveryButton, { borderColor: colors.primary, backgroundColor: `${colors.primary}12` }, pressed && styles.pressed]}>
              <IconSymbol name="music.note.list" size={19} color={colors.primary} />
              <View style={styles.discoveryCopy}>
                <Text style={[styles.discoveryTitle, { color: colors.foreground }]}>Discover current provider results</Text>
                <Text style={[styles.discoveryDetail, { color: colors.muted }]}>Runs only when you choose it; local results stay instant.</Text>
              </View>
              <IconSymbol name="chevron.right" size={18} color={colors.primary} />
            </Pressable>
            <Pressable onPress={() => { haptic.light(); router.push("/audio-player" as never); }} style={({ pressed }) => [styles.playerLink, { borderColor: colors.border, backgroundColor: colors.surface }, pressed && styles.pressed]}>
              <IconSymbol name="play.fill" size={18} color={colors.primary} />
              <Text style={[styles.playerLinkText, { color: colors.foreground }]}>Open local media player</Text>
              <IconSymbol name="chevron.right" size={18} color={colors.primary} />
            </Pressable>
          </View>
        }
        renderItem={({ item }) => {
          const isFavourite = favourites.includes(item.id);
          return (
            <Pressable onPress={() => { haptic.light(); router.push(`/aarti/${item.id}` as never); }} style={({ pressed }) => [pressed && styles.pressed]}>
              <SoftCard style={styles.itemCard}>
                <View style={styles.itemTop}>
                  <IconCircle name="book.fill" color={colors.primary} background={`${colors.primary}18`} />
                  <Pressable onPress={() => { haptic.medium(); toggleFavourite(item.id); }} hitSlop={10} style={({ pressed }) => [styles.heartButton, pressed && styles.pressed]} accessibilityLabel={isFavourite ? `Remove ${item.title} from favourites` : `Add ${item.title} to favourites`}>
                    <IconSymbol name={isFavourite ? "heart.fill" : "heart"} size={22} color={isFavourite ? colors.primary : colors.muted} />
                  </Pressable>
                </View>
                <Text style={[styles.itemTitle, { color: colors.foreground }]}>{item.title}</Text>
                <Text style={[styles.itemMeta, { color: colors.muted }]}>{item.deity} · {item.duration} · {item.languages.join(" / ")}</Text>
                <Text numberOfLines={2} style={[styles.itemSummary, { color: colors.muted }]}>{item.summary}</Text>
                <View style={styles.readRow}>
                  <Text style={[styles.readLabel, { color: colors.primary }]}>Read Aarti</Text>
                  <IconSymbol name="chevron.right" size={18} color={colors.primary} />
                </View>
              </SoftCard>
            </Pressable>
          );
        }}
        ItemSeparatorComponent={() => <View style={{ height: 10 }} />}
        ListEmptyComponent={<EmptyState title="No Aartis found" detail="Try a different category, deity, or language." />}
      />
    </ScreenContainer>
  );
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 36 },
  header: { gap: 14, marginBottom: 16 },
  subtitle: { fontSize: 14, lineHeight: 20 },
  search: { height: 48, flexDirection: "row", alignItems: "center", gap: 10, borderWidth: StyleSheet.hairlineWidth, borderRadius: 14, paddingHorizontal: 14 },
  searchInput: { flex: 1, height: "100%", fontSize: 15 },
  chips: { gap: 8, paddingRight: 8 },
  resultLabel: { fontSize: 13, fontWeight: "700" },
  discoveryButton: { borderWidth: StyleSheet.hairlineWidth, borderRadius: 15, padding: 13, flexDirection: "row", alignItems: "center", gap: 10 },
  discoveryCopy: { flex: 1, gap: 2 },
  discoveryTitle: { fontSize: 14, fontWeight: "800" },
  discoveryDetail: { fontSize: 12, lineHeight: 17 },
  playerLink: { minHeight: 44, borderWidth: StyleSheet.hairlineWidth, borderRadius: 14, paddingHorizontal: 13, alignItems: "center", flexDirection: "row", gap: 9 },
  playerLinkText: { flex: 1, fontSize: 14, fontWeight: "800" },
  itemCard: { gap: 10 },
  itemTop: { flexDirection: "row", justifyContent: "space-between", alignItems: "center" },
  heartButton: { width: 42, height: 42, alignItems: "center", justifyContent: "center", borderRadius: 21 },
  itemTitle: { fontSize: 18, lineHeight: 24, fontWeight: "800", letterSpacing: -0.2 },
  itemMeta: { fontSize: 13, lineHeight: 19, fontWeight: "600" },
  itemSummary: { fontSize: 14, lineHeight: 20 },
  readRow: { marginTop: 2, alignSelf: "flex-start", flexDirection: "row", alignItems: "center" },
  readLabel: { fontSize: 14, fontWeight: "800" },
  pressed: { opacity: 0.72, transform: [{ scale: 0.985 }] },
});
