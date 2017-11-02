CREATE TABLE GOC_REUNIONES_DOCUMENTOS
(
  ID           NUMBER                           NOT NULL PRIMARY KEY,
  REUNION_ID   NUMBER                           NOT NULL,
  DESCRIPCION  VARCHAR2(2048 BYTE)              NOT NULL,
  MIME_TYPE    VARCHAR2(512 BYTE)               NOT NULL,
  NOMBRE_FICHERO    VARCHAR2(512 BYTE)               NOT NULL,
  FECHA_ADICION DATE NOT NULL,
  DATOS BLOB NOT NULL,
  CREADOR_ID NUMBER NOT NULL
);

alter table GOC_REUNIONES_DOCUMENTOS
  add constraint fk_documentos_reuniones foreign key (reunion_id) references goc_reuniones(id);


CREATE TABLE GOC_REUNIONES_PUNTOS_ORDEN_DIA
(
  ID           NUMBER                           NOT NULL PRIMARY KEY,
  TITULO       VARCHAR2(4000 BYTE)              NOT NULL,
  DESCRIPCION  CLOB                             NOT NULL,
  ORDEN        NUMBER                           NOT NULL,
  REUNION_ID   NUMBER                           NOT NULL,
  CONCLUSION   CLOB
);

alter table GOC_REUNIONES_PUNTOS_ORDEN_DIA
  add constraint fk_ordendia_reunion foreign key (reunion_id) references goc_reuniones(id);


CREATE TABLE GOC_P_ORDEN_DIA_DOCUMENTOS
(
  ID              NUMBER                        NOT NULL PRIMARY KEY,
  PUNTO_ID        NUMBER                        NOT NULL,
  DESCRIPCION     VARCHAR2(2048 BYTE)           NOT NULL,
  MIME_TYPE       VARCHAR2(512 BYTE)            NOT NULL,
  NOMBRE_FICHERO  VARCHAR2(512 BYTE)            NOT NULL,
  FECHA_ADICION   DATE                          DEFAULT SYSDATE               NOT NULL,
  DATOS           BLOB                          NOT NULL,
  CREADOR_ID      NUMBER                        NOT NULL
);

alter table GOC_P_ORDEN_DIA_DOCUMENTOS
  add constraint fk_ordendia_documentos_reunion foreign key (punto_id) references GOC_REUNIONES_PUNTOS_ORDEN_DIA(id);

ALTER TABLE GOC_REUNIONES DROP COLUMN ORDEN_DIA;