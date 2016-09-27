CREATE TABLE GOC_MIEMBROS
  (
     id NUMBER  NOT NULL ,
     nombre VARCHAR2 (500)  NOT NULL ,
     email VARCHAR2(200) NOT NULL,
     organo_id NUMBER  NOT NULL
  )
;

ALTER TABLE GOC_MIEMBROS ADD CONSTRAINT MIEMBROS_PK PRIMARY KEY ( id )
;

ALTER TABLE GOC_MIEMBROS
      ADD CONSTRAINT GOC_MIEMBROS_ORGANOS_FK FOREIGN KEY ( organo_id )
      REFERENCES GOC_ORGANOS ( id )
;

CREATE TABLE GOC_REUNIONES
  (
     id NUMBER  NOT NULL ,
     asunto VARCHAR2 (500)  NOT NULL ,
     fecha DATE NOT NULL,
     duracion NUMBER NOT NULL,
     descripcion    CLOB,
     orden_dia CLOB,
     CREADOR_ID NUMBER NOT NULL,
     FECHA_CREACION DATE NOT NULL
  )
;

ALTER TABLE GOC_REUNIONES ADD CONSTRAINT REUNIONES_PK PRIMARY KEY ( id )
;

CREATE TABLE GOC_ORGANOS_REUNIONES
(
  ID             NUMBER                         NOT NULL,
  ORGANO_ID          NUMBER,
  REUNION_ID            NUMBER                  NOT NULL,
  ORGANO_EXTERNO_ID  VARCHAR2(8)
);

ALTER TABLE GOC_ORGANOS_REUNIONES ADD CONSTRAINT ORGANOS_REUNIONES_PK PRIMARY KEY ( id );

ALTER TABLE GOC_ORGANOS_REUNIONES
      ADD CONSTRAINT GOC_ORG_REU_ORGANOS_FK FOREIGN KEY ( organo_id )
      REFERENCES GOC_ORGANOS ( id )


ALTER TABLE GOC_ORGANOS_REUNIONES
      ADD CONSTRAINT GOC_ORG_REU_REUNIONES_FK FOREIGN KEY ( reunion_id )
      REFERENCES GOC_REUNIONES ( id )