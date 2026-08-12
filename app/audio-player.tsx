import { useEffect, useMemo, useState } from "react";
import { Alert, Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { Stack } from "expo-router";
import * as DocumentPicker from "expo-document-picker";
import { setAudioModeAsync, useAudioPlayer, useAudioPlayerStatus } from "expo-audio";
import { ScreenContainer } from "@/components/screen-container";
import { IconCircle, SoftCard } from "@/components/spiritual-ui";
import { IconSymbol } from "@/components/ui/icon-symbol";
import { useColors } from "@/hooks/use-colors";
import { haptic } from "@/lib/haptics";

type LocalTrack = {
  name: string;
  uri: string;
  size?: number;
};

export default function AudioPlayerScreen() {
  const colors = useColors();
  const [track, setTrack] = useState<LocalTrack | null>(null);
  const player = useAudioPlayer(track?.uri, { updateInterval: 500 });
  const status = useAudioPlayerStatus(player);

  useEffect(() => {
    void setAudioModeAsync({ playsInSilentMode: true });
  }, []);

  const progress = useMemo(() => {
    if (!status.duration || status.duration <= 0) return 0;
    return Math.min(status.currentTime / status.duration, 1);
  }, [status.currentTime, status.duration]);

  const chooseAudio = async () => {
    try {
      const selection = await DocumentPicker.getDocumentAsync({
        type: ["audio/*"],
        copyToCacheDirectory: true,
        multiple: false,
      });
      if (selection.canceled || !selection.assets[0]) return;
      const asset = selection.assets[0];
      haptic.success();
      setTrack({ name: asset.name, uri: asset.uri, size: asset.size });
    } catch {
      Alert.alert("Unable to select audio", "Please choose a supported audio file from your device and try again.");
    }
  };

  const togglePlayback = () => {
    if (!track) {
      void chooseAudio();
      return;
    }
    try {
      if (status.playing) {
        player.pause();
      } else {
        if (status.duration > 0 && status.currentTime >= status.duration) player.seekTo(0);
        player.play();
      }
      haptic.light();
    } catch {
      Alert.alert("Playback unavailable", "The selected file could not be played. Please choose another audio file.");
    }
  };

  return (
    <ScreenContainer>
      <Stack.Screen options={{ title: "Media player", headerBackTitle: "Aartis" }} />
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <IconCircle name="music.note.list" color={colors.primary} background={`${colors.primary}18`} />
        <Text style={[styles.title, { color: colors.foreground }]}>Your devotional media player</Text>
        <Text style={[styles.subtitle, { color: colors.muted }]}>Play an audio recording you own, have permission to use, or have already downloaded lawfully to your device. The file stays local to your device.</Text>

        <SoftCard style={[styles.playerCard, { backgroundColor: colors.foreground, borderColor: colors.foreground }]}>
          <View style={styles.trackTop}>
            <IconCircle name="music.note.list" color={colors.warning} background={`${colors.background}15`} />
            <View style={styles.trackCopy}>
              <Text numberOfLines={2} style={[styles.trackName, { color: colors.background }]}>{track?.name ?? "No audio selected"}</Text>
              <Text style={[styles.trackDetail, { color: `${colors.background}B8` }]}>{track ? `${formatTime(status.currentTime)} / ${formatTime(status.duration)}` : "Choose a local devotional recording to begin"}</Text>
            </View>
          </View>
          <View style={[styles.progressTrack, { backgroundColor: `${colors.background}2B` }]}>
            <View style={[styles.progressFill, { backgroundColor: colors.warning, width: `${progress * 100}%` }]} />
          </View>
          <Pressable onPress={togglePlayback} style={({ pressed }) => [styles.playButton, { backgroundColor: colors.warning }, pressed && styles.pressed]}>
            <IconSymbol name={status.playing ? "pause.fill" : "play.fill"} size={22} color={colors.foreground} />
            <Text style={[styles.playLabel, { color: colors.foreground }]}>{status.playing ? "Pause" : track ? "Play" : "Choose audio"}</Text>
          </Pressable>
        </SoftCard>

        <Pressable onPress={() => void chooseAudio()} style={({ pressed }) => [styles.importButton, { backgroundColor: colors.surface, borderColor: colors.border }, pressed && styles.pressed]}>
          <IconSymbol name="folder.fill" size={20} color={colors.primary} />
          <View style={styles.importCopy}>
            <Text style={[styles.importTitle, { color: colors.foreground }]}>Choose from this device</Text>
            <Text style={[styles.importDetail, { color: colors.muted }]}>Supported audio files are copied to a safe temporary playback location.</Text>
          </View>
          <IconSymbol name="chevron.right" size={18} color={colors.primary} />
        </Pressable>

        <View style={[styles.note, { borderColor: colors.border }]}>
          <Text style={[styles.noteLabel, { color: colors.muted }]}>ABOUT DISCOVERED VIDEO</Text>
          <Text style={[styles.noteBody, { color: colors.muted }]}>Video discovery opens authorised provider results in a secure in-app browser. This app does not download or extract provider video or audio streams.</Text>
        </View>
      </ScrollView>
    </ScreenContainer>
  );
}

function formatTime(value: number) {
  if (!Number.isFinite(value) || value <= 0) return "0:00";
  const minutes = Math.floor(value / 60);
  const seconds = Math.floor(value % 60).toString().padStart(2, "0");
  return `${minutes}:${seconds}`;
}

const styles = StyleSheet.create({
  content: { padding: 20, paddingBottom: 38, gap: 16 },
  title: { fontSize: 28, lineHeight: 34, fontWeight: "900", letterSpacing: -0.6 },
  subtitle: { marginTop: -7, fontSize: 14, lineHeight: 21 },
  playerCard: { gap: 18, padding: 20 },
  trackTop: { flexDirection: "row", alignItems: "center", gap: 12 },
  trackCopy: { flex: 1, gap: 3 },
  trackName: { fontSize: 17, lineHeight: 23, fontWeight: "800" },
  trackDetail: { fontSize: 13, lineHeight: 18 },
  progressTrack: { height: 7, borderRadius: 4, overflow: "hidden" },
  progressFill: { height: 7, borderRadius: 4 },
  playButton: { height: 50, borderRadius: 14, alignItems: "center", justifyContent: "center", flexDirection: "row", gap: 8 },
  playLabel: { fontSize: 15, fontWeight: "900" },
  importButton: { minHeight: 72, borderWidth: StyleSheet.hairlineWidth, borderRadius: 17, padding: 14, flexDirection: "row", alignItems: "center", gap: 11 },
  importCopy: { flex: 1, gap: 3 },
  importTitle: { fontSize: 15, fontWeight: "800" },
  importDetail: { fontSize: 12, lineHeight: 17 },
  note: { borderTopWidth: StyleSheet.hairlineWidth, paddingTop: 16, gap: 5 },
  noteLabel: { fontSize: 10, fontWeight: "900", letterSpacing: 1.1 },
  noteBody: { fontSize: 13, lineHeight: 20 },
  pressed: { opacity: 0.74, transform: [{ scale: 0.985 }] },
});
