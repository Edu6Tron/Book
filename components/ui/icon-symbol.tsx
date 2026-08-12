// Fallback for using MaterialIcons on Android and web.

import MaterialIcons from "@expo/vector-icons/MaterialIcons";
import { SymbolWeight } from "expo-symbols";
import { ComponentProps } from "react";
import { OpaqueColorValue, type StyleProp, type TextStyle } from "react-native";

type MaterialIconName = ComponentProps<typeof MaterialIcons>["name"];

const MAPPING = {
  "house.fill": "home",
  "paperplane.fill": "send",
  "chevron.left.forwardslash.chevron.right": "code",
  "chevron.right": "chevron-right",
  "music.note.list": "library-music",
  "play.fill": "play-arrow",
  "pause.fill": "pause",
  "folder.fill": "folder",
  "calendar": "calendar-month",
  "figure.mind.and.body": "self-improvement",
  "building.columns": "account-balance",
  "map.fill": "map",
  "location.fill": "location-on",
  "heart.fill": "favorite",
  "heart": "favorite-border",
  "book.fill": "menu-book",
  "clock.fill": "schedule",
  "sparkles": "auto-awesome",
  "checkmark.circle.fill": "check-circle",
  "circle": "radio-button-unchecked",
  "plus": "add",
  "arrow.counterclockwise": "restart-alt",
  "sliders.horizontal.3": "tune",
} as const satisfies Record<string, MaterialIconName>;

export type IconSymbolName = keyof typeof MAPPING;

export function IconSymbol({
  name,
  size = 24,
  color,
  style,
}: {
  name: IconSymbolName;
  size?: number;
  color: string | OpaqueColorValue;
  style?: StyleProp<TextStyle>;
  weight?: SymbolWeight;
}) {
  return <MaterialIcons color={color} size={size} name={MAPPING[name]} style={style} />;
}
