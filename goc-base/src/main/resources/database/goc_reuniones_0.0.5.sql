CREATE TABLE GOC_ORGANOS_REUNIONES_MIEMBROS
(
  ID                 NUMBER PRIMARY KEY,
  ORGANO_REUNION_ID  NUMBER                     NOT NULL,
  ORGANO_EXTERNO     NUMBER                     NOT NULL,
  REUNION_ID  NUMBER                     NOT NULL,
  ORGANO_ID  VARCHAR2(400)                     NOT NULL,
  NOMBRE             VARCHAR2(500 BYTE)         NOT NULL,
  EMAIL              VARCHAR2(200 BYTE)         NOT NULL,
  ASISTENCIA         NUMBER DEFAULT 0 NOT NULL,
  CARGO_ID VARCHAR2(400 BYTE) NOT NULL,
  CARGO_NOMBRE VARCHAR2(400 BYTE) NOT NULL
);

alter table GOC_ORGANOS_REUNIONES_MIEMBROS
  add constraint fk_miembros_organos_reuniones foreign key (ORGANO_REUNION_ID) references goc_organos_reuniones(id);

alter table GOC_REUNIONES RENAME COLUMN ACTA TO ACUERDOS;
alter table GOC_REUNIONES_PUNTOS_ORDEN_DIA RENAME COLUMN CONCLUSION TO ACUERDOS;

alter table GOC_REUNIONES add UBICACION VARCHAR2(4000);

alter table GOC_REUNIONES_PUNTOS_ORDEN_DIA add DELIBERACIONES CLOB;

ALTER TABLE GOC_REUNIONES ADD NUMERO_SESION NUMBER;