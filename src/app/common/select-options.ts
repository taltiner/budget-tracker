export interface SelectOptions {
  value: string, label: string
}

export const KATEGORIE_AUSGABE: SelectOptions[] = [
  {value: 'miete', label: 'Miete'},
  {value: 'strom', label: 'Strom'},
  {value: 'lebensmittel', label: 'Lebensmittel (Einkauf)'},
  {value: 'essenUnterwegs', label: 'Essen (Unterwegs)'},
  {value: 'transportOeffentliche', label: 'Transport Öffentliche'},
  {value: 'transportKfz', label: 'Transport Kfz'},
  {value: 'internet', label: 'Internet'},
  {value: 'mobilesInternet', label: 'Mobiles Internet'},
  {value: 'sport', label: 'Sport'},
  {value: 'sonstiges', label: 'Sonstiges'},
];

export const TRANSAKTION_JAHR: SelectOptions[] = [
  {value: '2023', label: '2023'},
  {value: '2024', label: '2024'},
  {value: '2025', label: '2025'},
];

export const TRANSAKTION_MONAT: SelectOptions[] = [
  {value: 'gesamt', label: 'Gesamt'},
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

export function getKategorieLabel(value: string): string {
  const kategorieLabel = KATEGORIE_AUSGABE.find((m) => m.value === value)?.label;

  return kategorieLabel ?? '';
}

export function getMonatLabel(value: string): string {
  const monaltLabel = TRANSAKTION_MONAT.find((m) => m.value === value)?.label;

  return monaltLabel ?? '';
}

export function getMonatValue(label: string): string {
  const monaltValue = TRANSAKTION_MONAT.find((m) => m.label === label)?.value;

  return monaltValue ?? '';
}
