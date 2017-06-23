CREATE TABLE goc_reuniones_invitados (
  id             NUMBER         NOT NULL PRIMARY KEY,
  reunion_id     NUMBER         NOT NULL,
  persona_id     NUMBER         NOT NULL,
  persona_nombre VARCHAR2(1000) NOT NULL
);

ALTER TABLE UJI_REUNIONES.GOC_REUNIONES_INVITADOS
  ADD
  CONSTRAINT GOC_REUNIONES_INVITADOS_R01
  FOREIGN KEY (REUNION_ID)
  REFERENCES UJI_REUNIONES.GOC_REUNIONES (ID);

CREATE INDEX UJI_REUNIONES.UJI_reuniones_invitados_reu_i
  ON UJI_REUNIONES.GOC_REUNIONES_INVITADOS
  (REUNION_ID);


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
    TIPO_ORGANO_ID
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
    o.tipo_organo_id
  FROM goc_reuniones r,
    goc_organos_reuniones o,
    goc_organos_reuniones_miembros orm
  WHERE r.id = o.reunion_id (+) AND o.id = orm.organo_reunion_id (+);

alter table goc_cargos drop column firma;
alter table goc_organos_reuniones_miembros drop column cargo_firma;

alter table goc_reuniones_invitados add (persona_email varchar2(1000) not null)

CREATE OR REPLACE FORCE VIEW UJI_REUNIONES.GOC_VW_REUNIONES_BUSQUEDA
(
    ID,
    REUNION_ID,
    ASUNTO_REUNION,
    DESCRIPCION_REUNION,
    ASUNTO_ALT_REUNION,
    DESCRIPCION_ALT_REUNION,
    TITULO_PUNTO,
    DESCRIPCION_PUNTO,
    ACUERDOS_PUNTO,
    DELIBERACIONES_PUNTO,
    TITULO_ALT_PUNTO,
    DESCRIPCION_ALT_PUNTO,
    ACUERDOS_ALT_PUNTO,
    DELIBERACIONES_ALT_PUNTO,
    ASUNTO_REUNION_BUSQ,
    DESCRIPCION_REUNION_BUSQ,
    ASUNTO_ALT_REUNION_BUSQ,
    DESCRIPCION_ALT_REUNION_BUSQ,
    TITULO_PUNTO_BUSQ,
    DESCRIPCION_PUNTO_BUSQ,
    ACUERDOS_PUNTO_BUSQ,
    DELIBERACIONES_PUNTO_BUSQ,
    TITULO_ALT_PUNTO_BUSQ,
    DESCRIPCION_ALT_PUNTO_BUSQ,
    ACUERDOS_ALT_PUNTO_BUSQ,
    DELIBERACIONES_ALT_PUNTO_BUSQ
)
AS
  SELECT r.id ||'-'|| p.id                      id,
         r.id                                   reunion_id,
         r.asunto                               asunto_reunion,
         r.descripcion                          descripcion_reunion,
         r.asunto_alt                           asunto_alt_reunion,
         r.descripcion_alt                      descripcion_alt_reunion,
         p.titulo                               titulo_punto,
         p.descripcion                          descripcion_punto,
         p.acuerdos                             acuerdos_punto,
         p.deliberaciones                       deliberaciones_punto,
         p.titulo_alt                           titulo_alt_punto,
         p.descripcion_alt                      descripcion_alt_punto,
         p.acuerdos_alt                         acuerdos_alt_punto,
         p.deliberaciones_alt                   deliberaciones_alt_punto,
         quita_acentos (r.asunto)               asunto_reunion_busq,
         quita_acentos_clob (r.descripcion)     descripcion_reunion_busq,
         quita_acentos (r.asunto_alt)           asunto_alt_reunion_busq,
         quita_acentos_clob (r.descripcion_alt) descripcion_alt_reunion_busq,
         quita_acentos (p.titulo)               titulo_punto_busq,
         quita_acentos_clob (p.descripcion)     descripcion_punto_busq,
         quita_acentos_clob (p.acuerdos)        acuerdos_punto_busq,
         quita_acentos_clob (p.deliberaciones)  deliberaciones_punto_busq,
         quita_acentos (p.titulo_alt)           titulo_alt_punto_busq,
         quita_acentos_clob (p.descripcion_alt) descripcion_alt_punto_busq,
         quita_acentos_clob (p.acuerdos_alt)    acuerdos_alt_punto_busq,
         quita_acentos_clob (p.deliberaciones_alt)
                                                deliberaciones_alt_punto_busq
  FROM goc_reuniones r, goc_reuniones_puntos_orden_dia p
  WHERE p.reunion_id(+) = r.id;


CREATE TABLE UJI_REUNIONES.GOC_ORGANOS_INVITADOS
(
  ID              NUMBER                        NOT NULL primary key,
  PERSONA_ID      NUMBER                        NOT NULL,
  PERSONA_NOMBRE  VARCHAR2(1000 BYTE)           NOT NULL,
  ORGANO_ID       VARCHAR2(100 BYTE)            NOT NULL
)
