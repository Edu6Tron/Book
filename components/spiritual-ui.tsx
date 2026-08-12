import { ReactNode } from "react";
import { Pressable, StyleSheet, Text, View, type StyleProp, type ViewStyle } from "react-native";
import { IconSymbol, type IconSymbolName } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";

export function SectionHeading({ eyebrow, title, actionLabel, onAction }: { eyebrow?: string; title: string; actionLabel?: string; onAction?: () => void }) {
  const colors = useColors();
  return (
    <View style={styles.sectionHeading}>
      <View style={styles.sectionHeadingText}>
        {eyebrow ? <Text style={[styles.eyebrow, { color: colors.primary }]}>{eyebrow.toUpperCase()}</Text> : null}
        <Text style={[styles.sectionTitle, { color: colors.foreground }]}>{title}</Text>
      </View>
      {actionLabel && onAction ? (
        <Pressable onPress={onAction} style={({ pressed }) => [styles.textAction, pressed && styles.pressed]}>
          <Text style={[styles.textActionLabel, { color: colors.primary }]}>{actionLabel}</Text>
          <IconSymbol name="chevron.right" size={18} color={colors.primary} />
        </Pressable>
      ) : null}
    </View>
  );
}

export function IconCircle({ name, color, background }: { name: IconSymbolName; color: string; background: string }) {
  return (
    <View style={[styles.iconCircle, { backgroundColor: background }]}>
      <IconSymbol name={name} size={22} color={color} />
    </View>
  );
}

export function Pill({ label, selected, onPress }: { label: string; selected?: boolean; onPress?: () => void }) {
  const colors = useColors();
  return (
    <Pressable
      disabled={!onPress}
      onPress={onPress}
      style={({ pressed }) => [
        styles.pill,
        { borderColor: selected ? colors.primary : colors.border, backgroundColor: selected ? colors.primary : colors.surface },
        pressed && onPress ? styles.pressed : undefined,
      ]}
    >
      <Text style={[styles.pillText, { color: selected ? colors.background : colors.muted }]}>{label}</Text>
    </Pressable>
  );
}

export function SoftCard({ children, style }: { children: ReactNode; style?: StyleProp<ViewStyle> }) {
  const colors = useColors();
  return <View style={[styles.card, { backgroundColor: colors.surface, borderColor: colors.border }, style]}>{children}</View>;
}

export function EmptyState({ title, detail }: { title: string; detail: string }) {
  const colors = useColors();
  return (
    <View style={styles.emptyState}>
      <IconCircle name="sparkles" color={colors.primary} background={`${colors.primary}18`} />
      <Text style={[styles.emptyTitle, { color: colors.foreground }]}>{title}</Text>
      <Text style={[styles.emptyDetail, { color: colors.muted }]}>{detail}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  sectionHeading: { flexDirection: "row", alignItems: "flex-end", justifyContent: "space-between", marginBottom: 12 },
  sectionHeadingText: { gap: 3 },
  eyebrow: { fontSize: 11, fontWeight: "800", letterSpacing: 1.1 },
  sectionTitle: { fontSize: 22, lineHeight: 28, fontWeight: "800", letterSpacing: -0.35 },
  textAction: { flexDirection: "row", alignItems: "center", gap: 2, paddingVertical: 4, paddingLeft: 8 },
  textActionLabel: { fontSize: 14, fontWeight: "700" },
  iconCircle: { width: 44, height: 44, borderRadius: 22, alignItems: "center", justifyContent: "center" },
  pill: { minHeight: 34, borderWidth: 1, borderRadius: 17, paddingHorizontal: 13, alignItems: "center", justifyContent: "center" },
  pillText: { fontSize: 13, fontWeight: "700" },
  card: { borderWidth: StyleSheet.hairlineWidth, borderRadius: 18, padding: 16 },
  pressed: { opacity: 0.72, transform: [{ scale: 0.98 }] },
  emptyState: { paddingVertical: 48, alignItems: "center", gap: 10 },
  emptyTitle: { fontSize: 17, fontWeight: "800" },
  emptyDetail: { maxWidth: 280, textAlign: "center", fontSize: 14, lineHeight: 20 },
});
