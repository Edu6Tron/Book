import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { Stack, router, useLocalSearchParams } from "expo-router";
import { ScreenContainer } from "@/components/screen-container";
import { IconCircle, SoftCard } from "@/components/spiritual-ui";
import { useColors } from "@/hooks/use-colors";
import { festivals } from "@/lib/spiritual-data";

export default function FestivalDetailScreen() {
  const colors = useColors();
  const { id } = useLocalSearchParams<{ id: string }>();
  const festival = festivals.find((item) => item.id === id);
  if (!festival) {
    return (
      <ScreenContainer className="p-5">
        <Text style={[styles.errorTitle, { color: colors.foreground }]}>This festival is not available.</Text>
        <Pressable onPress={() => router.back()}><Text style={[styles.backLink, { color: colors.primary }]}>Return to calendar</Text></Pressable>
      </ScreenContainer>
    );
  }
  return (
    <ScreenContainer>
      <Stack.Screen options={{ title: "Festival", headerBackTitle: "Calendar" }} />
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <IconCircle name="calendar" color={colors.warning} background={`${colors.warning}1D`} />
        <Text style={[styles.month, { color: colors.primary }]}>{festival.hinduMonth.toUpperCase()}</Text>
        <Text style={[styles.title, { color: colors.foreground }]}>{festival.name}</Text>
        <Text style={[styles.meta, { color: colors.muted }]}>{festival.dateNote} · {festival.deity}</Text>
        <SoftCard style={styles.card}>
          <Text style={[styles.label, { color: colors.primary }]}>WHY IT IS OBSERVED</Text>
          <Text style={[styles.body, { color: colors.foreground }]}>{festival.significance}</Text>
        </SoftCard>
        <SoftCard style={styles.card}>
          <Text style={[styles.label, { color: colors.primary }]}>A GENTLE OBSERVANCE</Text>
          <Text style={[styles.body, { color: colors.foreground }]}>{festival.observance}</Text>
        </SoftCard>
        <View style={[styles.note, { borderColor: colors.border }]}>
          <Text style={[styles.noteLabel, { color: colors.muted }]}>CALENDAR NOTE</Text>
          <Text style={[styles.noteText, { color: colors.muted }]}>Lunar dates and local practice can differ by region. Confirm the timing with a local panchang before planning a fast or ritual.</Text>
          <Text style={[styles.source, { color: colors.foreground }]}>Source · {festival.source}</Text>
        </View>
      </ScrollView>
    </ScreenContainer>
  );
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 38, gap: 14 },
  month: { marginTop: 6, fontSize: 11, fontWeight: "900", letterSpacing: 1.2 },
  title: { fontSize: 30, lineHeight: 36, fontWeight: "900", letterSpacing: -0.8 },
  meta: { marginTop: -8, fontSize: 14, lineHeight: 20, fontWeight: "600" },
  card: { gap: 8 },
  label: { fontSize: 11, fontWeight: "900", letterSpacing: 1.1 },
  body: { fontSize: 16, lineHeight: 24, fontWeight: "600" },
  note: { borderTopWidth: StyleSheet.hairlineWidth, paddingTop: 16, gap: 6 },
  noteLabel: { fontSize: 10, fontWeight: "900", letterSpacing: 1.1 },
  noteText: { fontSize: 13, lineHeight: 20 },
  source: { marginTop: 3, fontSize: 13, lineHeight: 19, fontWeight: "700" },
  errorTitle: { fontSize: 22, fontWeight: "800" },
  backLink: { marginTop: 12, fontSize: 15, fontWeight: "800" },
});
