import { Alert, Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { ScreenContainer } from "@/components/screen-container";
import { IconCircle, SectionHeading, SoftCard } from "@/components/spiritual-ui";
import { IconSymbol } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";
import { haptic } from "@/lib/haptics";
import { useSpiritualStore } from "@/lib/spiritual-store";

const rituals = [
  { id: "pause", title: "Pause for one minute", detail: "Settle your attention with three unhurried breaths." },
  { id: "reading", title: "Read one meaningful line", detail: "Choose an Aarti or a festival reflection that feels relevant today." },
  { id: "kindness", title: "Offer one act of kindness", detail: "Make your practice visible through a small, deliberate act." },
];

export default function PracticeScreen() {
  const colors = useColors();
  const { completedPracticeIds, japaCount, togglePractice, incrementJapa, resetJapa } = useSpiritualStore();
  const reachMilestone = japaCount > 0 && japaCount % 108 === 0;

  return (
    <ScreenContainer>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <SectionHeading eyebrow="Daily ritual" title="Practice with steadiness" />
        <Text style={[styles.subtitle, { color: colors.muted }]}>A quiet structure for the day. Your ritual and Japa count are stored only on this device.</Text>

        <SoftCard style={[styles.japaCard, { backgroundColor: colors.foreground, borderColor: colors.foreground }]}>
          <View style={styles.japaTop}>
            <View>
              <Text style={[styles.japaEyebrow, { color: colors.warning }]}>JAPA MALA</Text>
              <Text style={[styles.japaCount, { color: colors.background }]}>{japaCount}</Text>
              <Text style={[styles.japaCaption, { color: `${colors.background}B8` }]}>{reachMilestone ? "108 repetitions complete" : `${Math.max(108 - (japaCount % 108), 0)} to your next 108`}</Text>
            </View>
            <IconCircle name="sparkles" color={colors.warning} background={`${colors.background}15`} />
          </View>
          <Pressable onPress={() => { haptic.medium(); incrementJapa(); }} style={({ pressed }) => [styles.countButton, { backgroundColor: colors.warning }, pressed && styles.pressed]}>
            <IconSymbol name="plus" size={20} color={colors.foreground} />
            <Text style={[styles.countButtonLabel, { color: colors.foreground }]}>Count one repetition</Text>
          </Pressable>
          <Pressable onPress={() => Alert.alert("Reset Japa count?", "This clears the locally stored count for this practice session.", [{ text: "Cancel", style: "cancel" }, { text: "Reset", style: "destructive", onPress: () => { haptic.light(); resetJapa(); } }])} style={({ pressed }) => [styles.resetButton, pressed && styles.pressed]}>
            <IconSymbol name="arrow.counterclockwise" size={16} color={`${colors.background}B8`} />
            <Text style={[styles.resetLabel, { color: `${colors.background}B8` }]}>Reset count</Text>
          </Pressable>
        </SoftCard>

        <SectionHeading eyebrow="Three simple steps" title="Today’s ritual" />
        <View style={styles.ritualList}>
          {rituals.map((ritual) => {
            const complete = completedPracticeIds.includes(ritual.id);
            return (
              <Pressable key={ritual.id} onPress={() => { haptic.medium(); togglePractice(ritual.id); if (!complete) haptic.success(); }} style={({ pressed }) => [pressed && styles.pressed]}>
                <SoftCard style={styles.ritualCard}>
                  <View style={styles.ritualRow}>
                    <IconSymbol name={complete ? "checkmark.circle.fill" : "circle"} size={27} color={complete ? colors.success : colors.muted} />
                    <View style={styles.ritualCopy}>
                      <Text style={[styles.ritualTitle, { color: colors.foreground, textDecorationLine: complete ? "line-through" : "none" }]}>{ritual.title}</Text>
                      <Text style={[styles.ritualDetail, { color: colors.muted }]}>{ritual.detail}</Text>
                    </View>
                  </View>
                </SoftCard>
              </Pressable>
            );
          })}
        </View>
      </ScrollView>
    </ScreenContainer>
  );
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 36, gap: 20 },
  subtitle: { marginTop: -12, fontSize: 14, lineHeight: 21 },
  japaCard: { gap: 18, padding: 20 },
  japaTop: { flexDirection: "row", justifyContent: "space-between", alignItems: "flex-start" },
  japaEyebrow: { fontSize: 11, fontWeight: "900", letterSpacing: 1.2 },
  japaCount: { marginTop: 4, fontSize: 53, lineHeight: 59, fontWeight: "900", letterSpacing: -1.5 },
  japaCaption: { marginTop: 2, fontSize: 13, fontWeight: "600" },
  countButton: { height: 48, borderRadius: 14, alignItems: "center", justifyContent: "center", flexDirection: "row", gap: 8 },
  countButtonLabel: { fontSize: 15, fontWeight: "900" },
  resetButton: { alignSelf: "center", flexDirection: "row", alignItems: "center", gap: 6, padding: 5 },
  resetLabel: { fontSize: 13, fontWeight: "700" },
  ritualList: { gap: 10 },
  ritualCard: { padding: 15 },
  ritualRow: { flexDirection: "row", alignItems: "center", gap: 13 },
  ritualCopy: { flex: 1, gap: 3 },
  ritualTitle: { fontSize: 16, lineHeight: 21, fontWeight: "800" },
  ritualDetail: { fontSize: 13, lineHeight: 19 },
  pressed: { opacity: 0.74, transform: [{ scale: 0.985 }] },
});
