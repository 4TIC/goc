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
    AVISO_PRIMERA_REUNION_FECHA,
    URL_ACTA,
    URL_ACTA_ALT
)
AS
  SELECT
    r.id,
    r.asunto,
    r.asunto_alt,
    r.fecha,
    r.duracion,
    (SELECT COUNT(*)
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
    r.aviso_primera_reunion_fecha,
    r.url_acta,
    r.url_acta_alt
  FROM goc_reuniones r, goc_organos_reuniones o
  WHERE r.id = o.reunion_id (+)
  UNION
  SELECT
    r.id,
    r.asunto,
    r.asunto_alt,
    r.fecha,
    r.duracion,
    (SELECT COUNT(*)
     FROM goc_reuniones_documentos rd
     WHERE rd.reunion_id = r.id)
                 num_documentos,
    a.persona_id editor_id,
    r.completada completada,
    o.externo,
    o.organo_id,
    o.tipo_organo_id,
    r.aviso_primera_reunion,
    r.aviso_primera_reunion_user,
    r.aviso_primera_reunion_fecha,
    r.url_acta,
    r.url_acta_alt
  FROM goc_reuniones r, goc_organos_reuniones o, goc_organos_autorizados a
  WHERE r.id = o.reunion_id AND o.organo_id = a.organo_id;
