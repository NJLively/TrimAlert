# 🚢 TrimAlert

_An unavoidable reminder to trim your sails, you lazy scallywag!_

TrimAlert is a RuneLite plugin that detects when your ship needs sail trimming and blasts you with visual and audio cues so you never miss a gust again. Whether you're daydreaming, chasing loot, or distracted by Discord, TrimAlert has your back — loudly.

## ⚡ Features

On-screen flashing overlay whenever your sail needs trimming
Sound alert (toggleable) using RuneLite’s built-in audio

## Configurable settings:

Enable/disable audio
Enable/disable visual flashing
Seamlessly integrates with OverlayManager and RuneLite’s event bus

## 🧭 How it Works

TrimAlert watches the in-game varbit value for your sail state and listens for chat messages related to sail trimming. When a gust hits, it:
Flashes the overlay
Plays an alert sound (optional)
Sends a RuneLite desktop notification
Keeps reminding you until you actually trim the sails

_Lazy sailors be warned._

## ⚙️ Configuration

Inside RuneLite (wrench icon → Plugin List → TrimAlert):
Enable visual alert – toggle the flashing overlay
Enable audio alert – toggle the loud ping