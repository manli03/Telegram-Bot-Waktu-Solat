# Telegram Bot for Prayer Times

## Description

A Java-based Telegram bot that gives Muslims in Malaysia today's prayer times for their area. Users pick their state and zone once, and the bot remembers it, so a single command returns the day's prayer times.

Prayer time data comes from JAKIM's [e-solat](https://www.e-solat.gov.my/) through the [waktusolat.app](https://api.waktusolat.app/) API.

### Features

- Prayer times for every state and zone in Malaysia
- Remembers each user's state, zone and language
- Available in English and Bahasa Melayu
- Commands in both languages:

| English | Bahasa Melayu | Action |
| --- | --- | --- |
| `/prayer_time` | `/waktu_solat` | Show today's prayer times |
| `/switch_state` | `/tukar_negeri` | Change state |
| `/switch_area` | `/tukar_kawasan` | Change area |
| `/change_language` | `/tukar_bahasa` | Change language |
| `/source` | `/sumber` | Show the data source |
| `/show_menu` | `/papar_menu` | Show the menu |

## License

This project is licensed under the MIT License.
