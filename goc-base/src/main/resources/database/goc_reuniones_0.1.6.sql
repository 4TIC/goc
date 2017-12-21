ALTER TABLE goc_organos_reuniones_miembros
  ADD (
  delegado_voto_id NUMBER,
  delegado_voto_nombre VARCHAR2(1000),
  delegado_voto_email VARCHAR2(200)
  );

ALTER TABLE goc_reuniones ADD;
(admite_delegacion_voto NUMBER (1) DEFAULT 0 NOT NULL);

CREATE OR REPLACE FORCE VIEW GOC_VW_REUNIONES_PERMISOS
(
    ID,
    COMPLETADA,
    FECHA,
    ASUNTO,
    ASUNTO_ALT,
    PERSONA_ID,
    PERSONA_NOMBRE,
    ASISTENTE
)
AS
  SELECT
    id,
    completada,
    fecha,
    asunto,
    asunto_alt,
    persona_id,
    persona_nombre,
    SUM(asistente) asistente
  FROM (SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          r.creador_id     persona_id,
          r.creador_nombre persona_nombre,
          0                asistente
        FROM goc_reuniones r
        UNION
        SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          ri.persona_id     persona_id,
          ri.persona_nombre persona_nombre,
          1                 asistente
        FROM goc_reuniones r, goc_reuniones_invitados ri
        WHERE ri.reunion_id = r.id
        UNION
        SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          oi.persona_id     persona_id,
          oi.persona_nombre persona_nombre,
          1                 asistente
        FROM goc_reuniones r,
          goc_organos_reuniones orr,
          goc_organos_invitados oi
        WHERE r.id = orr.reunion_id AND oi.organo_id = orr.organo_id
        UNION
        SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          orrm.suplente_id     persona_id,
          orrm.suplente_nombre persona_nombre,
          1                    asistente
        FROM goc_reuniones r,
          goc_organos_reuniones orr,
          goc_organos_reuniones_miembros orrm
        WHERE r.id = orr.reunion_id
              AND orr.id = orrm.organo_reunion_id
              AND orrm.asistencia = 1
              AND suplente_id IS NOT NULL
        UNION
        SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          orrm.miembro_id                 persona_id,
          orrm.nombre                     persona_nombre,
          DECODE(suplente_id, NULL, 1, 0) asistente
        FROM goc_reuniones r,
          goc_organos_reuniones orr,
          goc_organos_reuniones_miembros orrm
        WHERE r.id = orr.reunion_id
              AND orr.id = orrm.organo_reunion_id
              AND orrm.asistencia = 1
        UNION
        SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          orrm.miembro_id persona_id,
          orrm.nombre     persona_nombre,
          0               asistente
        FROM goc_reuniones r,
          goc_organos_reuniones orr,
          goc_organos_reuniones_miembros orrm
        WHERE r.id = orr.reunion_id
              AND orr.id = orrm.organo_reunion_id
              AND orrm.asistencia = 0
  )
  GROUP BY id,
    completada,
    fecha,
    asunto,
    asunto_alt,
    persona_id,
    persona_nombre;

ALTER TABLE goc_cargos
  ADD (codigo VARCHAR2(10));

ALTER TABLE GOC_CARGOS
  MODIFY (CODIGO  NOT NULL);

ALTER TABLE GOC_CARGOS
  ADD
  CONSTRAINT GOC_CARGOS_U01
  UNIQUE (CODIGO)
  ENABLE
  VALIDATE;


ALTER TABLE goc_organos_reuniones_miembros
  ADD (cargo_codigo VARCHAR2(10));

UPDATE goc_organos_reuniones_miembros
SET cargo_codigo = (SELECT codigo
                    FROM goc_cargos
                    WHERE id = cargo_id);

ALTER TABLE goc_organos_reuniones_miembros
  MODIFY (cargo_CODIGO  NOT NULL);

ALTER TABLE GOC_MIEMBROS add(
  CONSTRAINT GOC_MIEMBROS_R01
  UNIQUE (ORGANO_ID, CARGO_ID, PERSONA_ID))