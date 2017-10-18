ALTER TABLE goc_reuniones
  ADD (url_acta VARCHAR2(1000), url_acta_alt VARCHAR2(1000));

CREATE OR REPLACE FORCE VIEW UJI_REUNIONES.GOC_VW_REUNIONES_PERMISOS
(
    ID,
    COMPLETADA,
    FECHA,
    ASUNTO,
    ASUNTO_ALT,
    PERSONA_ID,
    PERSONA_NOMBRE,
    ASISTENTE,
    URL_ACTA,
    URL_ACTA_ALT
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
    SUM(asistente) asistente,
    url_acta,
    url_acta_alt
  FROM (SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          r.creador_id     persona_id,
          r.creador_nombre persona_nombre,
          0                asistente,
          r.url_acta,
          r.url_acta_alt
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
          1                 asistente,
          r.url_acta,
          r.url_acta_alt
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
          1                 asistente,
          r.url_acta,
          r.url_acta_alt
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
          1                    asistente,
          r.url_acta,
          r.url_acta_alt
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
          DECODE(suplente_id, NULL, 1, 0) asistente,
          r.url_acta,
          r.url_acta_alt
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
          0               asistente,
          r.url_acta,
          r.url_acta_alt
        FROM goc_reuniones r,
          goc_organos_reuniones orr,
          goc_organos_reuniones_miembros orrm
        WHERE r.id = orr.reunion_id
              AND orr.id = orrm.organo_reunion_id
              AND orrm.asistencia = 0)
  GROUP BY id,
    completada,
    fecha,
    asunto,
    asunto_alt,
    persona_id,
    persona_nombre,
    url_acta,
    url_acta_alt;

ALTER TABLE goc_reuniones_puntos_orden_dia
  ADD (url_acta VARCHAR2(1000), url_acta_alt VARCHAR2(1000));

ALTER TABLE goc_organos_reuniones_miembros
  ADD (url_asistencia VARCHAR2(1000), url_asistencia_alt VARCHAR2(1000));

ALTER TABLE goc_reuniones_invitados
  ADD (url_asistencia VARCHAR2(1000), url_asistencia_alt VARCHAR2(1000));

CREATE TABLE UJI_REUNIONES.GOC_ORGANOS_REUNIONES_INVITS
(
  ID                 NUMBER,
  ORGANO_REUNION_ID  NUMBER             NOT NULL,
  ORGANO_EXTERNO     NUMBER             NOT NULL,
  REUNION_ID         NUMBER             NOT NULL,
  ORGANO_ID          VARCHAR2(400 BYTE) NOT NULL,
  NOMBRE             VARCHAR2(500 BYTE) NOT NULL,
  EMAIL              VARCHAR2(200 BYTE) NOT NULL,
  PERSONA_ID         NUMBER             NOT NULL,
  URL_ASISTENCIA     VARCHAR2(1000 BYTE),
  URL_ASISTENCIA_ALT VARCHAR2(1000 BYTE)
);

ALTER TABLE UJI_REUNIONES.GOC_ORGANOS_REUNIONES_INVITS
  ADD
  CONSTRAINT GOC_ORGANOS_REUNIONES_INVI_R01
  FOREIGN KEY (ORGANO_REUNION_ID)
  REFERENCES UJI_REUNIONES.GOC_ORGANOS_REUNIONES (ID)
  ENABLE
  VALIDATE;

CREATE OR REPLACE VIEW goc_vw_certificados_asistencia AS
  SELECT
    ri.reunion_id,
    ri.persona_id,
    ri.url_asistencia,
    ri.url_asistencia_alt
  FROM goc_reuniones_invitados ri
  WHERE ri.url_asistencia IS NOT NULL
  UNION
  SELECT
    roi.reunion_id,
    roi.persona_id,
    roi.url_asistencia,
    roi.url_asistencia_alt
  FROM goc_organos_reuniones_invits roi
  WHERE roi.url_asistencia IS NOT NULL
  UNION
  SELECT
    r.id             reunion_id,
    orrm.suplente_id persona_id,
    orrm.url_asistencia,
    orrm.url_asistencia_alt
  FROM goc_reuniones r,
    goc_organos_reuniones orr,
    goc_organos_reuniones_miembros orrm
  WHERE r.id = orr.reunion_id
        AND orr.id = orrm.organo_reunion_id
        AND orrm.asistencia = 1
        AND suplente_id IS NOT NULL
        AND orrm.url_asistencia IS NOT NULL
  UNION
  SELECT
    r.id            reunion_id,
    orrm.miembro_id persona_id,
    orrm.url_asistencia,
    orrm.url_asistencia_alt
  FROM goc_reuniones r,
    goc_organos_reuniones orr,
    goc_organos_reuniones_miembros orrm
  WHERE r.id = orr.reunion_id
        AND orr.id = orrm.organo_reunion_id
        AND orrm.asistencia = 1
        AND suplente_id IS NULL
        AND orrm.url_asistencia IS NOT NULL;


CREATE OR REPLACE FORCE VIEW UJI_REUNIONES.GOC_VW_REUNIONES_PERMISOS
(
    ID,
    COMPLETADA,
    FECHA,
    ASUNTO,
    ASUNTO_ALT,
    PERSONA_ID,
    PERSONA_NOMBRE,
    ASISTENTE,
    URL_ACTA,
    URL_ACTA_ALT,
    URL_ASISTENCIA,
    URL_ASISTENCIA_ALT
)
AS
  SELECT
    s.id,
    s.completada,
    s.fecha,
    s.asunto,
    s.asunto_alt,
    s.persona_id,
    s.persona_nombre,
    SUM(s.asistente)                                                           asistente,
    s.url_acta,
    s.url_acta_alt,
    (SELECT a.url_asistencia
     FROM goc_vw_certificados_asistencia a
     WHERE a.reunion_id = s.id AND a.persona_id = s.persona_id AND rownum = 1) url_asistencia,
    (SELECT a.url_asistencia_alt
     FROM goc_vw_certificados_asistencia a
     WHERE a.reunion_id = s.id AND a.persona_id = s.persona_id AND rownum = 1) url_asistencia_alt
  FROM (SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          r.creador_id     persona_id,
          r.creador_nombre persona_nombre,
          0                asistente,
          r.url_acta,
          r.url_acta_alt
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
          1                 asistente,
          r.url_acta,
          r.url_acta_alt
        FROM goc_reuniones r, goc_reuniones_invitados ri
        WHERE ri.reunion_id = r.id
        UNION
        SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          ori.persona_id persona_id,
          ori.nombre     persona_nombre,
          1              asistente,
          r.url_acta,
          r.url_acta_alt
        FROM goc_reuniones r,
          goc_organos_reuniones orr,
          goc_organos_reuniones_invits ori
        WHERE r.id = orr.reunion_id
              AND ori.organo_reunion_id = orr.id
        UNION
        SELECT
          r.id,
          r.completada,
          r.fecha,
          r.asunto,
          r.asunto_alt,
          orrm.suplente_id     persona_id,
          orrm.suplente_nombre persona_nombre,
          1                    asistente,
          r.url_acta,
          r.url_acta_alt
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
          DECODE(suplente_id, NULL, 1, 0) asistente,
          r.url_acta,
          r.url_acta_alt
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
          0               asistente,
          r.url_acta,
          r.url_acta_alt
        FROM goc_reuniones r,
          goc_organos_reuniones orr,
          goc_organos_reuniones_miembros orrm
        WHERE r.id = orr.reunion_id
              AND orr.id = orrm.organo_reunion_id
              AND orrm.asistencia = 0) s
  GROUP BY s.id,
    s.completada,
    s.fecha,
    s.asunto,
    s.asunto_alt,
    s.persona_id,
    s.persona_nombre,
    s.url_acta,
    s.url_acta_alt;

ALTER TABLE goc_reuniones
  ADD (aviso_primera_reunion_user VARCHAR2(100), aviso_primera_reunion_fecha DATE);

CREATE OR REPLACE FORCE VIEW UJI_REUNIONES.GOC_VW_REUNIONES_EDITORES
(
    ID,
    ASUNTO,
    ASUNTO_ALT,
    FECHA,
    DURACION,
    NUM_DOCUMENTOS,
    EDITOR_ID,
    COMPLETADA,
    EXTERNO,
    ORGANO_ID,
    TIPO_ORGANO_ID,
    AVISO_PRIMERA_REUNION,
    AVISO_PRIMERA_REUNION_USER,
    AVISO_PRIMERA_REUNION_FECHA
)
AS
  SELECT r.id,
    r.asunto,
    r.asunto_alt,
    r.fecha,
    r.duracion,
    (SELECT COUNT (*)
     FROM goc_reuniones_documentos rd
     WHERE rd.reunion_id = r.id)
                 num_documentos,
    r.creador_id editor_id,
    r.completada completada,
    o.externo,
    o.organo_id,
    o.tipo_organo_id,
    r.aviso_primera_reunion,
    r.aviso_primera_reunion_user,
    r.aviso_primera_reunion_fecha
  FROM goc_reuniones                  r,
    goc_organos_reuniones          o,
    goc_organos_reuniones_miembros orm
  WHERE r.id = o.reunion_id(+) AND o.id = orm.organo_reunion_id(+);

ALTER TABLE UJI_REUNIONES.GOC_REUNIONES_PUNTOS_ORDEN_DIA
  MODIFY(DESCRIPCION  NULL)
/