export interface SelectOptions {
  value: string, label: string
}

export const KATEGORIE_AUSGABE: SelectOptions[] = [
  {value: 'miete', label: 'Miete'},
  {value: 'strom', label: 'Strom'},
  {value: 'lebensmittel', label: 'Lebensmittel'},
];

export const TRANSAKTION_JAHR: SelectOptions[] = [
  {value: '2023', label: '2023'},
  {value: '2024', label: '2024'},
  {value: '2025', label: '2025'},
];

export const TRANSAKTION_MONAT: SelectOptions[] = [
  {value: 'januar', label: 'Januar'},
  {value: 'februar', label: 'Februar'},
  {value: 'märz', label: 'März'},
  {value: 'april', label: 'April'},
  {value: 'mai', label: 'Mai'},
  {value: 'juni', label: 'Juni'},
  {value: 'juli', label: 'Juli'},
  {value: 'august', label: 'August'},
  {value: 'september', label: 'September'},
  {value: 'oktober', label: 'Oktober'},
  {value: 'november', label: 'November'},
  {value: 'dezember', label: 'Dezember'},
];
