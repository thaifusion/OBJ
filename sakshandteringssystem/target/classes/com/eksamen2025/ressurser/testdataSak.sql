-- testdataSak.sql 
-- Testdata for tabellen sak i sakssystemet

INSERT INTO sak (
  tittel,
  beskrivelse,
  prioritet_id,
  kategori_id,
  status_id,
  reporter_id,
  mottaker_id,
  opprettetTid,
  oppdatertTid,
  utviklerkommentar,
  testerTilbakemelding
) VALUES
  (
    'Knapp svarer ikke',
    'Svar-knappen i dialogvinduet gjør ingenting når man klikker.',
    1,  -- prioritet_id = LAV
    1,  -- kategori_id = UI-feil
    1,  -- status_id = PÅGÅR
    1,  -- reporter_id = tester1
    2,  -- mottaker_id = utvikler1
    '2025-05-20 10:00:00',
    '2025-05-20 10:00:00',
    NULL,
    NULL
  ),
  (
    '500-feil i API',
    'API-et gir HTTP 500 når vi prøver å hente brukerdata.',
    3,  -- prioritet_id = HØY
    2,  -- kategori_id = Backend-feil
    1,  -- status_id = PÅGÅR
    1,
    2,
    '2025-05-19 14:30:00',
    '2025-05-19 14:30:00',
    'Jobber med feilen',
    NULL
  ),
  (
    'Ønsker søkefunksjon',
    'Må kunne søke på saker basert på prioritet og status.',
    2,  -- prioritet_id = MIDDELS
    3,  -- kategori_id = Funksjonsforespørsel
    2,  -- status_id = LØST
    1,
    2,
    '2025-05-18 09:15:00',
    '2025-05-19 11:45:00',
    'Implementert prototype',
    'Test OK'
  ),
  (
    'UI-overlapping mobil',
    'Elementer overlapper når skjermbredden er <400px.',
    2,
    1,
    1,
    1,
    2,
    '2025-05-17 16:20:00',
    '2025-05-17 16:20:00',
    NULL,
    NULL
  ),
  (
    'Database-låsproblem',
    'Deadlock oppstår ved høy samtidighet under batchkjøring.',
    3,
    2,
    1,
    1,
    2,
    '2025-05-16 12:45:00',
    '2025-05-16 12:45:00',
    'Trenger å tune transaksjoner',
    NULL
  );
