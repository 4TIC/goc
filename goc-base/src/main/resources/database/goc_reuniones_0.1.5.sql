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