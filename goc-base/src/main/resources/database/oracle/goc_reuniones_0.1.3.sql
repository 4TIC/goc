CREATE TABLE UJI_REUNIONES.GOC_P_ORDEN_DIA_ACUERDOS
(
  ID              NUMBER                             NOT NULL,
  PUNTO_ID        NUMBER                             NOT NULL,
  DESCRIPCION     VARCHAR2(2048 BYTE)                NOT NULL,
  MIME_TYPE       VARCHAR2(512 BYTE)                 NOT NULL,
  NOMBRE_FICHERO  VARCHAR2(512 BYTE)                 NOT NULL,
  FECHA_ADICION   DATE DEFAULT SYSDATE               NOT NULL,
  DATOS           BLOB                               NOT NULL,
  CREADOR_ID      NUMBER                             NOT NULL,
  DESCRIPCION_ALT VARCHAR2(2048 BYTE)
);

ALTER TABLE UJI_REUNIONES.GOC_P_ORDEN_DIA_ACUERDOS
  ADD (
  CONSTRAINT FK_ORDENDIA_ACUERDOS_REUNION
  FOREIGN KEY (PUNTO_ID)
  REFERENCES UJI_REUNIONES.GOC_REUNIONES_PUNTOS_ORDEN_DIA (ID)
  ENABLE VALIDATE);

CREATE OR REPLACE FUNCTION UJI_REUNIONES.quita_acentos(
  pTxt IN VARCHAR2
)
  RETURN VARCHAR2 IS
  vResult VARCHAR2(4000);
  BEGIN

    IF pTxt IS NULL
    THEN

      RETURN NULL;

    ELSE

      RETURN translate(lower(pTxt), 'áéíóúàèìòùäëïöüâêîôû', 'aeiouaeiouaeiouaeiou');

    END IF;
  END;
/

CREATE OR REPLACE FUNCTION quita_acentos_clob(
  pTxt IN CLOB
)
  RETURN CLOB IS
  vResult   CLOB;
  l_offset  PLS_INTEGER := 1;
  l_segment VARCHAR2(4000);
  BEGIN

    IF pTxt IS NULL
    THEN

      RETURN NULL;

    ELSE

      WHILE l_offset < DBMS_LOB.GETLENGTH(pTxt) LOOP

        l_segment := DBMS_LOB.SUBSTR(pTxt, 3000, l_offset);

        l_segment := quita_acentos(l_segment);

        vResult := vResult || l_segment;

        l_offset := l_offset + 3000;

      END LOOP;

      RETURN vResult;

    END IF;
  END;
/

CREATE OR REPLACE VIEW goc_vw_reuniones_busqueda AS
  SELECT
    r.id                                     id,
    r.asunto                                 asunto_reunion,
    r.descripcion                            descripcion_reunion,
    r.asunto_alt                             asunto_alt_reunion,
    r.descripcion_alt                        descripcion_alt_reunion,
    p.titulo                                 titulo_punto,
    p.descripcion                            descripcion_punto,
    p.acuerdos                               acuerdos_punto,
    p.deliberaciones                         deliberaciones_punto,
    p.titulo_alt                             titulo_alt_punto,
    p.descripcion_alt                        descripcion_alt_punto,
    p.acuerdos_alt                           acuerdos_alt_punto,
    p.deliberaciones_alt                     deliberaciones_alt_punto,
    quita_acentos(r.asunto)                  asunto_reunion_busq,
    quita_acentos_clob(r.descripcion)        descripcion_reunion_busq,
    quita_acentos(r.asunto_alt)              asunto_alt_reunion_busq,
    quita_acentos_clob(r.descripcion_alt)    descripcion_alt_reunion_busq,
    quita_acentos(p.titulo)                  titulo_punto_busq,
    quita_acentos_clob(p.descripcion)        descripcion_punto_busq,
    quita_acentos_clob(p.acuerdos)           acuerdos_punto_busq,
    quita_acentos_clob(p.deliberaciones)     deliberaciones_punto_busq,
    quita_acentos(p.titulo_alt)              titulo_alt_punto_busq,
    quita_acentos_clob(p.descripcion_alt)    descripcion_alt_punto_busq,
    quita_acentos_clob(p.acuerdos_alt)       acuerdos_alt_punto_busq,
    quita_acentos_clob(p.deliberaciones_alt) deliberaciones_alt_punto_busq
  FROM goc_reuniones r,
    goc_reuniones_puntos_orden_dia p
  WHERE p.reunion_id = r.id;

ALTER TABLE goc_reuniOnes
  ADD AVISO_PRIMERA_REUNION NUMBER DEFAULT 0 NOT NULL;

alter table goc_cargos add firma number default 0 not null;
alter table goc_organos_reuniones_miembros add cargo_firma number default 0 not null;

CREATE OR REPLACE VIEW goc_vw_reuniones_editores AS
  SELECT
    r.id,
    r.asunto,
    r.asunto_alt,
    r.fecha,
    r.duracion,
    (SELECT count(*)
     FROM goc_reuniones_documentos rd
     WHERE rd.reunion_id = r.id) num_documentos,
    r.creador_id                 editor_id,
    r.completada                 completada,
    o.externo,
    o.organo_id,
    o.tipo_organo_id
  FROM goc_reuniones r,
    goc_organos_reuniones o,
    goc_organos_reuniones_miembros orm
  WHERE r.id = o.reunion_id (+)
        AND o.id = orm.organo_reunion_id (+)
  UNION
  SELECT
    r.id,
    r.asunto,
    r.asunto_alt,
    r.fecha,
    r.duracion,
    (SELECT count(*)
     FROM goc_reuniones_documentos rd
     WHERE rd.reunion_id = r.id) num_documentos,
    orm.miembro_id               editor_id,
    r.completada,
    o.externo,
    o.organo_id,
    o.tipo_organo_id
  FROM goc_reuniones r,
    goc_organos_reuniones o,
    goc_organos_reuniones_miembros orm
  WHERE r.id = o.reunion_id
        AND o.id = orm.organo_reunion_id
        AND orm.cargo_firma = 1;

create table goc_descriptores_tipos_organo (id number primary key not null, descriptor_id number not null, tipo_organo_id number not null)

ALTER TABLE UJI_REUNIONES.GOC_DESCRIPTORES_TIPOS_ORGANO ADD
CONSTRAINT GOC_DESCRIPTORES_TIPOS_ORG_TO
 FOREIGN KEY (TIPO_ORGANO_ID)
 REFERENCES UJI_REUNIONES.GOC_TIPOS_ORGANO (ID);

ALTER TABLE UJI_REUNIONES.GOC_DESCRIPTORES_TIPOS_ORGANO ADD
CONSTRAINT GOC_DESCRIPTORES_TIPOS_ORG_DE
 FOREIGN KEY (DESCRIPTOR_ID)
 REFERENCES UJI_REUNIONES.GOC_DESCRIPTORES (ID);

CREATE INDEX UJI_REUNIONES.UJI_DESCRIPTORES_TIP_ORG_TO_I ON UJI_REUNIONES.GOC_DESCRIPTORES_TIPOS_ORGANO
(TIPO_ORGANO_ID)

CREATE INDEX UJI_REUNIONES.UJI_DESCRIPTORES_TIP_ORG_de_I ON UJI_REUNIONES.GOC_DESCRIPTORES_TIPOS_ORGANO
(DESCRIPTOR_ID)