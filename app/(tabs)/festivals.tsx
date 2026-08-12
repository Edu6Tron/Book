import { useMemo, useState } from "react";
import { FlatList, Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { router } from "expo-router";
import { ScreenContainer } from "@/components/screen-container";
import { EmptyState, IconCircle, Pill, SectionHeading, SoftCard } from "@/components/spiritual-ui";
import { IconSymbol } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";
import { haptic } from "@/lib/haptics";
import { festivals, filterFestivals, hinduMonths } from "@/lib/spiritual-data";

export default function FestivalsScreen() {
  const colors = useColors();
  const [month, setMonth] = useState<string>("All");
  const results = useMemo(() => filterFestivals(festivals, month), [month]);

  return (
    <ScreenContainer>
      <FlatList
        data={results}
        keyExtractor={(item) => item.id}
        contentContainerStyle={styles.content}
        showsVerticalScrollIndicator={false}
        ListHeaderComponent={
          <View style={styles.header}>
            <SectionHeading eyebrow="Offline calendar" title="Festivals & observances" actionLabel="Temple Finder" onAction={() => { haptic.light(); router.push("/temple-finder" as never); }} />
            <Text style={[styles.subtitle, { color: colors.muted }]}>Explore a compact local guide. Lunar dates and regional observances should always be confirmed with your local panchang.</Text>
            <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chips}>
              {hinduMonths.map((item) => <Pill key={item} label={item} selected={month === item} onPress={() => { haptic.selection(); setMonth(item); }} />)}
            </ScrollView>
            <Text style={[styles.resultLabel, { color: colors.muted }]}>{results.length} observances in view</Text>
          </View>
        }
        renderItem={({ item }) => (
          <Pressable onPress={() => { haptic.light(); router.push(`/festival/${item.id}` as never); }} style={({ pressed }) => [pressed && styles.pressed]}>
            <SoftCard style={styles.card}>
              <View style={styles.cardTop}>
                <IconCircle name="calendar" color={colors.warning} background={`${colors.warning}1D`} />
                <View style={styles.topCopy}>
                  <Text style={[styles.month, { color: colors.primary }]}>{item.hinduMonth.toUpperCase()}</Text>
                  <Text style={[styles.title, { color: colors.foreground }]}>{item.name}</Text>
                  <Text style={[styles.date, { color: colors.muted }]}>{item.dateNote}</Text>
                </View>
                <IconSymbol name="chevron.right" size={20} color={colors.muted} />
              </View>
              <Text numberOfLines={2} style={[styles.significance, { color: colors.muted }]}>{item.significance}</Text>
              <View style={[styles.sourceRow, { borderTopColor: colors.border }]}>
                <Text style={[styles.source, { color: colors.muted }]}>Source · {item.source}</Text>
              </View>
            </SoftCard>
          </Pressable>
        )}
        ItemSeparatorComponent={() => <View style={{ height: 10 }} />}
        ListEmptyComponent={<EmptyState title="Nothing in this month yet" detail="Choose another Hindu month to explore the bundled guide." />}
      />
    </ScreenContainer>
  );
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 36 },
  header: { gap: 14, marginBottom: 16 },
  subtitle: { fontSize: 14, lineHeight: 20 },
  chips: { gap: 8, paddingRight: 8 },
  resultLabel: { fontSize: 13, fontWeight: "700" },
  card: { gap: 13 },
  cardTop: { flexDirection: "row", alignItems: "center", gap: 12 },
  topCopy: { flex: 1, gap: 2 },
  month: { fontSize: 10, fontWeight: "900", letterSpacing: 1.1 },
  title: { fontSize: 17, lineHeight: 23, fontWeight: "800" },
  date: { fontSize: 13, lineHeight: 18 },
  significance: { fontSize: 14, lineHeight: 20 },
  sourceRow: { paddingTop: 10, borderTopWidth: StyleSheet.hairlineWidth },
  source: { fontSize: 11, lineHeight: 16 },
  pressed: { opacity: 0.72, transform: [{ scale: 0.985 }] },
});
