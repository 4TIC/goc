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

alter table goc_reuniones_puntos_orden_dia add (url_acta varchar2(1000), url_acta_alt varchar2(1000))