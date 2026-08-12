import { useMemo, useState } from "react";
import { Alert, FlatList, Linking, Pressable, ScrollView, StyleSheet, Text, TextInput, View } from "react-native";
import { Stack } from "expo-router";
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

  const openMaps = async (temple: Temple) => {
    const url = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(`${temple.name}, ${temple.address}`)}`;
    try {
      const supported = await Linking.canOpenURL(url);
      if (!supported) throw new Error("Maps unavailable");
      haptic.light();
      await Linking.openURL(url);
    } catch {
      Alert.alert("Unable to open maps", "Please check that a browser or maps application is available on your device.");
    }
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
            <Text style={[styles.title, { color: colors.foreground }]}>Temple Finder</Text>
            <Text style={[styles.subtitle, { color: colors.muted }]}>Browse this local directory instantly. Directions only open when you choose a temple, avoiding slow maps or location work on launch.</Text>
            <View style={[styles.search, { backgroundColor: colors.surface, borderColor: colors.border }]}>
              <IconSymbol name="location.fill" size={19} color={colors.muted} />
              <TextInput value={query} onChangeText={setQuery} placeholder="Search city, temple, or tradition" placeholderTextColor={colors.muted} style={[styles.searchInput, { color: colors.foreground }]} returnKeyType="done" />
            </View>
            <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chips}>
              {cities.map((item) => <Pill key={item} label={item} selected={city === item} onPress={() => { haptic.selection(); setCity(item); }} />)}
            </ScrollView>
            <Text style={[styles.resultLabel, { color: colors.muted }]}>{results.length} temples ready to browse</Text>
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
            <Pressable onPress={() => openMaps(item)} style={({ pressed }) => [styles.mapButton, { backgroundColor: colors.primary }, pressed && styles.pressed]}>
              <IconSymbol name="map.fill" size={18} color={colors.background} />
              <Text style={[styles.mapLabel, { color: colors.background }]}>Open in Maps</Text>
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
  mapButton: { height: 45, borderRadius: 13, alignItems: "center", justifyContent: "center", flexDirection: "row", gap: 8 },
  mapLabel: { fontSize: 14, fontWeight: "900" },
  pressed: { opacity: 0.74, transform: [{ scale: 0.985 }] },
});
