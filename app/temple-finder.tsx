import { useMemo, useState } from "react";
import { FlatList, Pressable, ScrollView, StyleSheet, Text, TextInput, View } from "react-native";
import { Stack } from "expo-router";
import * as WebBrowser from "expo-web-browser";
import { ScreenContainer } from "@/components/screen-container";
import { EmptyState, IconCircle, Pill, SoftCard } from "@/components/spiritual-ui";
import { IconSymbol } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";
import { haptic } from "@/lib/haptics";
import { filterTemples, temples, type Temple } from "@/lib/spiritual-data";

const cities = ["All", ...Array.from(new Set(temples.map((temple) => temple.city)))];

export default function TempleFinderScreen() {
  const colors = useColors();
  const [city, setCity] = useState("All");
  const [query, setQuery] = useState("");
  const results = useMemo(() => filterTemples(temples, city, query), [city, query]);

  const openRegistry = (temple: Temple) => {
    haptic.light();
    void WebBrowser.openBrowserAsync(temple.sourceUrl);
  };

  return (
    <ScreenContainer>
      <Stack.Screen options={{ title: "Temple Finder", headerBackTitle: "Back" }} />
      <FlatList
        data={results}
        keyExtractor={(item) => item.id}
        contentContainerStyle={styles.content}
        showsVerticalScrollIndicator={false}
        ListHeaderComponent={
          <View style={styles.header}>
            <Text style={[styles.title, { color: colors.foreground }]}>Trusted Temple Directory</Text>
            <Text style={[styles.subtitle, { color: colors.muted }]}>No maps or GPS. This directory begins with public authority records and shows each record’s jurisdiction, identifier, and source. India-wide coverage will grow state by state as official lists are verified.</Text>
            <View style={[styles.search, { backgroundColor: colors.surface, borderColor: colors.border }]}>
              <IconSymbol name="location.fill" size={19} color={colors.muted} />
              <TextInput value={query} onChangeText={setQuery} placeholder="Search city, temple, or tradition" placeholderTextColor={colors.muted} style={[styles.searchInput, { color: colors.foreground }]} returnKeyType="done" />
            </View>
            <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chips}>
              {cities.map((item) => <Pill key={item} label={item} selected={city === item} onPress={() => { haptic.selection(); setCity(item); }} />)}
            </ScrollView>
            <Text style={[styles.resultLabel, { color: colors.muted }]}>{results.length} government-source records in this release</Text>
          </View>
        }
        renderItem={({ item }) => (
          <SoftCard style={styles.card}>
            <View style={styles.topRow}>
              <IconCircle name="building.columns" color={colors.primary} background={`${colors.primary}18`} />
              <View style={styles.copy}>
                <Text style={[styles.name, { color: colors.foreground }]}>{item.name}</Text>
                <Text style={[styles.meta, { color: colors.muted }]}>{item.city}, {item.state} · {item.tradition}</Text>
              </View>
            </View>
            <Text style={[styles.note, { color: colors.muted }]}>{item.note}</Text>
            <View style={[styles.addressBox, { backgroundColor: `${colors.primary}0D` }]}>
              <IconSymbol name="location.fill" size={17} color={colors.primary} />
              <Text style={[styles.address, { color: colors.foreground }]}>{item.address}</Text>
            </View>
            <View style={[styles.registryBox, { borderColor: colors.border }]}>
              <Text style={[styles.registryStatus, { color: colors.success }]}>{item.registryStatus.toUpperCase()}</Text>
              <Text style={[styles.registryDetail, { color: colors.muted }]}>{item.authority} · {item.jurisdiction}</Text>
              <Text style={[styles.registryDetail, { color: colors.muted }]}>Record {item.registryId} · checked {item.lastChecked}</Text>
            </View>
            <Pressable onPress={() => openRegistry(item)} style={({ pressed }) => [styles.registryButton, { backgroundColor: colors.primary }, pressed && styles.pressed]}>
              <IconSymbol name="book.fill" size={18} color={colors.background} />
              <Text style={[styles.registryLabel, { color: colors.background }]}>View official registry</Text>
            </Pressable>
          </SoftCard>
        )}
        ItemSeparatorComponent={() => <View style={{ height: 10 }} />}
        ListEmptyComponent={<EmptyState title="No temples found" detail="Try a different city or temple name." />}
      />
    </ScreenContainer>
  );
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 36 },
  header: { gap: 14, marginBottom: 16 },
  title: { fontSize: 27, fontWeight: "900", letterSpacing: -0.6 },
  subtitle: { fontSize: 14, lineHeight: 20 },
  search: { height: 48, flexDirection: "row", alignItems: "center", gap: 10, borderWidth: StyleSheet.hairlineWidth, borderRadius: 14, paddingHorizontal: 14 },
  searchInput: { flex: 1, height: "100%", fontSize: 15 },
  chips: { gap: 8, paddingRight: 8 },
  resultLabel: { fontSize: 13, fontWeight: "700" },
  card: { gap: 13 },
  topRow: { flexDirection: "row", alignItems: "center", gap: 12 },
  copy: { flex: 1, gap: 3 },
  name: { fontSize: 17, lineHeight: 23, fontWeight: "800" },
  meta: { fontSize: 13, lineHeight: 18 },
  note: { fontSize: 14, lineHeight: 20 },
  addressBox: { borderRadius: 12, padding: 12, flexDirection: "row", gap: 9, alignItems: "flex-start" },
  address: { flex: 1, fontSize: 13, lineHeight: 19, fontWeight: "600" },
  registryBox: { borderTopWidth: StyleSheet.hairlineWidth, paddingTop: 11, gap: 3 },
  registryStatus: { fontSize: 10, lineHeight: 14, fontWeight: "900", letterSpacing: 0.8 },
  registryDetail: { fontSize: 12, lineHeight: 17 },
  registryButton: { height: 45, borderRadius: 13, alignItems: "center", justifyContent: "center", flexDirection: "row", gap: 8 },
  registryLabel: { fontSize: 14, fontWeight: "900" },
  pressed: { opacity: 0.74, transform: [{ scale: 0.985 }] },
});
