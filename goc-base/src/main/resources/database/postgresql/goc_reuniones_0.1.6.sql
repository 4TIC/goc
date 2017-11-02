ALTER TABLE goc.goc_organos_reuniones_miembros
  ADD COLUMN delegado_voto_id DOUBLE PRECISION,
  ADD COLUMN delegado_voto_nombre VARCHAR(1000),
  ADD COLUMN delegado_voto_email VARCHAR(200);

ALTER TABLE goc.goc_reuniones
  ADD COLUMN admite_delegacion_voto BOOLEAN DEFAULT FALSE NOT NULL;

ALTER TABLE goc.goc_cargos
  ADD COLUMN codigo VARCHAR(10);

ALTER TABLE goc.GOC_CARGOS
  ALTER COLUMN CODIGO SET NOT NULL;

ALTER TABLE goc.GOC_CARGOS
  ADD
  CONSTRAINT GOC_CARGOS_U01
  UNIQUE (CODIGO);

ALTER TABLE goc.goc_organos_reuniones_miembros
  ADD COLUMN cargo_codigo VARCHAR(10);

UPDATE goc_organos_reuniones_miembros
SET cargo_codigo = (SELECT codigo
                    FROM goc_cargos
                    WHERE TEXT(id) = cargo_id);

ALTER TABLE goc.goc_organos_reuniones_miembros
  ALTER COLUMN cargo_CODIGO SET NOT NULL;

ALTER TABLE goc.GOC_MIEMBROS
  ADD
  CONSTRAINT GOC_MIEMBROS_R01
  UNIQUE (ORGANO_ID, CARGO_ID, PERSONA_ID);