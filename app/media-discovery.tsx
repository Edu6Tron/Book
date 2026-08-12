import { useEffect, useState } from "react";
import { Alert, FlatList, Pressable, StyleSheet, Text, TextInput, View } from "react-native";
import { Stack, useLocalSearchParams } from "expo-router";
import * as WebBrowser from "expo-web-browser";
import { ScreenContainer } from "@/components/screen-container";
import { EmptyState, IconCircle, SoftCard } from "@/components/spiritual-ui";
import { IconSymbol } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";
import { haptic } from "@/lib/haptics";
import { trpc } from "@/lib/trpc";

export default function MediaDiscoveryScreen() {
  const colors = useColors();
  const { query: initialQuery } = useLocalSearchParams<{ query?: string }>();
  const [draft, setDraft] = useState(initialQuery ?? "Hindu Aarti");
  const [query, setQuery] = useState(initialQuery ?? "Hindu Aarti");
  const discovery = trpc.media.discover.useQuery({ query, limit: 6 }, { staleTime: 10 * 60 * 1000, refetchOnMount: false });

  useEffect(() => {
    if (initialQuery && initialQuery !== query) {
      setDraft(initialQuery);
      setQuery(initialQuery);
    }
  }, [initialQuery, query]);

  const submit = () => {
    const next = draft.trim();
    if (next.length < 2) {
      Alert.alert("Enter a longer search", "Please enter at least two characters to discover provider results.");
      return;
    }
    haptic.light();
    setQuery(next);
  };

  const openVideo = (videoId: string) => {
    haptic.light();
    void WebBrowser.openBrowserAsync(`https://www.youtube.com/watch?v=${encodeURIComponent(videoId)}`);
  };

  return (
    <ScreenContainer>
      <Stack.Screen options={{ title: "Discover media", headerBackTitle: "Back" }} />
      <FlatList
        data={discovery.data?.items ?? []}
        keyExtractor={(item) => item.videoId}
        contentContainerStyle={styles.content}
        refreshing={discovery.isFetching}
        onRefresh={() => { haptic.selection(); void discovery.refetch(); }}
        showsVerticalScrollIndicator={false}
        ListHeaderComponent={
          <View style={styles.header}>
            <IconCircle name="music.note.list" color={colors.primary} background={`${colors.primary}18`} />
            <Text style={[styles.title, { color: colors.foreground }]}>Discover new devotional media</Text>
            <Text style={[styles.subtitle, { color: colors.muted }]}>This search runs only when you request it. Results are supplied by YouTube and are not verified, selected, or endorsed by Spiritual Companion.</Text>
            <View style={[styles.search, { backgroundColor: colors.surface, borderColor: colors.border }]}>
              <TextInput value={draft} onChangeText={setDraft} onSubmitEditing={submit} placeholder="Search Aarti, bhajan, mantra…" placeholderTextColor={colors.muted} style={[styles.input, { color: colors.foreground }]} returnKeyType="search" />
              <Pressable onPress={submit} style={({ pressed }) => [styles.searchButton, { backgroundColor: colors.primary }, pressed && styles.pressed]} accessibilityLabel="Discover media">
                <IconSymbol name="chevron.right" size={20} color={colors.background} />
              </Pressable>
            </View>
            {discovery.isFetching ? <Text style={[styles.status, { color: colors.muted }]}>Refreshing provider results…</Text> : <Text style={[styles.status, { color: colors.muted }]}>{discovery.data?.available ? `${discovery.data.items.length} provider results` : "Provider discovery is unavailable right now."}</Text>}
          </View>
        }
        renderItem={({ item }) => (
          <Pressable onPress={() => openVideo(item.videoId)} style={({ pressed }) => [pressed && styles.pressed]}>
            <SoftCard style={styles.card}>
              <View style={styles.cardRow}>
                <IconCircle name="music.note.list" color={colors.warning} background={`${colors.warning}1D`} />
                <View style={styles.copy}>
                  <Text numberOfLines={2} style={[styles.videoTitle, { color: colors.foreground }]}>{item.title}</Text>
                  <Text numberOfLines={1} style={[styles.channel, { color: colors.muted }]}>{item.channelTitle} · YouTube</Text>
                </View>
                <IconSymbol name="chevron.right" size={19} color={colors.primary} />
              </View>
              {item.description ? <Text numberOfLines={2} style={[styles.description, { color: colors.muted }]}>{item.description}</Text> : null}
              <Text style={[styles.openLabel, { color: colors.primary }]}>Open in YouTube</Text>
            </SoftCard>
          </Pressable>
        )}
        ItemSeparatorComponent={() => <View style={{ height: 10 }} />}
        ListEmptyComponent={discovery.isFetching ? null : <EmptyState title="No provider results yet" detail="Try a broader Aarti, deity, bhajan, or mantra search. Pull to refresh only when you want a newer result set." />}
      />
    </ScreenContainer>
  );
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 38 },
  header: { gap: 12, marginBottom: 16 },
  title: { fontSize: 27, lineHeight: 33, fontWeight: "900", letterSpacing: -0.6 },
  subtitle: { fontSize: 14, lineHeight: 20 },
  search: { minHeight: 50, flexDirection: "row", alignItems: "center", gap: 8, borderWidth: StyleSheet.hairlineWidth, borderRadius: 14, padding: 5, paddingLeft: 14 },
  input: { flex: 1, fontSize: 15, minHeight: 40 },
  searchButton: { width: 40, height: 40, borderRadius: 11, alignItems: "center", justifyContent: "center" },
  status: { fontSize: 12, lineHeight: 17, fontWeight: "600" },
  card: { gap: 10 },
  cardRow: { flexDirection: "row", alignItems: "center", gap: 11 },
  copy: { flex: 1, gap: 3 },
  videoTitle: { fontSize: 16, lineHeight: 21, fontWeight: "800" },
  channel: { fontSize: 13, lineHeight: 18 },
  description: { fontSize: 13, lineHeight: 19 },
  openLabel: { fontSize: 13, fontWeight: "800" },
  pressed: { opacity: 0.74, transform: [{ scale: 0.985 }] },
});
