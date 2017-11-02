CREATE OR REPLACE VIEW goc.goc_vw_certificados_asistencia AS
  ((SELECT
      ri.reunion_id,
      ri.persona_id,
      ri.url_asistencia,
      ri.url_asistencia_alt
    FROM goc_reuniones_invitados ri
    WHERE (ri.url_asistencia IS NOT NULL)
    UNION SELECT
            roi.reunion_id,
            (roi.persona_id) :: DOUBLE PRECISION AS persona_id,
            roi.url_asistencia,
            roi.url_asistencia_alt
          FROM goc_organos_reuniones_invits roi
          WHERE (roi.url_asistencia IS NOT NULL))
   UNION SELECT
           r.id             AS reunion_id,
           orrm.suplente_id AS persona_id,
           orrm.url_asistencia,
           orrm.url_asistencia_alt
         FROM goc_reuniones r, goc_organos_reuniones orr,
           goc_organos_reuniones_miembros orrm
         WHERE (((((r.id = orr.reunion_id) AND
                   (orr.id = orrm.organo_reunion_id)) AND
                  (orrm.asistencia IS TRUE)) AND
                 (orrm.suplente_id IS NOT NULL)) AND
                (orrm.url_asistencia IS NOT NULL)))
  UNION SELECT
          r.id                                  AS reunion_id,
          (orrm.miembro_id) :: DOUBLE PRECISION AS persona_id,
          orrm.url_asistencia,
          orrm.url_asistencia_alt
        FROM goc_reuniones r, goc_organos_reuniones orr,
          goc_organos_reuniones_miembros orrm
        WHERE (((((r.id = orr.reunion_id) AND
                  (orr.id = orrm.organo_reunion_id)) AND
                 (orrm.asistencia IS TRUE)) AND (orrm.suplente_id IS NULL))
               AND (orrm.url_asistencia IS NOT NULL));

CREATE OR REPLACE VIEW goc.goc_vw_reuniones_busqueda AS
  SELECT
    ((r.id || '-' :: TEXT) || p.id)                            AS id,
    r.id                                                       AS reunion_id,
    r.asunto                                                   AS asunto_reunion,
    r.descripcion                                              AS descripcion_reunion,
    r.asunto_alt                                               AS asunto_alt_reunion,
    r.descripcion_alt                                          AS descripcion_alt_reunion,
    p.titulo                                                   AS titulo_punto,
    p.descripcion                                              AS descripcion_punto,
    p.acuerdos                                                 AS acuerdos_punto,
    p.deliberaciones                                           AS deliberaciones_punto,
    p.titulo_alt                                               AS titulo_alt_punto,
    p.descripcion_alt                                          AS descripcion_alt_punto,
    p.acuerdos_alt                                             AS acuerdos_alt_punto,
    p.deliberaciones_alt                                       AS deliberaciones_alt_punto,
    quita_acentos(r.asunto)                                    AS asunto_reunion_busq,
    quita_acentos((r.descripcion) :: CHARACTER VARYING)        AS descripcion_reunion_busq,
    quita_acentos(r.asunto_alt)                                AS asunto_alt_reunion_busq,
    quita_acentos((r.descripcion_alt) :: CHARACTER VARYING)    AS descripcion_alt_reunion_busq,
    quita_acentos(p.titulo)                                    AS titulo_punto_busq,
    quita_acentos((p.descripcion) :: CHARACTER VARYING)        AS descripcion_punto_busq,
    quita_acentos((p.acuerdos) :: CHARACTER VARYING)           AS acuerdos_punto_busq,
    quita_acentos((p.deliberaciones) :: CHARACTER VARYING)     AS deliberaciones_punto_busq,
    quita_acentos(p.titulo_alt)                                AS titulo_alt_punto_busq,
    quita_acentos((p.descripcion_alt) :: CHARACTER VARYING)    AS descripcion_alt_punto_busq,
    quita_acentos((p.acuerdos_alt) :: CHARACTER VARYING)       AS acuerdos_alt_punto_busq,
    quita_acentos((p.deliberaciones_alt) :: CHARACTER VARYING) AS deliberaciones_alt_punto_busq
  FROM goc_reuniones r LEFT JOIN goc_reuniones_puntos_orden_dia p
      on p.reunion_id = r.id;

CREATE OR REPLACE VIEW goc.goc_vw_reuniones_editores AS
  SELECT
    r.id,
    r.asunto,
    r.asunto_alt,
    r.fecha,
    r.duracion,
    (SELECT count(*) AS count
     FROM goc_reuniones_documentos rd
     WHERE (rd.reunion_id = r.id)) AS num_documentos,
    r.creador_id                   AS editor_id,
    r.completada,
    o.externo,
    o.organo_id,
    o.tipo_organo_id,
    r.aviso_primera_reunion,
    r.aviso_primera_reunion_user,
    r.aviso_primera_reunion_fecha
  FROM (goc_reuniones r LEFT JOIN goc_organos_reuniones o
      ON ((r.id = o.reunion_id)))
  UNION SELECT
          r.id,
          r.asunto,
          r.asunto_alt,
          r.fecha,
          r.duracion,
          (SELECT count(*) AS count
           FROM goc_reuniones_documentos rd
           WHERE (rd.reunion_id = r.id)) AS num_documentos,
          a.persona_id                   AS editor_id,
          r.completada,
          o.externo,
          o.organo_id,
          o.tipo_organo_id,
          r.aviso_primera_reunion,
          r.aviso_primera_reunion_user,
          r.aviso_primera_reunion_fecha
        FROM goc_reuniones r, goc_organos_reuniones o, goc_organos_autorizados a
        WHERE ((r.id = o.reunion_id) AND
               ((o.organo_id) :: TEXT = (a.organo_id) :: TEXT));

CREATE OR REPLACE VIEW goc.goc_vw_reuniones_permisos AS
  SELECT
    s.id,
    s.completada,
    s.fecha,
    s.asunto,
    s.asunto_alt,
    s.persona_id,
    s.persona_nombre,
    sum(s.asistente) AS asistente,
    s.url_acta,
    s.url_acta_alt,
    (SELECT a.url_asistencia
     FROM goc_vw_certificados_asistencia a
     WHERE ((a.reunion_id = s.id) AND (a.persona_id = s.persona_id))
     LIMIT 1)        AS url_asistencia,
    (SELECT a.url_asistencia_alt
     FROM goc_vw_certificados_asistencia a
     WHERE ((a.reunion_id = s.id) AND (a.persona_id = s.persona_id))
     LIMIT 1)        AS url_asistencia_alt
  FROM ((((((SELECT
               r.id,
               r.completada,
               r.fecha,
               r.asunto,
               r.asunto_alt,
               r.creador_id     AS persona_id,
               r.creador_nombre AS persona_nombre,
               0                AS asistente,
               r.url_acta,
               r.url_acta_alt
             FROM goc_reuniones r
             UNION SELECT
                     r.id,
                     r.completada,
                     r.fecha,
                     r.asunto,
                     r.asunto_alt,
                     ra.persona_id,
                     ra.persona_nombre,
                     0 AS asistente,
                     r.url_acta,
                     r.url_acta_alt
                   FROM goc_reuniones r, goc_organos_reuniones orr, goc_organos_autorizados ra
                   WHERE ((r.id = orr.reunion_id) AND ((orr.organo_id) :: TEXT = (ra.organo_id) :: TEXT)))
            UNION SELECT
                    r.id,
                    r.completada,
                    r.fecha,
                    r.asunto,
                    r.asunto_alt,
                    ri.persona_id,
                    ri.persona_nombre,
                    1 AS asistente,
                    r.url_acta,
                    r.url_acta_alt
                  FROM goc_reuniones r, goc_reuniones_invitados ri
                  WHERE (ri.reunion_id = r.id))
           UNION SELECT
                   r.id,
                   r.completada,
                   r.fecha,
                   r.asunto,
                   r.asunto_alt,
                   (ori.persona_id) :: DOUBLE PRECISION AS persona_id,
                   ori.nombre                           AS persona_nombre,
                   CASE WHEN (ori.solo_consulta IS FALSE)
                     THEN 1
                   ELSE 0 END AS asistente,
                   r.url_acta,
                   r.url_acta_alt
                 FROM goc_reuniones r, goc_organos_reuniones orr, goc_organos_reuniones_invits ori
                 WHERE ((r.id = orr.reunion_id) AND (ori.organo_reunion_id = orr.id)))
          UNION SELECT
                  r.id,
                  r.completada,
                  r.fecha,
                  r.asunto,
                  r.asunto_alt,
                  orrm.suplente_id     AS persona_id,
                  orrm.suplente_nombre AS persona_nombre,
                  1                    AS asistente,
                  r.url_acta,
                  r.url_acta_alt
                FROM goc_reuniones r, goc_organos_reuniones orr, goc_organos_reuniones_miembros orrm
                WHERE
                  ((((r.id = orr.reunion_id) AND (orr.id = orrm.organo_reunion_id)) AND (orrm.asistencia IS TRUE)) AND
                   (orrm.suplente_id IS NOT NULL)))
         UNION SELECT
                 r.id,
                 r.completada,
                 r.fecha,
                 r.asunto,
                 r.asunto_alt,
                 (orrm.miembro_id) :: DOUBLE PRECISION AS persona_id,
                 orrm.nombre                           AS persona_nombre,
                 CASE WHEN (orrm.suplente_id = NULL :: DOUBLE PRECISION)
                   THEN 1
                 ELSE 0 END                            AS asistente,
                 r.url_acta,
                 r.url_acta_alt
               FROM goc_reuniones r, goc_organos_reuniones orr, goc_organos_reuniones_miembros orrm
               WHERE (((r.id = orr.reunion_id) AND (orr.id = orrm.organo_reunion_id)) AND (orrm.asistencia IS TRUE)))
        UNION SELECT
                r.id,
                r.completada,
                r.fecha,
                r.asunto,
                r.asunto_alt,
                (orrm.miembro_id) :: DOUBLE PRECISION AS persona_id,
                orrm.nombre                           AS persona_nombre,
                0                                     AS asistente,
                r.url_acta,
                r.url_acta_alt
              FROM goc_reuniones r, goc_organos_reuniones orr, goc_organos_reuniones_miembros orrm
              WHERE (((r.id = orr.reunion_id) AND (orr.id = orrm.organo_reunion_id)) AND (orrm.asistencia IS FALSE))) s
  GROUP BY s.id, s.completada, s.fecha, s.asunto, s.asunto_alt, s.persona_id, s.persona_nombre, s.url_acta,
    s.url_acta_alt;